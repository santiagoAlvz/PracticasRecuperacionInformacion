package practica5;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;

public class IndexSearcher {
	
	private String episodesIndexPath = "./index/episodes/";
	private org.apache.lucene.search.IndexSearcher episodeSearcher;
	
	/**
	 * Loads the indexes from the filesystem
	 */
	public IndexSearcher() {		
		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(episodesIndexPath)));
			episodeSearcher = new org.apache.lucene.search.IndexSearcher(reader);
		    episodeSearcher.setSimilarity(new ClassicSimilarity());
		    
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
		String episodeData;
		
		try {
			TopDocs results = episodeSearcher.search(sp.getEpisodeQuery(), 30);
			ScoreDoc[] hits = results.scoreDocs;
			
			for(int i = 0; i < hits.length; i++) {
				Document doc = episodeSearcher.doc(hits[i].doc);
				
				episodeData = doc.get("title") + "(" +doc.get("original_air_date")+ "), season: "+doc.get("season")+", rating "+doc.get("imdb_rating")+" stars";
				returnValue.put(episodeData, new ArrayList<String>());
			}
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		return returnValue;
	}

}
