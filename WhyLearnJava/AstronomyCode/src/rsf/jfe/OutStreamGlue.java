package rsf.jfe;

// This class is needed as a go-between. Call new PrintStream() on an object of this type. 
// The resulting PrintStream can be used with System.setOut() or setErr(). When this is done,
// every print statement goes to the CmdLineWindow.

import java.io.OutputStream;


public class OutStreamGlue extends OutputStream {
  
  private CmdLineWindow theWindow = null;
  
  public OutStreamGlue(CmdLineWindow theWindow) {
    this.theWindow = theWindow;
  }
  
  public void write(int b) {
    theWindow.takeChar((char) b);
  }
  
  public void close() {
  }
  
  public void flush() {
  }
  
  public void write(byte[] b) {
  }
  
  public void write(byte[] b,int off,int len) {
    
    int j = off;
    for (int i = 0; i < len; i++)
      theWindow.takeChar((char) b[j++]);
  }
}