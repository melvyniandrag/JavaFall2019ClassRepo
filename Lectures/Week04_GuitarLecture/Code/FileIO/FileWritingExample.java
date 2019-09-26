/**
 * Compile and run
 * then extend the write() command such that it prints a new line.
 * use the xxd command to show the class the trailing bytes in the file.
 */

import java.io.FileWriter;

public class FileWritingExample{
	public static void main(String[] args){
		FileWriter fw = null;
		try{
			fw = new FileWriter("MyFile.txt");
			fw.write("Hello World!\r\n");
		}
		catch( Exception e ){
			System.out.println(e.getMessage());
		}
		finally{
			try{
				fw.close();
			}
			catch(Exception e2){
				System.out.println(e2.getMessage());
			}
		}
	}
}
