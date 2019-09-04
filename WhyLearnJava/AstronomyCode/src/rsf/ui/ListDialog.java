package rsf.ui;

// Shows a window with a list of items. The user can choose any one of them and choose one of
// a series of buttons related to that item. This is used only by the rsf.jfe package.
// 
// When this class is created, it must be given a class that implements ActionListener. When
// any of the button are clicked, ListDialog with then call ActionListener.actionPerformed()
// with argument "x:y", where x is the number of the button that was chosen, and y is the 
// number of the item chosen from the list. These numbers is given in string form.
//
// Note: The item chosen (the y value) could be -1 if no item is selected.

import java.awt.Panel;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.List;
import java.awt.Label;
import java.awt.FlowLayout;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;


public class ListDialog extends JFrame {
  
  public ListDialog(String title,String instructions,String[] butNames, String[] items,
                    ActionListener callback) {

    super(title);
    
    this.setFont(new Font("Sans Serif",Font.PLAIN,12));

    // North is instructions, Center is items, South are the buttons.
    this.setLayout(new BorderLayout(10,10));
    
    if (instructions != null)
      this.add(new Label(instructions),"North");
    
    // Define the list from which to choose.
    // Show 15 items at a time and do not allow multiple selections.
    List theList = new List(15,false);
    for (int i = 0; i < items.length; i++)
      theList.add(items[i]);  
    this.add(theList,"Center");
    
    // Panel to hold okay and cancel buttons.
    Panel buttonPanel = new Panel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,50,10));
    this.add(buttonPanel,"South");
    for (int i = 0; i < butNames.length; i++)
      {
        Button theButton = new Button(butNames[i]);
        ButtonHandler theHandler = new ButtonHandler(theList,i,callback);
        theButton.addActionListener(theHandler);
        buttonPanel.add(theButton);
      }
    
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack();
    this.setVisible(true);
  }

  public static void main(String[] args) {
    
    // This is the list of things shown.
    String[] list = new String[10];
    for (int i = 0; i < list.length; i++)
      list[i] = "choice number " + i;

    // These are things that can be "done" to the items
    // on the list (button names).
    String[] options = new String[3];
    for (int i = 0; i < options.length; i++)
      options[i] = "option " +i;
    
    // Create the dialog.
    ListDialog theDialog = new ListDialog("List Test","Pick Something",options,list,
                                          new TestEventHandler(options,list));
  }
}

// This helper-class is called whenever a button is clicked. It notes which button was 
// clicked and which item in the list is selected, then passes this info up to the class that
// cares (the callback argument to the ListDialog constructor). It would be more elegant to 
// define a new kind of event class for this purpose, and not use ActionEvent/ActionListener,
// but that's already available.

class ButtonHandler implements ActionListener {

  private List theList;
  private int buttonIndex;
  private ActionListener theHandler;
  
  public ButtonHandler(List theList,int buttonIndex,ActionListener theHandler) {
    this.theList = theList;
    this.buttonIndex = buttonIndex;
    this.theHandler = theHandler;
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // This is called whenever a button in ListDialog is clicked. Tell theHandler,
    // which is the class that cares.
    
    // Find out what item is selected.
    int itemSelected = theList.getSelectedIndex();
    String msg = buttonIndex+ ":" +itemSelected;
    
    // The this and 0 arguments are required by the the existing
    // ActionEvent class. That's why I shouldn't really use ActionEvent. These arguments
    // have no meaning in this situation.
    theHandler.actionPerformed(new ActionEvent(this,0,msg)); 
  }
}

// This helper-class is only used for testing. It's used (indirectly) when  main() is 
// invoked. In practice, the class that creates a ListDialog should probably implement 
// ActionListener.

class TestEventHandler implements ActionListener {
  
  private String[] buttons;
  private String[] listItems;
  
  public TestEventHandler(String[] buttons,String[] listItems) {
    this.buttons = buttons;
    this.listItems = listItems;
  }
  
  public void actionPerformed(ActionEvent e) {
    
    // Purely for testing. 
    String command = e.getActionCommand();
    System.out.println("The event is " +command+ " which corresponds to ");
    String[] parts = command.split(":");
    int butIndex = Integer.parseInt(parts[0]);
    int listIndex = Integer.parseInt(parts[1]);
    if (listIndex < 0)
      System.out.println("\t button only: " +buttons[butIndex]);
    else
      System.out.println("\t\"" +buttons[butIndex] + "\" on \"" +listItems[listIndex]+ "\"");
  }
}