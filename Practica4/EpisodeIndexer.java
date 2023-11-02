import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import import org.apache.lucene.index.IndexWriter;

import java.util.Map;
import java.util.HashMap;

public class EpisodeIndexer {

	private IndexWriter writer;
	bool create = false;

	EpisodeIndexer(boolean overwrite){
		create = overwrite;

		configureIndex();
	}

	void configureIndex(){
		Map<String,Analyzer> analyzerPerField = new HashMap<String,Analyzer>();
		analyzerPerField.put("spoken_words", new EnglishAnalyzer());
		analyzerPerField.put("raw_character_text", new CharacterTextAnalyzer());
		analyzerPerField.put("title", new EnglishAnalyzer());

		PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(), analyzerPerField);
		Similarity sim = new ClassicSimilarity();

		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setSimilarity(sim);

	}

	public static void index(String directory){
		System.out.println("Indexing episodes stored in " + directory);
	}
}