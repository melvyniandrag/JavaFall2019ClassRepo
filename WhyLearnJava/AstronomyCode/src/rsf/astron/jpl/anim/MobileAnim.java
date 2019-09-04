package rsf.astron.jpl.anim;

// Similar to SimpleAnim, but the observer has limited mobility. The observer is able to 
// move over a sphere centered at the origin using the keyboard. The eye is always pointed
// to the origin.
//
// This class manages the user-interface (menus mostly) and delegates the work to MobilePane.

import java.awt.Dimension;
import java.awt.MenuBar;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;

import rsf.ui.LoadOrSaveDialog;
import rsf.astron.jpl.JPLData;


public class MobileAnim extends JFrame implements ActionListener {

  // Where the animation is shown.
  private static MobilePane theAnim = null;
  
  public MobileAnim() {
    super("JPL Animation with Mobility");
    
    initMenus();
    
    theAnim.setPreferredSize(
      new Dimension(MobilePane.viewWidth,MobilePane.viewHeight));
    theAnim.setMaximumSize(
      new Dimension(MobilePane.viewWidth,MobilePane.viewHeight));

    this.getContentPane().add(theAnim);
    this.pack();
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);    
  }

  private void initMenus() {
    
    MenuBar theMenuBar = new MenuBar();

    Menu theMenu = new Menu("File",false);
    theMenu.add(new MenuItem("Quit",new MenuShortcut(KeyEvent.VK_Q,false)));
    theMenu.addActionListener(this);
    theMenuBar.add(theMenu);

    theMenu = new Menu("Movie",false);
    theMenu.add(new MenuItem("Run Movie",new MenuShortcut(KeyEvent.VK_R,false)));
    theMenu.add(new MenuItem("Pause",new MenuShortcut(KeyEvent.VK_P,false)));
    theMenu.add(new MenuItem("Continue",new MenuShortcut(KeyEvent.VK_C,false)));
    theMenu.add(new MenuItem("Faster",new MenuShortcut(KeyEvent.VK_F,false)));
    theMenu.add(new MenuItem("Slower",new MenuShortcut(KeyEvent.VK_S,false)));
    theMenu.add(new MenuItem("Dust On",new MenuShortcut(KeyEvent.VK_D,false)));
    theMenu.add(new MenuItem("Dust Off",new MenuShortcut(KeyEvent.VK_O,false)));
    theMenu.add(new MenuItem("More Dust",new MenuShortcut(KeyEvent.VK_M,false)));
    theMenu.add(new MenuItem("Less Dust",new MenuShortcut(KeyEvent.VK_L,false)));
    theMenu.addActionListener(this);
    theMenuBar.add(theMenu);
    
    // The complete menu bar has been defined. Make it part of this window.
    this.setMenuBar(theMenuBar);
  } 

  public void actionPerformed(ActionEvent e) {

    String cmd = e.getActionCommand();

    // Look for menu choices
    if (cmd.equals("Quit"))
      System.exit(0);
    else if (cmd.equals("Run Movie"))
      theAnim.startAnim();
    else if (cmd.equals("Faster"))
      theAnim.goFaster();
    else if (cmd.equals("Slower"))
      theAnim.goSlower();
    else if (cmd.equals("Pause"))
      theAnim.pause();
    else if (cmd.equals("Continue"))
      theAnim.continu();
    else if (cmd.equals("Dust On"))
      theAnim.dustOn();
    else if (cmd.equals("Dust Off"))
      theAnim.dustOff();
    else if (cmd.equals("More Dust"))
      theAnim.moreDust();
    else if (cmd.equals("Less Dust"))
      theAnim.lessDust();
  }
  
  public static void main(String[] args) {
    
    // Find the data file used to run the theAnim.
    String[] dataLoc = LoadOrSaveDialog.getLoadChoice("JPL Data?");
    if (dataLoc == null)
      System.exit(0);
    JPLData eph = new JPLData(dataLoc[0],dataLoc[1]);
      
    // Create the theAnim area and link it to the data.
    theAnim = new MobilePane(eph);
    
    // Open up the window that shows the theAnim.
    MobileAnim theWindow = new MobileAnim();
    
    // Start things off.
    theAnim.startAnim();
  }
}