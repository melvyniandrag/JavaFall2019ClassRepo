/*
 * Stolen from: https://www.tutorialspoint.com/java/java_files_io.htm
 *
 * I suspect this is wrong. I'm not sure if it is because I use the JDK on my computer and the Oracle JDK on others?
 *
 * In a previous example we saw that the FileReader was reading one byte at a time, not two as this example says.
 * There is alot of misinformation about Java file handling and text encodings on the internet.
 *
 * In general , the internet is full of liars, scoundrels, idiots, and more. Be careful what you read and how you interpret it.
 * Here is what the example says:
 *
 *
 *	"""
 *	Java Byte streams are used to perform input and output of 8-bit bytes,
 *	whereas Java Character streams are used to perform input and output for
 *	16-bit unicode. Though there are many classes related to character streams
 *	but the most frequently used classes are, FileReader and FileWriter.
 *	Though internally FileReader uses FileInputStream and FileWriter uses
 *	FileOutputStream but here the major difference is that FileReader reads two
 *	bytes at a time and FileWriter writes two bytes at a time.
 *	"""
 */
import java.io.*;
public class CopyFile {

   public static void main(String args[]) throws IOException {
      FileReader in = null;
      FileWriter out = null;

      try {
         in = new FileReader("inputUTF16.txt");
         out = new FileWriter("outputUTF16.txt");
         
         int c;
         while ((c = in.read()) != -1) {
		System.out.println(String.valueOf(c));
		 out.write(c); // Here note that c is being truncated to the last two bytes.
		 // See the documentation here: https://docs.oracle.com/javase/7/docs/api/java/io/Writer.html#write(char[])
		 // for 'public void write(int c)'

		 // TODO uncomment the line here to show that the whole character isn't being written, only the first byte.
		 // So write(int c) is actually just writing the last byte of a 4 byte int. Weird and frustrating.
		 //out.close(); // uncomment this and then run the file through xxd. Youll see that only one byte has been written.
		// return;
         }
      }finally {
         if (in != null) {
            in.close();
         }
         if (out != null) {
            out.close();
         }
      }
   }
}
