/*
 * This wont compile. Just try! The compiler complains about the lack of 'throws' or 'try/catch'
 * in this code. The goodMethod() could throw a file exception becasue the file we are trying to 
 * read from may not exist. But the CheckedException is not caught and handled in main!
 */

import java.io.*;

public class BetterButStillWontCompile{

	/*
	 * FileNotFoundException could occur in this method call because FileReader() might
	 * not be able to read a file if 'filename' doesn't exist on your computer!
	 */
	public static void goodMethod( String filename ) throws FileNotFoundException{
		    BufferedReader reader = new BufferedReader(new FileReader(filename));
	}

	public static void main(String[] args){
		String filename = "arrrgggggg";
		goodMethod(filename);
	}
}
