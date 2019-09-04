package rsf.astron.jpl;

// Class to hold the JPL data and extract positions from the data.

import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import rsf.graphics.GUtil;


public class JPLData implements Serializable {
  
  // Constants ordered to match the order of the JPL data.
  public final static int Mercury = 0;
  public final static int Venus = 1;
  public final static int Emb = 2;
  public final static int Mars = 3;
  public final static int Jupiter = 4;
  public final static int Saturn = 5;
  public final static int Uranus = 6;
  public final static int Neptune = 7;
  public final static int Pluto = 8;
  public final static int Moon = 9;
  public final static int Sun = 10;
  public final static int Nutations = 11;
  public final static int Librations = 12;
  
  // These don't refer to any body in the JPL data. In particular, don't try to 
  // refer to this.data[Earth], etc. They are derived by other routines, pleph() in 
  // particular. SSB is the Solar System Barycenter, which is the origin used by the
  // JPL data.
  public final static int Earth = 13;
  public final static int SSB = 14;
  
  public final static String[] BodyName = { 
    "MER", "VEN", "EMB", "MAR","JUP", "SAT", "URA", "NEP",
    "PLU", "MOO", "SUN", "NUT","LIB","EAR","SSB" };
  
  public final static String[] FullBodyName = {
    "Mercury", "Venus", "Earth/Moon Barycenter", "Mars", "Jupiter", "Saturn",
    "Uranus", "Neptune", "Pluto", "Moon", "Sun", "Nutation", "Libration",
    "Earth","Solar System Barycenter"};
  
  // Three lines of text that appears in the header file.
  public String[]  title = null;
  
  // The starting and ending time for the data. The startJD is  needed to index 
  // into this.data.
  public double startJD = 0;
  public double endJD = 0;
  
  // The number of Julian days in each block -- 32 or 64 days.
  public double jdStep = 0;
  
  // Each body has time chopped into different sizes pieces (though always an integral
  // fraction of jdStep). We need to know what these chunk sizes are for indexing into 
  // this.data. Sometimes there is no data (e.g., for librations in DE200), in which case
  // bodyStep[i] = -1. This is *not* the third row of the set of pointers in the header
  // file. It's days per block (32 or 64) divided by the value in the third row.
  public double[]  bodyStep = null;
  
  // These are the constants defined in the header file. They aren't needed,
  // but are stored for completeness.
  public String[]  constNames = null;
  public double[]  constValues = null;
  
  // EMRAT is the ratio of earth mass to moon mass (it's larger than 1). This is 
  // needed when trying to get the earth's coordinates. The JPL data has the 
  // E/M barycenter coordinates and the moon coordinates, but not the earth coordinates.
  public int NUMDE = 0;     // The DE number, as in DE200 or DE405.
  public double AU = 0;     // AU in kilometers.
  public double EMRAT = 0;  // Ratio of earth's mass to moon's mass.
  public double CLIGHT = 0; // Speed of light in km/sec.
  
  // This holds all the Chebyshev coefficients.
  // data[i] holds all the data for body i (i = this.Mercury, etc.).
  // data[i][j][k][l] is body i, axis j, coefficient index k, time l.
  // The time index, l, is defined relative to startJD and bodyStep.
  // Each of these values data[i][j][k][l] is one of the Chebyshev coefficients
  // in an ephemeris data file.
  // Some ephemera lack nutation and/or libration data. If so,
  // data[Nutation] and/or data[Libration] will be null.
  public double[][][][] data = null;
  
  
  public JPLData() {
    // Do-nothing constructor required for serialization.
  }
  
  public JPLData(String dir,String fname) {
    
    // A convenience constructor that takes the data from a file that was saved with 
    // save() below.
    JPLData temp = load(dir,fname);
    
    this.title = temp.title;
    this.startJD = temp.startJD;
    this.endJD = temp.endJD;
    this.jdStep = temp.jdStep;
    this.bodyStep = temp.bodyStep;
    this.constNames = temp.constNames;
    this.constValues = temp.constValues;
    this.NUMDE = temp.NUMDE;
    this.AU = temp.AU;
    this.EMRAT = temp.EMRAT;
    this.CLIGHT = temp.CLIGHT;
    this.data = temp.data;
  }
  
