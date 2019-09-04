package rsf.astron.stars;

// Shows a StarSphere, with mobile eye.

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import rsf.astron.graphics.intro.BaseSphereMgr;
import rsf.graphics.sphere.EyeKeyListener;
import rsf.ui.LoadOrSaveDialog;
import rsf.ui.MyPanel;
import rsf.ui.SimpleWindow;


public class StarWindow extends MyPanel implements ActionListener {
  
  private StarSphere theSphere = null;

  //   Manages movement of the eye.
  private BaseSphereMgr baseMgr = null;
  
  // Whenever theTimer "goes off", actionPerformed() is called and a new frame is drawn.
  // delay is the number of milliseconds between frames.
  private int delay = 10;
  private Timer theTimer = null;
  
  
  public StarWindow(StarList theStars) {
    
    theSphere = new StarSphere(this.initialWidth,this.initialHeight,theStars);
    baseMgr = new BaseSphereMgr(theSphere.baseSphere);
    this.addKeyListener(new EyeKeyListener(baseMgr,this));
    this.setFocusable(true);
    theTimer = new Timer(delay,this);
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // The timer calls this every this.delay milliseconds. It draws a new frame.
    // Check whether the window has been re-sized, then draw contents.
    theSphere.resize(this.getWidth(),this.getHeight());
    theSphere.update();
    this.repaint();
  }
  
  protected void paintComponent(Graphics g) {   
    // Very little to do since other threads continuously keep the buffer updated.
    theSphere.draw(g);
  }
  
  public void start() {
    baseMgr.start();
    theTimer.start();
  }
  
  public static void main(String[] args) {
    
    // Load the star data.
    String[] choice = LoadOrSaveDialog.getLoadChoice("Open Star File");
    if (choice == null)
      System.exit(0);
    
    StarList theStars = new StarList();
    theStars.load(choice[0],choice[1]);
    
    StarWindow thePane = new StarWindow(theStars);
    SimpleWindow theWindow = new SimpleWindow("Star Sphere",thePane);
    
    // Start the two threads that maintain finalImage.
    thePane.start();
  }
}