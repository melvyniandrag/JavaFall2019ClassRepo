 package rsf.astron.place;

// This class can be used to store celestial coordinates in any of the four systems, and 
// to convert between these systems. Also, toRectangular() converts any of these celestial
// coordinates to rectangular coordinates, where the result is a point on the unit sphere.
// fromRectangular() converts from rectangular coordinates to celestial coordinates. See 
// the Appendix for a discussion of spherical versus rectangular coordinates.
//
// Be careful in how this is used. For example, if an instance of this class holds
// ecliptic coordinates relative to the sun, then toHorizon() will convert to coordinates
// relative to an observer on the sun, not on the earth.
// 
// There are three base cases: 
// (alpha,delta)  <--> (H,delta)      or   dec/RA <--> dec/HA
// (alpha,delta)  <--> (lambda,beta)  or   dec/RA <--> Ecliptic
// (H,delta)      <--> (A,h)          or   dec/HA <--> Horizon
// All of the other cases can be derived by repeated application of these three.
// 
// Some of these conversions require the local sidereal time (lst) and/or the geographic
// latitude (phi). A transformations may also use epsilon, the obliquity of the ecliptic.
// There are six possible transformations, and their use of lst, phi and epsilon is shown
// below.
//
//                        lst phi epsilon
// dec/RA <--> dec/HA      Y   N     N      (base case)
// dec/RA <--> Horizon     Y   Y     N
// dec/RA <--> Ecliptic    N   N     Y      (base case)
// decHA <--> Horizon      N   Y     N      (base case)
// decHA <--> Ecliptic     Y   N     Y
// Horizon <--> Ecliptic   Y   Y     Y
// 
// There are four basic methods: toDecRA(), toDecHA(), toHorizon() and toEcliptic(). Each
// method is able to convert from any of the four coordinate systems to the desired 
// system. When converting to/from Dec/RA from/to ecliptic coordinates, neither lst nor 
// phi are needed. So the methods toDecRA() and toEcliptic() have a no-argument form. If
// the no-argument method is called when either lst or phi is required, then an error
// message is printed and the program exits. If lst and phi are available, then calling
// the method that takes both of these as arguments will always work, even if the values
// aren't used.

import rsf.ui.LineReader;
import rsf.graphics.GUtil;


public class Coord {
  
  // These are the types of coordinate that this class knows about: 
  // declination & right ascension, declination & hour angle, etc.
  public final static int Unknown = 0;
  public final static int DecRA = 1;
  public final static int DecHA = 2;
  public final static int Horizon = 3;
  public final static int Ecliptic = 4;
  
  // This should be one of the values above: DecRA, DecHA, etc.
  public int type = Unknown;
  
  // These are the two coordinates, in radians. One has a range of 2 pi possible values;
  // the other has a range of only one pi. For example, in the horizon system, azimuth 
  // is the twoPiTerm and altitude is the piTerm.
  public double twoPiTerm = 0;
  public double piTerm = 0;
  
  // Most of the time this default value for epsilon will be fine. For greater
  // accuracy, use PlaceUtil.meanObliquity().
  public double epsilon = Math.toRadians(23.43772);
  
  
  public Coord() {
  }
  
  public Coord(double twoPiTerm,double piTerm,int type) {
    
    // This will be in error if type is not one of the known values: DecRA, DecHA, etc.
    this.twoPiTerm = twoPiTerm;
    while (this.twoPiTerm >= 2.0*Math.PI) this.twoPiTerm -= 2.0 * Math.PI;
    while (this.twoPiTerm < 0.0) this.twoPiTerm += 2.0 * Math.PI;
    this.piTerm = piTerm;
    this.type = type;
  }
  
  private void error(int badType) {
    
    // Called when an attempt is made to convert to a given type of coordinates
    // without sufficient information -- lst or phi is needed but not given.
    System.err.println("Unable to convert to type " +badType+ " from type " +type);
    (new Exception()).printStackTrace();
    System.exit(-1);
  }
  
