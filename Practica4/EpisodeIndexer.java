import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.index.IndexWriter;

import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

public class EpisodeIndexer {

	private IndexWriter writer;
	boolean create = false;
	String indexPath = "./index/episodes";

	EpisodeIndexer(boolean overwrite){
		create = overwrite;

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
		
		if(create) {
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		} else {
			iwc.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
		}
		
		Directory dir = FSDirectory.open(Paths.get(indexPath));
		
		writer = new IndexWriter(dir, iwc);

	}

	public void index(String directory){
		System.out.println("Indexing episodes stored in " + directory);
		
		File folder = new File(directory);
		File[] files = folder.listFiles();
		
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