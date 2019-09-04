package rsf.ui;

// Use this to bring up a list of choices among which the user may choose using radio 
// buttons.
// 
// To use this, instantiate the class with an array of Strings giving the choices. Then 
// call getChoice() to find out what the user chose. The integer returned corresponds to 
// the index of the choice in the array passed to the constructor, with -1 indicating
// cancel. See the main() method for an example.

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.BoxLayout;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ChoiceDialogRadio extends JDialog implements ActionListener {
  
  private int userChoice = -1;
    
  public ChoiceDialogRadio(String[] choices) {
      
    // This is a modal dialog.
    super(new JFrame(),"Make a Choice",true);
      
    // North is radio button choices; South is Okay/Cancel.
    this.getContentPane().setLayout(new BorderLayout(10,10));
    JPanel buttonPanel = new JPanel();
    
    JButton but = new JButton("OK");
    but.addActionListener(this);
    buttonPanel.add(but);
    
    but = new JButton("Cancel");
    but.addActionListener(this);
    buttonPanel.add(but);
    
    this.getContentPane().add(buttonPanel,"South");
    
    // Create the selections.
    JPanel choicePanel = new JPanel();
    choicePanel.setLayout(new BoxLayout(choicePanel,BoxLayout.Y_AXIS));
    ButtonGroup radioGroup = new ButtonGroup();
    for (int i = 0; i < choices.length; i++)
      {
        JRadioButton radioBut = null;
        if (i == 0)
          radioBut = new JRadioButton(choices[i],true);
        else
          radioBut = new JRadioButton(choices[i],false);
        choicePanel.add(radioBut);
        radioGroup.add(radioBut);
        
        radioBut.addActionListener(this);
        radioBut.setActionCommand(Integer.toString(i));
      }
    this.getContentPane().add(choicePanel,"North");
    userChoice = 0;
  
    this.pack();
  }
  
  public void actionPerformed(ActionEvent e) {
      
    if (e.getActionCommand().equals("OK"))
      this.dispose();
    else if (e.getActionCommand().equals("Cancel"))
      {
        userChoice = -1;
        this.dispose();
      }
      else
        // The action command string should be an integer (in string form)
        // indicating which button was clicked on.
        userChoice = Integer.parseInt(e.getActionCommand());
  }

  public int getChoice() {
    this.setVisible(true);
    return userChoice;
  }
  
  public static void main(String[] args) {
    
    // An example of how to use this class.
    String[] choiceText = new String[2];
    choiceText[0] = "Choose This";
    choiceText[1] = "Choose That";
    
    ChoiceDialogRadio choiceDialog = new ChoiceDialogRadio(choiceText);
    int choice = choiceDialog.getChoice();
    
    if (choice != -1)
      System.out.println("The user " +choiceText[choice]);
    else
      System.out.println("The user canceled");
  }
}