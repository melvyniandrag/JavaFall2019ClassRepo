  package rsf.astron.place;

// This adds a main() to PlaceUtil0. This can be used to determine the place of the planets.

import rsf.astron.jpl.JPLData;
import rsf.ui.LineReader;
import rsf.ui.LoadOrSaveDialog;
import rsf.astron.time.TimeUtil;
import rsf.graphics.GUtil;


public class PlaceUtil extends PlaceUtil0 {
  
  // This is the equatorial radius, in kilometers.
  private static final double EarthRadius = 6378.140;
  
  
  public static double[] observerLocation(double gst,double latitude,double longitude) {
    
    // Return the location of an observer on the surface of the earth, in rectangular
    // coordinates relative to the center of the earth, using the same axes as the
    // JPL data (dec/ra as of 2000.0). Both latitude and longitude are in radians. The
    // result is in kilometers to make it consistent with the JPL data.
    // NOTE: This assumes a spherical earth.
    Coord observer = new Coord(gst+longitude,latitude,Coord.DecHA);
    double[] p = observer.toRectangular();
    p = GUtil.times(p,EarthRadius);
    return p;
  }
  
  public static void main(String[] args) {
    
    // Based on the users location and time, report the positions of the planets.

    // Load a JPL file.
    String[] dataLoc = LoadOrSaveDialog.getLoadChoice("JPL Data?");
    if (dataLoc == null)
      System.exit(0);
    JPLData eph = JPLData.load(dataLoc[0],dataLoc[1]);
    
    // Find out the observer's location.
    System.out.println("Input the latitude and longitude of the observer, in decimal");
    System.out.println("degrees. Latitude is in [-90,+90], positive to the north ");
    System.out.println("of the equator, and longitude is in [-180,+180], positive");
    System.out.println("to the east of Greenwich.");
    double latitude = LineReader.queryDouble("Latitude: ");
    double longitude = LineReader.queryDouble("Longitude: ");
    
    // Convert latitude/longitude to radians, with latitude in [-pi/2,+pi/2] and 
    // longitude in [0,2 pi].
    latitude = Math.toRadians(latitude);
    longitude = Math.toRadians(longitude);
    
    // Find out the time at the observer's location.
    System.out.println("Input the time. Give the date in the form YYYYMMDD. The time");
    System.out.println("should be as given by a clock, but without any adjustment for");
    System.out.println("daylight-savings, Time should be in the form HHMMSS.");
    int dateInt = LineReader.queryInt("Date: ");
    double timeDbl = LineReader.queryDouble("Time: ");
    int zoneDiff = LineReader.queryInt("Zones from Greenwich: ");
    
    // Convert time to the number of hours, as measured at Greenwich on the
    // given day. This may be negative, but that's OK.
    double localClock = TimeUtil.hhmmssToHours(timeDbl);
    localClock -= zoneDiff;
    
    // Get the Julian day. This is the value that is used below. TimeUtil.yyyymmddToJD()
    // returns the JD as of Greenwich noon and this value must be adjusted by the 
    // extent that localClock is before or after noon.
    double theJD = TimeUtil.yyyymmddToJD(dateInt) + (localClock - 12.0) / 24.0;
    
    // Because both the Julian day and our clocks are based on the same
    // uniform time scale (up to a small adjustment), theJD refers to exactly
    // the time in question. This is enough information to determine the 
    // ecliptic coordinates of the planets.
    System.out.println("\nThe declination and right ascension of the planets are:\n");
    for (int i = 0; i <= JPLData.Sun; i++)
      {
        if (i == JPLData.Emb)
          // Don't report the position of the earth-moon barycenter.
          continue;
        
        // Get rectangular coordinates of the planet, relative to the earth, and
        // adjusted for light-time.
        double[] p = eph.getObservedPosition(theJD,i,JPLData.Earth);
        
        // Convert to celestial coordinates. To be precise, adjust epsilon for the JD.
        Coord raDec = Coord.fromRectangular(p[0],p[1],p[2],Coord.DecRA);
        raDec.epsilon = PlaceUtil.meanObliquity(theJD);
        
        // Precess these coordinates, then report them.
        Coord precessed = PlaceUtil.from2000(raDec,theJD);
        System.out.println(JPLData.FullBodyName[i]+ "\t" +precessed);
      }
    
    // Determining the positions of the planets in horizon coordinates requires the
    // observer's LST.
    double gst = TimeUtil.jdToGST(theJD,eph);
    double lst = TimeUtil.gstToLST(gst,longitude);
    
    //  Report the horizon coordinates. The is very much as in the previous loop.
    System.out.println("\nThe horizon coordinates of the planets are:\n");
    for (int i = 0; i <= JPLData.Sun; i++)
      {
        if (i == JPLData.Emb)
          continue;
        
        double[] p = eph.getObservedPosition(theJD,i,JPLData.Earth);
        
        // Determine observer's location relative to the center of the earth, then
        // adjust p so that it is given with this observer at the origin.
        double[] e = observerLocation(gst,latitude,longitude);
        p = GUtil.minus(p,e);
        
        Coord raDec = Coord.fromRectangular(p[0],p[1],p[2],Coord.DecRA);
        raDec.epsilon = PlaceUtil.meanObliquity(theJD);
        Coord precessed = PlaceUtil.from2000(raDec,theJD);
        
        // Convert to horizon coordinates.
        Coord horizon = precessed.toHorizon(lst,latitude);
        System.out.println(JPLData.FullBodyName[i]+ "\t" +horizon);
      }
  }
}