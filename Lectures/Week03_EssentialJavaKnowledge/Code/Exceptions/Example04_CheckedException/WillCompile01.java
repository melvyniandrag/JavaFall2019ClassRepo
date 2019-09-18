/*
 * This will compile because the checked exception is caught and handled in main(). 
 * 
 */

import java.io.*;

public class WillCompile01{

	/*
	 * FileNotFoundException could occur in this method call because FileReader() might
	 * not be able to read a file if 'filename' doesn't exist on your computer!
	 */
	public static void goodMethod( String filename ) throws FileNotFoundException{
		    BufferedReader reader = new BufferedReader(new FileReader(filename));
	}

	public static void main(String[] args){
		String filename = "arrrgggggg";
		try{
			goodMethod(filename);
		}
		catch( FileNotFoundException e ){
			System.out.println("Exception occured while trying to open: " + filename);
			System.out.println(e.getMessage());
		}
	}
}