  public double[] getObservedPosition(double jd,int theBody,int theObserver) {
    
    // Similar to getPosition(), but includes an approximate adjustment for light-time.
    // Roughly, if the distance from theObserver to theBody is d light-seconds, then 
    // theObserver observes theBody to be at the position it had at time d seconds before
    // jd. This is approximate because both theBody and theObserver are in motion, so that
    // an exact solution requires something like Newton's method. The error due to this 
    // approximation is generally less than 5 arc-seconds.
    
    // Determine the distance between theBody and theObserver at time jd.
    double[] bodyRaw = getPosition(jd,theBody,SSB);
    double[] obsRaw = getPosition(jd,theObserver,SSB);
    double d = GUtil.length(GUtil.minus(bodyRaw,obsRaw));
    
    // dt is the amount of time it takes light to travel from theBody to theObserver,
    // in seconds.
    double dt = d / CLIGHT;
    
    // Return the position of theBody, relative to theObserver, as of dt seconds before 
    // jd. The adjusted JD is obtained by subtracting the fraction of a day equal to dt 
    // seconds. This is where the approximation is made. dt seconds ago, the distance 
    // between the two bodies was not *exactly* d.
    double adjustedJD = jd - dt / (24.0 * 60.0 * 60.0);
    return getPosition(adjustedJD,theBody,theObserver);
  }
  
  public double[] getPosition(double jd,int theBody,int theOrigin) {
    
    // Same as pleph(), but it doesn't return velocity. It just gives the position without
    // any adjustment for light-time. Calling this doesn't really make sense for nutation 
    // or libration.
    double[] plephValue = pleph(jd,theBody,theOrigin);
    return GUtil.arrayStart(plephValue,3);
  }
  
  private double[] pleph(double jd,int theBody,int theOrigin) {
    
    // Gives the position and velocity of theBody with respect to the body given by 
    // theOrigin. jd is the date at which interpolation is wanted. theBody and theOrigin 
    // values are as at JPLData.Mercury, etc. If nutations or librations are theBody, then
    // theOrigin is ignored. JPLData.Earth can be used as either theBody or theOrigin, 
    // even though the position of the earth is not explicitly given in the JPL data.
    // The method returns a 6 element array containing the "state vector". The first 
    // three elements are the position and the next three are the velocity, in km and 
    // km/day. For librations the units are radians and radians/day. For nutations, only 
    // the first 4 entries are used, expressed in radians and radians/day. The nutation in
    // longitude appears first, then the nutation in obliquity.
    
    // Make sure that the data exists.
    if (this.data == null)
      {
        System.err.println("Calling JPLData.pleph() with no data!");
        System.exit(1);
      }
    
    if (theBody == Nutations)
      return interp(jd,true,Nutations);
    if (theBody == Librations)
      return interp(jd,true,Librations);
    
    if (theOrigin == JPLData.Nutations || theOrigin == JPLData.Librations)
       {
         System.err.println("Called pleph() with theOrigin set to Nutations or\n"
                            + "Librations, which makes no sense!");
         System.exit(1);
       }
     
    // This is kind of stupid, but handle it as a special case.
    if (theBody == theOrigin)
      {
        double[] answer = new double[6];
        for (int i = 0; i < 6; i++)
          answer[i] = 0.0;
        return answer;
      }
    
    // We need the coordinates for theBody and theOrigin. The SSB is at (0,0,0),
    // by definition, so don't bother with that case.
    double[] originCoords = new double[6];
    double[] bodyCoords = new double[6];
    if (theOrigin != SSB)
      originCoords = rawCoords(jd,true,theOrigin);
    if (theBody != SSB)
      bodyCoords = rawCoords(jd,true,theBody);
    
    // The answer is just the difference between the coordinates at theBody and theOrigin.
    if (theOrigin == JPLData.SSB)
      // Nothing to do, really. The requested origin is the same as the JPL origin.
      return bodyCoords;
    
    if (theBody == JPLData.SSB)
      {
        // As above, but answer = -originCoords.
        double[] answer = new double[6];
        for (int i = 0; i < answer.length; i++)
          answer[i] = -originCoords[i];
        return answer;
      }
  
    // Standard case. Take the difference bodyCoords - originCoords.
    double[] answer = new double[6];
    for (int i = 0; i < answer.length; i++)
      answer[i] = bodyCoords[i] - originCoords[i];
    return answer;
  }
  
