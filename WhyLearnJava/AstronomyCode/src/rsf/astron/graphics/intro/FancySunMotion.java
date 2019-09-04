package rsf.astron.graphics.intro;

// Similar to SunMotion, but this has a little earth in the middle of the Sphere.
// The earth is shaded using the sun (which is in motion) as the light source. The
// celestial sphere is drawn with a fixed light source.

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

import rsf.ui.MyPanel;
import rsf.ui.SimpleWindow;
import rsf.graphics.GUtil;
import rsf.graphics.sphere.EyeKeyListener;
import rsf.graphics.sphere.OutSphere;


public class FancySunMotion extends MyPanel implements ActionListener {

  // As in SunMotion.
  private double day = 0.0;
  private double daysPerYear = 10.0;
  private double daysPerFrame = 0.01;
  private Timer theTimer = null;
  private int delay = 10;
  private FancySphereMgr eyeMgr = null;
  
  // There are three spheres: The shaded celestial sphere, the celestial
  // sphere with equator and ecliptic, and the earth.
  private OutSphere plainSphere = null;
  private EqEcSphere celestialSphere = null;
  private OutSphere earthSphere = null;
  
  // The final result. This should be valid at all times. It's made by copying in
  // the image from plainSphere, copying in the image from earthSphere, adding
  // the equator and ecliptic using celestialSphere, then adding the sun.
  private BufferedImage finalImage = null;

  // The celestial sphere is taken to have the default radius of one. 
  // This is the radius of the earth.
  private double earthRadius = 0.20;
  
  
  public FancySunMotion() {
    
    plainSphere = new OutSphere(this.initialWidth,this.initialHeight);
    celestialSphere = new EqEcSphere(this.initialWidth,this.initialHeight);
    earthSphere = new OutSphere(this.initialWidth,this.initialHeight,earthRadius,
                    new double[] {10,0,0}, // light is basically junk, but that's OK.
                    new Color(0.2f,0.7f,1.0f),0.10f,10.0,0.00); 
    finalImage = new BufferedImage(this.initialWidth,this.initialHeight,
        BufferedImage.TYPE_INT_RGB);
    
    eyeMgr = new FancySphereMgr(celestialSphere,earthSphere);
    this.addKeyListener(new EyeKeyListener(eyeMgr,this));
    
    this.setFocusable(true);
    
    theTimer = new Timer(delay,this);
  }
  
  private double[] sunCoords() {
    
    // Convert this.day to the rectangular coordinates of the sun.
    // Similar code appears in SunMotion.updateFinal().
    double[] sunSpherical = new double[3];
    sunSpherical[0] = celestialSphere.getRadius();
    if (day < daysPerYear / 2.0)
      sunSpherical[1] = 113.50 - day * (23.50 * 2.0) / (daysPerYear / 2.0);
    else
      sunSpherical[1] = 66.50 + (day - daysPerYear/2) * (23.50 * 2.0) /
          (daysPerYear / 2.0);
    sunSpherical[1] = Math.toRadians(sunSpherical[1]);

    double hour = (day - (int) day) * 24.0;;
    sunSpherical[2] = hour * 2.0 * Math.PI / 24.0;
      
    // Convert to rectangular and return.
    return GUtil.sphericalToRectangular(sunSpherical);
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // Generate a new frame.
    updateFinal();
    this.repaint();
    this.day += daysPerFrame;
    if (day > this.daysPerYear)
      day = 0.0;
    
    // Make the light source for earthSphere equal to the sun's position.
    earthSphere.requestLight(sunCoords());
  }
  
  public void updateFinal() {
    
    // Re-draw finalSphere by copying plainSphere, adding earthSphere, the equator
    // and ecliptic, then the sun.
    
    // There are two spheres being drawn. Synchronize on both of them so that their
    // state is fixed while drawing.
    synchronized (earthSphere) { synchronized (celestialSphere) {
    
      // First check if the window size has changed.
      if ((finalImage.getWidth() != this.getWidth()) ||
          (finalImage.getHeight() != this.getHeight()))
        {
          // Have to allocate new image buffers because the window size has changed.
          plainSphere.resize(this.getWidth(),this.getHeight());
          celestialSphere.resize(this.getWidth(),this.getHeight());
          earthSphere.resize(this.getWidth(),this.getHeight());
          finalImage = new BufferedImage(this.getWidth(),this.getHeight(),
              BufferedImage.TYPE_INT_RGB);
        }
      
      Graphics g = finalImage.getGraphics();
    
      // Copy over the plainSphere's image.
      BufferedImage baseImage = plainSphere.getImage();
      g.drawImage(baseImage,0,0,null);
    
      // Get sun's position.
      double[] sunRect = sunCoords();
      
      // This is similar to what was done in SunMotion, except that the order in which
      // things are drawn depends on whether the sun is on the front or the back of the
      // sphere.
      int[] m = celestialSphere.sphereToScreen(sunRect);
      double[] V = new double[3];
      double[] s = new double[3];
      celestialSphere.screenToViewport(m[0],m[1],V);
      celestialSphere.isSpherePoint(V,s); // Ignore result. We know it's on the sphere.
      double dist = GUtil.length(GUtil.minus(sunRect,s));
      boolean sunOnFront;
      if (dist < 0.05)
        sunOnFront = true;
      else
        sunOnFront = false;
      
      if (sunOnFront == true)
        {
          // Add the earth and equator & ecliptic before drawing the sun.
          earthSphere.update(finalImage);
          celestialSphere.addEqEc(finalImage);
        }
      
      // Draw the sun -- just as before.
      int width = finalImage.getWidth();
      int height = finalImage.getHeight();
      int lowx = m[0] - 20; if (lowx < 0) lowx = 0;
      int hix = m[0] + 20; if (hix >= width) hix = width - 1;
      int lowy = m[1] - 20; if (lowy < 0) lowy = 0;
      int hiy = m[1] + 20; if (hiy >= height) hiy = height - 1;
      
      int rgb = Color.yellow.getRGB();
      float[] yellow = Color.yellow.getComponents(null);
      float brgtness = 0.4f;
      int dimrgb = (new Color(brgtness * yellow[0],brgtness * yellow[1],
                              brgtness*yellow[2])).getRGB(); 
    
      for (int x = lowx; x <= hix; x++)
        {
          for (int z = lowy; z <= hiy; z++)
            {
              celestialSphere.screenToViewport(x,z,V);
              if (celestialSphere.isSpherePoint(V,s))
                {
                  dist = GUtil.length(GUtil.minus(sunRect,s)); 
                  if (dist < 0.05)
                    finalImage.setRGB(x,z,rgb);
                  else if (celestialSphere.isRearSpherePoint(V,s))
                    {
                      dist = GUtil.length(GUtil.minus(sunRect,s));
                      if (dist < 0.05)
                        finalImage.setRGB(x,z,dimrgb);
                    }
                }
            }
        }  
      
      if (sunOnFront == false)
        {
          // NOW draw the earth and equator/ecliptic.
          earthSphere.update(finalImage);
          celestialSphere.addEqEc(finalImage);
        }
    } } // close synchronization.
  }
  
  protected void paintComponent(Graphics g) {
    
    // Copy finalImage to screen. That's it.
    g.drawImage(finalImage,0,0,null);
  }
  
  public static void main(String[] args) {
    
    FancySunMotion thePane = new FancySunMotion();
    SimpleWindow theWindow = new SimpleWindow("Fancy Sun Motion",thePane);
    
    thePane.eyeMgr.start();
    thePane.theTimer.start();
  }
}