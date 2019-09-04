package rsf.astron.jpl.anim;

// Here is where the animation is drawn. It is the work-horse of the program.
// 
// There may be multiple threads accessing this class: a thread that's drawing
// a frame of the animation, one that's responding to user input and another
// that's moving time forward to the next step.

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

import rsf.astron.jpl.JPLData;
import rsf.graphics.GUtil;


public class MobilePane extends JPanel implements KeyListener,ActionListener {

  // Dimensions of the drawing pane.
  public static final int viewWidth = 1000;
  public static final int viewHeight = 1000;
  
  // The observer lies on a sphere of large indeterminate radius. These two
  // vectors are perpendicular vectors that are tangent to the sphere at
  // the point where the observer is at. They define the tangent plane.
  // The observer is always assumed to look toward the origin.
  // u1 is the x-axis of the screen, so it changes when the observer moves
  // left/right. u2 is the y-axis of the screen and changes in response to
  // an up/down move.
  private double[] u1 = new double[3];
  private double[] u2 = new double[3];

  // How fast the animation runs. This is the number of days between each frame
  // of the animation.
  protected double timeStep = 1.0;

  // The JD of the current frame of the animation.
  protected double curJD = 0;
  
  // How long the trail of dust should be, expressed as a number in the range
  // from 0 to 10,MaxTrailLength.
  private final static int MaxTrailLength = 10000;
  private int dustLength = 1000;
  
  // The trail of dust can be up to MaxTrailLength samples long. There are 7 
  // planets shown (including the sun) and 3 coordinates for each point of dust.
  // dustTrail[i][j] is the j-th point of dust trailed by the i-th planet.
  private double[][][] dustTrail = new double[7][MaxTrailLength][3];
  
  // The dust trail for each planet is a rolling window of the position in
  // time. Initially, the trail is empty and dustFull is false. Each new
  // position is inserted at dustTrail[][dustNext] until dustNext reaches
  // the limit of the length of dustTrail, at which point dustFull becomes
  // true.
  private boolean dustFull = false;
  private int dustNext = 0;
  
  // Whether to draw the dust at all.
  private boolean drawDust = true;
  
  // Whether the animation has been paused.
  private boolean paused = true;
  
  // A timer used to advance the animation through time.
  private Timer theTimer = null;
  private int delay = 10;
  
  // The JPL ephemeris data.
  private JPLData eph = null;
  

  public MobilePane(JPLData eph) {
    this.eph = eph;
    
    // The initial plane onto which the system is projected.
    u1[0] = 1.0;
    u1[1] = 0.0;
    u1[2] = 0.0;

    u2[0] = 0.0;
    u2[1] = 1.0;
    u2[2] = 0.0;
    
    this.setFocusable(true);
    this.addKeyListener(this);
  }
  
  public void goFaster() {
    timeStep = timeStep * 1.4;
  }
  
  public void goSlower() {
    timeStep = timeStep / 1.4;
  }
  
  public void pause() {
    
    // Pause the animation without losing its current position in time.
    paused = true;
  }
  
  public void continu() {
    
    // Continue to run the animation from where it was paused.
    paused = false;
  }
  
  public void dustOn() {
    
    // Turn on the dust that makes the paths of the the orbits visible.
    this.drawDust = true;
  }
  
  public void dustOff() {
    
    // Turn off the dust that makes the paths of the the orbits visible.
    this.drawDust = false;
  }
  
  public void moreDust() {
    dustLength *= 1.4;
    if (dustLength > MaxTrailLength)
      dustLength = MaxTrailLength;
  }
  
  public void lessDust() {
    dustLength /= 1.4;
    if (dustLength < 4)
      dustLength = 4;
  }
  
