package rsf.graphics.sphere;

// Used to listen for typing on the keyboard and to move the eye in response.

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;


public class EyeKeyListener implements KeyListener {
  
  private EyeAdjuster theEyeListener = null;
  private JPanel thePane = null;
  
  
  public EyeKeyListener(EyeAdjuster theEyeListener,JPanel thePane) {
    this.theEyeListener = theEyeListener;
    this.thePane = thePane;
  }

  public void keyTyped(KeyEvent e) { 

    // J ==> move left, L ==> right, I ==> up, M ==> down, X ==> quit,
    // U ==> rotate counter-clockwise and O ==> rotate clockwise.

    // This is the angle by which the eye moves with each step.
    double angle = Math.PI/120.0;
    
    // Get the character typed by the user.
    char c = e.getKeyChar();
    
    if ((c == 'l') || (c == 'L'))
      {
        // Move right by angle. 
        theEyeListener.rotEyeRight(angle); 
        
        // Request a re-draw.
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
    if ((c == 'x') || (c == 'X'))
      System.exit(0);
  }
  
  // These methods are required by the KeyListener interface but are not needed.
  public void keyPressed(KeyEvent e) { ; }
  public void keyReleased(KeyEvent e) { ; } 
}