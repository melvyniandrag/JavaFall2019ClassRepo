package rsf.astron.graphics.intro;

// An OutSphere with the equator and ecliptic as additional decorations.

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import rsf.graphics.GUtil;
import rsf.graphics.sphere.OutSphere;


public class EqEcSphere extends OutSphere {

  public EqEcSphere(int width,int height) {
    super(width,height);
  }
  
  public EqEcSphere(int width,int height,boolean noShade) {
    super(width,height,noShade);
  }
  
  synchronized public void addEqEc(BufferedImage destImage) {
      
    // Draw equator and ecliptic to destImage. It is assumed that the eye, and so forth,
    // are as given in OutSphere, but destImage could be different than this.image.
    Graphics g = destImage.getGraphics();
    g.setColor(Color.red);
    
    // Number of points used for connect-the-dots.
    int numPts = 100;
    
    // Equator appears at z = 0 or (r,theta,phi) = (1,90,phi). Work around the circle in 
    // spherical coordinates. 
    double[] s = new double[3];
    s[0] = getRadius();
    s[1] = Math.PI / 2.0;
    int[] oldm = null;
    double[] oldc = null;
    
    // The +1 is so that the curve is closed, in case you want to draw the curve on the 
    // back-side too.
    for (int i = 1; i <= numPts + 1; i++)
      {
        s[2] = 2.0 * Math.PI * (double) i / (double) numPts;
        double[] c = GUtil.sphericalToRectangular(s);
        GUtil.addTo(this.center,c);
        int[] m = sphereToScreen(c);
        
        // Only draw if c and oldc are on the visible side of the sphere.
        if (isFrontPoint(c) && (oldm != null) && isFrontPoint(oldc))
          g.drawLine(oldm[0],oldm[1],m[0],m[1]);
        oldm = m;
        oldc = c;
      }
    
    // Same basic idea for the ecliptic, which is the same as the equator, after
    // rotating it through 23.50 degrees about the x-axis. 
    oldm = null;
    oldc = null;  
    g.setColor(Color.green);  
    for (int i = 1; i <= numPts + 1; i++)
      {
        s[2] = 2.0 * Math.PI * (double) i / (double) numPts;
        double[] c = GUtil.sphericalToRectangular(s);
        c = GUtil.rotate3(Math.toRadians(-23.50),new double[] {1,0,0},c);
        GUtil.addTo(this.center,c);
        int[] m = sphereToScreen(c);
        
        if (isFrontPoint(c) && (oldm != null) && (isFrontPoint(oldc)))
          g.drawLine(oldm[0],oldm[1],m[0],m[1]);
        oldm = m;
        oldc = c;
      }
  }
  
  synchronized public void addEqEc() {
    
    // This is what should normally be called.
    addEqEc(this.image);
  }
  
  synchronized public boolean update() {
    
    // Draw the underlying sphere, then add equator and ecliptic.
    boolean dirty = super.update();
    if (dirty == false)
      return false;
    
    // Go ahead and redraw the equator and ecliptic since the sphere was re-drawn.
    addEqEc();
    return true;
  }
}