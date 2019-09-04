package rsf.jfe;

// This class can be used to run an arbitrary command in a seperate thread. Provide it with a
// string, and this string is passed to Runtime.exec(). Call CmdRunner.start() to execute the
// command in a new thread.


public class CmdRunner extends Thread {
  
  private String cmd = null;
  
  
  public CmdRunner(String cmd) {
    this.cmd = cmd;
  }
  
  public void run() {  
    
    try {
      // Start the requested process.
      Process p = Runtime.getRuntime().exec(cmd);
      
      // Sit here till the process is done.
      int exitValue = p.waitFor();
    } catch (Exception e) {
      System.err.println("Problem with " +cmd+ ": " +e);
      e.printStackTrace();
    }
  }
}