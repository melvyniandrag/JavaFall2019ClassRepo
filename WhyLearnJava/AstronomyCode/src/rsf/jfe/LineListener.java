package rsf.jfe;

// This is to be used with CmdLineWindow. As lines of text are completed (by typing return)
// in a CmdLineWindow object, these completed lines are passed back as strings to a 
// LineListener. takeLine() returns true if theLine was successfully handed off.
// If it returns false, then the caller should hold theLine briefly, then try again.


public interface LineListener {
  public boolean takeLine(String theLine);
}
