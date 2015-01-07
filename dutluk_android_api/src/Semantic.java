import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class Semantic {

        public static ArrayList<String> getSimilar(String query) throws IOException{
                ArrayList<String> sim = new ArrayList<String>();

                String requestURL = "https://www.googleapis.com/freebase/v1/search?lang=en&indent=true&key=AIzaSyDaFirSUUeGlNSg7bxGcyqdf9L8NzHKA10&query=" + query;
                URL freebaseRequest = new URL(requestURL);
                URLConnection connection = freebaseRequest.openConnection();  
                connection.setDoOutput(true);  

                Scanner scanner = new Scanner(freebaseRequest.openStream());
                String response = scanner.useDelimiter("\\Z").next();
                scanner.close();
                
                String[] lines = response.toString().split(System.getProperty("line.separator"));
                
                //String pattern = "\"name\".*\"($1)\"";

                for(int i=0;i<lines.length;i++){
                        if(lines[i].contains("       \"name\":")){
                                String x = lines[i].substring(17, lines[i].lastIndexOf('"'));
                                sim.add(x);
                        }
                }
                
                return sim;
        }
        
        /*
         * As an example:
         * 
         * Semantic.getSimilar("cat");
         * should return an array list consisting of
         * 
         * Domesticated animal, Diagnostic Test, Biological Family
         * Biological Species, Animal breed, Invention, Animal breed
         * Soft rock Artist, Comic Book Character, Organism Classification
         * Biological Species, Organism Classification, Comic Book Character
         * Biological Order, Animal breed, Animal breed
         */
}