package rsf.astron.graphics.incs;

// Shows the celestial sphere from inside the sphere, including the stars and planets at a
// particular time. The eye is assumed to be at the center of the earth. It is used with
// CEarthAnim to show an animation over time.

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import rsf.ui.MyPanel;
import rsf.astron.jpl.JPLData;
import rsf.astron.stars.StarList;
import rsf.astron.graphics.incs.InsideStarSphere;
import rsf.graphics.sphere.InSphere;


public class CEarthPane extends MyPanel implements ActionListener {
  
  // These are used in a way that is similar to earlier classes.
  public InSphere baseSphere = null;
  private BufferedImage finalImage = null;
  private StarList theStars = null;
  private JPLData eph = null;
  private String message = null;
  
  // The Julian day to use for the planets' positions, along the the time between frames.
  public double jd = 0.0;
  private double timeStep = 0.005;
  
  
  public CEarthPane(StarList theStars,JPLData eph) {
    
    // Constructors for earlier sphere-drawing programs are similar.
    this.theStars = theStars;
    this.eph = eph;
    baseSphere = new InSphere(this.initialWidth,this.initialHeight);
    finalImage = new BufferedImage(initialWidth,initialHeight,BufferedImage.TYPE_INT_RGB);
    this.jd = eph.startJD;
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // When this is called, generate a new frame. First, check whether the window has 
    // been re-sized, then draw contents.
    this.resize(this.getWidth(),this.getHeight());
    addToJD(timeStep);
    String msg = String.format("JD = %4.2f",jd);
    setMsg(msg); 
    update();
    this.repaint();
  }

  protected void paintComponent(Graphics g) {
  
    // Very little to do since other threads continuously keep the buffer updated.
    draw(g);
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
      baseSphere.setSize(width,height);
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
      
      // Allow the sphere to update its position.
      boolean dirty = baseSphere.update();
      
      // Erase the image.
      Graphics g = finalImage.getGraphics();
      g.clearRect(0,0,finalImage.getWidth(),finalImage.getHeight()); 
      
      // Add any message.
      if (message != null)
        {
          g.setColor(Color.red);
          g.drawString(message,20,20);
        }
      
      // Add stars and planets.
      if (theStars != null)
        InsideStarSphere.addStars(baseSphere,finalImage,theStars);
      if (eph != null)
        InsideJPLSphere.addPlanets(eph,jd,baseSphere,finalImage);
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