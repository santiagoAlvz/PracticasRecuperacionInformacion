import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.index.IndexWriter;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.io.Reader;
import java.io.FileReader;
import java.nio.file.Path;



public class ScriptIndexer {

	private IndexWriter writer;
	boolean create = false;
	String indexPath = "./index/scripts";

	ScriptIndexer(boolean create){
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
		
		Directory dir = FSDirectory.open(Paths.get(this.indexPath));
		
		this.writer = new IndexWriter(dir, iwc);

	} 

	public void index(String uri){
        // Convert the string to a Path
        Path path = Paths.get(uri);

        // Get the file or directory name
        String directory = path.getFileName().toString();
        
    	System.out.println("Indexing scripts stored in " + directory);
    	
    	File folder = new File(uri);
		File[] files = folder.listFiles();
			
		for (File file : files) {
            if (file.isFile()) {
                try {
                    Reader reader = new FileReader(file);
                    
                    if (!reader.ready()){
                        System.out.println("Error");
                    }
                    
                    CSVReader csvReader = new CSVReader(reader);
                    
                    String[] nextRecord;
                    csvReader.readNext();
                    
                    
                    while((nextRecord = csvReader.readNext()) != null){
                    	Document doc = new Document();
                    	
                    	doc.add(new IntPoint("episode_id", Integer.parseInt(nextRecord[0])));
                    	doc.add(new StoredField("episode_id", nextRecord[0]));
                    	
                    	doc.add(new IntPoint("number", Integer.parseInt(nextRecord[2])));
                    	
                    	doc.add(new IntPoint("timestamp_in_ms", Integer.parseInt(nextRecord[3])));
                    	
                    	doc.add(new TextField("raw_character_text", nextRecord[4], Field.Store.YES));
         
                    	doc.add(new TextField("raw_location_text", nextRecord[5], Field.Store.YES));
             
                    	doc.add(new TextField("spoken_words", nextRecord[6], Field.Store.YES));
                    	
                    	writer.addDocument(doc);
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
		
		close();
    }

	
	
	
	public void close() {
		try {
			writer.commit();
			writer.close();
		}catch (IOException e) {
			System.out.println("Error closing the index");
		}
	}
}
