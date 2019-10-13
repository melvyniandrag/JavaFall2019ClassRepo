/*
 * This code is quite simple, yet involves quite a bit of knowledge about Java and text encodings.
 * ( Note to instructor = make sure to look at input files hello_utf8/16 and doystoyevsky_utf8/16 using xxd to 
 * verify hex contents are utf8/utf16 )
 *
 * Note that the return type of fileReader.read() is an int, not a char. What file type are we reading if we are returning an int?? 
 *
 * Immediately, many questions come to my mind. Two of them are 
 * 1. Is the int i encoded utf-16, or utf8, or something else?
 * 2. Can the int i store 2, 3, and 4 byte utf8 characters?
 * 3. In this code I call 'System.out.println(i)' - verify with your ASCII table that the output is correct with respect to hello_utf8.txt
 *
 * Run this code against doystoyevsky-utf8.txt
 *
 * Note that the output is 10 integers, not 5 as we would expect from a utf8 reader. Not good. This FileReader seems to be reading byte by byte and not Character by Character 
 * as indicated in the documentation.
 *
 * See the documentation here: https://docs.oracle.com/javase/8/docs/api/java/io/Reader.html#read-char:A-
 * It says that read() "Reads a single character. This method will block until a character is available . . ."
 * Whatever the case, it did nto read a character, it read a byte.
 *
 * Then try to run the code against the utf16 files. Inspect the behavior. You will see that it runs fine, but doesn't return use code points. Instead, it returns 10 ints. So now
 * not only do we not have characters, we have a mess of integers. We have to turn the integers to bytes, group them, and then convert the pairs to characters. 
 * Too much work!!
 *
 * Whatever the case, I don't like this FileReader class. I don't know what it's doing internally, the documentation is imprecise.
 *
 * Maybe it's a good class, but I read about it for 15 minutes and still didn't know exactly what it's supposed to be doing. Next.
 */


import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;


public class FileReaderExample{ 
	public static void main(String[] args){
		File helloFile = new File(args[0]);
		FileReader fileReader = null;
		try{
			fileReader = new FileReader(helloFile);
		} catch( FileNotFoundException ex ){
			System.out.println(ex.getMessage());
			return;
		}
		
		int i = 0;
		try{
			i = fileReader.read();
		}
		catch (IOException ex ){
			System.out.println(ex.getMessage());
			return;
		}
		while( i != -1){
			System.out.println(String.format("%04X", i));
			try{
				i = fileReader.read();	
			}
			catch(IOException ex){
				System.out.println(ex.getMessage());
				return;
			}
		}
		try{
			fileReader.close();
		} catch ( IOException ex ){
			System.out.println(ex.getMessage());
			return;
		}
	}
}
