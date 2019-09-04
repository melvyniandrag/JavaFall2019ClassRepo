package rsf.astron.graphics.surface;

// Program that shows a view from the surface of the earth. 

import java.awt.CheckboxMenuItem;
import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import rsf.ui.LoadOrSaveDialog;
import rsf.ui.TextInputDialog;
import rsf.ui.WindowQuitter;
import rsf.astron.graphics.incs.InsideSphereMgr;
import rsf.astron.time.TimeUtil;
import rsf.astron.jpl.JPLData;
import rsf.astron.stars.StarList;
import rsf.graphics.sphere.InsideKeyListener;


public class SurfaceMain extends JFrame implements ActionListener,ItemListener {
  
  // These variables are similar to other cases.
  private SurfacePane theSky = null;
  private InsideSphereMgr sphereMgr = null;
  int delay = 10;
  Timer theTimer = null;
  private double timeStep = 0.01;
  private boolean paused = false;
  
  // See initMenus().
  private CheckboxMenuItem[] perspMenu = new CheckboxMenuItem[4];
  private CheckboxMenuItem altAzMenu = null;
  
  
  public SurfaceMain(StarList theStars,JPLData eph) {
    
    super("View from Earth's Surface");
    initMenus();
    this.addWindowListener(new WindowQuitter());
    
    this.theSky = new SurfacePane(theStars,eph);
    sphereMgr = new InsideSphereMgr(theSky.baseSphere);
    theSky.baseSphere.makeRadialAxisU1();
    
    this.addKeyListener(new InsideKeyListener(sphereMgr,theSky));
    this.setFocusable(true);
    theTimer = new Timer(delay,this);
    
    theSky.setPreferredSize(new Dimension(theSky.initialWidth,theSky.initialHeight));
    theSky.setMaximumSize(new Dimension(theSky.initialWidth,theSky.initialHeight));
    this.getContentPane().add(theSky);
    this.pack();
    this.setVisible(true);
  }
  
  private void initMenus() {
    
    // Prepare the menus for use. This is a lot like CEarthAnim.
    MenuBar theMenuBar = new MenuBar();

    Menu theMenu = new Menu("File",false);
    theMenu.add(new MenuItem("Quit",new MenuShortcut(KeyEvent.VK_Q,false)));
    theMenu.addActionListener(this);
    theMenuBar.add(theMenu);
    
    theMenu = new Menu("Place");
    theMenu.add(new MenuItem("Set Date/Time...",new MenuShortcut(KeyEvent.VK_T,false)));
    theMenu.add(new MenuItem("Set Julian Date...",new MenuShortcut(KeyEvent.VK_J,false)));
    theMenu.add(new MenuItem("Set Lat/Long...",new MenuShortcut(KeyEvent.VK_L,false)));
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
    
    theMenu = new Menu("Movie",false);
    theMenu.add(new MenuItem("Pause", new MenuShortcut(KeyEvent.VK_P,false)));
    theMenu.add(new MenuItem("Continue", new MenuShortcut(KeyEvent.VK_C,false)));
    theMenu.add(new MenuItem("Faster", new MenuShortcut(KeyEvent.VK_F,false)));
    theMenu.add(new MenuItem("Slower", new MenuShortcut(KeyEvent.VK_S,false)));
    theMenu.add(new MenuItem("Set Step Rate...", new MenuShortcut(KeyEvent.VK_R,false)));
    this.altAzMenu = new CheckboxMenuItem("Draw Alt-Az Lines");
    altAzMenu.addItemListener(this);
    theMenu.add(altAzMenu);
    
    theMenu.addActionListener(this);
    theMenuBar.add(theMenu);
    
    this.setMenuBar(theMenuBar);
  }
  
  public void actionPerformed(ActionEvent e) {
   
    // When this is called, it could be a menu item or timer asking for a new frame.
    // The action command for the timer is "null".
    String cmd = e.getActionCommand();

    if (cmd == null)
      {
        // It's the timer. Generate a new frame. Check whether the window has been re-sized, 
        // then draw its contents.
        theSky.resize(this.getWidth(),this.getHeight());
        
        if (paused == false)
          {
            theSky.addToJD(timeStep);
            String msg = String.format("JD = %4.2f",theSky.jd);
            theSky.setMsg(msg); 
          }
        theSky.update();
        this.repaint();
        return;
      }
    
    // Must be a menu item.
    if (cmd.equals("Quit"))
      System.exit(0);
    else if (cmd.equals("Set Date/Time..."))
      setDateTime();
    else if (cmd.equals("Set Julian Date..."))
      setJulianDate();
    else if (cmd.equals("Set Lat/Long..."))
      setLatLong();
    else if (cmd.equals("Pause"))
      this.paused = true;
    else if (cmd.equals("Continue"))
      this.paused = false;
    else if (cmd.equals("Faster"))
      timeStep *= 1.4;
    else if (cmd.equals("Slower"))
      timeStep /= 1.4;
    else if (cmd.equals("Set Step Rate..."))
      setRate();
  }
  
