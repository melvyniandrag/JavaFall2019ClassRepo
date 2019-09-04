package rsf.astron.time;

// Various methods, all static, for converting from one representation of time to another.

public class TimeUtil0 {
  
  public static double yyyymmddToJD(int date) {

    // Convert an int in the form YYYYMMDD to a Julian date. The result will be
    // a whole number, plus 0.5. This method is here as a convenience.
    int year = date/10000;
    int month = (date - year * 10000) / 100;
    int day = (date - year * 10000 - month * 100);
    return calendarToJD(year,month,day);
  }
  
  public static double calendarToJD(int year,int month,double day) {
    
    // Convert to a Julian date. The day could be fractional, in which case it should be 
    // the fraction of a day that has passed since noon in Greenwich. This is taken from 
    // Meeus, Astronomical Algorithms, pp. 60-1. This gives the calendar date using the 
    // calendar that was in use at the time, where it is assumed that any date on or 
    // before October 4, 1582 is in the Julian calendar, and those later are in the 
    // Gregorian calendar. For the most part, this isn't important, but it makes this
    // method consistent with jdToCalendar() below.
    
    // Test: Gregorian or Julian?
    boolean julian = false;
    if ((year < 1582) || ((year == 1582) && (month < 10)) ||
        ((year == 1582) && (month == 10) && (day <= 4)))
      julian = true;
    
    if (month <= 2)
      {
        year -= 1;
        month += 12;
      }
    
    int a = 0;
    int b = 0;
    if (julian == false)
      {
        a = year / 100;
        b = 2  - a + (a/4);
      }
    
    return (int)(365.25 * (double) (year + 4716)) 
                 + (int) (30.600001 * (double)(month + 1)) + day + b - 1524.5;
  }
  
  public static double hhmmssToHours(double hhmmss) {
    
    // Convert a double of the form hhmmss.f, where f is any fraction of a second,
    // to hours and fractions of an hour.
    int h = (int)(hhmmss / 3600.0);
    int m = (int) ((hhmmss - h * 3600.0) / 60.0);
    double s = hhmmss - h * 3600 - m * 60;
    return (double) h + (double) m / 60.0 + s / 3600.0; 
  }
  
  public static double timeToHARad(double h) {
    
    // Convert h, given in decimal hours, to an angle in radians. For example, h = 1.75
    // corresponds to 1:45AM, which is 1.75/24 of a day, or 2 pi * 1.75 /24 radians.
    return Math.PI * h / 12.0;
  }
  
  public static double gstToLST(double gst,double geoLong) {
    
    // Convert Greenwich sidereal time to local sidereal time based on the
    // geographic longitude of the observer. Everything is in radians.
    double answer = gst - geoLong;
    if (answer < 0.0)
      answer += 2.0 * Math.PI;
    return answer;
  }
  
  public static CalDate jdToCalendar(double jd) {

    // Converts a Julian day to an ordinary calendar date. Not valid for jd < 0.
    // The calendar used (Julian or Gregorian) is consistent with what was used
    // at the time. See calendarToJD(). Taken from Meeus, Astronomical Algorithms, p. 63.
    double z = (int) (jd + 0.5);
    double f = (jd + 0.5) - z;
    double a = 0.0;
    double alpha = 0.0;
    if (z < 2299161.0)
      a = z;
    else
     {
        alpha = (int) ((z - 1867216.25) / 36524.25);
        a = z + 1.0 + alpha - (int) (alpha / 4.0);
      }

    double b = a + 1524.0;
    double c = (int) ((b - 122.1) / 365.25);
    double d = (int) (365.25 * c);
    double e = (int) ((b - d) / 30.6001);
    
    // This includes any fractional part of a day.
    double partOfDay = b - d - (int) (30.6001 * e) + f;
    
    CalDate answer = new CalDate();
    answer.day = (int) partOfDay;
    answer.time = 24.0 * (partOfDay - (double) answer.day);

    if (e  < 14)
      answer.month = (int) (e - 1.0);
    else
      answer.month = (int) (e - 13.0);
    if (answer.month > 2) 
      answer.year = (int) (c - 4716.0);
    else
      answer.year = (int) (c - 4715.0);

    return answer;
  }
}