  public Coord toDecRA(double lst,double phi) {
    
    if (this.type == DecRA)
      // Already in the target type. Just return a duplicate.
      return new Coord(twoPiTerm,piTerm,type);
    
    if (this.type == DecHA)
      // Base case: (H,delta) -> (alpha,delta).
      return new Coord(lst-twoPiTerm,piTerm,DecRA);
    else if (this.type == Horizon)
      // Two steps: (A,h) -> (H,delta) -> (alpha,delta)
      return this.toDecHA(lst,phi).toDecRA(lst,phi);
    else if (this.type == Ecliptic)
      {
        // Base case: (lambda,beta) -> (alpha,delta)
        double sinlambda = Math.sin(twoPiTerm);
        double coslambda = Math.cos(twoPiTerm);
        double sinbeta = Math.sin(piTerm);
        double cosbeta = Math.cos(piTerm);
        double tanbeta = Math.tan(piTerm);
        double coseps = Math.cos(epsilon);
        double sineps = Math.sin(epsilon);
        return new Coord(
            Math.atan2(sinlambda*coseps-tanbeta*sineps,coslambda),
            Math.asin(sinbeta*coseps+cosbeta*sineps*sinlambda),DecRA);
      }
    return null;
  }
  
  public Coord toDecRA() {
    
    // No arguments only works for ecliptic -> Dec/RA.
    if ((this.type != Ecliptic) && (this.type != DecRA))
      error(DecRA);
    return toDecRA(0.0,0.0);
  }
  
  public Coord toDecHA(double lst,double phi) {
    
    if (this.type == DecHA)
      // Already in the target type.
      return new Coord(twoPiTerm,piTerm,type);
    
    if (this.type == DecRA)
      {
        // Base case: (alpha,delta) -> (H,delta)
        return new Coord(lst-twoPiTerm,piTerm,DecHA);
      }
    else if (this.type == Horizon)
      {
        // Base case: (A,h) -> (H,delta)
        double cosh = Math.cos(piTerm);
        double sinh = Math.sin(piTerm);
        double tanh = Math.tan(piTerm);
        double sinphi = Math.sin(phi);
        double cosphi = Math.cos(phi);
        double cosA = Math.cos(twoPiTerm);
        double sinA = Math.sin(twoPiTerm);
        return new Coord(
            Math.atan2(-sinA,-cosA*sinphi+tanh*cosphi),
            Math.asin(cosh*cosA*cosphi+sinh*sinphi),DecHA);
      }
    else if (this.type == Ecliptic)
      // Two steps: (lambda,beta) -> (alpha,delta) -> (H,delta)
      return this.toDecRA(lst,phi).toDecHA(lst,phi);
    return null;
  }
  
  public Coord toHorizon(double lst,double phi) {
    
    if (this.type == Horizon)
      // Already in the target type.
      return new Coord(twoPiTerm,piTerm,type);
    
    if (this.type == DecRA)
      // Two steps: (alpha,delta) -> (H,delta) -> (A,h)
      return this.toDecHA(lst,phi).toHorizon(lst,phi);
    else if (this.type == DecHA)
      {
        // Base case: (H,delta) -> (A,h).
        double sinH = Math.sin(twoPiTerm);
        double cosH = Math.cos(twoPiTerm);
        double sinphi = Math.sin(phi);
        double cosphi = Math.cos(phi);
        double cosdelta = Math.cos(piTerm);
        double sindelta = Math.sin(piTerm);
        double tandelta = Math.tan(piTerm);
        return new Coord(
            //Math.atan2(sinH,sinphi*cosH-cosphi*tandelta) + Math.PI
            Math.atan2(-sinH,-sinphi*cosH+cosphi*tandelta),
            Math.asin(cosphi*cosdelta*cosH+sinphi*sindelta),Horizon);
      }
    else if (this.type == Ecliptic)
      // Three steps: (lambda,beta) ->(alpha,delta) -> (H,delta) -> (A,h)
      return this.toDecRA(lst,phi).toDecHA(lst,phi).toHorizon(lst,phi);
    return null;
  }
  
