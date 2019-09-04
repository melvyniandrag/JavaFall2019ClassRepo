package rsf.jfe;

// Listen for changes made to a CmdLineWindow.

import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;


public class CmdLineDocFilter extends DocumentFilter {

  // theWindow.theText is the JTextArea object with which this DocumentFilter is associated.
  private CmdLineWindow theWindow = null;
  
  
  public CmdLineDocFilter(CmdLineWindow theWindow) {
    this.theWindow = theWindow;
  }
  
  public void insertString(DocumentFilter.FilterBypass fb,int offset,String string,
                           AttributeSet attr) throws BadLocationException {
    
    // This is called by JTextArea.append() (for example).
    super.insertString(fb,offset,string,attr);
    
    // Make sure that the caret is at the end. For extra safety, this should probably be done
    // with EventQueue.invokeLater() just like as is done in CmdLineWindow.takeChar().
    theWindow.theText.setCaretPosition(theWindow.theText.getText().length());
  }
  
  public void remove(DocumentFilter.FilterBypass fb,int offset,int length) 
                                                      throws BadLocationException {
    
    // Typically called for back-space.
    Document theDoc = fb.getDocument();
    int docLength = theDoc.getLength();
    
    if (docLength != offset+1)
      // User trying to move the caret back and edit. Not allowed.
      return;
    
    super.remove(fb,offset,length);
  }
  
  public void replace(DocumentFilter.FilterBypass fb,int offset,int length,String text,
                      AttributeSet attrs) throws BadLocationException {
    
    // This is called when the user does any typing.
    Document theDoc = fb.getDocument();
    int docLength = theDoc.getLength();
    
    if (docLength != offset)
      // User trying to move the caret back and insert. This is not allowed.
      // Only allow typing at the end of a line.
      return;
    
    super.replace(fb,offset,length,text,attrs);
    
    // If the user just typed return, then tell theWindow.theListener what was typed.
    if ((theWindow.theListener != null) && (text.equals("\n") == true))
      {
        int len = theWindow.theText.getText().length() - theWindow.getLastTake();
        String typed = theWindow.theText.getText(theWindow.getLastTake(),len);
        
        // Theoretically the call to takeLine() could return false (though it's very 
        // unlikely), in which case this thread should hold typed and try again. This kind of
        // loop so deep in the document-handling process is not really a good thing because 
        // it could prevent windows from updating while waiting for this to be resolved, but
        // it should be OK in this case.
        boolean tookIt = false;
        while (tookIt == false)
          {
            tookIt = theWindow.theListener.takeLine(typed);
            if (tookIt == false)
              {
                try {
                  Thread.sleep(5);
                } catch (Exception e) {
                  // Do nothing.
                }
              }
          }
        
        // Since theListener has heard about this text, move the lastTakePos.
        theWindow.updateLastTake(theWindow.theText.getText().length());
      }
  }
}