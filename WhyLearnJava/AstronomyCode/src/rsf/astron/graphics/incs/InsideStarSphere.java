package rsf.astron.graphics.incs;

// To show the view of the stars from inside the celestial sphere. This is a lot like 
// StarSphere, except that it is based on InSphere instead of OutSphere so that the eye is
// inside the sphere.

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import rsf.astron.place.Coord;
import rsf.astron.stars.StarList;
import rsf.graphics.sphere.InSphere;


public class InsideStarSphere {
  
  // These should be clear.
  public InSphere baseSphere = null;
  public StarList theStars = null;
  private BufferedImage starImage = null;
  
  
  public InsideStarSphere(int width,int height,StarList theStars) {
    
    // Earlier sphere-drawing programs are similar.
    this.theStars = theStars;
    baseSphere = new InSphere(width,height);
    baseSphere.makePerspectiveProjection();
    starImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
  }
  
  synchronized public void resize(int width,int height) {
    
    if ((starImage.getWidth() == width) && (starImage.getHeight()== height))
      // No need to resize.
      return;
    
    // Have to allocate new image buffers because the window size has changed.
    synchronized(baseSphere) {
    synchronized(starImage) {
      baseSphere.setSize(width,height);
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
    // round-about way (with the static addStars() below) so that this code can be reused.
    
    synchronized (baseSphere) {
    synchronized (starImage) {
      
      Graphics g = starImage.getGraphics();
      g.clearRect(0,0,starImage.getWidth(),starImage.getHeight());      
      
      if (theStars == null)
        // No star data loaded.
        return;
       
      // Add the stars.
      synchronized (theStars) {
        addStars(baseSphere,starImage,theStars);
      }
    }
    }
  }
  
  synchronized public static void addStars(InSphere theSphere,BufferedImage theImage,
                                           StarList theStars) {
    
    // Add the stars. The is almost identical to what is done in 
    // rsf.astron.stars.StarSphere.addStars().
    Coord starPos = new Coord();
    starPos.type = Coord.DecRA;
    synchronized (theStars) {
      for (int i = 0; i < theStars.ra.length; i++)
        {
          // Convert from RA/Dec to (x,y,z) coordinates on the sphere.
          starPos.piTerm = theStars.dec[i];
          starPos.twoPiTerm = theStars.ra[i];
          double[] rect = starPos.toRectangular();
          
          // Convert to screen coordinates.
          int[] screen = theSphere.sphereToScreen(rect);
          
          // See if visible.
          if (screen == null)
            continue;
          
          // Convert the (x,y,z) coordinates to screen coordinates, and draw it.
          // Every monitor (and set of eyes) will need slightly different settings here.
          // See StarSphere.addStars() for an explanation of this equation.
          double d0 = 0.3;
          double d1 = 1.0;
          double m0 = 4.0;
          double m1 = -1.1;
          
          float intensity = (float)(d0 - 1.0 + 
                                    Math.pow(d1 - d0 + 1.0,(m0 - theStars.mag[i])/(m0-m1)));
            
          // Don't draw this star if it has negative intensity.
          if (intensity < 0.0)
            continue;
          
          int rgb = (new Color(intensity,intensity,intensity)).getRGB();
          theImage.setRGB(screen[0],screen[1],rgb);
        }
    }
  }
}