package rsf.graphics;

// Open and view a single JPEG image.

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import rsf.ui.LoadOrSaveDialog;
import rsf.ui.WindowQuitter;


public class JPEGViewer extends JPanel {
  
  Image image = null;
  
  public JPEGViewer() {
    
    // Let the user choose a file.
    String[] choice = LoadOrSaveDialog.getLoadChoice("Choose a JPEG File");
    if (choice == null)
      System.exit(0);
    String pathToFile = choice[0] + File.separator + choice[1];
    
    // Load the image into memory. This is a bit of magic that you can ignore.
    ImageIcon icon = new ImageIcon(pathToFile);
    image = icon.getImage();
    
    // Make the size of this JPanel equal to the size of the image.
    this.setPreferredSize(new Dimension(image.getWidth(null),image.getHeight(null)));
  }
  
  protected void paintComponent(Graphics g) {
    g.drawImage(image,0,0,null);
  }
  
  public static void main(String[] args) {
    
    JFrame theWindow = new JFrame("JPEG Viewer");
    theWindow.addWindowListener(new WindowQuitter());
    
    JPEGViewer theViewer = new JPEGViewer();
    theWindow.getContentPane().add(theViewer);
    theWindow.pack();
    theWindow.setVisible(true);
  }
}