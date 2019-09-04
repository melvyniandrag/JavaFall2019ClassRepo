package rsf.astron.jpl.anim;

// This animates the JPL data, with the planets on the celestial sphere.

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import rsf.ui.MyPanel;
import rsf.ui.SimpleWindow;
import rsf.ui.LoadOrSaveDialog;
import rsf.astron.graphics.intro.BaseSphereMgr;
import rsf.astron.jpl.JPLData;
import rsf.graphics.sphere.EyeKeyListener;


public class JPLSphereAnim extends MyPanel implements ActionListener {
  
  private JPLSphere theSphere = null;
  private BaseSphereMgr baseMgr = null;
  int delay = 10;
  Timer theTimer = new Timer(delay,this);
  
  // The amount of time from one frame to the next, in Julian days.
  private double timeStep = 0.5;
  
  
  public JPLSphereAnim(JPLData eph) {
    
    this.theSphere = new JPLSphere(this.initialWidth,this.initialHeight,eph);
    baseMgr = new BaseSphereMgr(theSphere.baseSphere);
    this.addKeyListener(new EyeKeyListener(baseMgr,this));
    this.setFocusable(true);
    theTimer = new Timer(delay,this);
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // Generate a new frame.
    
    // Check whether the window has been re-sized, then draw contents.
    theSphere.resize(this.getWidth(),this.getHeight());
    
    // Move time forward.
    theSphere.addToJD(timeStep);
    theSphere.update();
    this.repaint();
  }
  
  protected void paintComponent(Graphics g) {
    // Very little to do since other threads continuously keep the buffer updated.
    theSphere.draw(g);
  }
  
  public static void main(String[] args) {
  
    // Load the JPL data.
    String[] choice = LoadOrSaveDialog.getLoadChoice("JPL Data?");
    if (choice == null)
      System.exit(0);
    JPLData eph = JPLData.load(choice[0],choice[1]);
    
    JPLSphereAnim theAnim = new JPLSphereAnim(eph);
    SimpleWindow theWindow = new SimpleWindow("JPL Animation",theAnim);
    
    // Start the two threads that maintain finalImage.
    theAnim.theTimer.start();
  }
}