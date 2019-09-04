package rsf.astron.graphics.intro;

// Used to illustrate precession. It shows the sphere with equator, ecliptic and
// the two poles.

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

import rsf.graphics.GUtil;
import rsf.graphics.sphere.OutSphere;
import rsf.graphics.sphere.EyeKeyListener;
import rsf.ui.MyPanel;
import rsf.ui.SimpleWindow;
import rsf.astron.place.Coord;
import rsf.astron.time.CalDate;
import rsf.astron.time.TimeUtil0;
import rsf.astron.place.PlaceUtil0;


public class PrecessSphere extends MyPanel implements ActionListener {
  
  // The Julian day at which the sphere is being displayed and the number 
  // of days between frames.
  private double theJD = 0.0;
  private double dayStep = 10000.0;
  
  // These fields play the same role that they did in SunMotion.
  private OutSphere baseSphere = null;
  private BaseSphereMgr baseMgr = null;
  private BufferedImage finalImage = null; 
  private Timer theTimer = null;
  private int delay = 10;
  
  
  public PrecessSphere() {
    
    // This stuff should be clear. Earlier sphere-drawing programs are similar. 
    baseSphere = new OutSphere(this.initialWidth,this.initialHeight);
    finalImage = new BufferedImage(this.initialWidth,this.initialHeight,
                        BufferedImage.TYPE_INT_RGB);
    baseMgr = new BaseSphereMgr(baseSphere);
    this.addKeyListener(new EyeKeyListener(baseMgr,this));
    
    // This call makes FocusTaker unnecessary.
    this.setFocusable(true);
    
    // Allocate (but don't start) the timer.
    theTimer = new Timer(delay,this);
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // The timer calls this every delay milliseconds. It advances the time and draws a 
    // new frame.
    updateFinal();
    this.repaint();
    this.theJD += dayStep;
  }
  
  public void updateFinal() {
    
    // Re-draw finalSphere by copying baseSphere, then adding equator, ecliptic
    // and the poles. The strategy is very similar to the one in EqEcSphere
    // (connect-the-dots), although the great circles could be at any angle.
    
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
    
    // Synchronize on baseSphere so that it doesn't change while the equator and
    // ecliptic are being drawn.
    synchronized (baseSphere) {
      
      // Copy over the shaded sphere with no decorations.
      BufferedImage baseImage = baseSphere.getImage();
      g.drawImage(baseImage,0,0,null);
      
      // Add a great circle for the equator. Create a series of points along the
      // intersection of the sphere with the (x,y)-plane. Then apply the precession
      // matrix to these points to move the equator to its correct position.
      
      // Number of points used for connect-the-dots.
      int numPts = 100;
      
      // oldm and oldc are used to make sure that only what is visible on the
      // front of the sphere is drawn.
      int[] oldm = null;
      double[] oldc = null;

      // Loop over numPts. The +1 is so that the circle is complete. The final entry
      // should coincide with the first entry.
      g.setColor(Color.blue);
      
      for (int i = 0; i < numPts+1; i++)
        {
          Coord dots = new Coord(2.0 * Math.PI * (double) i /(double) numPts,0.0,
                                 Coord.DecRA);
          Coord precessed = PlaceUtil0.from2000(dots,this.theJD);
          double[] c = precessed.toRectangular();
          GUtil.addTo(baseSphere.getCenter(),c);
          int[] m = baseSphere.sphereToScreen(c);
          
          // Only draw if c and oldc are on the visible side of the sphere.
          if (baseSphere.isFrontPoint(c) && (oldm != null) && 
              baseSphere.isFrontPoint(oldc))
            g.drawLine(oldm[0],oldm[1],m[0],m[1]);
          oldm = m;
          oldc = c;
        }
      
      // Similar for the ecliptic.
      oldm = null;
      oldc = null;
      g.setColor(Color.yellow);
      for (int i = 0; i < numPts + 1; i++)
        {
          Coord dots = new Coord(2.0 * Math.PI * (double) i /(double) numPts,
                                 0.0,Coord.Ecliptic);
          Coord precessed = PlaceUtil0.from2000(dots,this.theJD);
          double[] c = precessed.toRectangular();
          GUtil.addTo(baseSphere.getCenter(),c);
          int[] m = baseSphere.sphereToScreen(c);
          
          // Only draw if c and oldc are on the visible side of the sphere.
          if (baseSphere.isFrontPoint(c) && (oldm != null) && 
              baseSphere.isFrontPoint(oldc))
            g.drawLine(oldm[0],oldm[1],m[0],m[1]);
          oldm = m;
          oldc = c;
        }
      
      // Add the north and south poles. These are the poles at their position today. 
      // As the equator moves so do the poles.
      double[] north = new double[] {0.0,0.0,1.0};
      baseSphere.addDisk(finalImage,north,0.05,Color.red);
      double[] south = new double[] {0.0,0.0,-1.0};
      baseSphere.addDisk(finalImage,south,0.05,Color.green);
      
      // Put the date at the top-left.
      g.setColor(Color.white);
      CalDate theDate = TimeUtil0.jdToCalendar(this.theJD);
      String msg = "Year = " +Integer.toString(theDate.year);
      g.drawString(msg,10,20);
    }
  }
  
  protected void paintComponent(Graphics g) {
    
    // Very little to do since other threads continuously keep the buffer updated.
    g.drawImage(finalImage,0,0,null);
  }
  
  public static void main(String[] args) {
    
    PrecessSphere thePane = new PrecessSphere();
    SimpleWindow theWindow = new SimpleWindow("General Precession",thePane);
    
    // Start the two threads that maintain finalImage.
    thePane.baseMgr.start();
    thePane.theTimer.start();
  }
}