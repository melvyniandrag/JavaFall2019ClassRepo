package rsf.astron.graphics.incs;

// A window onto InsideStarSphere. 

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import rsf.ui.MyPanel;
import rsf.ui.LoadOrSaveDialog;
import rsf.ui.SimpleWindow;
import rsf.astron.stars.StarList;
import rsf.graphics.sphere.InsideKeyListener;


public class InsideStarWindow extends MyPanel implements ActionListener {
  
  // These are much as in previous programs.
  private InsideStarSphere theSphere = null;
  private InsideSphereMgr baseMgr = null;
  private int delay = 10;
  private Timer theTimer = null;
  
  
  public InsideStarWindow(StarList theStars) {
    
    theSphere = new InsideStarSphere(this.initialWidth,this.initialHeight,theStars);
    baseMgr = new InsideSphereMgr(theSphere.baseSphere);
    this.addKeyListener(new InsideKeyListener(baseMgr,this));
    this.setFocusable(true);
    theTimer = new Timer(delay,this);
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // Called by theTimer to draw a new frame.
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
    
    InsideStarWindow thePane = new InsideStarWindow(theStars);
    SimpleWindow theWindow = new SimpleWindow("Star Sphere",thePane);
    
    // Start the two threads that maintain finalImage.
    thePane.start();
  }
}