/*
 * This will compile because the checked exception is caught and handled in goodMethod().
 * In WillCompile01 we threw the exception from goodMethod() to main(), but here we just handle it in
 * goodMethod(). The function no longer needs the 'throws' keyword. 
 */

import java.io.*;

public class WillCompile02{

	/*
	 * FileNotFoundException could occur in this method call because FileReader() might
	 * not be able to read a file if 'filename' doesn't exist on your computer!
	 */
	public static void goodMethod( String filename ){
		try{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		}
		catch( Exception e ){
			System.out.println("Exceptin occured in goodMethod() while trying to open: " + filename );
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args){
		String filename = "arrrgggggg";
		goodMethod(filename);
	}
}
