/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplosimple;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

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
  Tika tika = new Tika();

  File directory = new File(args[0]);
  File[] files = directory.listFiles();

  if(files == null){
    System.out.println("Error. Specified directory wasn't found");
    return;
  }

  switch(args[1]){
    case "-d":

      UniversalEncodingDetector encDet = new UniversalEncodingDetector();
      LanguageDetector detector = new OptimaizeLangDetector().loadModels();
      
      for(File f: files){
        InputStream is = new FileInputStream(f);
        Metadata meta = new Metadata();
        BodyContentHandler ch = new BodyContentHandler(-1);
        ParseContext parseContext = new ParseContext();

        AutoDetectParser parser = new AutoDetectParser();

        try {
          parser.parse(is, ch, meta, parseContext);
          detector.addText(ch.toString());

        } finally {
          is.close();
        }

        String encoding = meta.get(Metadata.CONTENT_ENCODING);

        if(encoding == null){
          encoding = "Unknown";
        }

        String lang = detector.detect().getLanguage();

        if(lang == ""){
          lang = "Unknown";
        }
        System.out.println("\n"+String.format("%-30s %-55s %-20s %-20s", f.getName(), tika.detect(f), encoding, lang));
        detector.reset();

      }

      break;
    case "-l":
      System.out.println("L option");
      break;
    case "-t":
      System.out.println("T option");
      break;

    default:
      System.out.println("Error. Unrecognized option");
      break;
  }

  System.out.println(files.length);
  /*
  // Se parsean todos los ficheros pasados como argumento y se extrae el contenido
  for (String file : args) {
      File f = new File(file);
      
      String type = tika.detect(f);
      System.out.println(file +":"+type);
      
     
      String text = tika.parseToString(f);
      System.out.print(text);
      
     
  }

  String[] textos={"Esto es un ejemplo de uso del detector de lenguaje",
  "this is an example of language detector", 
  "bon jour, mademoiselle"};
  LanguageDetector detector = new OptimaizeLangDetector().loadModels();
  for (int i =0;i<3;i++){  
      detector.addText(textos[i]);
      System.out.println(detector.detect().getLanguage());
      detector.reset();
  }

  //System.out.println("Escrito en :"+detector.detect().getLanguage());
  */
  }
}
