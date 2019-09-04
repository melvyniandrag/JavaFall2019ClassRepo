package rsf.astron.time;

// Represents an ordinary calendar date and time of day.

import rsf.ui.LineReader;

public class CalDate  {
  
  public int    year;
  public int    month; // January = 1 and December = 12.
  public int    day;
  public double time;  // Hours since midnight.
  
  
  private static String[] monthName = {
    "January","Feburary","March","April","May","June","July","August",
    "September","October","November","December" };
  
  public String toString() {
    
    // Convert the year, month, day and time to a string.
    String answer = Integer.toString(year) + " " + 
                    monthName[month-1] + " " +
                    Integer.toString(day);
    
    int hour = (int) time;
    int min = (int) ((double) (time - hour) * 60.0);
    int sec = (int) (((double) time - (double) hour - (double) min / 60.0) 
                     * 3600.0 + 0.5);
    
    String hourstr = new String();
    if (hour < 10)
      hourstr = "0";
    hourstr += Integer.toString(hour);
    
    String minstr = new String();
    if (min < 10)
      minstr = "0";
    minstr += Integer.toString(min);
    
    String secstr = new String();
    if (sec < 10)
      secstr = "0";
    secstr += Integer.toString(sec);
    
    answer += " " + hourstr + ":" + minstr + ":" + secstr;
    
    return answer;
  }
  
  public static void main(String[] args) {
    
    CalDate testDate = new CalDate();
    testDate.year = LineReader.queryInt("Year: ");
    testDate.month = LineReader.queryInt("Month:");
    testDate.day = LineReader.queryInt("Day:");
    testDate.time = LineReader.queryDouble("Hour:");
    
    System.out.println("Time is " +testDate);
  }
}