package rsf.graphics.sphere;

// Like EyeKeyListener, but for an InSphere.

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;


public class InsideKeyListener implements KeyListener {
  
  private InsideEyeAdjuster theEyeListener = null;
  private JPanel thePane = null;
  
  
  public InsideKeyListener(InsideEyeAdjuster theEyeListener,JPanel thePane) {
    this.theEyeListener = theEyeListener;
    this.thePane = thePane;
  }

  public void keyTyped(KeyEvent e) { 

    // J ==> move left, L ==> right, I ==> up, M ==> down,
    // U ==> rotate counter-clockwise and O ==> rotate clockwise.
    // A ==> zoom in and Z ==> zoom out.

    // This is the angle by which the eye moves with each step.
    double angle = Math.PI/120.0;
    
    // Get the character typed by the user.
    char c = e.getKeyChar();
    
    if ((c == 'l') || (c == 'L'))
      {
        // Move right by angle. 
        theEyeListener.rotEyeRight(angle);
        thePane.repaint();
      }
    if ((c == 'j') || (c == 'J'))
      {
        // Move left.
        theEyeListener.rotEyeRight(-angle);
        thePane.repaint();
      }
    if ((c == 'i') || (c == 'I'))
      {
        // Move up.
        theEyeListener.rotEyeUp(angle);
        thePane.repaint();
      }
    if ((c == 'm') || (c == 'M'))
      {
        // Move down.
        theEyeListener.rotEyeUp(-angle);
        thePane.repaint();
      }
    if ((c == 'o') || (c == 'O'))
      {
        // Twist clockwise.
        theEyeListener.twistEye(angle);
        thePane.repaint();
      }
    if ((c == 'u') || (c == 'U'))
      {
        // Twist counter-clockwise.
        theEyeListener.twistEye(-angle);
        thePane.repaint();
      }
    if ((c == 'a') || (c == 'A'))
      {
        // Zoom in.
        theEyeListener.zoomIn();
        thePane.repaint();
      }
    if ((c == 'z') || (c == 'Z'))
      {
        // Zoom out.
        theEyeListener.zoomOut();
        thePane.repaint();
      }
    if ((c == 'x') || (c == 'X'))
      System.exit(0);
  }
  
  // These methods are required by the KeyListener interface but are not needed.
  public void keyPressed(KeyEvent e) { ; }
  public void keyReleased(KeyEvent e) { ; } 
}