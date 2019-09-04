package rsf.astron.graphics.cs;

// A combination of StarSphere and JPLSphere, so it shows both the planets and the stars.
// For speed and simplicity, the stars are treated as fixed.

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

import rsf.astron.jpl.anim.JPLSphere;
import rsf.astron.stars.StarList;
import rsf.astron.stars.StarSphere;
import rsf.graphics.sphere.OutSphere;
import rsf.astron.graphics.intro.EqEcSphere;
import rsf.astron.jpl.JPLData;


public class CelestialSphere {
  
  // As in earlier programs. 
  public OutSphere baseSphere = null;
  private BufferedImage finalImage = null;
  private StarList theStars = null;
  private JPLData eph = null;
  private String message = null;
  
  // The Julian day to use for the planets' positions.
  public double jd = 0.0;
  
  
  public CelestialSphere(int width,int height,StarList theStars,JPLData eph) {
    
    // This stuff should be clear. Earlier sphere-drawing programs are similar.
    this.theStars = theStars;
    this.eph = eph;
    baseSphere = new EqEcSphere(width,height,true);
    finalImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    this.jd = eph.startJD;
  }
  
  public void setMsg(String msg) {
    this.message = msg;
  }
  
  synchronized public void resize(int width,int height) {
    
    if ((finalImage.getWidth() == width) && (finalImage.getHeight()== height))
      // No need to resize.
      return;
    
    // Have to allocate new image buffers because the window size has changed.
    synchronized(baseSphere) {
    synchronized(finalImage) {
      baseSphere.resize(width,height);
      finalImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      update();
    }
    }
  }
  
  public void addToJD(double x) {

    // Move the JD forward by x days.
    if (this.jd + x < eph.endJD)
      this.jd += x;  
    
    // Adjust the displayed data.
    message = String.format("JD = %.2f",jd);
  }
  
  synchronized public void update() {

    synchronized (baseSphere) {
    synchronized (finalImage) {
      boolean dirty = baseSphere.update();
      
      // Copy over the celestial sphere (with equator and ecliptic).
      Graphics g = finalImage.getGraphics();
      BufferedImage baseImage = baseSphere.getImage();
      g.drawImage(baseImage,0,0,null);
      
      // Add any message.
      if (message != null)
        {
          g.setColor(Color.red);
          g.drawString(message,20,20);
        }
      
      if (theStars != null)
        StarSphere.addStars(g,baseSphere,finalImage,theStars);
      if (eph != null)
        JPLSphere.addPlanets(eph,jd,baseSphere,finalImage);
    }
    }
  }
  
  synchronized public void draw(Graphics g) {
    synchronized (baseSphere) {
    synchronized (finalImage) {
      g.drawImage(finalImage,0,0,null);
    }
    }
  }
}