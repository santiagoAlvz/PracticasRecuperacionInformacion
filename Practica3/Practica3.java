package Practica3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.pattern.SimplePatternSplitTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class Practica3 {
    private static void readData(){
        String folderPath = "CapitulosUnidos"; // Path to the folder containing the files

        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        Float sum_rating = 0.0f;
        Float sum_votes = 0.0f;
        int num_files = 0;
        int num_votes = 0;

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        Reader reader = new FileReader(file);
                        if (!reader.ready()){
                            System.out.println("Error");
                        }
                        CSVReader csvReader = new CSVReader(reader);
                        String[] nextRecord;
                        String[] firstLine;
                        int i = 1;
                        firstLine = csvReader.readNext();
                        while((nextRecord = csvReader.readNext()) != null){
                            Float rating = Float.parseFloat(nextRecord[4]);
                            Float vote = Float.parseFloat(nextRecord[5]);
                            sum_rating += rating;
                            sum_votes += vote;
                            i++;
                        }
                        
                        reader.close();
                        num_files++;
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    catch (CsvValidationException e){
                        System.out.println(e.getMessage());
                    }        
                }
            }
            Float avg_rating = sum_rating / num_files;
            Float avg_votes = sum_votes / num_files;
            System.out.println("Average rating: " + avg_rating);
            System.out.println("Average votes: " + avg_votes);
        }

        //Obtaining the average characters that have dialogue
        folderPath = "Capitulos"; // Path to the folder containing the files

        folder = new File(folderPath);
        files = folder.listFiles();

        int sumCharacters =  0;
        num_files = 0;

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        Reader reader = new FileReader(file);
                        if (!reader.ready()){
                            System.out.println("Error");
                        }

                        CSVReader csvReader = new CSVReader(reader);
                        String[] nextRecord;
                        String[] firstLine;

                        Set<String> characters = new HashSet<String>();

                        int i = 1;
                        firstLine = csvReader.readNext();

                        while((nextRecord = csvReader.readNext()) != null){
                            characters.add(nextRecord[4]);
                        }
                        
                        reader.close();

                        num_files++;
                        sumCharacters += characters.size();
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    catch (CsvValidationException e){
                        System.out.println(e.getMessage());
                    }        
                }
            }
            Float avg_characters = (float)sumCharacters / num_files;
            System.out.println("Average characters: " + avg_characters);
        }
    }


    public static void main(String[] args) {
        readData();

        try {

            CharacterTextAnalyzer charAnalyzer = new CharacterTextAnalyzer();
            String text = "Ralph Wiggum, Miss Hoover, Chief Wiggum, Milhouse Van Houten, JANEY, Hayseed #2, Agnes Skinner, Apu Nahasapeemapetilon, Marge Simpson, Class, Nelson Muntz, Company, Homer Simpson, Sherri Mackleberry, Baby Bart, Lisa Simpson, Gary Chalmers, Seymour Skinner, Groundskeeper Willie, Rowdy Soldier, Colonel, Private, Ned Flanders, Martin Prince, Flanders's Beatnik Dad, Kearney Zzyzwicz, Hayseed #1, Leopold, Luigi, Jimbo Jones, Bart Simpson, Edna Krabappel-Flanders, Lunchlady Doris";

            TokenStream sf = charAnalyzer.tokenStream(null, text);

            System.out.println("\nCustom Analyzer demo: ");
            System.out.println("Initial text: " + text);
            System.out.println("Obtained tokens:");
            
            sf.reset();
            while (sf.incrementToken()) {
                System.out.println("\t" + sf.getAttribute(CharTermAttribute.class));
            }
            sf.end();
            sf.close();

        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }  
}

public class CharacterTextAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        SimplePatternSplitTokenizer src = new SimplePatternSplitTokenizer(",");
        TokenStream result = new LowerCaseFilter(src);
        result = new TrimFilter(result);
        return new TokenStreamComponents(src, result);
    }
}
