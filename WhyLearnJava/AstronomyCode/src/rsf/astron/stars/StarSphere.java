package rsf.astron.stars;

// Displays the stars on a sphere. This is much like the SunMotion class, but with stars 
// instead of the sun. This is static in the sense that, although the eye can be moved, time
// is fixed.

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import rsf.graphics.GUtil;
import rsf.graphics.sphere.OutSphere;
import rsf.astron.graphics.intro.EqEcSphere;
import rsf.astron.place.Coord;


public class StarSphere {
  
  // The sphere on which the stars are drawn. It's really an EqEcSphere so that the user can
  // orient himself relative to the equator and ecliptic.
  public OutSphere baseSphere = null;
  
  // The image from baseSphere is copied to this field, then the stars are added to it.
  private BufferedImage starImage = null;
  
  public StarList theStars = null;
  private String message = null;
  
  
  public StarSphere(int width,int height,StarList theStars) {
    
    // This stuff should be clear. Earlier sphere-drawing programs are similar.
    this.theStars = theStars;
    baseSphere = new EqEcSphere(width,height,true);
    starImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
  }
  
  public void setMsg(String msg) {
    this.message = msg;
  }
  
  synchronized public void resize(int width,int height) {
    
    if ((starImage.getWidth() == width) && (starImage.getHeight()== height))
      // No need to resize.
      return;
    
    // Have to allocate new image buffers because the window size has changed.
    synchronized(baseSphere) {
    synchronized(starImage) {
      baseSphere.resize(width,height);
      starImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      update();
    }
    }
  }
  
  synchronized public void update() {

    synchronized (baseSphere) {
    synchronized (starImage) {
      boolean dirty = baseSphere.update();
      addStars();
    }
    }
  }
  
  synchronized public void draw(Graphics g) {
    synchronized (baseSphere) {
    synchronized (starImage) {
      g.drawImage(starImage,0,0,null);
    }
    }
  }
  
  synchronized public void addStars() {
    
    // Copy sphere from baseSphere to starImage, then add the stars. This is done in a 
    // round-about way (with the static addStars() method) so that the code can be reused.
    synchronized (baseSphere) {
    synchronized (starImage) {
       
      // Copy over the celestial sphere (with equator and ecliptic).
      Graphics g = starImage.getGraphics();
      BufferedImage baseImage = baseSphere.getImage();
      g.drawImage(baseImage,0,0,null);
      
      // Add any message.
      if (message != null)
        {
          g.setColor(Color.red);
          g.drawString(message,20,20);
        }
      
      if (theStars == null)
        // No star data loaded.
        return;
       
      // Add the stars.
      addStars(g,baseSphere,starImage,theStars);
    }
    }
  }
  
  synchronized public static void addStars(Graphics g,OutSphere theSphere,
                                           BufferedImage destImage,StarList theStars) {
    
    // Add theStars to destImage.
    Coord starPos = new Coord();
    starPos.type = Coord.DecRA;
    double r = theSphere.getRadius();
    synchronized (theStars) {
      for (int i = 0; i < theStars.ra.length; i++)
        {
          // Convert from RA/Dec to (x,y,z) coordinates on the sphere, making sure that the 
          // radius is correct.
          starPos.piTerm = theStars.dec[i];
          starPos.twoPiTerm = theStars.ra[i];
          double[] rect = starPos.toRectangular();
          GUtil.times(rect,r);
        
          // Check if this point is on the visible side of the sphere.
          if (theSphere.isFrontPoint(rect) == true)
            {
              // Convert the (x,y,z) coordinates to screen coordinates, and draw it.
              int[] screen = theSphere.sphereToScreen(rect);
              
              // Adjust the brightness of the pixel to match the visual magnitude. Strictly
              // speaking, for each upward step in visual magnitude, the intensity should go
              // down by a factor of about 2.5, but this makes magnitude 6 (and magnitude 5)
              // stars too dim to see.
              //
              // Suppose that the dimmest star has magnitude m_0 and that the brightest has 
              // magnitude m_1. If the dimmest star is to have intensity d_0, and the 
              // brightest star is to have intensity d_1, and the intensity is to increase 
              // exponentially, then the intensity of a magnitude i star should be 
              // f(i) = (d_0 - 1) + (d_1 - d_0 + 1)^((i-m_1)/(m_0-m_1)).
              // 
              // Every monitor (and set of eyes) will require slightly different settings.
              double d0 = 0.3;
              double d1 = 1.0;
              double m0 = 4.0;
              double m1 = -1.1;
              
              float intensity = (float) (d0 - 1.0 + 
                  Math.pow(d1 - d0 + 1.0,(m0 - theStars.mag[i])/(m0-m1)));
                
              // Depending on the visual magnitude of the star and the values of d0, d1, m0 
              // and m1, the intensity could be negative. Skip those.
              if (intensity < 0.0)
                continue;
              
              int rgb = (new Color(intensity,intensity,intensity)).getRGB();
              destImage.setRGB(screen[0],screen[1],rgb);
            }
        }
    }
  }
}