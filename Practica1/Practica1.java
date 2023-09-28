/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplosimple;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.parser.txt.UniversalEncodingDetector;

/**
 *
 * @author jhg
 */

public class Practica1 {

  public static void main(String[] args) throws Exception {

    // Creamos una instancia de Tika con la configuracion por defecto

    File directory = new File(args[0]);
    File[] files = directory.listFiles();

    if(files == null){
      System.out.println("Error. Specified directory wasn't found");
      return;
    }

    switch(args[1]){
      case "-d":
        fileSummary(files);
        break;

      case "-l":
        System.out.println("L option");
        break;

      case "-t":
        frequencyCount(files, args[0]);
        break;

      default:
        System.out.println("Error. Unrecognized option");
        break;
    }
  }

  private static void fileSummary(File[] files){
    Tika tika = new Tika();
    String tableFormat = "%-30s %-55s %-20s %-20s";

    System.out.println("File Summary:");
    System.out.println("-".repeat(125));
    System.out.println(String.format(tableFormat, "Filename", "Content-Type", "Encoding", "Language"));
    System.out.println("-".repeat(125));

    UniversalEncodingDetector encDet = new UniversalEncodingDetector();
    LanguageDetector detector = new OptimaizeLangDetector().loadModels();
    
    for(File f: files){
      try {
        InputStream is = new FileInputStream(f);
        Metadata meta = new Metadata();
        BodyContentHandler ch = new BodyContentHandler(-1);
        ParseContext parseContext = new ParseContext();
        AutoDetectParser parser = new AutoDetectParser();

        parser.parse(is, ch, meta, parseContext);
        detector.addText(ch.toString());

        String encoding = meta.get(Metadata.CONTENT_ENCODING);

        if(encoding == null){
          encoding = "Unknown";
        }

        String lang = detector.detect().getLanguage();

        if(lang == ""){
          lang = "Unknown";
        }
        System.out.println(String.format(tableFormat, f.getName(), tika.detect(f), encoding, lang));
        detector.reset();

      } catch (Exception e){

      }
    }
  }

  private static void frequencyCount(File[] files, String folder){
    Tika tika = new Tika();
    String filename;
    
    for(File f: files){
      //HashMap<String, Integer> frequencyCount;

      try {
        InputStream is = new FileInputStream(f);
        Metadata meta = new Metadata();
        BodyContentHandler ch = new BodyContentHandler(-1);
        ParseContext parseContext = new ParseContext();
        AutoDetectParser parser = new AutoDetectParser();

        parser.parse(is, ch, meta, parseContext);

        filename = f.getName();
        filename = "word_count_" + filename.substring(0, filename.lastIndexOf('.')) + ".csv";

        FileWriter output = new FileWriter(folder + "/" +filename);

        String[] words = ch.toString().split(" ");

        for(String word: words) {
          /*if( frequencyCount.containsKey(word) ){

          }*/
        }

        output.close();

      } catch (Exception e){

      }
    }
  }
}
