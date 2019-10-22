import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ChangeBackground{
	
	public static int[][] ReadPPMIntoIntArray(String fileName) throws FileNotFoundException, IOException{
	    final int[][] ErrorRetVal = null;

		File f = new File(fileName);	
		byte[] fileArr = new byte[(int) f.length()];
		InputStream is = new BufferedInputStream(new FileInputStream(f));
		for( int i = 0, read_value = 0; ( read_value = is.read() ) != -1; i++ ){
			fileArr[i] = (byte)read_value;
		}
		String fileString = new String(fileArr, Charset.forName("UTF-8"));
		String[] stringArr = fileString.split("\\R+"); // split on new lines.
		
		String imageTypeString = stringArr[0];
		if( !imageTypeString.equals( new String("P3") ) ){
			System.err.println("First line in PPM invalid");
			return ErrorRetVal;
		}	
		
		String dimensionString = stringArr[1];
		String[] dimensionStrings = dimensionString.split("\\s+");
		if( dimensionStrings.length != 2 ){
			System.err.println("Invalid dimension specified.");
			return ErrorRetVal;
		}
		final int Width  = Integer.parseInt(dimensionStrings[0]);
		final int Height = Integer.parseInt(dimensionStrings[1]);
			
		String maxValueString  = stringArr[2]; // I think the 255 line means maxValue, but have still failed to verify.
			
		return ErrorRetVal;
	}

	public static void ChangeWhiteToRed(int[][] inputArray){
	}

	public static void WritePPMFile(int[][] Pixels, String fileName){
	}
}
