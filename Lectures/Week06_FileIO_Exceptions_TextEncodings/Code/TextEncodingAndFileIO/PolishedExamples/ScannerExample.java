import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScannerExample
{
    public static void main(String[] args){
        Scanner fileReader = null;
        try{
            fileReader = new Scanner(new File(args[0]), args[1]);
            
        }
        catch(FileNotFoundException ex){
            System.err.println("Unable to open file " + args[0] + " : " + ex.getMessage());
        }
        catch(Exception ex2){
            System.err.println("Other error " + ex2.getMessage());
        }

        if(fileReader != null){
            List<String> lines = new ArrayList<String>();
            while(fileReader.hasNext()){
                lines.add(fileReader.next());
            }

            for(String line : lines){
                System.out.println(line);
            }
        }
    }
}
