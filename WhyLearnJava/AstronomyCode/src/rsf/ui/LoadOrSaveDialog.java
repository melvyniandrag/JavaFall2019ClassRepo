package rsf.ui;

// Use this to bring up a dialog that asks the user where to save or load a file.
// See main() for an example.

import java.awt.FileDialog;
import java.awt.Frame;


public class LoadOrSaveDialog {
  
  public static String[] getSaveChoice(String message) {
 
    // Open up a file dialog. String[0] is a path to the directory in which the
    // file resides, and String[1] is the file name. Return null if canceled.
    FileDialog fdlog = new FileDialog(new Frame(),message,FileDialog.SAVE);
    fdlog.setVisible(true);
    String dir = fdlog.getDirectory();
    String fname = fdlog.getFile();
    if (fname == null)
      return null;
    fdlog.dispose();
     
    String[] answer = new String[2];
    answer[0] = dir;
    answer[1] = fname;
    return answer;
  }
 
  public static String[] getLoadChoice(String message) {
 
    // Similar to getSaveChoice(), but for loading files.
    FileDialog fdlog = new FileDialog(new Frame(),message,FileDialog.LOAD);
    fdlog.setVisible(true);
    String dir = fdlog.getDirectory();
    String fname = fdlog.getFile();
    if (fname == null)
      return null;
    fdlog.dispose();
 
    String[] answer = new String[2];
    answer[0] = dir;
    answer[1] = fname;
    return answer;
  }
   
  public static void main(String[] args) {
     
    // For testing, ask the user to choose a file and print out the choice.
    String[] choice = getLoadChoice("Choose a file to load");
    if (choice == null)
      System.out.println("User aborted");
    else
      {
        System.out.println("Directory: " +choice[0]);
        System.out.println("File name: " + choice[1]);
      }
  }
}