package rsf.jfe;

// This program looks inside the JAR file to which it belongs, and shows a list of the names
// of all the classes in that JAR file that include a main(). The user can then choose any of
// these classes and execute their main().
//
// It can also be used as the entry point to any main()-class in any jar file.
// The typical way that a jar file is used is for there to be a single class with a main()
// so that the command
// java -jar jarfile.jar
// executes that single main(), which is specified in the manifest. This class allows 
// something slightly more sophisticated. The command "jar -jar jarfile.jar"
// with no arguments executes the main() below, as usual. If there are any arguments,
// then that argument is assumed to be the name of a class that has a main() and
// should be executed. So
// java -jar jarfile.jar rsf.graphics.JPEGViewer
// will run the main() in the given class. This can only be used to call main()'s that
// ignore their arguments.
// 
// EXERCISE: modify the program so that
// java -jar jarfile.jar package.name.theClass param1 param2 etc
// executes package.name.theClass, passing it param1, param2 and etc as parameters, 

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.JFrame;

import rsf.ui.ListDialog;


public class Main implements ActionListener {
  
  // The name of the JAR file in which this class resides.
  private String jarFileName;
  
  // The names of all the classes in the JAR file that include a main().
  private String[] classNames;
  
  
  public void actionPerformed(ActionEvent e) {
    
    // See what the user did.  The ActionEvent, e, is is a message passed back from an 
    // rsf.ui.ListDialog object created by showList().
    String command = e.getActionCommand();
    String[] parts = command.split(":");
    int butIndex = Integer.parseInt(parts[0]);
    int listIndex = Integer.parseInt(parts[1]);
    
    // Button 1 means quit.
    if (butIndex == 1)
      System.exit(0);
    
    if (listIndex < 0)
      // Nothing is selected.
      return;
    
    // If we got here, then button 0 was pressed, which means run a program. The program is
    // run as an entirely separate Java process. It would also be possible to run it with 
    // the same JRE as is running this code (by calling this.execute()). One reason not to do
    // that is that if the invoked main() calls System.exit(0) or bombs, then the whole thing
    // quits. The lines below create a new java process that calls Main.main() again, but
    // with a class name as argument, so that Main.execute() is called for that class.
    String cmd = "java -jar \"" + jarFileName + "\" \"" +classNames[listIndex]+ "\"";
    CmdRunner theCmd = new CmdRunner(cmd);
    theCmd.start();
  }
 
  private void digestJar(URL jarURL) {
    
    // Look in the JAR file pointed to by jarURL, and set this.jarFileName to point to the
    // file, and this.classNames to be a list of all the classes in the JAR file that
    // include a main().
    
    String[] tempList = new String[500];
    int listSize = 0;
        
    try {
      File f = new File(jarURL.toURI());
      this.jarFileName = f.toString();
      
      // Create a URLClassLoader so that we can load classes from this jar file.
      URL[] urls = new URL[1];
      urls[0] = f.toURL();
      URLClassLoader cl = new URLClassLoader(urls);
      
      // And a JarFile object to go with the class loader so that we can see what's in there.
      JarFile jf = new JarFile(f);
      
      // Work through all the items in the jar file.
      Enumeration e = jf.entries();
      while (e.hasMoreElements())
        {
          JarEntry je = (JarEntry) e.nextElement();
          String name = je.getName();
          
          // The name should end with ".class" and we don't want that.
          int last = name.lastIndexOf(".");
          name = name.substring(0,last);
          
          // Need to convert every "/" to ".". The entries of the jar file are in "/" format, 
          // like a file (which they are), but class names uses "." to delimit the package 
          // hierarchy.
          name = name.replace('/','.');
          
          // See if it really is a class.
          Class c = null;
          try {
           c = Class.forName(name,false,cl);
          } catch (Exception e2) {
            // Probably not a class file, which is OK; just skip the item.
            continue;
          }
          
          // Don't include this class in the list.
          if (name.equals(Main.class.getName()))
            continue;
          
          // See if there is a main().
          Method[] m = c.getMethods();
          boolean hasMain = false;
          for (int i = 0; i < m.length; i++)
            {
              String methodName = m[i].getName();
              if (methodName.equals("main") == true)
                  hasMain = true;
            }
          if (hasMain == true)
            { 
              tempList[listSize] = name;
              ++listSize;
            }
        }
    } catch (Exception e) {
      e.printStackTrace();
      System.err.print("Error: " +e);
      System.exit(0);
    }
    
    // Copy only the results we filled in.
    this.classNames = new String[listSize];
    for (int i = 0; i < listSize; i++)
      classNames[i] = tempList[i];
  }
  
  private static void showList() {
   
    // Figure out the name of the JAR file that was used to launch this.
    Main theMain = new Main();
    URL url = theMain.getClass().getProtectionDomain().getCodeSource().getLocation();
    
    // Figure out which classes in the JAR file have a main().
    theMain.digestJar(url);
    
    // Bring up a window showing a list of these items, with two buttons: 
    // quit (meaning quit out of this main()), and "run".
    String[] actions = {"Run" , "Quit"};
    
    ListDialog theDialog = new ListDialog("Executable Chooser",
        "Choose an item, then action button",actions,theMain.classNames,theMain);    
  }
  
  private static void execute(String className) {
    
    // Call the main() of the given class.
    
    // Open up a CmdLineWindow and redirect System.out/err/in to point to that window.
    CmdLineWindow cmdWindow = new CmdLineWindow(className,null);
    PrintStream newOut = new PrintStream(new OutStreamGlue(cmdWindow));
    System.setOut(newOut);
    System.setErr(newOut);
    InStreamGlue newIn = new InStreamGlue();
    System.setIn(newIn);
    cmdWindow.setLineListener(newIn);
    
    // Find the class's main() and invoke it.
    try {
      Class theClass = Class.forName(className);

      // It is assumed that this class's main() does not take any arguments. Actually, it 
      // must take the argument String[], but the code here assumes that the argument is 
      // ignored.
      // Find the main() with the signature (String[]).
      Class[] parameters = new Class[1];
      parameters[0] = (new String[] {}).getClass();
      Method main = theClass.getMethod("main",parameters);
      
      // Now invoke it. Wrap it in try/catch. Otherwise, when certain un-caught exceptions 
      // occur, like null pointer, the invocation of main() never returns, and there's
      // no way to quit.
      try {
        main.invoke(null,(Object) new String[]{});
      } catch (Exception e) {
        System.err.println("High level error: " +e);
        e.printStackTrace();
      }
      
      // Got here, so the underlying main() is essentially done. "Essentially" in the
      // sense that if it's a command-line program, then it must already have received
      // the input it wants. Disconnect System.in from the CmdLineWindow, and
      // allow the user to close if they want.
      cmdWindow.setLineListener(null);
      cmdWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    } catch (Exception e) {
      System.err.println("Error: " +e);
      e.printStackTrace();
    }
  }
  
  public static void main(String[] args) {
    
    if ((args == null) || (args.length == 0))
      // Show a list of those classes in this jar file that include a main().
      showList();
    else
      // Execute the given class.
      execute(args[0]);
  }
}