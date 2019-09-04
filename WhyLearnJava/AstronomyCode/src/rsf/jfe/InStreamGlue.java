package rsf.jfe;

// This is used as a replacement for System.in. Feed the class a line of text by calling
// takeLine(), and the class will hold it in a buffer that can be read by calls to read().

import java.util.Vector;
import java.io.InputStream;


public class InStreamGlue extends InputStream implements LineListener {
  
  // This holds the data pushed in by calls to takeLine(), but not yet pulled off by calls to
  // read(). This is used like a queue. Using Vector for this is inefficient, but it's easy.
  private Vector buf = new Vector(100);
  
  
  public boolean takeLine(String theLine) {
    
    char[] c = theLine.toCharArray();
    synchronized (buf) {
      for (int i = 0; i < c.length; i++)
        buf.add(new Integer(c[i]));
    }
    
    // There's no way that this could fail (famous last words).
    return true;
  }
  
  public int read() {
    
    synchronized (buf) {
      
      // See if there's anything available.
      if (buf.size() <= 0)
        return -1;
      
      int answer = ((Integer) buf.firstElement()).intValue();
      buf.remove(0);
      return answer;
    }
  }
  
  public int read(byte[] b) {
    return 0;
  }
  
  public int read(byte[] b,int off,int len) {
    
    // More cleverness might be appropriate. E.g., handing back more than one byte at a time,
    // but this works. Note that this method is *supposed* to block until at least one byte
    // is available.
    int value = read();
    while (value < 0)
      // Keep waiting for data.
      value = read();
    
    b[0] = (byte) value;
    return 1;
  }
}