package rsf.ui;

// A class for listening to WindowListener events, and ignoring all of them except the 
// window close event, which causes the entire program to terminate.
//
// This is handy because it occurs so often. Rather than defining all of these methods in
// each JFrame, just say
// this.addWindowListener(new WindowQuitter());
// However, this class is primarily meant as an example. Calling
// JFrame.setDefaultCloseOperation(EXIT_ON_CLOSE) instead of addWindowListener()
// does the same thing.

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;


public class WindowQuitter implements WindowListener {
  
  public void windowClosing(WindowEvent e) {
    // A WindowListener event called when the user tries to close the window.
    System.exit(0);
  }

  // Ignore these.
  public void windowActivated(WindowEvent e) { ;; }
  public void windowClosed(WindowEvent e) { ;; }
  public void windowDeactivated(WindowEvent e) { ;; }
  public void windowDeiconified(WindowEvent e) { ;; }
  public void windowIconified(WindowEvent e) { ;; }
  public void windowOpened(WindowEvent e) { ;; }
}