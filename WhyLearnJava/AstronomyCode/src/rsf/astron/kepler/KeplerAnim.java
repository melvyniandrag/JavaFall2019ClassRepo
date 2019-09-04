package rsf.astron.kepler;

// Program that animates a planet going around the sun, in two dimensions.
//
// The basic method works like this. We know that M(t) grows at a constant rate. M(t)
// basically *is* time t. Kepler's Equation provides alpha (eccentric anomaly), then we 
// can get theta (true anomaly), which is what we need.
//
// Once theta is determined, assuming that the origin is at the sun (which is a focus), 
// the planet can be drawn using polar coordinates since
// r = l / (1 + e cos(theta)), where l = latus rectum.

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.Color;

import rsf.ui.MyPanel;
import rsf.ui.SimpleWindow;


public class KeplerAnim extends MyPanel implements ActionListener {

  // Eccentricity.
  double e = 0.40;
  
  // Used to trigger a new frame of the animation.
  Timer theTimer = null;
  int delay = 10;
  
  // The time and period, from which mean anomaly is M = 2 pi t / T.
  // T is a totally arbitrary quantity.
  double t = 0.0;
  double T = 100.0;
  
  // With each frame, t is advanced by this amount.
  double timeStep = 0.1;
  
  
  public KeplerAnim() {
    theTimer = new Timer(delay,this);
    this.setPreferredSize(new Dimension(this.initialWidth,this.initialHeight));
    this.setMaximumSize(new Dimension(this.initialWidth,this.initialHeight));
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // When this is called, generate a new frame.
    this.repaint();
    t += timeStep;
    while (t >= T)
      t -= T;
  }
  
  protected void paintComponent(Graphics g) {
    
    g.fillRect(0,0,this.getWidth(),this.getHeight());
    
    // Determine size of the ellipse. Make the major axis, a, equal to 0.80 of the window 
    // width.
    e = 0.80;
    double a = (0.80 * this.getWidth())/2.0;
    double b = a * Math.sqrt(1 - e * e);
    double c = a * e;
    double l = b * b / a;
    
    // Change the origin of g to make (0,0) the left focus. There should be enough space
    // to the left of the origin for the rest of the ellipse. This "rest of the ellipse" 
    // needs a - c pixels.
    // Define pad so that the left side of the ellipse is not right against the left side
    // of the window.
    int pad = 40; 
    g.translate((int)((a - c) + pad),this.getHeight() / 2);
      
    g.setColor(Color.blue);
    for (double theta = 0.0; theta < Math.PI * 2.0; theta += 0.04)
      {
        double r = l / (1.0 + e * Math.cos(theta));
        
        // Note the minus here in translating to polar coordinates due to the way screen
        // coordinates work.
        double x = -r * Math.cos(theta);
        double y = r * Math.sin(theta);
        g.drawLine(0,0,(int) x,(int) y);
      }

    // Draw the sun. Need to offset.
    int sunRadius = 8;
    g.setColor(Color.yellow);
    g.fillOval(-sunRadius,-sunRadius,2 * sunRadius,2 * sunRadius);
    
    // Trace out the ellipse. One way to do this is with g.fillOval().
    g.setColor(Color.gray);
    g.drawOval((int)(c-a),-(int)b,(int)(2*a),(int)(2*b));
    
    // Convert t to M, alpha and theta.
    double M = 2.0 * Math.PI * t / T;
    double alpha = KeplersEqn.solve(e,M);
    double theta = KeplersEqn.alphaToTheta(alpha,e);
    
    // Convert to rectangular coordinates.
    double r = l / (1.0 + e * Math.cos(theta));
    double x = -r * Math.cos(theta);
    double y = r * Math.sin(theta);
    
    int planetRadius = 8;
    g.setColor(Color.magenta);
    g.fillOval((int)(x-planetRadius),(int)(y-planetRadius),2*planetRadius,2*planetRadius);
    
    // Put coordinates back to normal.
    g.translate(-(int)((a - c) + pad),-this.getHeight() / 2);
    
    // Print the values.
    g.setColor(Color.red);
    
    String msg = String.format("t = %4.2f",t);
    g.drawString(msg,10,20);
    msg = String.format("M = %4.2f",M * 180.0 / Math.PI);
    g.drawString(msg,10,34);
    msg = String.format("alpha = %4.2f",alpha * 180.0 / Math.PI);
    g.drawString(msg,10,48);
    msg = String.format("theta = %4.2f",theta * 180.0 / Math.PI);
    g.drawString(msg,10,62);
  }
  
  public static void main(String[] args) {
    
    KeplerAnim thePane = new KeplerAnim();
    SimpleWindow theWindow = new SimpleWindow("Kepler Animation",thePane);
    thePane.theTimer.start();
  }
}