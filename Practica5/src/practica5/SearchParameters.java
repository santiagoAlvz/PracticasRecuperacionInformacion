package practica5;

import java.util.ArrayList;

import org.apache.lucene.document.IntPoint;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class SearchParameters {
	
	private ArrayList<BooleanClause> episodeFilters = new ArrayList<BooleanClause>();
	
	/**
	 * Returns the Lucene query for the episode filters stored in the object, so it can be used
	 * to filter results
	 * 
	 * @return The BooleanQuery representation of the episode filters
	 */
	public BooleanQuery getEpisodeQuery() {
		BooleanQuery.Builder bqbuilder = new BooleanQuery.Builder();
		
		for(BooleanClause clause: episodeFilters) {
			bqbuilder.add(clause);
		}
		
		return bqbuilder.build();
	}

	/**
	 * Adds a filter as specified in the interface so its used in the query. Creates the
	 * appropiate boolean clause for the field and adds it in either episodeFilters or lineFilters
	 * 
	 * @param field, field to be filtered
	 * @param text, user specified data for the filter
	 */
	public void addFilter(FilterFields field, String text) {
		
		//If there wasn't a filter specified, there's no query to be added
		if(text.isEmpty()) {
			return;
		}
		
		Query qe;
		
		switch(field) {
		case EPISODE_SEASON_GREATER_THAN:
			qe = IntPoint.newRangeQuery("season", Integer.parseInt(text) + 1, 1000);
			episodeFilters.add(new BooleanClause(qe, BooleanClause.Occur.MUST));
			break;
		case EPISODE_SEASON_EQUAL_THAN:
			qe = IntPoint.newExactQuery("season", Integer.parseInt(text));
			episodeFilters.add(new BooleanClause(qe, BooleanClause.Occur.MUST));
			break;
		case EPISODE_SEASON_LESSER_THAN:
			qe = IntPoint.newRangeQuery("season", 0, Integer.parseInt(text) - 1);
			episodeFilters.add(new BooleanClause(qe, BooleanClause.Occur.MUST));
			break;
		case EPISODE_RATING_GREATER_THAN:
			break;
		case EPISODE_TITLE:
			break;
		case LINE_CHARACTER:
			break;
		case LINE_SPOKEN_WORDS:
			break;
		default:
			break;
		}
	}
		
}
