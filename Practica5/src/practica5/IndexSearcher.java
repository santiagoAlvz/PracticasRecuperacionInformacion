package practica5;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;

public class IndexSearcher {
	
	private String episodesIndexPath = "./index/episodes/";
	private String scriptsIndexPath = "./index/scripts/";
	private org.apache.lucene.search.IndexSearcher episodeSearcher;
	private org.apache.lucene.search.IndexSearcher scriptSearcher;
	
	/**
	 * Loads the indexes from the filesystem
	 */
	public IndexSearcher() {		
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(episodesIndexPath)));
			episodeSearcher = new org.apache.lucene.search.IndexSearcher(reader);
		    episodeSearcher.setSimilarity(new ClassicSimilarity());
		    
		    reader = DirectoryReader.open(FSDirectory.open(Paths.get(scriptsIndexPath)));
		    scriptSearcher = new org.apache.lucene.search.IndexSearcher(reader);
		    scriptSearcher.setSimilarity(new ClassicSimilarity());
		    
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Searches in the indexes, using the specified data
	 * 
	 * @param sp	Custom Object that contains the query arguments and generates Lucene Queries
	 */
	public HashMap<String,ArrayList<String>> search(SearchParameters sp) {
		System.out.println("Searching the index...");
		
		HashMap<String,ArrayList<String>> returnValue = new HashMap<String,ArrayList<String>>();
		String episodeData, lineData;
		TopDocs episodes, lines;
		
		try {
			episodes = episodeSearcher.search(sp.getEpisodeQuery(), 30);
			ScoreDoc[] episodesHits = episodes.scoreDocs;
			
			for(int i = 0; i < episodesHits.length; i++) {
				Document episode = episodeSearcher.doc(episodesHits[i].doc);
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
				
				if(episodeLines.size() > 0) {
					returnValue.put(episodeData, episodeLines);
				}
			}
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		return returnValue;
	}

	
	public HashMap<String,ArrayList<String>> searchEpisodes(SearchParameters sp) {		
		System.out.println("search Episodes");
		
		HashMap<String,ArrayList<String>> returnValue = new HashMap<String,ArrayList<String>>();
		String episodeData;
		TopDocs episodes;
		
		try {
			episodes = episodeSearcher.search(sp.getEpisodeQuery(), 400);
			ScoreDoc[] episodesHits = episodes.scoreDocs;
			
			for(int i = 0; i < episodesHits.length; i++) {
				Document episode = episodeSearcher.doc(episodesHits[i].doc);
				episodeData = episode.get("title") + " (" +episode.get("original_air_date")+ "), season: "+episode.get("season")+", US viewers "+episode.get("us_viewers_in_millions")+"M";
				
				ArrayList<String> episodeLines = new ArrayList<String>();
							
				BooleanQuery.Builder bqbuilder = new BooleanQuery.Builder();
				bqbuilder.add(new BooleanClause(sp.getScriptQuery(), BooleanClause.Occur.MUST));
				
				Query qe = IntPoint.newExactQuery("episode_id", Integer.parseInt(episode.get("episode_id")));
				bqbuilder.add(new BooleanClause(qe, BooleanClause.Occur.FILTER));
				BooleanQuery query = bqbuilder.build();
				
				returnValue.put(episodeData,null);
			}
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		return returnValue;
	}
		
	public HashMap<String,ArrayList<String>> searchLines(SearchParameters sp) {
		System.out.println("search Lines");
		
		String episodeData, lineData;
		TopDocs episodes, lines;
		Document line, episode;
		
		HashMap<String,ArrayList<String>> returnValue = new HashMap<String,ArrayList<String>>();

		try {
			lines = scriptSearcher.search(sp.getScriptQuery(), 100);
			ScoreDoc[] linesHits = lines.scoreDocs;
			HashMap<Integer, ArrayList<String>> groupedEpisodes = new HashMap<Integer, ArrayList<String>>();
						
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
				episodes = episodeSearcher.search(IntPoint.newExactQuery("episode_id", episodeId), 1);
				ScoreDoc[] episodesHits = episodes.scoreDocs;
				
				if(episodesHits.length > 0) {
					episode = episodeSearcher.doc(episodesHits[0].doc);
					episodeData = getEpisodeData(episode);
				} else {
					episodeData = "Unknown Episode";
				}
				
				returnValue.put(episodeData, groupedEpisodes.get(episodeId));
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		} 
		
		return returnValue;
	}
	
	private String getEpisodeData(Document episode) {
		return episode.get("title") + " (" +episode.get("original_air_date")+ "), season: "+episode.get("season")+", US viewers "+episode.get("us_viewers_in_millions")+"M";
	}
	
	private String getLineData(Document line) {
		return line.get("raw_character_text") + " ("+ line.get("raw_location_text") + "): "+line.get("spoken_words");
	}

}
