package Practica3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Practica3 {
    private static void readData(){
        String folderPath = "CapitulosUnidos"; // Path to the folder containing the files

        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
    
                    for (CSVRecord csvRecord : csvParser) {
                        // Access individual fields using csvRecord.get(index)
                        // For example, csvRecord.get(0) will give you the first field.
                        String field1 = csvRecord.get(0);
                        String field2 = csvRecord.get(1);
                        
                        // Process your data here
                    }
                                
                            
                    } else {
                            System.err.println("El directorio no existe o está vacío.");
                    }
                }
        }
    }


    public static void main(String[] args) {
        readData();
    }
    
}
