/**
 * I stole this code off the internet and tweaked it a bit. Not sure where I got it from.
 *
 * This code is hopelessly broken on my windows computer.
 */
import java.io.*;
import java.nio.charset.Charset;

public class InputStreamReaderExample{
	public static void main(String[] args) throws Exception{
		//File file = new File("hello-utf16.txt");
		//File file = new File("hello-utf8.txt");
		File file = new File("dostoyevsky-utf8.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    
		    String fileContent = sb.toString();
	            System.out.println("length of unconverted string: " + fileContent.length());
		    System.out.println(fileContent);

		    final String defaultEncoding = Charset.defaultCharset().name();
		    System.out.println("Machine default encoding is: " + defaultEncoding);
		    final byte[] utfString = fileContent.getBytes(Charset.forName(defaultEncoding));
		    for( byte b : utfString){
			System.out.println(String.valueOf(b));
		    }
		    fileContent = new String(utfString, Charset.forName("UTF-8"));
		    System.out.println("length of converted string: " + fileContent.length());
		    System.out.println(fileContent);

		    
		} finally {
		     br.close();
		}
	}
}
