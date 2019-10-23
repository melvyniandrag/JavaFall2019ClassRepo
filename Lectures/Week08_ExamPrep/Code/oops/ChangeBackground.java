import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ChangeBackground{

	private final static int CHANNELS_PER_PIXEL = 3;
	
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
		int[][] ret = new int[Height*CHANNELS_PER_PIXEL][Width*CHANNELS_PER_PIXEL];		
		System.out.println(String.format("Read an %dx%d image", Width, Height));			
		if(	stringArr.length != Height + 3 ){
			System.err.println("Not enough rows in datafile.");
			return ErrorRetVal;
		}
		String maxValueString  = stringArr[2]; // I think the 255 line means maxValue, but have still failed to verify.
		final int MaxValue = Integer.parseInt(maxValueString);
		
		for( int lineIndex = 3; lineIndex < stringArr.length; lineIndex++){
			String[] numbers = stringArr[lineIndex].split("\\s+");
			if( numbers.length != Width * CHANNELS_PER_PIXEL ){
				System.err.println(String.format("Found invalid number of pixels in row %d of ppm image", lineIndex));
				return ErrorRetVal;
			}
			else{
				for( int numberIdx = 0; numberIdx < numbers.length; ++numberIdx){
					ret[lineIndex - 3][numberIdx] = Integer.parseInt(numbers[numberIdx]);
				}	
			}
		}
				
		return ret;
	}

	public static void ChangeWhiteToRed(int[][] inputArray){
		for( int row = 0; row < inputArray.length; row++ ){
			for( int col = 0; col < inputArray[row].length; col += 3 ){
				//Here comes the background changing logic!
				if(PixelIsWhite(inputArray[row][col], inputArray[row][col+1], inputArray[row][col+2])){
					inputArray[row][col]=255;
					inputArray[row][col+1] = 0;
					inputArray[row][col+2] = 0;
				}
			}
		}
	}
	
	public static boolean PixelIsWhite(int red, int green, int blue){
		final int THRESHOLD = 200;
		if( (red > THRESHOLD) && (green > THRESHOLD) && (blue > THRESHOLD) ){
			// then the pixel is white ( by our standards ).
			// btw how do you say 'threshold' in spanish and portuguese?
			return true;	
		}
		return false;
	}

	public static void WritePPMFile(int[][] Pixels, String fileName) throws IOException, FileNotFoundException{
		OutputStreamWriter osw = new OutputStreamWriter(
				new FileOutputStream(
					new File(fileName),
					true
				),
				Charset.forName("UTF-8")
			);
		osw.write("P3\n");
		osw.write(String.format("%d %d\n", Pixels[0].length, Pixels.length));
		osw.write("255\n");
		for( int row = 0; row < Pixels.length; ++row ){
			int col = 0;
			for( col = 0; col < Pixels[0].length - 1; ++col ){
				osw.write(String.valueOf(Pixels[row][col]) + " ");
			}
			osw.write(String.valueOf(Pixels[row][col]) + "\n");
		}		
	}
}
