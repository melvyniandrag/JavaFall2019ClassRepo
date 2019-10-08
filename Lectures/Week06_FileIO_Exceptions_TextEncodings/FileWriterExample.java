/**
 * What encoding does this code generate?
 * Run the code and use a hex inspection tool like 'xxd' to inspect the file.
 *
 * You should note that it outputs the default encoding of your machine - but what if you want 
 * a different encoding??
 *
 * In java, the string "Hello" is stored in 10 bytes, but the output file only contains 5 because the FileWriter 
 * implicity changed the encoding of the string. This could be disastrous if you expected utf-16, or if you ran this code 
 * on a machine with a strange new encoding you never heard of.
 */

import java.io.FileWriter;

public class FileWriterExample{
	public static void main(String[] args){
		String hello = "Hello";
		try{
			FileWriter fw = new FileWriter("helloFileWriter.txt");
			fw.write(hello);
			fw.close();
		}
		catch ( Exception e ){
			System.err.println(e.getMessage());
			return;
		}

	}
}
