package rsf.graphics.sphere.intro;

// Used to listen for typing on the keyboard. The eye is moved around a fixed sphere in 
// response. Exactly what's going on with u1 and u2 is described in MobileSphere.

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import rsf.graphics.GUtil;


public class EyeKeyListener implements KeyListener {
  
  private MobilePane thePane = null;
  
  
  public EyeKeyListener(MobilePane thePane) {
    this.thePane = thePane;
  }

  public void keyTyped(KeyEvent e) { 

    // J ==> move left, L ==> right, I ==> up, M ==> down, and X ==> quit.

    // This is the angle by which the eye moves with each step.
    double angle = Math.PI/120.0;
    
    // Get the character typed by the user.
    char c = e.getKeyChar();
    
    if ((c == 'l') || (c == 'L'))
      {
        // Move right by angle. That is, rotate u1 about u2.
        thePane.u1 = GUtil.rotate3(-angle,thePane.u2,thePane.u1);
        
        // Request a re-draw.
        thePane.repaint();
      }
    if ((c == 'j') || (c == 'J'))
      {
        // Move left. 
        thePane.u1 = GUtil.rotate3(angle,thePane.u2,thePane.u1);
        thePane.repaint();
      }
    if ((c == 'i') || (c == 'I'))
      {
        // Move up. Now rotating u2 about u1.
        thePane.u2 = GUtil.rotate3(-angle,thePane.u1,thePane.u2);
        thePane.repaint();
      }
    if ((c == 'm') || (c == 'M'))
      {
        // Move down.
        thePane.u2 = GUtil.rotate3(angle,thePane.u1,thePane.u2);
        thePane.repaint();
      }
    if ((c == 'x') || (c == 'X'))
      System.exit(0);
  }
  
  // These methods are required by the KeyListener interface but are not needed.
  public void keyPressed(KeyEvent e) { ; }
  public void keyReleased(KeyEvent e) { ; } 
}