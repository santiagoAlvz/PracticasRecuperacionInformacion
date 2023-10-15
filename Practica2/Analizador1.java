/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.classic.ClassicFilter;
import org.apache.lucene.analysis.classic.ClassicTokenizer;
import org.apache.lucene.analysis.classic.ClassicTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilterFactory;
import org.apache.lucene.analysis.pattern.SimplePatternSplitTokenizerFactory;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.SynonymMap.Builder;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.analysis.commongrams.CommonGramsFilter;

/**
 *
 * @author jhg
 */
public class Analizador1 {

    public static void muestraTexto(Analyzer an, String cadena) throws IOException {
        TokenStream sf = an.tokenStream(null, cadena);

        sf.reset();
        while (sf.incrementToken()) {

            System.out.println(sf.getAttribute(CharTermAttribute.class));
        }
        sf.end();
        sf.close();
    }

    public static Analyzer crearAnalyzer() throws IOException {

        Analyzer lineaAnalyzer = CustomAnalyzer.builder()
                .addCharFilter(PatternReplaceCharFilterFactory.NAME, "pattern", "[/(/)/{/};]", "replacement", "X")
                .withTokenizer(ClassicTokenizerFactory.NAME)
                .addTokenFilter(LowerCaseFilterFactory.NAME)
                .build();
        System.out.println(lineaAnalyzer.getClass());
        return lineaAnalyzer;

    }
    
    public static Analyzer crearAnalyzer2() {
        Analyzer mi_analizador = new Analyzer() {
            @Override
            protected Analyzer.TokenStreamComponents createComponents(String todo) {
                Tokenizer source = new WhitespaceTokenizer();

                TokenStream filter = source; // new ClassicFilter(source);

                filter = new EdgeNGramTokenFilter(filter, 3, 3, true);
                //   TokenStream  filter = new LowerCaseFilter(source);
                filter = normalizar(filter);
                return new Analyzer.TokenStreamComponents(source, filter);
            }

            protected TokenStream normalizar(TokenStream in) {

                return new LowerCaseFilter(in);
            }
        };
        return mi_analizador;
    }

    public static Analyzer myAnalyzer(
        final String language, 
        final CharArraySet stopwords, 
        SynonymMap synonymMap, 
        final CharArraySet commonWords,
        boolean stop,
        boolean snowball,
        boolean shingle,
        boolean edgeNGramToken,
        boolean nGramToken,
        boolean commonGrams,
        boolean synonym
    ) {
        return new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String string) {
                // Create the tokenizer (use StandardTokenizer)
                Tokenizer source = new StandardTokenizer();

                // Create a filter chain (use LowerCaseFilter)
                TokenStream result = new LowerCaseFilter(source);

                /* Removes stop words from a token stream. */
                if (stop) result = new StopFilter(result, stopwords);

                /* stem words based on a specific stemmer ex. “indexing”, “indexes”, etc will be stemmed as “index”.*/
                if (snowball) result = new SnowballFilter(result, language);
                
                /*  A ShingleFilter constructs shingles (token n-grams) from a token stream. 
                In other words, it creates combinations of tokens as a single token. */
                if (shingle) result = new ShingleFilter(result); 

                /* Tokenizes the given token into n-grams of given size(s). */
                if (edgeNGramToken) result = new EdgeNGramTokenFilter(result, 2);
                
                /* Tokenizes the input into n-grams of the given size(s). 
                    - handles supplementary characters correctly,
                    - emits all n-grams for the same token at the same position,
                    - does not modify offsets,
                    - sorts n-grams by their offset in the original token first, 
                        then increasing length (meaning that "abc" will give "a", "ab", "abc", "b", "bc", "c").
                 */
                if (nGramToken) result = new NGramTokenFilter(result, 2);
                
                /* Construct bigrams for frequently occurring terms while indexing. 
                   Single terms are still indexed too, with bigrams overlaid. 
                   This is achieved through the use of PositionIncrementAttribute.setPositionIncrement(int). 
                   Bigrams have a type of GRAM_TYPE Example:
                    input:"the quick brown fox"
                    output:|"the","the-quick"|"brown"|"fox"|
                    "the-quick" has a position increment of 0 so it is in the same position as "the" "the-quick" has a term.type() of "gram" */
                
                if (commonGrams) result = new CommonGramsFilter(result, commonWords);
                
                /*
                 Matches single or multi word synonyms in a token stream. 
                 This token stream cannot properly handle position increments != 1, 
                 ie, you should place this filter before filtering out stop words.
                 */
                if (synonym) result = new SynonymFilter(result, synonymMap, true);
                
                return new TokenStreamComponents(source, result);
            }
        };
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
            
        /*String cadena = "Ejemplo,  whiteSpace (3+2) \n de analizador + WhiteSpace, \n lucene-7.1.0 \n"
                + "Analyzer mi_analizador = new Analyzer() {\n"
                + "            @Override\n"
                + "            protected Analyzer.TokenStreamComponents createComponents(String todo) {\n"
                + "                Tokenizer source = new WhitespaceTokenizer(); \n";*/
        // String cadena = "hola hola hola";

        // // Analyzer an = new WhitespaceAnalyzer();
        // Analyzer an = new SimpleAnalyzer();

        // System.out.println("Ejemplo SimpleAnalyzer");
        // muestraTexto(an, cadena);

        // System.out.println("Ejemplo nuevo");
        // Analyzer nuevo = crearAnalyzer();
        // muestraTexto(nuevo, cadena);
                
        // System.out.println("Ejemplo otro");

        // Analyzer otro = crearAnalyzer2();
        // muestraTexto(otro, cadena);
        
        
        String text = "the quick brown fox";
    
        List<String> stopWordsList = Arrays.asList("a", "an", "the","is","this","and");

        Builder builder = new SynonymMap.Builder(true);
        // Add your synonym rules
        builder.add(new CharsRef("happy"), new CharsRef("joyful"), true);
        builder.add(new CharsRef("car"), new CharsRef("automobile"), true);
        SynonymMap synonymMap = builder.build();

        List<String> commonWords = Arrays.asList("the","a","that","they");

        Analyzer customAnalyzer = myAnalyzer(
            "English", new CharArraySet(stopWordsList, true), 
            synonymMap, 
            new CharArraySet(commonWords, true),
            true, //StopFilter,
            false, //SnowballFilter
            false, //ShingleFilter
            false, //EdgeNGramCommonFilter
            false, //NGramTokenFilter
            false, //CommonGramsFilter
            false //SynonymFilter
        );

        muestraTexto(customAnalyzer, text);
    }
}
