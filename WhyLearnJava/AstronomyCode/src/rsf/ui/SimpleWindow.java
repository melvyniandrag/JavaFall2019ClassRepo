package rsf.ui;

// Window onto the contents of anything that extends MyPanel.

import javax.swing.JFrame;
import java.awt.Dimension;

public class SimpleWindow extends JFrame {
  
  public SimpleWindow(String title,MyPanel thePane) {
    
    super(title);
    
    // Need to listen for window events to handle appropriate close action. 
    this.addWindowListener(new WindowQuitter());
    
    thePane.setPreferredSize(new Dimension(thePane.initialWidth,thePane.initialHeight));
    thePane.setMaximumSize(new Dimension(thePane.initialWidth,thePane.initialHeight));
    
    this.getContentPane().add(thePane);
    this.pack();
    this.setVisible(true);
  }
}