  private void setDateTime() {
    
    // Let the user set the current time by specifying a calendar date and clock time.
    boolean valid = false;
    while (valid == false)
      {
        // Get user inputs from a dialog
        String[] labels = new String[3];
        labels[0] = "YYYYMMDD";
        labels[1] = "HHMMSS";
        labels[2] = "Greenwich Offset";
        TextInputDialog inputDialog = new TextInputDialog("Input Local Date and Time",
                                                          labels,10);
        String[] inputs = inputDialog.getInputs();
        if (inputs == null)
          // User canceled.
          return;
        
        // See if the user's inputs are sensible.
        int yyyymmdd = 0;
        int hhmmss = 0;
        int zoneOffset = -1;;
        valid = true;
        try {
          yyyymmdd = Integer.parseInt(inputs[0]);
          hhmmss = Integer.parseInt(inputs[1]);
          zoneOffset = Integer.parseInt(inputs[2]);
        } catch (Exception e) {
          valid = false;
        }
        
        if (valid == true)
          {
            // The inputs parse. Change the current JD accordingly. PlaceUtil.main() 
            // is similar.
            double localClock = TimeUtil.hhmmssToHours(hhmmss);
            localClock -= zoneOffset;
            double theJD = TimeUtil.yyyymmddToJD(yyyymmdd) + (localClock - 12.0) / 24.0;
            theSky.jd = theJD;
            
            // Request a screen update.
            String msg = String.format("JD = %4.2f",theSky.jd);
            theSky.setMsg(msg); 
            theSky.update();
            this.repaint(); 
          }
        else
          // User's input doesn't make sense.
          JOptionPane.showMessageDialog(null,
              "Input the date in the form YYYYMMDD, together with\n" +
              "the time in the form HHMMSS, as given by a local\n" +
              "clock. Greenwich offset is the number of time\n" +
              "zones that the clock is from Greenwich, England.");
      }
  }

  private void setJulianDate() {
    
    // Let the user set the current time.
    boolean valid = false;
    while (valid == false)
      {
        // Get user inputs from a dialog
        String[] labels = new String[1];
        labels[0] = "Julian date";
        TextInputDialog inputDialog = new TextInputDialog("Input Julian Date",labels,10);
        String[] inputs = inputDialog.getInputs();
        if (inputs == null)
          // User canceled.
          return;
        
        // See if the user's inputs are sensible.
        double inputJD = 0.0;
        valid = true;
        try {
          inputJD = Double.parseDouble(inputs[3]);
        } catch (Exception e) {
          valid = false;
        }
        
        if (valid == true)
          {
            // The inputs parse. Change the current JD accordingly.
            theSky.jd = inputJD;
            
            // Request a screen update.
            String msg = String.format("JD = %4.2f",theSky.jd);
            theSky.setMsg(msg); 
            theSky.update();
            this.repaint(); 
          }
        else
          // User's input doesn't make sense.
          JOptionPane.showMessageDialog(null,"Input the Julian date as a decimal value.");
      }
  }
  
  private void setLatLong() {
    
    // Let the user set the observer's latitude and longitude. Similar to setTime().
    boolean valid = false;
    while (valid == false)
      {
        String[] labels = new String[2];
        labels[0] = "Latitude (decimal degrees)";
        labels[1] = "Longitude (decimal degrees)";
        TextInputDialog inputDialog = new TextInputDialog("Input Observer's Lat/Long",
                                                          labels,10);
        String[] inputs = inputDialog.getInputs();
        if (inputs == null)
          // User canceled.
          return;
    
        double latitude = 0.0;
        double longitude = 0.0;
        valid = true;
        try {
          latitude = Double.parseDouble(inputs[0]);
          longitude = Double.parseDouble(inputs[1]);
        } catch (Exception e) {
          valid = false;
        }
        
        if (valid == true)
          {
            latitude = Math.toRadians(latitude);
            longitude = Math.toRadians(longitude);
            theSky.latitude = latitude;
            theSky.longitude = longitude;
            theSky.update();
            this.repaint(); 
          }
        else
          JOptionPane.showMessageDialog(null,
              "Inputs are in decimal degrees, with latitude in the\n" +
              "range [-90,+90], positive in the northern hemisphere.\n" +
              "Longitude is in [-180,+180], positive to the east of\n" +
              "Greenwich, England");
      }
  }

  private void setRate() {
    
    // Dialog to let the user set the time-step from frame to frame. 
    boolean valid = false;
    while (valid == false)
      {
        String[] labels = new String[1];
        labels[0] = "Julian days per frame";
        TextInputDialog inputDialog = new TextInputDialog("Time per Frame",labels,10);
        String[] inputs = inputDialog.getInputs();
        if (inputs == null)
          // User canceled.
          return;
        
        double frameStep = 0.0;
        valid = true;
        try {
          frameStep = Double.parseDouble(inputs[0]);
        } catch (Exception e) {
          valid = false;
        }
        
        if (valid == true)
          this.timeStep = frameStep;
        else
          JOptionPane.showMessageDialog(null,"Input the Julian step size as a decimal.");
      }
  }
  
  public void itemStateChanged(ItemEvent e) {
    
    // Handle choices among the "Projection" menu, and whether to draw alt/az lines.
    // This is as in CEarthAnim.
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
    else if (cmd.equals("Draw Alt-Az Lines"))
      {
        if (theSky.drawAltAzLines == true)
          { 
            this.altAzMenu.setState(false);
            theSky.drawAltAzLines = false;
          }
        else
          { 
            this.altAzMenu.setState(true);
            theSky.drawAltAzLines = true;
          }
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
    
    // Bring up the window and start the animation.
    SurfaceMain theAnim = new SurfaceMain(theStars,eph);
    theAnim.start();
  }
}