package textencodings;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.Charset;

public class SimpleWriter{
    public static void main(String[] args){
        List<String> stringList = new ArrayList<String>();
        String hi = "hello";
        String world = "world"; 
        stringList.add(hi);
        stringList.add(world);
        
        Path utf8path = Paths.get("src/output-utf8.txt");
        Path utf16path = Paths.get("src/output-utf16.txt");
        try {
        	Files.write(utf8path, stringList, Charset.forName("UTF8"));
        	Files.write(utf16path, stringList, Charset.forName("UTF16"));
        }
        catch(IOException ex){
        	System.err.println(ex.getMessage());
        }
    }   
}
