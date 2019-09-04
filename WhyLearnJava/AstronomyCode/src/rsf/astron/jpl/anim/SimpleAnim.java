package rsf.astron.jpl.anim;

// Creates a simple window, with no user interaction, that shows the motion 
// of the planets over time.

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

import rsf.astron.jpl.JPLData;
import rsf.ui.LoadOrSaveDialog;
import rsf.graphics.GUtil;


public class SimpleAnim extends JFrame {
  
  // Specifies the size of the window's drawing area.
  final static int viewWidth = 1000;
  final static int viewHeight = 1000;
  
  // This holds the image, which is then copied to the window.
  public BufferedImage theImage = null;
  
  public SimpleAnim() {
    super("Simple JPL Animation");
    theImage = new BufferedImage(viewWidth,viewHeight,BufferedImage.TYPE_INT_BGR);
  }
  
  private void runAnim(String[] dataLoc) {
    
    // Load the JPL data specified by the user.
    JPLData theData = JPLData.load(dataLoc[0],dataLoc[1]);
    
    // Loop over time, one day per step, and draw the planets on that day.
    double step = 1;
    for (double jd = theData.startJD; jd+step < theData.endJD; jd += step)
      {
        // To reduce flicker, draw each frame of the animation to theImage.
        // Once the frame is complete copy it to the screen.
        // Clear the image.
        Graphics g = theImage.getGraphics();
        g.clearRect(0,0,viewWidth,viewHeight);
        
        // Draw each of the planets.
        drawPlanet(g,theData,jd,JPLData.Mercury,Color.YELLOW,2);
        drawPlanet(g,theData,jd,JPLData.Venus,Color.PINK,3);
        drawPlanet(g,theData,jd,JPLData.Earth,Color.BLUE,4);
        drawPlanet(g,theData,jd,JPLData.Mars,Color.RED,3);
        drawPlanet(g,theData,jd,JPLData.Sun,Color.ORANGE,7);
        drawPlanet(g,theData,jd,JPLData.Jupiter,Color.GRAY,6);
        drawPlanet(g,theData,jd,JPLData.Saturn,Color.GREEN,5);
        
        // Update the screen. This example is so simple that, unlike in other
        // programs, the image is drawn directly to the window without using
        // repaint() and paintComponent().
        g = this.getGraphics();
        g.drawImage(theImage,0,0,null);
      }
  }
  
  public static void drawPlanet(Graphics g,JPLData theData, double jd,
                                int whichPlanet,Color c,int radius) {
    
    int origin = JPLData.SSB;
    
    // Draw whichPlanet from origin, using color c. First, get the coordinates
    double[] p = theData.getPosition(jd,whichPlanet,origin);

    double xx = p[0];
    double yy = p[1];
    double zz = p[2];

    // Convert these to AU.
    xx = xx / theData.AU;
    yy = yy / theData.AU;
    zz = zz / theData.AU;
    
    // Something like a factor of 50 is good for seeing the planets going
    // around the sun, and something like 5000 for seeing the moon go around
    // the earth if origin is is set to JPLData.Earth.
    int mag = 50;
    xx *= mag;
    yy *= mag;
    zz *= mag;
    
    // Shift the coordinates to put the image in the center of the draw area.
    int x = (int) Math.round(xx + viewWidth / 2);
    int y = (int) Math.round(yy + viewHeight / 2);

    // Draw a circle to represent the planet.
    g.setColor(c);
    GUtil.fillCircle(x,y,radius,g);
  }
  
  public static void main(String[] args) {
    
    // Create a window to hold the animation.    
    SimpleAnim animWindow = new SimpleAnim();
    animWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    // Find the data file from which to run the animation.
    String[] dataLoc = LoadOrSaveDialog.getLoadChoice("JPL Data?");
    if (dataLoc == null)
      System.exit(0);
    
    // Size of window and its location on the screen.
    animWindow.setSize(viewWidth,viewHeight);
    animWindow.setLocation(10,10);
    animWindow.setVisible(true);
    
    animWindow.runAnim(dataLoc);
  }
}