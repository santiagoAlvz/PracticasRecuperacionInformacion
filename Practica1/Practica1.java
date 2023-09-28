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
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.parser.txt.UniversalEncodingDetector;
import org.apache.tika.sax.LinkContentHandler;


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
        findLinks(files);
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

    UniversalEncodingDetector encDet = new UniversalEncodingDetector();
    LanguageDetector detector = new OptimaizeLangDetector().loadModels();

    System.out.println("File Summary:");
    System.out.println("-".repeat(125));
    System.out.println(String.format(tableFormat, "Filename", "Content-Type", "Encoding", "Language"));
    System.out.println("-".repeat(125));
    
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
    HashMap<String, Integer> frequencyCount = new HashMap<String, Integer>();
    
    for(File f: files){
      frequencyCount.clear();

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

        String[] words = ch.toString().toLowerCase().replaceAll("\\r\\n|\\r|\\t|\\n", " ").replaceAll("[^\\s\\p{L}\\p{N}]", "").split(" ");

        for(String word: words) {
          if(word.length() > 0) {
            frequencyCount.put(word, frequencyCount.getOrDefault(word, 0) + 1);
          }
        }

        for( String s: frequencyCount.keySet()){
          output.write(s + ";" + frequencyCount.get(s) + "\n");
        }

        output.close();

        System.out.println("Counted words for file " + f.getName() + ". Output in " + filename);

      } catch (Exception e){

      }
    }
  }

  private static void findLinks(File[] files){
    // find the links of each file
    Tika tika = new Tika();
    try {
      for (File f : files){
        // if file is txt use another parse
        if (tika.detect(f).equals("text/plain")){
          // TXTParser can not find links
          continue;
        }
        else{
          // find the links
          InputStream is = new FileInputStream(f);
          BodyContentHandler textHandler = new BodyContentHandler(-1);
          LinkContentHandler linkHandler = new LinkContentHandler();
          Metadata meta = new Metadata();
          ParseContext parseContext = new ParseContext();

          AutoDetectParser parser = new AutoDetectParser();
          parser.parse(is, linkHandler, meta, parseContext);
          // print the links
          System.out.println("File: " + f.getName());
          System.out.println("-".repeat(125));
          if (linkHandler.getLinks().size() == 0){
            System.out.println("No links found");
          }
          else{
            for (int i = 0; i < linkHandler.getLinks().size(); i++){
              System.out.println(linkHandler.getLinks().get(i).getUri());
            }
          }
          System.out.println("-".repeat(125));
        }
      }
    }
    catch (Exception e){
      System.out.println("Error: " + e);
    }
  }
}
