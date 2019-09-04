package rsf.astron.graphics.incs;

// This animates CEarthPane. It's similar to CSAnim. It shows the celestial sphere from the
// point of view of an observer at the center of the earth. The eye always points to a fixed
// point on the sphere; the eye does not move as the earth rotates.

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.CheckboxMenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import javax.swing.Timer;
import javax.swing.JFrame;

import rsf.ui.LoadOrSaveDialog;
import rsf.ui.WindowQuitter;
import rsf.astron.jpl.JPLData;
import rsf.astron.stars.StarList;
import rsf.graphics.sphere.InsideKeyListener;


public class CEarthAnim extends JFrame implements ActionListener,ItemListener {
  
  // These variables are very much as in previous cases.
  private CEarthPane theSky = null;
  private InsideSphereMgr sphereMgr = null;
  int delay = 10;
  Timer theTimer = null;

  // Copies of the menu items must be held here to manage checking and un-checking
  // of user choices. See initMenus().
  private CheckboxMenuItem[] perspMenu = new CheckboxMenuItem[4];

  
  public CEarthAnim(StarList theStars,JPLData eph) {
    
    // The constructor follows the usual sequence.
    initMenus();
    this.addWindowListener(new WindowQuitter());
    
    this.theSky = new CEarthPane(theStars,eph);
    sphereMgr = new InsideSphereMgr(theSky.baseSphere);
    theSky.baseSphere.makeRadialAxisU1();
    
    this.addKeyListener(new InsideKeyListener(sphereMgr,theSky));
    this.setFocusable(true);
    theTimer = new Timer(delay,theSky);
    
    theSky.setPreferredSize(new Dimension(theSky.initialWidth,theSky.initialHeight));
    theSky.setMaximumSize(new Dimension(theSky.initialWidth,theSky.initialHeight));
    this.getContentPane().add(theSky);
    this.pack();
    this.setVisible(true);
  }
  
  public void initMenus() {
    
    // Prepare the menus for use.
    MenuBar theMenuBar = new MenuBar();

    Menu theMenu = new Menu("File",false);
    theMenu.add(new MenuItem("Quit",new MenuShortcut(KeyEvent.VK_Q,false)));
    theMenu.addActionListener(this);
    theMenuBar.add(theMenu);

    theMenu = new Menu("Projection",false);
    perspMenu[0] = new CheckboxMenuItem("Perspective");
    perspMenu[1] = new CheckboxMenuItem("Parallel"); 
    perspMenu[2] = new CheckboxMenuItem("Radial U1");
    perspMenu[3] = new CheckboxMenuItem("Radial U2");
    
    // Check the radial-U1 projection item since that's the initial setting.
    perspMenu[2].setState(true);
    
    // Add the items, and register their listener.
    for (int i = 0; i < perspMenu.length; i++)
      {
        theMenu.add(perspMenu[i]);
        perspMenu[i].addItemListener(this);
      }
    
    theMenu.addActionListener(this);
    theMenuBar.add(theMenu);

    this.setMenuBar(theMenuBar);
  }

  public void actionPerformed(ActionEvent e) {
   
    // Handle a menu command. The projection menu is handled by itemStateChanged()
    // so the only thing to worry about is "Quit".
    String cmd = e.getActionCommand();
    
    if (cmd.equals("Quit"))
      System.exit(0);
  }
  
  public void itemStateChanged(ItemEvent e) {
    
    // Just like actionPerformed(), but this is called for one of the items on the
    // projection menu.
    String cmd = (String) e.getItem();
    
    if (cmd.equals("Perspective"))
      {
        adjustMenuChecks(0);
        theSky.baseSphere.makePerspectiveProjection();
      }
    else if (cmd.equals("Parallel"))
      {
        adjustMenuChecks(1);
        theSky.baseSphere.makeParallelProjection();
      }
    else if (cmd.equals("Radial U1"))
      {
        adjustMenuChecks(2);
        theSky.baseSphere.makeRadialAxisU1();
      }
    else if (cmd.equals("Radial U2"))
      {
        adjustMenuChecks(3);
        theSky.baseSphere.makeRadialAxisU2();
      }
  }
  
  private void adjustMenuChecks(int whichOn) {
    
    // Called when the user changes the method of projection with the menu. Un-check all
    // the menu items, then turn on the item whichOn.
    for (int i = 0; i < perspMenu.length; i++)
      perspMenu[i].setState(false);
    perspMenu[whichOn].setState(true);
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
    CEarthAnim theAnim = new CEarthAnim(theStars,eph);
    
    // Start the two threads that maintain finalImage.
    theAnim.start();
  }
}