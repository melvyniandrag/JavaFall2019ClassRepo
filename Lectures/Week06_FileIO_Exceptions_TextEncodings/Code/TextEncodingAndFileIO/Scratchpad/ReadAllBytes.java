/*
 *
 * Here is one way to read all the bytes in a file without caring what the text encoding in the file is.
 */

import java.io.File;
import java.nio.file.Files;

public class ReadAllBytes{
	public static void main(String[] args) throws Exception{
		File file = new File("hello-utf16.txt");
		byte[] fileContent = Files.readAllBytes(file.toPath());
		for( byte b : fileContent ){
			String s = String.format("%02X", b);
			System.out.println(s);
		}
	}
}
