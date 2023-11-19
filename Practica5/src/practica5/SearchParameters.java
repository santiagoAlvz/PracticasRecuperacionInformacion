package practica5;

import java.util.Hashtable;

import org.apache.lucene.search.BooleanQuery;

public class SearchParameters extends Hashtable<String, String> {

	private static final long serialVersionUID = -6841033309419798955L;
	
	/**
	 * Returns the Lucene query for the episode filters stored in the object, so it can be used
	 * to filter results
	 * 
	 * @return The BooleanQuery representation of the episode filters
	 */
	public BooleanQuery getEpisodeQuery() {
		BooleanQuery.Builder bqbuilder = new BooleanQuery.Builder();
		
		return bqbuilder.build();
	}
		
}
