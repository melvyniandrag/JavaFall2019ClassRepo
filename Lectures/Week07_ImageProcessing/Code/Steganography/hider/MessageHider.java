import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

public class MessageHider{
	
	/**
	 * Read a png image file into a 2D array of ARGB values.
	 *
	 * @param fileName - the name of the image we want to read in
	 * @return A 2D array of ARGB values in row major order.
	 * @throws NoAlphaChannelException if the image being read has no Alpha channel.
	 * @throws IOException if the image can't be read.
	 */
	public static int[][] imageTo2DArr(String fileName) throws NoAlphaChannelException, IOException {
		BufferedImage image = ImageIO.read(MessageHider.class.getResource(fileName));
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		int[][] result = new int[height][width];
		final int pixelLength = 4;
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;
		if( !hasAlphaChannel ){
			throw new NoAlphaChannelException("This simple code can only handle pngs with an alpha channel. Yours has none.");
		}

		for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
			int argb = 0; // argb = Alpha,Red, Green, Blue
			int blue = ((int) pixels[pixel + 1] & 0xff);
			int green = (((int) pixels[pixel + 2] & 0xff) << 8);
			int red = (((int) pixels[pixel+3] & 0xff ) << 16 );
			int	alpha =  (((int) pixels[pixel] & 0xff) << 24); 
			argb += blue;
			argb += green;
			argb += red;      
			argb += alpha;
			result[row][col] = argb;
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
		return result;
	}


	/**
	 * Hide an array of bytes in a 2d byte array.
	 *
	 * @param toHide - byte array containing the message to hide
	 * @param imageArray - 2D byte array containing pixel vals
	 * @return void - imageArray is modified in place
	 *
	 * For each byte in imageArray, replace the last bit with a bit 
	 * from toHide until the bits are exhausted. At the time of
	 * writing this code, imageArray contains 4 byte ints. The bytes
	 * represent the ARGB channels ( a byte each for A,R,G,B ). As
	 * such, as we replace the last bit in each int in imageArray
	 * we are creating a minor change in the Blue channel of the
	 * image. This minor change will be unnoticeable to the human
	 * eye. 
	 *
	 * This is a cool function from the students' perspective
	 * because we use bitwise operators like &= and |=, 
	 * and we modify a 2d array in place because Java
	 * is pass by value, which in this case behaves like pass by
	 * reference because of the way Java works. All these Java 
	 * syntax tidbits are combined with the cool practical thing
	 * that we are doing steganography.
	 */	
	public static void HideBytes( byte[] toHide, int[][] imageArray){
		int bitIdx = 0;
		for( int row = 0; row < imageArray.length; row++ ){
			for ( int col = 0; col < imageArray[row].length; ++col ) {
				int bitToHide = getBit(bitIdx, toHide);
				// clear the last bit by setting it to zero
				imageArray[row][col] &= 0xFFFFFFFE;
				// now set the last bit to the bit we want to hide.
				imageArray[row][col] |= bitToHide;
				bitIdx++;	
			}
		}
	}

	
	/**
	 * Return 0 or 1 representing the bit at bitIdx in toHide
	 * 
	 * @param bitIdx - the index of the bit we want to extract.
	 * @param toHide - the sequence of bytes we extract bits from.
	 * @return 0 if the bit at position bitIdx is 0
	 * @return 1 if the bit at position bitIdx is 1
	 * @return BIT_INDEX_OUT_OF_RANGE_ERR if we request a bit out of range
	 *
	 * If we want to extact bit 0 from array [ 10000000b, 00000000b ] we will
	 * return '1'. To extract the 1 from arr = [ 00000000b, 01000000b ] we would call
	 * getBit(9, arr).
	 * 
	 * A call like getBit( 18, arr ), if arr = [ 00000000b, 01000001b ] will return 
	 * BIT_INDEX_OUT_OF_RANGE_ERR as there aren't enough bits in the array to get bit 18.
	 */
	private static int getBit( int bitIdx, byte[] toHide){
		final int BIT_INDEX_OUT_OF_RANGE_ERROR = 0x02;
		int byteIdx = bitIdx / 8;
		if( byteIdx >= toHide.length){
			return BIT_INDEX_OUT_OF_RANGE_ERROR;	
		}
		int localBitIdx = bitIdx % 8;
		byte current = toHide[byteIdx];
		current >>>= ( 7 - localBitIdx );
		return 0x00000001 & current; 
	}
	

	/**
	 * Print the last bit from the first 8 * numBytesInMessage bytes
	 * 
	 * @param imageArray - a row-major-ordered 2D array of ARGB ints representing an image.
	 * @param numBytesInMessage - the number of bytes hidden in the image array
	 * @return void - no return value
	 * @return TOO_MANY_BYTES_ERROR ( todo ) - error if the number of hidden bytes exceeds
	 *                                         the image size
	 * 
	 * Print to stdout the binary encoded bytes found in the image.
	 */
	public static void extractMessage(int[][] imageArray, int numBytesInMessage){
		System.out.println("Bytes extracted: ");
		int numBits = 8 * numBytesInMessage;
		for( int row = 0; row < imageArray.length; row++){
			for( int col = 0; col < imageArray[row].length; col++){
				if( row * imageArray[row].length + col == numBits ){
					// We found all the bits, let's get out of here.
					return;
				}
				else{
					System.out.print(String.format("%d", (0x00000001 & imageArray[row][col])));
					if((row*imageArray[0].length + col + 1) % 8 == 0)
						System.out.print("\n");
				}
			}
		}
	}

	/**
	 * Write the 2D array color as a png image.
	 * 
	 * @param color - 2D array representing the ARGB values in an image.
	 */
	public static void writeImage(int[][] color) {
		String path = "output.png";
		BufferedImage image = new BufferedImage(color[0].length, color.length, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < color.length; x++) {
			for (int y = 0; y < color[x].length; y++) {
				image.setRGB(y, x, color[x][y]);
			}
		}
		File ImageFile = new File(path);
		try {
			ImageIO.write(image, "png", ImageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


/**
 * Custom exception thrown when there is no Alpha Channel in an image
 */	
class NoAlphaChannelException extends Exception{
	NoAlphaChannelException(String s){
		super(s);
	}
}