  synchronized public void keyTyped(KeyEvent e) {
    
    // This is very much as is done by EyeKeyListener. When the user types one of
    // J, L, M, I, U or O, the eye is moved or rotated.
    
    char c = e.getKeyChar();
    
    // This is the angle by which the eye moves with each key event.
    double angle = Math.PI/120.0;
    
    if ((c == 'l') || (c == 'L'))
      {
        // Move right by angle. That is, rotate u1 about u2.
        u1 = GUtil.rotate3(-angle,u2,u1);
        
        // Done. Force a new frame to be drawn.
        repaint();
      }

    if ((c == 'j') || (c == 'J'))
      {
        // Move left.
        u1 = GUtil.rotate3(angle,u2,u1);
        repaint();
      }
      
    if ((c == 'i') || (c == 'I'))
      {
        // Move up. Now rotating u2 about u1.
        u2 = GUtil.rotate3(-angle,u1,u2);
        repaint();
      }

    if ((c == 'm') || (c == 'M'))
      {
        // Move down.
        u2 = GUtil.rotate3(angle,u1,u2);
        repaint();
      }
    
    if ((c == 'o') || (c == 'O'))
      {
        // Twist clockwise.
        double[] eyeVector = GUtil.cross(u1,u2);
        u1 = GUtil.rotate3(angle,eyeVector,u1);
        u2 = GUtil.rotate3(angle,eyeVector,u2);
        repaint();
      }
    
    if ((c == 'u') || (c == 'U'))
      {
        // Twist counter-clockwise.
        double[] eyeVector = GUtil.cross(u1,u2);
        u1 = GUtil.rotate3(-angle,eyeVector,u1);
        u2 = GUtil.rotate3(-angle,eyeVector,u2);
        repaint();
      }
  }
  
  // These are ignored.
  public void keyPressed(KeyEvent e) { ; }
  public void keyReleased(KeyEvent e) { ; }

