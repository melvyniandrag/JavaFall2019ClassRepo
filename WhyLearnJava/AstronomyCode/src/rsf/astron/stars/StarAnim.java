package rsf.astron.stars;

// Like StarSphere, but it animates the stars' positions, taking into account their proper
// motion. The most direct way to do this is to use StarSphere, but adjust the coordinates
// that are given in StarSphere.theStars.

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import rsf.astron.graphics.intro.BaseSphereMgr;
import rsf.graphics.sphere.EyeKeyListener;
import rsf.ui.LoadOrSaveDialog;
import rsf.ui.MyPanel;
import rsf.ui.SimpleWindow;


public class StarAnim extends MyPanel implements ActionListener {
  
  // This is an unchanged copy of the star data, as loaded from disk.
  private StarList theStars = null;
  
  // As time passes, the values stored in theSky.theStars change.
  private StarSphere theSky = null;
  
  // As in earlier programs.
  private BaseSphereMgr baseMgr = null;
  int delay = 10;
  Timer theTimer = null;
  
  // Time of current frame, and amount of time stepped with each frame, in years.
  double t = -100000.0;
  double timeStep = 100.0;
  double tFinal = 100000.0;
  
  
  public StarAnim(StarList theStars) {
    
    this.theSky = new StarSphere(this.initialWidth,this.initialHeight,theStars);
    this.theStars = theStars;
    this.theSky.theStars = theSky.theStars;
    baseMgr = new BaseSphereMgr(theSky.baseSphere);
    
    this.addKeyListener(new EyeKeyListener(baseMgr,this));
    this.setFocusable(true);
    theTimer = new Timer(delay,this);
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // When this is called, generate a new frame. Reset theStars to have new positions, and
    // request an update.
    
    // Check whether the window has been re-sized, then draw contents.
    theSky.resize(this.getWidth(),this.getHeight());
    
    if ((theStars != null) && (t <= tFinal))
      {
        synchronized (theSky.theStars) {
          theSky.theStars = theStars.move(t);
        }
      }
    
    String msg = String.format("Year = %4.2f",t);
    theSky.setMsg(msg); 
    theSky.update();
    this.repaint();
    
    if (t < tFinal)
      t += timeStep;
    if (t > tFinal)
      t = tFinal;
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
    
    StarAnim theAnim = new StarAnim(theStars);
    theAnim.theSky.theStars = theStars;
    
    SimpleWindow theWindow = new SimpleWindow("Proper Motion Animation",theAnim);
    
    // Start the two threads that maintain finalImage.
    theAnim.start();
   }
}