import java.io.*;
import java.nio.charset.Charset;

public class OutputStreamWriterExample{
	public static void main(String[] args){
		try {
			final String Encoding = args[0];
			final String Filename = "output" + Encoding + ".txt";
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
				new File(Filename), true), Charset.forName(Encoding)
			);
			String hello = "Hello";
			osw.write(hello, 0, hello.length());
			osw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}


