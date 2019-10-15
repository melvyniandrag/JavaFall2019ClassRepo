import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

public class TurnImageGreen{

	public static void main(String[] args) throws IOException {
		BufferedImage hugeImage = ImageIO.read(TurnImageGreen.class.getResource("greenman.png"));
		int[][] result = null;
		try{
			result = convertToModifiedArray(hugeImage);
		}
		catch(NoAlphaChannelException ex){
			System.err.println(ex.getMessage());
			return;
		}
		writeImage(result);
	}
	
	private static int[][] convertToModifiedArray(BufferedImage image) throws NoAlphaChannelException{
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;
		if( !hasAlphaChannel ){
			throw new NoAlphaChannelException("This simple code can only handle pngs with an alpha channel. Yours has none.");
		}
		int[][] result = new int[height][width];
		final int pixelLength = 4;
		for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
			int argb = 0; // argb = Alpha,Red, Green, Blue
			int blue = 0x00;
			int green = 0x0000FF00;
			int red = 0x00 ;
			int	alpha = 0xFF000000; 
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
	
	public static void writeImage(int[][] color) {
		String path = "output.png";
		BufferedImage image = new BufferedImage(color[0].length, color.length, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < color.length; x++) {
			for (int y = 0; y < color[x].length; y++) {
				image.setRGB(y,x, color[x][y]);
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
