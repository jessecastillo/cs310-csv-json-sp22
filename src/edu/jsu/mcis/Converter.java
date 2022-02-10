package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following:
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        (Tabs and other whitespace have been added here for clarity.)  Note the
        curly braces, square brackets, and double-quotes!  These indicate which
        values should be encoded as strings, and which values should be encoded
        as integers!  The data files which contain this CSV and JSON data are
        given in the "resources" package of this project.
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity and readability.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            
            JSONObject jsonO = new JSONObject();
            JSONArray colH = new JSONArray(); 
            JSONArray rowH = new JSONArray(); 
            JSONArray data = new JSONArray();            
            
            JSONArray current;
            String[] line = iterator.next();
            
            for (int i = 0; i < line.length; ++i) {     
                colH.add(line[i]);
            }
            
            while (iterator.hasNext()) {        
                line = iterator.next();       
                current = new JSONArray();
                rowH.add(line[0]);        
                
                for (int j = 1; j < line.length; ++j) { 
                    int data_convert = Integer.parseInt(line[j]);   
                    current.add(data_convert);
                }
                data.add(current);                   
            }
            jsonO.put("colHeaders", colH);
            jsonO.put("rowHeaders", rowH);
            jsonO.put("data", data);
            
            results = JSONValue.toJSONString(jsonO);
            
            
        }        
        catch(Exception e) { e.printStackTrace(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");
            
      
           JSONParser parser = new JSONParser();
           JSONObject jsonO = (JSONObject)parser.parse(jsonString);
           
           JSONArray colHJson = (JSONArray)jsonO.get("colHeaders");
           JSONArray rowHJson = (JSONArray)jsonO.get("rowHeaders");
           JSONArray dataJson = (JSONArray)jsonO.get("data");
           
           int colsize = colHJson.size();
           int rowsize = rowHJson.size();

           String[] colHeaders = new String[colsize];
           
           for (int i = 0; i < colsize; ++i) {
               colHeaders[i] = (String)colHJson.get(i);     
           }
           csvWriter.writeNext(colHeaders);
           
           for (int i = 0; i < rowsize; ++i) {
               JSONArray data_line = (JSONArray)dataJson.get(i);
               String[] current = new String[colsize];
               current[0] = (String)rowHJson.get(i);
               
               for (int j = 0; j < data_line.size(); ++j) {
                   current[j+1] = Long.toString((long) data_line.get(j));
               }
               
               csvWriter.writeNext(current);
           }
           
           results = writer.toString();
        }
            
        
            
        
        
        catch(Exception e) { e.printStackTrace(); }
        
        return results.trim();
        
    }

}