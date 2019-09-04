package rsf.graphics.sphere.intro;

// Shows a sphere with a single JPEG pasted onto it.

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

import rsf.graphics.GUtil;
import rsf.ui.LoadOrSaveDialog;
import rsf.ui.SimpleWindow;


public class ImagePastePane extends MobilePane {
  
  // The image loaded from disk.
  BufferedImage jpegImage = null;
  
  
  public ImagePastePane() {
    
    // Choose and load a jpeg file.
    String[] choice = LoadOrSaveDialog.getLoadChoice("Choose a JPEG File");
    if (choice == null)
      System.exit(0);
    String dir = choice[0] + File.separator + choice[1];
    
    // Load image and convert it to a BufferedImage.
    ImageIcon icon = new ImageIcon(dir);
    Image tempImage = icon.getImage();
    
    jpegImage = new BufferedImage(tempImage.getWidth(null),tempImage.getHeight(null),
                                  BufferedImage.TYPE_INT_RGB);
    Graphics g = jpegImage.getGraphics();
    g.drawImage(tempImage,0,0,null);
  }
  
  public void drawSphere(BufferedImage image) {
    
    // Draw the sphere to the given BufferedImage. Very much as in SpherePane.
    double[] V = new double[3];
    double[] s = new double[3];
    this.portHeight = portWidth * (double) height/ (double) width;
    
    // Loop over each pixel.
    for (int x = 0; x < width; x++)
      {
        for (int z = 0; z < height; z++)
          {
            screenToViewport(x,z,V);
            if (isSpherePoint(V,s))
              {
                // Point is on the sphere. Assume that jpegImage is centered at 
                // s = (0,0,1). So the point is part of jpegImage iff s_z > 0. 
                if (s[2] <= 0)
                  {
                    // Old shading method.
                    double[] n = s;
                    float intensity = shadeIntensity(n,s);
                    int rgb = (new Color(intensity,intensity,intensity)).getRGB();
                    image.setRGB(x,z,rgb);
                  }
                else
                  {
                    // Part of the pasted image.
                    // These values should be in the range [-1,+1]. This assumes parallel
                    // projection.
                    double alpha1 = GUtil.dot(s,new double[] {-1,0,0});
                    double alpha2 = GUtil.dot(s,new double[] {0,1,0});
                    
                    // Scale them to lie on the image.
                    int x1 = (int)((alpha1 + 1.0) * (jpegImage.getWidth() - 1.0) / 2.0);
                    int y1 = (int)((alpha2 + 1.0) * (jpegImage.getHeight() - 1.0) / 2.0);
                    
                    // Pull out the corresponding point of the image and copy it over.
                    int rgb = jpegImage.getRGB(x1,y1);
                    image.setRGB(x,z,rgb);
                  }
              }
            else
              {
                // Background point that does not appear on the sphere.
                int rgb = (Color.black).getRGB();
                image.setRGB(x,z,rgb);
              }
          }
      }
  }
  
  public static void main(String[] args) {
    SimpleWindow theWindow = new SimpleWindow("Image Paste Sphere",new ImagePastePane());
  }
}