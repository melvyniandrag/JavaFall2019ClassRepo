import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

public class MessageHider{
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
	
	public static void HideBytes( byte[] toHide, int[][] imageArray){
		int bitIdx = 0;
		int bitsPrinted = 0;
		for( int row = 0; row < imageArray.length; row++ ){
			for ( int col = 0; col < imageArray[row].length; ++col ) {
				imageArray[row][col] &= 0xFFFFFFFE; // set the last blue bit to zero.a
				int bitToHide = getBit(bitIdx, toHide);
				if(bitToHide != 0x02){
					System.out.print(bitToHide);	
					bitsPrinted++;
					if( bitsPrinted == 8 ){
						System.out.print(" ");
						bitsPrinted = 0;
					}
				}
				imageArray[row][col] |= bitToHide; // set the last blue bit to zero.
				bitIdx++;	
			}
		}
		System.out.println();
		// Note the lack of return value here. In Java, if you modify an array in a function
		// the array is modified in place.
		// The reasoning for this requires some discussion and deals with pass by value / pass by reference	
	}

	private static int getBit( int bitIdx, byte[] toHide){
		int byteIdx = bitIdx / 8;
		if( byteIdx >= toHide.length){
			return 0x02;
		}
		int localBitIdx = bitIdx % 8;
		//System.out.println(String.format("Getting bit %d from byte %d at localIndex %d", bitIdx, byteIdx, localBitIdx));
		byte current = toHide[byteIdx];
		current >>>= ( 7 - localBitIdx );
		return 0x00000001 & current; 
	}
	

	/**
	 * Return the last bit from the first 8 * numBytesInMessage bytes
	 */
	public static void extractMessage(int[][] imageArray, int numBytesInMessage){
		System.out.println("Last bits from each pixel of the imageArray are:");
		int numBits = 8 * numBytesInMessage;
		for( int row = 0; row < imageArray.length; row++){
			for( int col = 0; col < imageArray[row].length; col++){
				if( row * imageArray[row].length + col == numBits ){
					System.out.println("Did these bits agree with the bits we set when we hid a message?");
					return;
				}
				else{
					System.out.println(String.format("0x%08X", (0x00000001 & imageArray[row][col])));
				}
			}
		}
	}

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
	
class NoAlphaChannelException extends Exception{
	NoAlphaChannelException(String s){
		super(s);
	}
}
