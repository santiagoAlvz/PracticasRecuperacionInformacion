package Practica3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

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
    }


    public static void main(String[] args) {
        readData();
    }
    
}
