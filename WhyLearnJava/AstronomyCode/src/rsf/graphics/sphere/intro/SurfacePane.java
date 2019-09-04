package rsf.graphics.sphere.intro;

// Shows a sphere with surface features based on six image files.

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.io.File;

import rsf.graphics.GUtil;
import rsf.ui.LoadOrSaveDialog;
import rsf.ui.SimpleWindow;


public class SurfacePane extends MobilePane {
  
  // These are the six images that make up the sphere, ordered arctic, antarctic,
  // 0 degrees (Greenwich/Africa), 90 degrees (China/India), 180 degrees (Pacific) and
  // 270 degrees (Americas).
  BufferedImage[] globeImage = new BufferedImage[6];
  
  // The names of the six files.
  String[] fileName = {"arc.jpg","ant.jpg","0deg.jpg","90deg.jpg","180deg.jpg","270deg.jpg"};
  
  // These are the vectors defining the tangent plane at the point of tangency for each
  // of the six projected planes. The vector for the i-th plane is <tan1[i],tan2[i]>.
  double[][] tan1 = {{-1,0,0},
                     {1,0,0},
                     {0,1,0},
                     {-1,0,0},
                     {0,1,0},
                     {1,0,0}
                     };
  double[][] tan2 = {{0,1,0},
                     {0,1,0},
                     {0,0,-1},
                     {0,0,-1},
                     {0,0,1},
                     {0,0,-1}
                     };
  
  
  public SurfacePane() {
    
    // Load the six files into globeImage.
    String choice[] = LoadOrSaveDialog.getLoadChoice("Choose a JPEG File");
    if (choice == null)
      System.exit(0);
        
    for (int i = 0; i < 6; i++)
      {
        String dir = choice[0] + File.separator + fileName[i];
        ImageIcon icon = new ImageIcon(dir);
        Image tempImage = icon.getImage();
        globeImage[i] = new BufferedImage(tempImage.getWidth(null),
                                          tempImage.getHeight(null),
                                          BufferedImage.TYPE_INT_RGB);
        Graphics g = globeImage[i].getGraphics();
        g.drawImage(tempImage,0,0,null);
      }
  }
  
  public void drawSphere(BufferedImage image) {
    
    // Draw the sphere to the given BufferedImage, as in previous cases.
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
                // Convert to spherical coordinates.
                double[] spherical = GUtil.rectangularToSpherical(s);
                
                // Figure out which image we want. The correspondence is
                // theta in [0,pi/4]     ==> image 0
                // theta in [3pi/4,pi]   ==> image 1
                // phi in [-pi/4,pi/4]   ==> image 2
                // phi in [pi/4,3pi/4]   ==> image 3
                // phi in [3pi/4,5pi/4]  ==> image 4
                // phi in [5pi/4,7pi/4]  ==> image 5
                int imageNum = 0;
                if (spherical[1] <= Math.PI/4.0) 
                  imageNum = 0;
                else if (spherical[1] >= 3.0 * Math.PI / 4.0)
                  imageNum = 1;
                else if ((spherical[2] < Math.PI/4) || (spherical[2] > 7.0 * Math.PI / 4.0)) 
                  imageNum = 2;
                else if (spherical[2] < 3.0 * Math.PI / 4.0) 
                  imageNum = 3;
                else if (spherical[2] < 5.0 * Math.PI / 4.0) 
                  imageNum = 4;
                else
                  imageNum = 5;
                
                // Determine the pixel of the image needed. Do this by projecting
                // the image to the sphere using parallel projection. Each of the six 
                // images is a projection from a different plane. Parallel  projection isn't 
                // really right since that's not how a camera works, and these images were
                // obtained with a digital camera, but it's very close and it's easy to 
                // implement.
                // The correspondence between image, point of tangency and the vectors 
                // defining the plane is
                // image   tan pt        u1      u2         rot
                //   0     (0,0,1)    (-1,0,0)  (0,1,0)     none
                //   1     (0,0,-1)   (1,0,0)   (0,1,0)     none
                //   2     (1,0,0)   (0,1,0)   (0,0,-1)     90 around x-axis
                //   3     (0,1,0)    (-1,0,0)   (0,0,-1)   90 around y-axis
                //   4     (1,0,0)    (0,1,0)   (0,0,1)     90 around x-axis
                //   5     (0,1,0)   (1,0,0)   (0,0,-1)     -90 around y-axis
                // These are set above in tan1 and tan2.
                
                // For every case except imageNum == 0 or 1, s must be rotated to be 
                // centered around (x,y,z) = (0,0,1).
                // Exercise: Try eliminating these if-statements, and simply setting p=s.
                // The panels of the globe will be flipped and/or rotated compared to
                // their correct orientation.
                double[] p = null;
                if (imageNum <= 1) p = s;
                if (imageNum == 2) p = GUtil.rotate3(Math.PI/2,new double[] {1,0,0},s);
                else if (imageNum == 3)
                  p = GUtil.rotate3(Math.PI/2,new double[] {0,1,0},s);
                else if (imageNum == 4)
                  p = GUtil.rotate3(Math.PI/2,new double[] {1,0,0},s);
                else if (imageNum == 5)
                  p = GUtil.rotate3(-Math.PI/2,new double[] {0,1,0},s);

                // Get the coordinates of the pixel within the appropriate globeImage.
                // These should be in the range [-1,+1]. This assumes parallel projection.
                double alpha1 = GUtil.dot(p,tan1[imageNum]);
                double alpha2 = GUtil.dot(p,tan2[imageNum]);
                
                // Scale them to the image
                int x1 = (int)((alpha1 + 1.0) * 
                               (globeImage[imageNum].getWidth() - 1.0) / 2.0);
                int y1 = (int)((alpha2 + 1.0) * 
                               (globeImage[imageNum].getHeight() - 1.0) / 2.0);

                // Pull out the corresponding point and draw it to the sphere.
                int rgb = globeImage[imageNum].getRGB(x1,y1);
                image.setRGB(x,z,rgb);
                
                /* Instead of calling image.setRGB() immediately, the lines below could be
                   used instead. This makes the globe appear as it would with a light source.
                
                // Convert to a color, then adjust the intensity according to the light
                // source.
                Color tempc = new Color(rgb);
                double[] n = s;
                float intensity = shadeIntensity(n,s);
                
                rgb = (new Color(
                    (float)(intensity * (float)tempc.getRed() / 255.0),
                    (float)(intensity * (float)tempc.getGreen() / 255.0),
                    (float)(intensity * (float)tempc.getBlue() / 255.0))).getRGB();
                image.setRGB(x,z,rgb);
               */
              }
            else
              {
                // Background point that does appear on the sphere.
                int rgb = (Color.black).getRGB();
                image.setRGB(x,z,rgb);
              }
          }
      }
  }
  
  public static void main(String[] args) {
    SimpleWindow theWindow = new SimpleWindow("Surface Sphere",new SurfacePane());
  }
}