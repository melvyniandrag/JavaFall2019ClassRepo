package rsf.ui;

// Methods in this class are for reading values from standard input. Say, for example,
// double value = LineReader.queryDouble("Give me a double: "):
// and the user will be asked for a double, which will be parsed and returned.

import java.io.InputStreamReader;
import java.io.BufferedReader;


public class LineReader {

  private static String readln() {

    // Use this to read a single line of input from the keyboard. This is private; the 
    // queryInput() method is for external classes.
    String answer = null;
    try {
      InputStreamReader in = new InputStreamReader(System.in);
      BufferedReader bin = new BufferedReader(in);
      answer = bin.readLine();
    } catch (Exception e) {
      // Theoretically, this shouldn't ever happen, but the compiler requires a check.
      System.out.println("Problem reading");
    }

    return answer;
  }

  public static String queryInput(String question) {

    // Use this to give text that prompts for input.
    System.out.print(question);
    return readln();
  }

  public static int queryInt(String question) {

    // Use to query for a value that is to be an integer.
    int answer = 0;
    String input = queryInput(question);
    boolean done = false;
    while (done == false)
      { 
        try {
          answer = Integer.parseInt(input);
          done = true;
        } catch (Exception e) {
          input = queryInput("Not an integer. Try again: ");
        }
      }
    return answer; 
  }

  public static double queryDouble(String question) {

    // Use to query for a value that is to be a double.
    String input = queryInput(question);
    return Double.parseDouble(input);
  }

  public static boolean queryBoolean(String question) {

    // Use to query for a boolean value, like for a y/n question. 
    // True = Y or y, False = anything else.
    String input = queryInput(question);
    if (input.charAt(0) == 'y' || input.charAt(0) == 'Y')
      return true;
    else
      return false;
  }
  
  public static void main(String[] args) {
    
    // Some examples.
    double i = queryInt("Give me an int: ");
    System.out.println("User said: " +i);
    
    double d = queryDouble("Give me a double: ");
    System.out.println("User said: " +d);
    
    boolean b = queryBoolean("Yes or No?");
    System.out.println("User said: " +b);
  }
}