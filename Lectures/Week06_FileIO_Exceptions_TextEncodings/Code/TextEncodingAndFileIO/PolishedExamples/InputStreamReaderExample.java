/**
 * I stole this code off the internet and tweaked it a bit. Not sure where I got it from.
 */
import java.io.*;
import java.nio.charset.Charset;

public class InputStreamReaderExample{
	public static void main(String[] args) throws Exception{
		//File file = new File("hello-utf16.txt");
		//File file = new File("hello-utf8.txt");
		if( args.length != 2 ){
			System.err.println("Provide two command line arguments. filename, then encoding ( liked UTF-8 or UTF-16)");
		}
		File file = new File(args[0]);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName(args[1])));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    
		    String fileContent = sb.toString();
	    	    System.out.println(fileContent);
		    
		} finally {
		     br.close();
		}
	}
}
