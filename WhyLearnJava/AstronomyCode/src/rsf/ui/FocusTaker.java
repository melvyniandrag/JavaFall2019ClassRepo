package rsf.ui;

// Use this to listen for when the mouse enters a JComponent. Whenever this happens, make
// that JComponent take keyboard focus. Most of the time, the JComponent will be a JPanel
// inside a JFrame (a window).
// 
// As with WindowQuitter, this is primarily meant as an example. Calling
// Component.setFocusable(true) does the same thing.

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;


public class FocusTaker implements MouseListener {
  
  private JComponent theComponent = null;
  
  public FocusTaker(JComponent comp) {
    this.theComponent = comp;
  }
  public void mouseEntered(MouseEvent e) { 

    // Need so that this hears keyboard events.
    theComponent.requestFocus();
  }
  
  // Not needed.
  public void mouseReleased(MouseEvent e) { ;; }
  public void mousePressed(MouseEvent e) { ;; }
  public void mouseExited(MouseEvent e) { ;; }
  public void mouseClicked(MouseEvent e) { ;; }
}