  synchronized protected void paintComponent(Graphics g) {

    // Clear the picture to black.
    int width = this.getSize().width;
    int height = this.getSize().height;
    g.setColor(Color.BLACK);
    g.fillRect(0,0,width,height);
    
    // Show the current JD in the upper left.
    g.setColor(Color.RED);
    g.drawString(String.format("JD = %4.2f",curJD),0,15);
    
    // Use this as the origin of the drawing. SSB is what you'd expect, but 
    // other planets work too.
    int origin = JPLData.SSB;
    
    PlanetState[] thePlanet = new PlanetState[7];
    for (int i = 0; i < 7; i++)
      thePlanet[i] = new PlanetState();
    
    // For each planet, get its coordinates and set its color. pleph() returns
    // the position and velocity and we only want position.
    double[] state = eph.getPosition(curJD,JPLData.Sun,origin);
    thePlanet[0].position = GUtil.arrayStart(state,3);
    thePlanet[0].pixelRadius = 6;
    thePlanet[0].theColor = Color.ORANGE;
    
    state = eph.getPosition(curJD,JPLData.Mercury,origin);
    thePlanet[1].position = GUtil.arrayStart(state,3);
    thePlanet[1].pixelRadius = 1;
    thePlanet[1].theColor = Color.YELLOW;
    
    state = eph.getPosition(curJD,JPLData.Venus,origin);
    thePlanet[2].position = GUtil.arrayStart(state,3);
    thePlanet[2].pixelRadius = 2;
    thePlanet[2].theColor = Color.PINK;
    
    state = eph.getPosition(curJD,JPLData.Earth,origin);
    thePlanet[3].position = GUtil.arrayStart(state,3);
    thePlanet[3].pixelRadius = 3;
    thePlanet[3].theColor = Color.BLUE;
    
    state = eph.getPosition(curJD,JPLData.Mars,origin);
    thePlanet[4].position = GUtil.arrayStart(state,3);
    thePlanet[4].pixelRadius = 2;
    thePlanet[4].theColor = Color.RED;
    
    state = eph.getPosition(curJD,JPLData.Jupiter,origin);
    thePlanet[5].position = GUtil.arrayStart(state,3);
    thePlanet[5].pixelRadius = 5;
    thePlanet[5].theColor = Color.GRAY;
    
    state = eph.getPosition(curJD,JPLData.Saturn,origin);
    thePlanet[6].position = GUtil.arrayStart(state,3);
    thePlanet[6].pixelRadius = 4;
    thePlanet[6].theColor = Color.GREEN;
    
    // Before sorting below, note the positions of the objects in dustTrail.
    for (int i = 0; i < thePlanet.length; i++)
      dustTrail[i][dustNext] = thePlanet[i].position;
    
    if (dustFull == false)
      {
        if (dustNext == MaxTrailLength - 1)
          dustFull = true;
      }
    ++dustNext;
    
    // If the dustTrail array is full, then overwrite the array, starting from the
    // beginning.
    if (dustNext == MaxTrailLength)
      dustNext = 0;
    
    // Draw the dust, if called for. No attempt is made to see which dust is in front. 
    // In theory, it could be sorted, but it would be too time consuming. The purpose of
    // the dust is to help visualize the orbits. Overlapping shouldn't hurt that.
    if (drawDust == true)
      {
        // Loop over each planet.
        for (int i = 0; i < dustTrail.length; i++)
          {
            int startIndex = dustNext - dustLength;
            if (startIndex < 0)
              startIndex = startIndex + MaxTrailLength;
            
            // Draw the trail for the current planet.
            boolean done = false;
            int j = startIndex;
            int curPoint = 0;
            float[] planetRGB = thePlanet[i].theColor.getRGBColorComponents(null);
            while (done == false)
              {
                double[] projection = GUtil.parallelProject(u1,u2,dustTrail[i][j]);
                
                // Scale the position to the monitor.
                int mag = 50;
                int offset = 500;
                int x = (int) (projection[0]/eph.AU * mag) + offset;
                int y = (int) (projection[1]/eph.AU * mag) + offset;
                
                // Reduce the intensity of the color as the point is further back in time.
                // Multiplying each term by curPoint/dustLength works, but making the 
                // intensity decay a bit faster looks better.
                float decayFactor = (float) curPoint / (float) dustLength;
                decayFactor = decayFactor * decayFactor;
                Color curColor = new Color(planetRGB[0] * decayFactor,
                                           planetRGB[1] * decayFactor,
                                           planetRGB[2] * decayFactor);
                g.setColor(curColor);
                g.fillRect(x,y,1,1);
                
                j++;
                if (j >= MaxTrailLength)
                  j = 0;
                if (j == dustNext)
                  done = true;
                ++curPoint;
              }
          }
      }
    
    // Determine the distance of each object from the observer. This is used later when 
    // things are drawn so that whatever is in front when planets cross is drawn last. 
    // Only relative distances are needed, so the fact that the observer is at an 
    // indeterminate distance from the origin doesn't matter.
    double[] u3 = GUtil.cross(u1,u2);
    for (int i = 0; i < thePlanet.length; i++)
      thePlanet[i].distance = GUtil.dot(thePlanet[i].position,u3);
    
    // Before doing any drawing, sort the items according to their distance from the
    // observer. The most distant objects are drawn first; then things overlap correctly.
    for (int i = 0; i < thePlanet.length; i++)
      {
        for (int j = i+1; j < thePlanet.length; j++)
          {
            if (thePlanet[i].distance < thePlanet[j].distance)
              {
                PlanetState temp = thePlanet[i];
                thePlanet[i] = thePlanet[j];
                thePlanet[j] = temp;
              }
          }
      }
    
    // Draw the planets.
    for (int i = 0; i < thePlanet.length; i++)
      {   
        // Project it onto the plane of u1 and u2.
        double[] projection = GUtil.parallelProject(u1,u2,thePlanet[i].position);
        
        // Scale the position to the monitor.
        int mag = 50;
        int offset = 500;
        int x = (int) (projection[0]/eph.AU * mag) + offset;
        int y = (int) (projection[1]/eph.AU * mag) + offset;
        
        g.setColor(thePlanet[i].theColor);
        GUtil.fillCircle(x,y,thePlanet[i].pixelRadius,g);
      }
  }
  
  synchronized public void actionPerformed(ActionEvent e) {
    
    // When this is called, advance the animation by one frame, for timeStep
    // Julian days.
    
    // Don't bother if off the end of the data.
    if (curJD + timeStep > eph.endJD)
      return;
    
    // Don't advance if the user paused the animation.
    if (paused == true)
      return;
      
    if (curJD + timeStep < eph.endJD)
      curJD += timeStep;
    
    this.repaint();
  }

  public void startAnim() {
    
    this.curJD = eph.startJD;
    this.theTimer = new Timer(delay,this);
    theTimer.start();
    
    // Erase the dust trail.
    dustFull = false;
    dustNext = 0;
    continu();
  }
}