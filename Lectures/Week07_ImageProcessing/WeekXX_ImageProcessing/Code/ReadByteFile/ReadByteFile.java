import java.io.File;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ReadByteFile{ 
	public static void main(String[] args) throws Exception{
		File f = new File("ByteFile.txt");
		byte[] fileArr = new byte[(int) f.length()];
		InputStream is = new BufferedInputStream(new FileInputStream(f));
		for( int i = 0, read_value = 0; ( read_value = is.read() ) != -1; i++ ){
			fileArr[i] = (byte)read_value;

		}
		System.out.println("What bytes were read from the file?");
		for( byte i : fileArr ){
			System.out.println(String.format("0x%08X", i ) );
		}

		String fileString = new String(fileArr, Charset.forName("UTF-8"));
		System.out.println("What do those bytes translate to as text?");
		System.out.println(fileString);
		String[] allNumbers = fileString.split("\\s+");
		int[] ints = new int[allNumbers.length];
		int currIdx = 0;
		Arrays.fill(ints, 256); // should be no 256 in our arrays.
		for( String s : allNumbers ){
			try{
				int i = Integer.parseInt(s);
				ints[currIdx] = i;
				currIdx++;
			}
			catch(Exception e){
				System.out.println("Ignore this, can't convert to int: " + s );
			}
		}

		System.out.println("What does that text translate to as integers?");
		for( int i : ints ){
			System.out.println(String.format("0x%08X", i));
		}

		System.out.println("Can I set the last bit in every int to 0?");
		for( int i: ints ){
			System.out.println(String.format("0x%08X", (i & 0xFFFFFFFE)));
		}


		

	}
}
