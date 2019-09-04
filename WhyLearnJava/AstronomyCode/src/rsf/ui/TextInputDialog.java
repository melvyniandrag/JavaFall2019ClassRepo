package rsf.ui;

// Creates a dialog with short text boxes for user input and labels indicating what to input.
//
// To use this, instantiate the class with a title for the dialog box, an array of labels 
// for the user inputs, and the minimum size that the user input boxes should have. Call 
// getInputs() to find out what the user did with the dialog. If getInputs() returns null,
// then the user canceled. If the user chose "OK", then getInputs() returns the text typed 
// by the user for each field.

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TextInputDialog extends JDialog implements ActionListener {
  
  private String[] results = null;
  private JTextField[] inputFields = null;
  
  public TextInputDialog(String title,String[] labels,int minSize) {
   
    // This is a modal dialog.
    super(new JFrame(),title,true);  
   
    // North is text area; South is Okay/Cancel.
    this.getContentPane().setLayout(new BorderLayout(10,10));
    JPanel buttonPanel = new JPanel();
 
    JButton but = new JButton("OK");
    but.addActionListener(this);
    buttonPanel.add(but);
    
    but = new JButton("Cancel");
    but.addActionListener(this);
    buttonPanel.add(but);
 
    this.getContentPane().add(buttonPanel,"South");
 
    // Create the labels and text input areas.
    JPanel inputPanel = new JPanel();
    inputFields = new JTextField[labels.length];
    inputPanel.setLayout(new GridLayout(labels.length,2,8,10));
    for (int i = 0; i < labels.length; i++)
      {
        JLabel theLabel = new JLabel(labels[i]);
        inputPanel.add(theLabel);
        
        // Make sure that the input fields have room for at least minSize characters.
        inputFields[i] = new JTextField(minSize);
        inputPanel.add(inputFields[i]);
      }
    this.getContentPane().add(inputPanel,"North");

    this.pack();
  }

  public void actionPerformed(ActionEvent e) {
   
    if (e.getActionCommand().equals("OK"))
      {
        // Pull the text out of the JTextField items.
        results = new String[inputFields.length];
        for (int i = 0; i < inputFields.length; i++)
          results[i] = inputFields[i].getText();
      }  
    else if (e.getActionCommand().equals("Cancel"))
      results = null;
    
    this.dispose();
  }

  public String[] getInputs() {
    this.setVisible(true);
    return results;
  }

  public static void main(String[] args) {
   
    // An example of how to use this class.
    String[] labels = new String[2];
    labels[0] = "First Label";
    labels[1] = "Second Label";
   
    TextInputDialog inputDialog = new TextInputDialog("Input Text",labels,10);
    String[] inputs = inputDialog.getInputs();
   
    if (inputs == null)
      System.out.println("The user canceled");
    else
      {
        for (int i = 0; i < labels.length; i++)
          System.out.println("For \"" +labels[i]+ "\" the user input \"" +inputs[i]+ "\"");
      }
    
    System.exit(0);
  }
}