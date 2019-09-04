package rsf.jfe;

// This can be used to create a window that lets the user type text, but not use the mouse.
// It should act much like a window onto the command-line, so the user can backspace within
// a line, but not backspace to edit a previous line.
//
// This does not act exactly like the command-line because it doesn't have three truly 
// distinct streams for in, out and err. It approximates this by assuming that anything that
// comes in via takeChar() gets printed to the window. This effectively collapses out and err
// into one stream. Stdin is approximated by saying that whenever the user types return, then
// anything that appears in theText after the character most recently added by takeChar() is
// passed to a LineListener. Normally, these characters would be anything the user typed
// since the last call to takeChar(). If there are multiple threads calling takeChar(), and 
// the user is typing at the same time, then, when the user hits return, the LineListener 
// might not receive everything the user typed since the last time he hit return, which is 
// the ideal solution. To do that would require an extra buffer layer to hold everything the
// user types separately. The CmdLineDocFilter is where the user's text (i.e., when they hit
// return) is noted and passed along to the listener. 

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.text.Document;
import javax.swing.text.AbstractDocument;
import java.awt.EventQueue;


public class CmdLineWindow extends JFrame {
  
  JTextArea theText = null;
  LineListener theListener = null;
  
  // The position of the last character that was displayed by a call
  // to takeChar() as opposed to having been typed by the user.
  private int lastTakePos = 0;
  
  
  synchronized public void takeChar(char c) {

    // Append the given char to theText.
    // If Swing weren't so thread-unfriendly, these two lines would do the job:
    // theText.append(Character.toString(c));
    // lastTakePos = theText.getText().length();
    
    // As it is, because the thread where this method is called is likely to be the Swing 
    // event-handler, those two lines could cause a problem. The rule is that the Swing 
    // event-handler is not allowed to do anything that modifies Swing objects. Therefore 
    // this call to append() must be handled as in:
    EventQueue.invokeLater(new AppendRunnable(this,c));
    
    // To make absolutely sure that lastTakePos is equal to the correct position for the end
    // of the text, the updateLastTake() method, which is synchronized, is used to modify 
    // lastTakePos.
  }
  
  synchronized public void updateLastTake(int newValue) {
    this.lastTakePos = newValue;
  }
  
  synchronized public int getLastTake() {
    return lastTakePos;
  }
  
  public CmdLineWindow(String title,String initialText) {
    
    super(title);
    
    theText = new JTextArea(initialText,24,80);
    theText.setFont(new Font("MonoSpaced",Font.PLAIN,12));
    theText.setEditable(true);
    this.add("Center",new JScrollPane(theText));
    
    // This window is not close-able (by default).
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    
    Document theDoc = theText.getDocument();
    AbstractDocument abDoc = (AbstractDocument) theDoc;
    abDoc.setDocumentFilter(new CmdLineDocFilter(this));
    
    this.pack();
    this.setVisible(true);
  }
  
  public void setLineListener(LineListener theListener) {
    this.theListener = theListener;
  }
}


// See CmdLineWindow.takeChar() for what this class does and why it exists.

class AppendRunnable implements Runnable {
  
  private char c;
  private CmdLineWindow theWindow;
  
  public AppendRunnable(CmdLineWindow theWindow,char c) {
    this.c = c;
    this.theWindow = theWindow;
  }
  
  public void run() {
    theWindow.theText.append(Character.toString(c));
    theWindow.updateLastTake(theWindow.theText.getText().length());
  }
}