/**
 * I stole this code off the internet and tweaked it a bit. Not sure where I got it from.
 *
 * This code is hopelessly broken on my windows computer.
 *
 * How does the input know the text encoding????
 * Why does the input happily read the hello-utf16?? Does java discard null bytes??
 * That is VERY weird behavior in the context of other programming languages!!
 *
 *Actually, look at this:
 * java ModifiedInputStreamExample | head -n2 | tail -n1 | xxd
 *
 * Note that the output bytes are as expected, they are utf-16 bytes. But the terminal interprets them
 * as utf-8 bytes.
 */
import java.io.*;
import java.nio.charset.Charset;

public class ModifiedInputStreamExample{
	public static void main(String[] args) throws Exception{
		//File file = new File("hello-utf16.txt");
		//File file = new File("hello-utf8.txt");
		//File file = new File("dostoyevsky-utf8.txt");
		File file = new File("dostoyevskyUTF16.txt");
		try{
		
		InputStreamReader is = new InputStreamReader(new FileInputStream(file), "UTF-16");
		byte[] buf = new buf[(int) file.length()];
		System.out.println(new String(buf, Charset.forName("UTF-16")));
		}
		catch(Exception ex){
			System.out.println(e.getMessage());
		}
		/*

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-16"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    System.out.println(line);
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    
		    String fileContent = sb.toString(); // The string is interpereted with respect to the default encoding. For one byte code points it works okay.
	            System.out.println("length of unconverted string: " + fileContent.length());
		    System.out.println(fileContent);

		    final byte[] utfString16 = fileContent.getBytes(Charset.forName("UTF-16"));
		    final byte[] utfString8 = fileContent.getBytes(Charset.forName("UTF-8"));
		   
		    String fileContent16 = new String(utfString16, Charset.forName("UTF-16"));
		    System.out.println(fileContent16);
			
		    String fileContent8 = new String(utfString8, Charset.forName("UTF-8"));
		    System.out.println(fileContent8);
 
		    
		} finally {
		     br.close();
		}
		*/
	}
}