  public Coord toEcliptic(double lst,double phi) {
    
    if (this.type == Ecliptic)
      // Already in the target type.
      return new Coord(twoPiTerm,piTerm,type);
    
    if (this.type == DecRA)
      {
        // Base case: (alpha,delta) -> (lambda,beta)
        double sinalpha = Math.sin(twoPiTerm);
        double cosalpha = Math.cos(twoPiTerm);
        double sindelta = Math.sin(piTerm);
        double cosdelta = Math.cos(piTerm);
        double tandelta = Math.tan(piTerm);
        double coseps = Math.cos(epsilon);
        double sineps = Math.sin(epsilon);
        return new Coord(
            Math.atan2(sinalpha*coseps+tandelta*sineps,cosalpha),
            Math.asin(sindelta*coseps-cosdelta*sineps*sinalpha),Ecliptic);
      }
    else if (this.type == DecHA)
      // Two steps: (H,delta) -> (alpha,delta) -> (lambda,beta)
      return this.toDecRA(lst,phi).toEcliptic(lst,phi);
    else if (this.type == Horizon)
      // Three steps: (A,h) -> (H,delta) -> (alpha,delta) -> (lambda,beta)
      return this.toDecHA(lst,phi).toDecRA(lst,phi).toEcliptic(lst,phi);
    return null;
  }
  
  public Coord toEcliptic() {
  
    // This only works when converting from DecRA.
    if ((this.type != Ecliptic) && (this.type != DecRA))
      error(Ecliptic);
    return toEcliptic(0.0,0.0);
  }
  
  public Coord to(int type,double lst,double phi) {
    
    // A convenience method; use it to convert to an arbitrary type.
    if (type == DecRA)
      return toDecRA(lst,phi);
    else if (type == DecHA)
      return toDecHA(lst,phi);
    else if (type == Horizon)
      return toHorizon(lst,phi);
    else if (type == Ecliptic)
      return toEcliptic(lst,phi);
    else
      return null;
  }
  
  public double[] toRectangular() {
    
    // Convert any of the celestial coordinates to rectangular coordinates, where the 
    // result is a point on the unit sphere, returned as an array of the form (x,y,z).
    // The coordinate axes are those of the underlying system. For instance,
    // if this.type == Horizon, then the z-axis points to the observer's zenith.
    double[] answer = new double[3];
    answer[0] = Math.cos(piTerm) * Math.cos(twoPiTerm);
    answer[1] = Math.cos(piTerm) * Math.sin(twoPiTerm);
    answer[2] = Math.sin(piTerm);
    return answer;
  }
  
  public static Coord fromRectangular(double x,double y,double z,int type) {

    // Convert from the rectangular coordinates (x,y,z) to the form of celestial
    // coordinates given by type. 
    Coord answer = new Coord();
    answer.type = type;
    answer.twoPiTerm = Math.atan2(y,x);
    if (answer.twoPiTerm < 0.0) answer.twoPiTerm += 2.0 * Math.PI;
    answer.piTerm = Math.atan2(z,Math.sqrt(x*x+y*y));
    return answer;
  }
  
  public String toString() {
    
    // Return a string with the angle in degrees, minutes and seconds.
    if (this.type == DecRA)
      return "RA: " +GUtil.radToHMSString(twoPiTerm)+ " Dec: " +
            GUtil.radToDMSString(piTerm);
    else if (this.type == DecHA)
      return "HA: " +GUtil.radToHMSString(twoPiTerm)+ " Dec: " +
            GUtil.radToDMSString(piTerm);
    else if (this.type == Horizon)
      return "Az: " +GUtil.radToDMSString(twoPiTerm)+ " Alt: " +
            GUtil.radToDMSString(piTerm);
    else if (this.type == Ecliptic)
      return "Lo: " +GUtil.radToDMSString(twoPiTerm)+ " La: " +
            GUtil.radToDMSString(piTerm);
    else
      return "";
  }
  
