/*
 * This wont compile. Just try! The compiler complains about the lack of 'throws' or 'try/catch'
 * in this code. The badMethod() could throw a file exception becasue the file we are trying to 
 * read from may not exist.
 */

import java.io.*;

public class WontCompile{
	public static void badMethod( String filename ){
		    BufferedReader reader = new BufferedReader(new FileReader(filename));
	}

	public static void main(String[] args){
		String filename = "arrrgggggg";
		badMethod(filename);
	}
}
