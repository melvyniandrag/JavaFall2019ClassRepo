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

public class InputStreamExample{
	public static void main(String[] args){
		//File file = new File("hello-utf16.txt");
		File file = new File("hello-utf8.txt");
		//File file = new File("dostoyevsky-utf8.txt");
		//File file = new File("dostoyevsky-utf16.txt");
		try{
			InputStreamReader is = new InputStreamReader(new FileInputStream(file), "UTF-8");
			char[] buf = new char[(int) file.length()];
			is.read(buf, 0, buf.length);
			System.out.println(new String(buf, Charset.forName("UTF-8")));
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
