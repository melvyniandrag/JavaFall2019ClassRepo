/*
 * This code was taken from stackoverflow
 * https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
 *
 * Please see the 'ImageManipulator' class instead of this one.
 *
 */
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageManipulator {

   public static void main(String[] args) throws IOException {
      BufferedImage hugeImage = ImageIO.read(PerformanceTest.class.getResource("12000X12000.jpg"));
      int[][] result = convertTo2DWithoutUsingGetRGB(hugeImage);
      writeImage(result);
   }

   private static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

      final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
      final int width = image.getWidth();
      final int height = image.getHeight();
      final boolean hasAlphaChannel = image.getAlphaRaster() != null;

      int[][] result = new int[height][width];
      if (hasAlphaChannel) {
         final int pixelLength = 4;
         for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
            int argb = 0;
            argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
            argb += ((int) pixels[pixel + 1] & 0xff); // blue
            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
            result[row][col] = argb;
            col++;
            if (col == width) {
               col = 0;
               row++;
            }
         }
      } else {
         final int pixelLength = 3;
         for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
            int argb = 0;
            argb += -16777216; // 255 alpha
            argb += ((int) pixels[pixel] & 0xff); // blue
            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
            result[row][col] = argb;
            col++;
            if (col == width) {
               col = 0;
               row++;
            }
         }
      }

      return result;
   }

   public static void writeImage(int[][] color) {
       String path = "output.png";
       BufferedImage image = new BufferedImage(color.length, color[0].length, BufferedImage.TYPE_INT_RGB);
       for (int x = 0; x < color.length; x++) {
           for (int y = 0; y < color[x].length; y++) {
               image.setRGB(x, y, color[x][y]);
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