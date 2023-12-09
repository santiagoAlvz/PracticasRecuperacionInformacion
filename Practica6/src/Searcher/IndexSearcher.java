package Searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.facet.FacetsCollectorManager;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;

public class IndexSearcher {
	
	private String episodesIndexPath = "./index/episodes/index/";
	private String episodesFacetPath = "./index/episodes/facets/";
	private String scriptsIndexPath = "./index/scripts/index/";
	private String scriptsFacetPath = "./index/episodes/facets/";
	
	private org.apache.lucene.search.IndexSearcher episodeSearcher;
	private org.apache.lucene.search.IndexSearcher scriptSearcher;
	private TaxonomyReader episodeTaxoReader;
	private TaxonomyReader scriptTaxoReader;
	private FacetsCollector fc = new FacetsCollector();
	
	private LabelAndValue episodeYears[] = {};
	
	/**
	 * Loads the indexes from the filesystem
	 */
	public IndexSearcher() {		
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(episodesIndexPath)));
			episodeSearcher = new org.apache.lucene.search.IndexSearcher(reader);
		    episodeSearcher.setSimilarity(new ClassicSimilarity());
		    
		    episodeTaxoReader = new DirectoryTaxonomyReader(FSDirectory.open(Paths.get(episodesFacetPath)));
		    
		    reader = DirectoryReader.open(FSDirectory.open(Paths.get(scriptsIndexPath)));
		    scriptSearcher = new org.apache.lucene.search.IndexSearcher(reader);
		    scriptSearcher.setSimilarity(new ClassicSimilarity());
		    
		    scriptTaxoReader = new DirectoryTaxonomyReader(FSDirectory.open(Paths.get(scriptsFacetPath)));
		    
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Searches in the indexes, using the specified data
	 * 
	 * @param sp	Custom Object that contains the query arguments and generates Lucene Queries
	 */
	public LinkedHashMap<String,ArrayList<String>> search(SearchParameters sp) {
		System.out.println("Searching the index...");
		
		LinkedHashMap<String,ArrayList<String>> returnValue = new LinkedHashMap<String,ArrayList<String>>();
		String episodeData, lineData;
		TopDocs episodes, lines;
		fc = new FacetsCollector();
		
		try {
			episodes = episodeSearcher.search(sp.getEpisodeQuery(), 30);
									
			for(ScoreDoc doc: episodes.scoreDocs) {
				Document episode = episodeSearcher.doc(doc.doc);
				episodeData = getEpisodeData(episode);
				
				ArrayList<String> episodeLines = new ArrayList<String>();
							
				BooleanQuery.Builder bqbuilder = new BooleanQuery.Builder();
				bqbuilder.add(new BooleanClause(sp.getScriptQuery(), BooleanClause.Occur.MUST));
				
				Query qe = IntPoint.newExactQuery("episode_id", Integer.parseInt(episode.get("episode_id")));
				bqbuilder.add(new BooleanClause(qe, BooleanClause.Occur.FILTER));
				BooleanQuery query = bqbuilder.build();
				
				lines = scriptSearcher.search(query, 20);
				ScoreDoc[] linesHits = lines.scoreDocs;
				
				for(int j = 0; j < linesHits.length; j++) {
					Document line = scriptSearcher.doc(linesHits[j].doc);

					episodeLines.add(getLineData(line));
				}
				
				//For every episode that'll be included in the results, perform a search so its facets are included in fc
				if(episodeLines.size() > 0) {
					returnValue.put(episodeData, episodeLines);
					FacetsCollector.search(episodeSearcher, qe, 1, fc);
				}				
			}
			
			if(episodes.scoreDocs.length > 0) {
				Facets episodeFacets = new FastTaxonomyFacetCounts(episodeTaxoReader, new FacetsConfig(), fc);
				episodeYears = episodeFacets.getTopChildren(100,"original_air_year").labelValues;
			}
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		
		return returnValue;
	}

	
	public LinkedHashMap<String,ArrayList<String>> searchEpisodes(SearchParameters sp) {		
		System.out.println("search Episodes");
		
		LinkedHashMap<String,ArrayList<String>> returnValue = new LinkedHashMap<String,ArrayList<String>>();
		String episodeData;
		TopDocs episodes;
		fc = new FacetsCollector();
				
		try {
			episodes = FacetsCollector.search(episodeSearcher, sp.getEpisodeQuery(), 100, fc);
			
			Facets episodeFacets = new FastTaxonomyFacetCounts(episodeTaxoReader, new FacetsConfig(), fc);
			if(episodes.scoreDocs.length > 0) {
				//episodeYears = new LabelAndValue[]();
				episodeYears = null;
				episodeYears = episodeFacets.getTopChildren(100,"original_air_year").labelValues;
			}
			
			for(ScoreDoc doc: episodes.scoreDocs) {
				Document episode = episodeSearcher.doc(doc.doc);
				episodeData = getEpisodeData(episode);
				
				returnValue.put(episodeData,null);
			}
			
			//fc.
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		return returnValue;
	}
		
	public LinkedHashMap<String,ArrayList<String>> searchLines(SearchParameters sp) {
		System.out.println("search Lines");
		
		String episodeData, lineData;
		TopDocs episodes, lines;
		Document line, episode;
		fc = new FacetsCollector();
		
		LinkedHashMap<String,ArrayList<String>> returnValue = new LinkedHashMap<String,ArrayList<String>>();

		try {
			lines = scriptSearcher.search(sp.getScriptQuery(), 100);
			ScoreDoc[] linesHits = lines.scoreDocs;
			LinkedHashMap<Integer, ArrayList<String>> groupedEpisodes = new LinkedHashMap<Integer, ArrayList<String>>();
						
			//Group the lines by their episode id
			for(int i = 0; i < linesHits.length; i++) {
				
				line = scriptSearcher.doc(linesHits[i].doc);
							
				int episodeId = Integer.parseInt(line.get("episode_id"));
				
				if(!groupedEpisodes.containsKey(episodeId)) {
					groupedEpisodes.put(episodeId, new ArrayList<String>());
				}
				
				groupedEpisodes.get(episodeId).add(getLineData(line));
			}
			
			//Get episode information, and add it to the result
			for(int episodeId: groupedEpisodes.keySet()) {
				episodes = FacetsCollector.search(episodeSearcher, IntPoint.newExactQuery("episode_id", episodeId), 1, fc);
							
				if(episodes.scoreDocs.length > 0) {
					episode = episodeSearcher.doc(episodes.scoreDocs[0].doc);
					episodeData = getEpisodeData(episode);
				} else {
					episodeData = "Unknown Episode";
				}
				
				returnValue.put(episodeData, groupedEpisodes.get(episodeId));
			}
			
			if(linesHits.length > 0) {
				Facets episodeFacets = new FastTaxonomyFacetCounts(episodeTaxoReader, new FacetsConfig(), fc);
				episodeYears = episodeFacets.getTopChildren(100,"original_air_year").labelValues;
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		} 
		
		return returnValue;
	}
	
	private String getEpisodeData(Document episode) {
		return episode.get("title") + " (" +episode.get("original_air_date")+ ")"
				+ ", season: "+episode.get("season")
				+ ", episode: "+episode.get("number_in_season")
				+ ", imdb rating: "+episode.get("imdb_rating")
				+ ", US viewers "+episode.get("us_viewers_in_millions")+"M";
	}
	
	private String getLineData(Document line) {
		return line.get("raw_character_text")
				+ " ("+ line.get("raw_location_text") 
				+ "): "+line.get("spoken_words");
	}
	
	public LabelAndValue[] getResultYearsFacets() {
		return episodeYears;
	}

}
