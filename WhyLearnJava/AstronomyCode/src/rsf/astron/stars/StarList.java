package rsf.astron.stars;

// This loads and holds in memory a list of stars. The data for each star consists of the 
// visual magnitude, position and proper motion.

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;


public class StarList {
  
  // This is the data for the stars. The i-th star has right ascension and declination equal
  // to ra[i] and dec[i], and visual magnitude equal to mag[i]. The change in right 
  // ascension and declination is pmRA[i] and pmDec[i] per year. These values are in radians.
  public double[] ra = null;
  public double[] dec = null;
  public double[] pmRA = null;
  public double[] pmDec = null;
  public double[] mag = null;
  
  public StarList move(double t) {
    
    // Create a copy of the star data, and adjust the ra and dec to take the proper motion 
    // into account, bringing the stars to the positions that they have at year t. 
    // So t = 2000.0 will leave the data unchanged.
    StarList answer = new StarList();
    answer.ra = new double[this.ra.length];
    answer.dec = new double[this.dec.length];
    answer.pmRA = this.pmRA;
    answer.pmDec = this.pmDec;
    answer.mag = this.mag;
    
    double dt = t - 2000.0;
    for (int i = 0; i < ra.length; i++)
      {
        answer.ra[i] = this.ra[i] + dt * pmRA[i];
        answer.dec[i] = this.dec[i] + dt * pmDec[i];
      }
    return answer;
  }
  
  public void load(String dir,String fileName) {
    
    // Load the data from a CSV (comma-separated value) file. The order is right ascension, 
    // declination (as of J2000.0, in degrees), proper motion in right ascension (stated as
    // milli-arc-seconds per year, change in RA * cos(Dec)), proper motion in declination, 
    // and visual magnitude.
    
    // Open the file.
    BufferedReader bin = null;
    FileReader freader = null;
    try {
      File f = new File(dir,fileName);
      freader = new FileReader(f);
      bin = new BufferedReader(freader);
    } catch (Exception e) {
      System.err.println("Problem opening star data in " +fileName+ ": " +e);
      e.printStackTrace();
      return;
    }
    
    // Count the number of lines in the file.
    try {
      bin.mark(200000);
      int lineCount = 0;
      while (true)
        {
          String line = bin.readLine();
          if (line == null)
            // reached EOF.
            break;
          ++lineCount;
        }
      
      // Allocate space to hold data.
      ra = new double[lineCount];
      dec = new double[lineCount];
      pmRA = new double[lineCount];
      pmDec = new double[lineCount];
      mag = new double[lineCount];
    } catch (Exception e) {
      System.err.println("Problem getting size of star data in " +fileName+ ": " +e);
      e.printStackTrace();
      return;
    }
    
    // Reset the mark and read the data.
    try {
      bin.reset();

      int lineCount = 0;
      while (true)
        {
          String line = bin.readLine();
          if (line == null)
            // reached EOF.
            break;
          
          // Break the line up on commas, then parse each value and convert to radians. The 
          // values for proper motion are in milli-arc-seconds, which must be converted to 
          // degrees before converting to radians. Also, the motion in right ascension is 
          // quoted as the change in RA * cos(declination), and this is converted to change
          // in RA (without the cosine).
          String[] parts = line.split(",");
          ra[lineCount] = Math.toRadians(Double.parseDouble(parts[0]));
          dec[lineCount] = Math.toRadians(Double.parseDouble(parts[1]));
          pmRA[lineCount] = Math.toRadians(Double.parseDouble(parts[2]) / 
                             (1000.0 * 60.0 * 60.0)) * Math.cos(dec[lineCount]);
          pmDec[lineCount] = Math.toRadians(Double.parseDouble(parts[3]) / 
                               (1000.0 * 60.0 * 60.0));
          mag[lineCount] = Double.parseDouble(parts[4]);
          
          ++lineCount;
        }
    } catch (Exception e) {
      System.err.println("Problem reading star data from " +fileName+ ": " +e);
      e.printStackTrace();
      
      // Make sure that the data is null so that the caller doesn't try to use bad data.
      ra = null;
      dec = null;
      pmRA = null;
      pmDec = null;
      mag = null;
      return;
    }
  }
}