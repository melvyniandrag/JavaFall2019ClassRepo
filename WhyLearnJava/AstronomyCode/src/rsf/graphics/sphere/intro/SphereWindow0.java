package rsf.graphics.sphere.intro;

// Window onto a picture of a shaded sphere.  SimpleWindow is an improved version. 

import java.awt.Dimension;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;


public class SphereWindow0 extends JFrame implements WindowListener {

  public SphereWindow0() {
    
    super("Shaded Sphere");
 
    // Need to listen for window events to handle appropriate close action.
    this.addWindowListener(this);
    
    // Create the drawing area and set its size.
    SpherePane shadePane = new SpherePane();
    shadePane.setPreferredSize(
        new Dimension(shadePane.initialWidth,shadePane.initialHeight));
    shadePane.setMaximumSize(new Dimension(shadePane.initialWidth,
                                           shadePane.initialHeight));
  
    this.getContentPane().add(shadePane);
    this.pack();
    this.setVisible(true);
  } 

  public void windowClosing(WindowEvent e) {
    // A WindowListener event called when the user tries to close the window.
    System.exit(0);
  }
  
  // Don't care about these.
  public void windowActivated(WindowEvent e) { ;; }
  public void windowClosed(WindowEvent e) { ;; }
  public void windowDeactivated(WindowEvent e) { ;; }
  public void windowDeiconified(WindowEvent e) { ;; }
  public void windowIconified(WindowEvent e) { ;; }
  public void windowOpened(WindowEvent e) { ;; }
  
  public static void main(String[] args) {
    SphereWindow0 stWindow = new SphereWindow0();
  }
}