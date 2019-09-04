package rsf.graphics.graphing;

// Use this to show an animation of a graph over time. To use this, extend the class with
// a definition of f(), so that the class is no longer abstract.

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;


public abstract class GraphAnimWindow extends JFrame implements ActionListener {
  
  private GraphPanel thePanel = null;
  
  // The highest time value to display.
  private double maxt = 0.0;
  
  // The t value of f() currently being shown, and the t-step from one frame to the next.
  double t = 0.0;
  private double tstep = 0.0;
  
  // The data being displayed.
  private int xsamples = 1000;
  private double[] x = new double[xsamples];
  private double[] y = new double[xsamples];
  
  // The user must call theTimer.start() to start the animation.
  public Timer theTimer = null;
  private int delay = 10;
  
  // The function whose graph is being shown.
  abstract public double f(double x,double t);
  
  
  public GraphAnimWindow(double minx,double maxx,double mint,double maxt,
                         double miny,double maxy,double tstep) {
    
    // Prepare to show f(x,t) for minx < x < maxx and mint < t < maxt.
    // Only the range miny < f(x,t) < maxy will be shown.
    this.maxt = maxt;
    this.t = mint;
    this.tstep = tstep;
    
    thePanel = new GraphPanel(minx,maxx,miny,maxy);
    theTimer = new Timer(delay,this);
    
    this.setTitle("Graph Animation");
    
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.getContentPane().add(thePanel);
    thePanel.setPreferredSize(new Dimension(800,600));
    this.pack();
    
    // Sample the x values from minx to maxx, xsamples times. 
    for (int i = 0; i < xsamples; i++)
      x[i] = minx + i * (maxx - minx) / (double) (xsamples - 1);
    
    // Sample the function.
    for (int i = 0; i < xsamples; i++)
      y[i] = f(x[i],mint);
    
    thePanel.setXY(x,y);
    this.setVisible(true);
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // When this is called, generate a new frame.
    if (t > maxt)
      return;
    
    // Generate new data.
    for (int i = 0; i < xsamples; i++)
      y[i] = f(x[i],t);
    
    // Update the data being shown, and the time displayed.
    thePanel.setXY(x,y);
    thePanel.setMsg("t = " +t);
    
    repaint();
    
    // Step time forward to the next frame. 
    t += tstep;
  }
}