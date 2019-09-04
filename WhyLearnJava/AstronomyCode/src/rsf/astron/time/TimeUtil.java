package rsf.astron.time;

// Holds two methods necessary to convert from JD to GST. 

import rsf.astron.jpl.JPLData;
import rsf.astron.place.Coord;


public class TimeUtil extends TimeUtil0 {
  
  // The length of a mean sidereal day, in seconds.
  private static final double SecondsPerSiderealDay = 86164.09054;
    
    
  public static double jdToGST(double jd,JPLData eph) {
    
    // Convert the Julian day to GST, the Greenwich Sidereal Time. This is the hour angle
    // of Aries, in radians. earthLong should be the ecliptic longitude of the earth 
    // relative to the sun (not the SSB). This longitude should be given without any 
    // adjustment for precession. This can easily be determined from the JPL data with
    // getEarthLong().
    
    // Determine the number seconds since 2000.0.
    double secSince2000 = (jd - 2451545.0) * 24.0 * 60.0 * 60.0;
    
    // This is the angle of the earth relative to the stars, with the position of the
    // earth at 2000.0 take to have angle zero.
    double earthRot = 2.0 * Math.PI * secSince2000 / SecondsPerSiderealDay;
    
    // The GST at 2000.0 was 18 hours, 41 minutes and 50 seconds (by observation).
    double gstAt2000 = timeToHARad(18.0 + 41.0/60.0 + 50.0 / 3600.0);
    
    double answer = gstAt2000 + earthRot;
    answer = answer % (2.0 * Math.PI);
    if (answer < 0.0)
      answer += 2.0 * Math.PI;
    
    return answer;
  }
  
  public static double getEarthLong(double jd,JPLData eph) {
    
    // Return the ecliptic longitude of the earth, using the sun as the origin. This is 
    // the angle between the earth and Aries. The JPL axes are equatorial (dec/ra), so 
    // the x-axis of the JPL system points to Aries. The angle needed is the right 
    // ascension of the earth. This is needed by jdToGST().
    // NOTE: This uses getObservedPosition(), which takes light-time into account.
    double[] p = eph.getObservedPosition(jd,JPLData.Sun,JPLData.Earth);
    Coord earthDecRA = Coord.fromRectangular(p[0],p[1],p[2],Coord.DecRA);
    return earthDecRA.twoPiTerm;
  }
}