package practica6;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.io.Reader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.nio.file.Path;

public class EpisodeIndexer {

	private IndexWriter indexWriter;
	private DirectoryTaxonomyWriter taxoWriter;
	FacetsConfig fconfig;
	private boolean create = false;
	private String indexPath = "./index/episodes/index";
	private String facetPath = "./index/episodes/facets";

	EpisodeIndexer(boolean create){
		this.create = create;
		try {
			configureIndex();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	void configureIndex() throws IOException {
		Map<String,Analyzer> analyzerPerField = new HashMap<String,Analyzer>();
		analyzerPerField.put("spoken_words", new EnglishAnalyzer());
		analyzerPerField.put("raw_character_text", new CharacterTextAnalyzer());
		analyzerPerField.put("title", new EnglishAnalyzer());

		PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(), analyzerPerField);
		Similarity sim = new ClassicSimilarity();

		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setSimilarity(sim);
		
		if(this.create) {
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		} else {
			iwc.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
		}
		
	    fconfig = new FacetsConfig();
	    fconfig.setHierarchical("original_air_date", true);
		
		Directory indexDir = FSDirectory.open(Paths.get(this.indexPath));
		Directory taxoDir = FSDirectory.open(Paths.get(this.facetPath));
		
		this.indexWriter = new IndexWriter(indexDir, iwc);
		this.taxoWriter = new DirectoryTaxonomyWriter(taxoDir);

	} 

	public void index(String uri){
        // Convert the string to a Path
        Path path = Paths.get(uri);

        // Get the file or directory name
        String directory = path.getFileName().toString();
        
    	System.out.println("Indexing episodes stored in " + directory);
    	
		File folder = new File(uri);
		File[] files = folder.listFiles();
		
		DateFormat df = new SimpleDateFormat("yyyy");
			
		for (File file : files) {
            if (file.isFile()) {
                try {
                    Reader reader = new FileReader(file);
                    Document doc = new Document();
                    
                    if (!reader.ready()){
                        System.out.println("Error");
                    }
                    
                    CSVReader csvReader = new CSVReader(reader);
                    
                    String[] nextRecord;
                    csvReader.readNext();
                    
                    while((nextRecord = csvReader.readNext()) != null){
                    	//add our fields to the lucene doc
                    	
                    	//Index it "twice", so the value can be both used to search and be displayed in the results
                    	doc.add(new IntPoint("episode_id", Integer.parseInt(nextRecord[1])));
                    	doc.add(new StoredField("episode_id", nextRecord[1]));
                    	
                    	doc.add(new TextField("spoken_words", nextRecord[2], Field.Store.YES));
                    	
                    	doc.add(new TextField("raw_character_text", nextRecord[3], Field.Store.NO));
                    	
                    	doc.add(new FloatPoint("imdb_rating", Float.parseFloat(nextRecord[4])));
                    	doc.add(new StoredField("imdb_rating", nextRecord[4]));
                    	
                    	doc.add(new IntPoint("imdb_votes", (int)Float.parseFloat(nextRecord[5])));
                    	
                    	doc.add(new IntPoint("number_in_season", Integer.parseInt(nextRecord[6])));
                    	doc.add(new StoredField("number_in_season", nextRecord[6]));
                    	
                    	try {                    	
	                    	Date date = new SimpleDateFormat("yyyy-MM-dd").parse(nextRecord[7]);
	                    		                    	
	                    	//doc.add(new LongPoint("original_air_date", date.getTime()));
	                    	doc.add(new StoredField("original_air_date", nextRecord[7]));
	                    	
                    	} catch(Exception e) {
                    		System.out.println(e.getMessage());
                    	}
                    	
                    	doc.add(new FacetField("original_air_year", nextRecord[8]));
                    	
                    	doc.add(new FacetField("season", nextRecord[9]));
                    	doc.add(new StoredField("season", nextRecord[9]));
                    	
                    	doc.add(new TextField("title", nextRecord[10], Field.Store.YES));
                    	
                    	doc.add(new FloatPoint("us_viewers_in_millions", Float.parseFloat(nextRecord[11])));
                    	doc.add(new StoredField("us_viewers_in_millions", nextRecord[11]));
                    	
                    	doc.add(new FloatPoint("views", Float.parseFloat(nextRecord[12])));
                    	
                    	indexWriter.addDocument(fconfig.build(taxoWriter, doc));
                    }
                    
                    csvReader.close();
                    reader.close();
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                catch (CsvValidationException e){
                    System.out.println(e.getMessage());
                }        
            }
        }
		
		/*try {
		Directory indexDir = FSDirectory.open(Paths.get(this.indexPath));
		Directory taxoDir = FSDirectory.open(Paths.get(this.facetPath));
		
	    DirectoryReader indexReader = DirectoryReader.open(indexDir);
	    IndexSearcher searcher = new IndexSearcher(indexReader);
	    TaxonomyReader taxoReader = new DirectoryTaxonomyReader(taxoDir);
	    FacetsCollector fc = new FacetsCollector();
	    FacetsCollector.search(searcher, new MatchAllDocsQuery(), 10, fc);
	    List<FacetResult> results = new ArrayList<>();
	    Facets facets = new FastTaxonomyFacetCounts(taxoReader, fconfig, fc);
	    results.add(facets.getTopChildren(20, "original_air_date"));
	    indexReader.close();
	    taxoReader.close();
	    
	    System.out.println(results);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}*/
		
		close();
    }
        
	
	
	public void close() {
		try {
			indexWriter.commit();
			indexWriter.close();
			
			taxoWriter.commit();
			taxoWriter.close();
		}catch (IOException e) {
			System.out.println("Error closing the index");
		}
	}
}