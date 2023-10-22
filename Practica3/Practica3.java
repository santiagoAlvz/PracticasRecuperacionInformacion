package Practica3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;

// import org.apache.commons.csv.CSVFormat;
// import org.apache.commons.csv.CSVParser;
// import org.apache.commons.csv.CSVRecord;
import com.opencsv.CSVReader;

public class Practica3 {
    private static void readData(){
        String folderPath = "CapitulosUnidos"; // Path to the folder containing the files

        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        Reader reader = new FileReader(file);
                        if (!reader.ready()){
                            System.out.println("Error");
                        }
                        CSVReader csvReader = new CSVReader(reader);


                        reader.close();
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    
                }
            }
        }
    }


    public static void main(String[] args) {
        readData();
    }
    
}
