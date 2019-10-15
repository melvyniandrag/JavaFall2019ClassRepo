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
