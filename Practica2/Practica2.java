/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador1;

import java.io.IOException;
import java.nio.file.Paths;
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
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilterFactory;
import org.apache.lucene.analysis.pattern.SimplePatternSplitTokenizerFactory;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

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
        String cadena = "hola hola hola";

        // Analyzer an = new WhitespaceAnalyzer();
        Analyzer an = new SimpleAnalyzer();

        System.out.println("Ejemplo SimpleAnalyzer");
        muestraTexto(an, cadena);

        System.out.println("Ejemplo nuevo");
        Analyzer nuevo = crearAnalyzer();
        muestraTexto(nuevo, cadena);
                
        
        
        System.out.println("Ejemplo otro");

        Analyzer otro = crearAnalyzer2();
        muestraTexto(otro, cadena);

    }

}
