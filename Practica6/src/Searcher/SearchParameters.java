package Searcher;

import java.util.ArrayList;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.facet.DrillDownQuery;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;

public class SearchParameters {
	
	//private ArrayList<BooleanClause> episodeFilters = new ArrayList<BooleanClause>();
	//private ArrayList<BooleanClause> scriptFilters = new ArrayList<BooleanClause>();
	private BooleanQuery.Builder episodeBQBuilder = new BooleanQuery.Builder();
	private BooleanQuery.Builder scriptBQBuilder = new BooleanQuery.Builder();
	private DrillDownQuery episodeFacetFilters;
	private DrillDownQuery lineFacetFilters;
	private boolean lineFacetsApplied = false, episodeFacetsApplied = false;
	
	/**
	 * Returns the Lucene query for the episode filters stored in the object, so it can be used
	 * to filter results
	 * 
	 * @return The BooleanQuery representation of the episode filters
	 */
	public SearchParameters() {
		episodeFacetFilters = new DrillDownQuery(new FacetsConfig());
		lineFacetFilters = new DrillDownQuery(new FacetsConfig(), scriptBQBuilder.build());
	}
	
	public void prepareFacets() {
		episodeFacetFilters = new DrillDownQuery(new FacetsConfig(), episodeBQBuilder.build());
	}
	
	public Query getEpisodeQuery() {
		if(episodeFacetsApplied) {
			return episodeFacetFilters;
		}
				
		return episodeBQBuilder.build();
	}
	
	public Query getScriptQuery() {
		if(lineFacetsApplied) {
			return lineFacetFilters;
		}
		
		return scriptBQBuilder.build();
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
		case EPISODE_GENERIC:
			
			an = new EnglishAnalyzer();
			
			try {
				parser = new QueryParser("title", an);
				qe = parser.parse(text);
//				Should operator is for clauses that should appear in the matching documents.
				episodeBQBuilder.add(new BooleanClause(qe, BooleanClause.Occur.SHOULD));
				
				parser = new QueryParser("spoken_words", an);
				qe = parser.parse(text);
				scriptBQBuilder.add(new BooleanClause(qe, BooleanClause.Occur.SHOULD));
				
				break;
			}
			catch (ParseException e) {
				System.out.println(e.getMessage());
			}
			
			break;
		case EPISODE_RATING_GREATER_THAN:
			qe = FloatPoint.newRangeQuery("imdb_rating", Float.parseFloat(text), 10.0f);
			episodeBQBuilder.add(new BooleanClause(qe, BooleanClause.Occur.FILTER));
			break;
		case EPISODE:
			qe = IntPoint.newExactQuery("number_in_season", Integer.parseInt(text));
			episodeBQBuilder.add(new BooleanClause(qe, BooleanClause.Occur.FILTER));
			break;
		case EPISODE_TITLE:
			an = new EnglishAnalyzer();
			parser = new QueryParser("title", an);
			
			try {
				qe = parser.parse(text);
				episodeBQBuilder.add(new BooleanClause(qe, BooleanClause.Occur.MUST));
			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
			
			break;
		case LINE_CHARACTER:
			an = new SimpleAnalyzer();
			parser = new QueryParser("raw_character_text", an);
			
			try {
				qe = parser.parse(text);
				scriptBQBuilder.add(new BooleanClause(qe, BooleanClause.Occur.MUST));
			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
			break;
		case LINE_SPOKEN_WORDS:
			an = new EnglishAnalyzer();
			parser = new QueryParser("spoken_words", an);
			
			try {
				qe = parser.parse(text);
				scriptBQBuilder.add(new BooleanClause(qe, BooleanClause.Occur.MUST));
			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
			break;
		default:
			break;
		}
	}

	public void addFacetFilter(FacetFilters facet, LabelAndValue facetData) {		
		switch(facet) {
		case EPISODE_YEAR:
			episodeFacetsApplied = true;
			episodeFacetFilters.add("original_air_year", facetData.label);
			break;
		case LINE_CHARACTER:
			lineFacetsApplied = true;
			lineFacetFilters.add("raw_character_text", facetData.label);
			break;
		case SEASONS:
			episodeFacetsApplied = true;
			episodeFacetFilters.add("season", facetData.label);
			break;		
		}
	}
		
}
