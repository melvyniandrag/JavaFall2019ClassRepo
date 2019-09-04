package rsf.astron.graphics.cs;

// Window onto an animation of CelestialSphere, which shows both the stars and
// the planets. Very similar to JPLSphereAnim and StarAnim.

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import rsf.ui.MyPanel;
import rsf.ui.LoadOrSaveDialog;
import rsf.ui.SimpleWindow;
import rsf.astron.graphics.intro.BaseSphereMgr;
import rsf.graphics.sphere.EyeKeyListener;
import rsf.astron.stars.StarList;
import rsf.astron.jpl.JPLData;


public class CSAnim extends MyPanel implements ActionListener {

  // As in earlier programs.
  private CelestialSphere theSky = null;
  private BaseSphereMgr baseMgr = null;
  int delay = 10;
  Timer theTimer = null;
  private double timeStep = 0.5;
  
  public CSAnim(StarList theStars,JPLData eph) {
 
    this.theSky = new CelestialSphere(this.initialWidth,this.initialHeight,theStars,eph);
    baseMgr = new BaseSphereMgr(theSky.baseSphere); 
    this.addKeyListener(new EyeKeyListener(baseMgr,this));
    this.setFocusable(true);
    theTimer = new Timer(delay,this);
  }

  public void actionPerformed(ActionEvent e) {
   
    // When this is called, generate a new frame.
    
    // Check whether the window has been re-sized, then draw contents.
    theSky.resize(this.getWidth(),this.getHeight());

    theSky.addToJD(timeStep);
    String msg = String.format("JD = %4.2f",theSky.jd);
    theSky.setMsg(msg); 
    theSky.update();
    this.repaint();
  }

  protected void paintComponent(Graphics g) {
  
    // Very little to do since other threads continuously keep the buffer updated.
    theSky.draw(g);
  }

  public void start() {
    theSky.update();
    theTimer.start();
  }

  public static void main(String[] args) {
 
    // Load the star data.
    String[] choice = LoadOrSaveDialog.getLoadChoice("Open Star File");
    if (choice == null)
      System.exit(0);

    StarList theStars = new StarList();
    theStars.load(choice[0],choice[1]);
    
    // Load the JPL data.
    choice = LoadOrSaveDialog.getLoadChoice("Open JPL File");
    if (choice == null)
      System.exit(0);
    
    JPLData eph = JPLData.load(choice[0],choice[1]);
    
    CSAnim theAnim = new CSAnim(theStars,eph);
    SimpleWindow theWindow = new SimpleWindow("Celestial Sphere Animation",theAnim);
 
    // Start the two threads that maintain finalImage.
    theAnim.start();
  }
}