package rsf.astron.graphics.intro;

// Shows the sun moving around the celestial sphere.

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


public class SunMotion extends MyPanel implements ActionListener {

  // Really, there are 365.25 days per year, but reducing the assumed number of days per
  // year makes the motion of the sun easier to see.
  private double daysPerYear = 10.0;
  
  // The day of the year. Days go from 0 to daysPerYear.
  private double day = 0.0;
  
  // How much to advance time with each frame of the animation.
  private double daysPerFrame = 0.01;
  
  // The shaded sphere with equator and ecliptic, but no sun.
  private EqEcSphere baseSphere = null;

  // Manages movement of the eye.
  private BaseSphereMgr baseMgr = null;
  
  // This should be ready to be drawn at all times -- paintComponent() just copies it.
  private BufferedImage finalImage = null;
  
  // Every time theTimer "goes off" a new frame is drawn when actionPerformed() is called.
  // delay is the number of milliseconds between frames. 
  private Timer theTimer = null;
  private int delay = 10;
  
  
  public SunMotion() {
    
    // This stuff should be clear. Earlier sphere-drawing programs are similar. 
    baseSphere = new EqEcSphere(this.initialWidth,this.initialHeight);
    finalImage = new BufferedImage(this.initialWidth,this.initialHeight,
                                   BufferedImage.TYPE_INT_RGB);
    baseMgr = new BaseSphereMgr(baseSphere);
    this.addKeyListener(new EyeKeyListener(baseMgr,this));
    
    // This call means that we don't have to use FocusTaker. Swing handles it
    // automatically.
    this.setFocusable(true);
    
    // Allocated (but don't start) the timer.
    theTimer = new Timer(delay,this);
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // The timer calls this every delay milliseconds. It advances the time and draws a new
    // frame.
    updateFinal();
    this.repaint();
    this.day += daysPerFrame;
    if (day > this.daysPerYear)
      day = 0.0;
  }
  
  public void updateFinal() {
    
    // Re-draw finalSphere by copying baseSphere, then adding the sun.
    
    // Check if the window size has changed.
    if ((finalImage.getWidth() != this.getWidth()) || 
        (finalImage.getHeight()!= this.getHeight()))
      {
        // Have to allocate new image buffers because the window size has changed.
        baseSphere.resize(this.getWidth(),this.getHeight());
        finalImage = new BufferedImage(this.getWidth(),this.getHeight(),
            BufferedImage.TYPE_INT_RGB);
      }
    
    Graphics g = finalImage.getGraphics();
    
    // Synchronize on baseSphere; otherwise, it could change in the middle of the
    // drawing process.
    synchronized (baseSphere) {
      
      // Copy over the celestial sphere with equator and ecliptic.
      BufferedImage baseImage = baseSphere.getImage();
      g.drawImage(baseImage,0,0,null); 
    
      // Determine the sphere coordinates of the sun. This is not perfectly correct since
      // it assumes that the sun moves along the ecliptic linearly with time.
      // At day = 0, the sun is at an angle of 23.5 degrees below the equator.
      // After half a year it's 23.50 degrees above. This determines the angle
      // theta (of polar coordinates). The angle phi is determined from the fractional part of
      // a day. phi makes one complete cycle per day.
      double[] sunSpherical = new double[3];
      sunSpherical[0] = baseSphere.getRadius();
      if (day < daysPerYear / 2.0)
        sunSpherical[1] = 113.50 - day * (23.50 * 2.0) / (daysPerYear / 2.0);
      else
        sunSpherical[1] = 66.50 + (day - daysPerYear/2) * (23.50 * 2.0) / 
        (daysPerYear / 2.0);
      sunSpherical[1] = Math.toRadians(sunSpherical[1]);
      
      double hour = (day - (int) day) * 24.0;;
      sunSpherical[2] = hour * 2.0 * Math.PI / 24.0;
      
      double[] sunRect = GUtil.sphericalToRectangular(sunSpherical);
    
      // Most of what follows is similar to what appears in OutSphere.addDisk().
      // The reason that method could not be used here is that the disk should be drawn 
      // even when it's on the back-side of the sphere -- it's just drawn in a darker 
      // shade. OutSphere.addDisk() will draw the disk only if it's on the visible side of
      // the sphere. This implementation avoids calling OutSphere.diskRadius() and just
      // hard-codes the maximum possible radius.
      int[] m = baseSphere.sphereToScreen(sunRect);
      
      int width = finalImage.getWidth();
      int height = finalImage.getHeight();
      int lowx = m[0] - 20; if (lowx < 0) lowx = 0;
      int hix = m[0] + 20; if (hix >= width) hix = width - 1;
      int lowy = m[1] - 20; if (lowy < 0) lowy = 0;
      int hiy = m[1] + 20; if (hiy >= height) hiy = height - 1;
    
      // Allocate colors. On the visible side of the sphere, use yellow. On the far side
      // of the sphere, use a darker shade of yellow.
      int rgb = Color.yellow.getRGB();
      float[] yellow = Color.yellow.getComponents(null);
      float brgtness = 0.4f;
      int dimrgb = (new Color(brgtness * yellow[0],brgtness * yellow[1],
          brgtness*yellow[2])).getRGB(); 
      
      double[] V = new double[3];
      double[] s = new double[3];
    
      // Scan the pixels.
      for (int x = lowx; x <= hix; x++)
        {
          for (int z = lowy; z <= hiy; z++)
            {
              baseSphere.screenToViewport(x,z,V);
              
              // See whether this is a point on the sphere or the background.
              if (baseSphere.isSpherePoint(V,s))
                {
                  double dist = GUtil.length(GUtil.minus(sunRect,s)); 
                  if (dist < 0.05)
                    // s is close to the sun, and it's on front side.
                    finalImage.setRGB(x,z,rgb);
                  else if (baseSphere.isRearSpherePoint(V,s))
                    {
                      dist = GUtil.length(GUtil.minus(sunRect,s));
                      if (dist < 0.05)
                        finalImage.setRGB(x,z,dimrgb);
                    }
                }
            }
        }
    }
  }
  
  protected void paintComponent(Graphics g) {
    
    // Very little to do since other threads continuously keep the buffer updated.
    g.drawImage(finalImage,0,0,null);
  }
  
  public static void main(String[] args) {
    
    SunMotion thePane = new SunMotion();
    SimpleWindow theWindow = new SimpleWindow("Sun Motion",thePane);
    
    // Start the two threads that maintain finalImage.
    thePane.baseMgr.start();
    thePane.theTimer.start();
  }
}