  public String toDecimalDegString() {
    
    // As in toString(), but don't bother with degrees, minutes, seconds. Just output
    // decimal degrees.
    if (this.type == DecRA)
      return "RA: " +Math.toDegrees(twoPiTerm)+ " Dec: " +Math.toDegrees(piTerm);
    else if (this.type == DecHA)
      return "HA: " +Math.toDegrees(twoPiTerm)+ " Dec: " +Math.toDegrees(piTerm);
    else if (this.type == Horizon)
      return "Az: " +Math.toDegrees(twoPiTerm)+ " Alt: " +Math.toDegrees(piTerm);
    else if (this.type == Ecliptic)
      return "Lo: " +Math.toDegrees(twoPiTerm)+ " La: " +Math.toDegrees(piTerm);
    else
      return "";
  }
  
  public static void main(String[] args) {
    
    // Use this for testing. The user chooses an input coordinate system, then gives
    // two angles in degrees, chooses the output system, and the program prints the
    // coordinates in that system.
    
    int inType= 0;
    while ((inType < 1) || (inType > 4))
      inType = LineReader.queryInt("Input system (1=dec/ra, 2=dec/ha, 3=horizon, " +
                                   "4=ecliptic): ");
    double twoPiTerm = 0.0;
    double piTerm = 0.0;
    System.out.println("Input angles as decimal degrees.");
    switch (inType)
      {
        case DecRA    : twoPiTerm = LineReader.queryDouble("Right ascension [0,360]: ");
                        piTerm = LineReader.queryDouble("Declination [-90,+90]: ");
                        break;
        case DecHA    : twoPiTerm = LineReader.queryDouble("Declination [0,360]: ");
                        piTerm = LineReader.queryDouble("Right ascension [-90,+90]: ");
                        break;
        case Horizon  : twoPiTerm = LineReader.queryDouble("Azimuth [0,360]: ");
                        piTerm = LineReader.queryDouble("Altitude [-90,+90]: ");
                        break;
        case Ecliptic : twoPiTerm = LineReader.queryDouble("Longitude [0,360]: ");
                        piTerm = LineReader.queryDouble("Latitude [-90,+90]: ");
                        break;
      }
    
    Coord original = new Coord(Math.toRadians(twoPiTerm),Math.toRadians(piTerm),inType);
    
    
    int outType= 0;
    while ((outType < 1) || (outType > 4))
      outType = LineReader.queryInt("Output system (1=dec/ra, 2=dec/ha, 3=horizon, " +
                                   "4=ecliptic): ");
    
    // Some of these transformations require LST and/or latitude.
    double lst = 0.0;
    double phi = 0.0;
    if (((inType == DecRA) && ((outType == DecHA) || (outType == Horizon))) ||
        ((inType == DecHA) && ((outType == DecRA) || (outType == Ecliptic))) ||
        ((inType == Horizon) && ((outType == DecRA) || (outType == Ecliptic))) ||
        ((inType == Ecliptic) && ((outType == DecHA) || (outType == Horizon))))
        {
          lst = LineReader.queryDouble("LST is required (decimal hours [0,24]): ");
          lst = lst * 2.0 * Math.PI / 24.0;
        }

    if (((inType == DecRA) && (outType == Horizon)) ||
        ((inType == DecHA) && (outType == Horizon)) ||
        ((inType == Horizon) && ((outType == DecRA) || (outType == DecHA) ||
            (outType == Ecliptic))) ||
        ((inType == Ecliptic) && (outType == Horizon)))
        {
          phi = LineReader.queryDouble("Latitude is required (degrees in [0,360]): ");
          phi = Math.toRadians(phi);
        }
    
    // Convert to the desired system, print the angles and exit.
    Coord output = original.to(outType,lst,phi);
    System.out.println("Result: " +output.toDecimalDegString());
    System.exit(0);
  }
}