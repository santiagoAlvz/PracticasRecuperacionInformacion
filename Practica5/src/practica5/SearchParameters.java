package practica5;

import java.util.ArrayList;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
public class SearchParameters {
	
	private ArrayList<BooleanClause> episodeFilters = new ArrayList<BooleanClause>();
	private ArrayList<BooleanClause> scriptFilters = new ArrayList<BooleanClause>();
	
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
	
	public BooleanQuery getScriptQuery() {
		BooleanQuery.Builder bqbuilder = new BooleanQuery.Builder();
		
		if(scriptFilters.size() > 0) {
			
			for(BooleanClause clause: scriptFilters) {
				bqbuilder.add(clause);
			}
			
		} else {
			bqbuilder.add(new BooleanClause(new MatchAllDocsQuery(), BooleanClause.Occur.SHOULD));
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
		Analyzer an;
		QueryParser parser;
		
		switch(field) {
		case EPISODE_SEASON_GREATER_THAN:
			qe = IntPoint.newRangeQuery("season", Integer.parseInt(text) + 1, 1000);
			episodeFilters.add(new BooleanClause(qe, BooleanClause.Occur.FILTER));
			break;
		case EPISODE_SEASON_EQUAL_THAN:
			qe = IntPoint.newExactQuery("season", Integer.parseInt(text));
			episodeFilters.add(new BooleanClause(qe, BooleanClause.Occur.FILTER));
			break;
		case EPISODE_SEASON_LESSER_THAN:
			qe = IntPoint.newRangeQuery("season", 0, Integer.parseInt(text) - 1);
			episodeFilters.add(new BooleanClause(qe, BooleanClause.Occur.FILTER));
			break;
		case EPISODE_RATING_GREATER_THAN:
			qe = FloatPoint.newRangeQuery("imdb_rating", Float.parseFloat(text), 10.0f);
			episodeFilters.add(new BooleanClause(qe, BooleanClause.Occur.FILTER));
			break;
		case EPISODE_TITLE:
			an = new EnglishAnalyzer();
			parser = new QueryParser("title", an);
			
			try {
				qe = parser.parse(text);
				episodeFilters.add(new BooleanClause(qe, BooleanClause.Occur.MUST));
			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
			
			break;
		case LINE_CHARACTER:
			an = new SimpleAnalyzer();
			parser = new QueryParser("raw_character_text", an);
			
			try {
				qe = parser.parse(text);
				episodeFilters.add(new BooleanClause(qe, BooleanClause.Occur.MUST));
			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
			break;
		case LINE_SPOKEN_WORDS:
			qe = new PhraseQuery("spoken_words", text);
			scriptFilters.add(new BooleanClause(qe, BooleanClause.Occur.FILTER));
			break;
		default:
			break;
		}
	}
		
}