  private double[] rawCoords(double jd,boolean velocityToo,int theBody) {
    
    // Returns the coordinates of theBody relative to the standard origin
    // of the JPL data (the SSB). The earth and moon are special cases.
    
    if ((theBody != Earth) && (theBody != Moon))
      return interp(jd,velocityToo,theBody);
    
    // For both the moon and the earth, the position must be worked out based
    // on the relative masses of the two bodies and the EMB and moon coordinates
    // as given by the JPL data.
    double[] emb = interp(jd,velocityToo,JPLData.Emb);
    double[] moon = interp(jd,velocityToo,JPLData.Moon);
    double[] answer = null;
    if (velocityToo == true)
      answer = new double[6];
    else
      answer = new double[3];

    if (theBody == Earth)
      {
        for (int i = 0; i < answer.length; i++)
          answer[i] = emb[i] - moon[i] / (EMRAT + 1.0);
        return answer;
      }
      
    // Must have theBody == Moon. Similar to the earth case.
    for (int i = 0; i < answer.length; i++)
      answer[i] = emb[i] + moon[i] * EMRAT / (EMRAT + 1.0);
    return answer;
  }
  
  private double[] interp(double jd,boolean velocityToo,int theBody) {
        
    // This returns the position and velocity (if desired) of a given body at a given 
    // time, jd. theBody must be one of the bodies for which JPL has explicit data. 
    // E.g., theBody = Earth is not valid. This is the ultimate access point through which
    // the JPL data is obtained.
    if (theBody > Librations)
      {
        // No such data exists.
        System.err.println("Attempting to interpolate JPL data for " 
                            +FullBodyName[theBody]);
        System.exit(1);
      }
    
    double[] answer = null;
    if (theBody != Nutations)
      {
        if (velocityToo == true)
          answer = new double[6];
        else
          answer = new double[3];
      }
    else
      {
        if (velocityToo == true)
          answer = new double[4];
        else
          answer = new double[2];
      }
    
    // Make sure that the data requested exists.
    if (data[theBody] == null)
      {
        System.err.println("Attempting JPLData.interp() for " +
                           FullBodyName[theBody]+ "when it doesn't exist!");
        return null;
      }
    
    // Figure out the time index we care about.
    double deltaT = jd - this.startJD;
    if (deltaT < 0)
      {
        System.err.println("Attempt to run JPLData.interp() for a time\n"+
                           "earlier than available data!");
        System.err.println("Seeking JD = " +jd+ " versus " +this.startJD);
        return null;
      }
    if (jd > this.endJD)
      {
        System.err.println("Attempt to run JPLData.interp() for a time\n"+
                           "later than available data!");
        System.err.println("Seeking JD = " +jd+ " versus " +this.endJD);
        return null;
      }
    
    // This is the time index. The data we care about is at 
    // this.data[body][][][timeIndex].
    int timeIndex = (int) Math.floor(deltaT / bodyStep[theBody]);
    
    // Deal with the fractional part of the time period.
    // Each polynomial, being a Chebyshev polynomial, is defined on [-1,+1], but it must
    // be scaled to be defined on an interval bodyStep[body] Julian days long. The linear
    // function that maps [a,b] to [-1,1] is g(x) = 2(x-a)/(b-a) -1. In our case, we need
    // to map the interval [t_0,t_0+u] to [-1,+1], where t_0 is the start time of the
    // interval (some multiple of bodyStep[body]) and u = bodyStep[body]. In practice,
    // we only care about the excess time beyond this t_0, which is easy. So we need to 
    // map [0,u] --> [-1,+1]. This is g(x) = 2 x / u - 1.
    
    // This is the number of Julian days we need to look beyond the start 
    // time for this time block.
    double excessTime = deltaT - (double) timeIndex * bodyStep[theBody];
    
    // This is the argument to the Chebyshev polynomials. It's the time
    // scaled to the interval [-1,+1].
    double chebyTime = 2.0 * (excessTime / bodyStep[theBody]) - 1.0;
    
    // Get the Chebyshev polynomial terms. In our case, we only want to evaluate 
    // at x = chebyTime, so we can use the recurrence relation that defines the
    // polynomials to get the values T_i(chebyTime).
    
    // How many terms are there? That is, how many coefficients?
    int terms = data[theBody][0].length;
    double[] polyValues = new double[terms];
    polyValues[0] = 1.0;
    polyValues[1] = chebyTime;
    for (int i = 2; i < terms; i++)
      polyValues[i] = 2.0 * chebyTime * polyValues[i-1] - polyValues[i-2];
    
    // The values we care about are of the form
    // \sum_j c_j polyValues[j],
    // where the c_j are in data[body][axis][j][timeIndex].
    int numAxes = 3;
    if (theBody == Nutations)
      numAxes = 2;
    for (int i = 0; i < numAxes; i++)
      {
        answer[i] = 0.0;
        for (int j = 0; j < terms; j++) {
          answer[i] += data[theBody][i][j][timeIndex] * polyValues[j];
        }
      }
    
    // The position data is done. See if we need to do velocity too.
    if (velocityToo == false)
      return answer;
    
    // Get velocity by taking derivative of Chebyshev polynomials.
    // If g(x) = \sum_{j=0}^n c_j T_j(x) is an expression for a function g(x) in terms of
    // a sum of Chebyshev polynomials, then
    // g'(x) = \sum_{j=1}^n c_j (T_j)'(x) is the derivative. We need an an expression for
    // (T_j)'(x). The recurrence relation that defines T_j(x) is 
    // T_j(x) = 2 x T_{j-1}(x) - T_{j-2}(x). So the derivative is
    // (T_j)'(x) = 2 T_{j-1}(x) + 2 x (T_{j-1})'(x) - (T_{j-2})'(x)
    // Use this recurrence relation to evaluate the (T_j)'(x) at the value x = chebyTime.
    // Start the recurrence with (T_0)'(x) = 0 and (T_1)'(x) = 1.
    double[] derivValues = new double[terms];
    derivValues[0] = 0;
    derivValues[1] = 1;
    for (int i = 2; i < derivValues.length; i++)
      derivValues[i] = 2.0 * polyValues[i-1] + 2.0 * chebyTime * derivValues[i-1]
                         - derivValues[i-2];
    
    for (int i = 0; i < numAxes; i++)
      {
        answer[numAxes + i] = 0.0;
        for (int j = 1; j < terms; j++)
          answer[numAxes + i] += data[theBody][i][j][timeIndex] * derivValues[j];
      }
    
    // The last tricky point about these derivatives is a scaling factor.
    // Each set of coefficients applies to a period of bodyStep[body] days. The functions
    // are defined on [-1,+1], an interval of length 2. The unscaled derivative, as just
    // calculated, gives change in km per (bodyStep[body] / 2) days. We want the change
    // per single day, so divide by (bodyStep[body] / 2).
    for (int j = 0; j < numAxes; j++)
      answer[numAxes + j] *= (2.0 / bodyStep[theBody]);
    
    return answer;
  }
  
  public static JPLData load(String dir,String fname) {
    
    // Does what it says.
    JPLData answer = null;
    try {
      File f = new File(dir,fname);
      FileInputStream fin = new FileInputStream(f);
      ObjectInputStream oin = new ObjectInputStream(fin);
      answer = (JPLData) oin.readObject();
      oin.close();
      fin.close();
    } catch (Exception e) {
      System.err.println("Error reading serialized JPL data: " +e);
      return null;
    }
    return answer;
  }
  
  public void save(String dir,String fname) {
    
    // Save by serializing to a file.
    try {
      File f = new File(dir,fname);
      FileOutputStream fout = new FileOutputStream(f);
      ObjectOutputStream oout = new ObjectOutputStream(fout);
      oout.writeObject(this);
      oout.close();
      fout.close();
    } catch (Exception e) {
      System.err.println("Trouble serializing JPL data: " +e);
      return;
    }
  }
}