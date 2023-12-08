package practica6;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.pattern.SimplePatternSplitTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.TokenStream;

public class CharacterTextAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        SimplePatternSplitTokenizer src = new SimplePatternSplitTokenizer(",");
        TokenStream result = new LowerCaseFilter(src);
        result = new TrimFilter(result);
        return new TokenStreamComponents(src, result);
    }
}