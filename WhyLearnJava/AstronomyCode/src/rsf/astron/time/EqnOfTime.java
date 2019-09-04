package rsf.astron.time;

// Methods related to determination of the equation of time. All angles are expressed in
// radians -- arguments to methods and their results. The only exception to this is the 
// eqnOfTime() method, which returns minutes of time.
// 
// Many of these methods take jcentury as an argument. This is the number of Julian 
// centuries from 2000.0. That is, (jd - 2451545.0) / 36525, where jd is the Julian date 
// in question.

import rsf.astron.kepler.KeplersEqn;
import rsf.graphics.GUtil;


public class EqnOfTime {
  
  // The eccentricity of the earth's orbit. For greater accuracy, use
  // e = 0.016708634 - 0.000042037 T - 0.0000001267 T^2,
  // where T is Julian centuries from J2000.0.
  private static double eccentricity = 0.016708634;
  
  public static double jdToFractionOfYear(double jd,double earthLong) {

    // Given a Julian day, return the fraction of a year (in [0,2 pi]) that this time 
    // corresponds to, where the year begins when FMS is at perihelion. earthLong should
    // be the ecliptic longitude of the earth with the sun (not SSB) as the origin. This
    // longitude should be given relative to Aries as of 2000.0, so use unprecessed 
    // coordinates to obtain the longitude. This can easily be derived from the JPL data.
    // Many of the methods below take the value returned as an argument.
    double jcentury = (jd - 2451545.0) / 36525.0;
    double L = longPeri(jcentury);
    
    // L - earthLong is the amount by which the earth differs from perihelion, increasing
    // as the earth moves west. The result should increase as the earth moves east.
    double answer = (2.0 * Math.PI - (L- earthLong)) / (2.0 * Math.PI);
    if (answer >= 2.0 * Math.PI)
      answer -= 2.0 * Math.PI;
    return answer;
  }
  
  public static double longPeri(double jcentury) {
    
    // Return the longitude of perihelion at the given time. This is measured from Aries 
    // as of 2000.0 -- the unprecessed longitude. The result is measured east from Aries.
    return Math.toRadians(282.937348 + 1.7195366 * jcentury 
        + 0.00045688 * jcentury * jcentury
        - 0.000000018 * jcentury * jcentury * jcentury);
  }
  
  public static double obliquity(double jcentury) {
    
    // Return the obliquity of the ecliptic.
    return Math.toRadians(23 + 26.0/60.0 + 21.448/3600.0
      - jcentury * 46.8150/3600.0
      - jcentury * jcentury * 0.00059/3600.0
      + jcentury * jcentury * jcentury * 0.001813 / 3600.0);
  }
  
  public static double trueAnomalySun(double t) {
    
    // Return the true anomaly of the sun. This is measured east from perihelion
    // so that t = 0 corresponds to the time when the true sun is at perihelion. 
    // t is in fractions of a year, so that t = 1 means a single year.
    //
    // Convert to mean anomaly. t is already a fraction of a year, so omit the
    // denominator T. Then solve Kepler's equation and convert to theta.
    double M = 2.0 * Math.PI * t;
    return KeplersEqn.MToTheta(M,eccentricity);
  }
  
  public static double timeFMSAtAries(double jcentury) {
    
    // Return the amount of time from when the TS is at perihelion to when the FMS is at 
    // Aries (which is also when the DMS is at Aries). Result is a fraction of a year.
    // This is easy since the DMS and FMS move at a constant rate.
    double longPeri = longPeri(jcentury);
    return (2.0 * Math.PI - longPeri) / (2.0 * Math.PI);
  }
  
  public static double longTrueSun(double jcentury,double t) {
    
    // Return the longitude of the true sun. This is just like the true anomaly of the 
    // sun, but measured east from Aries instead of east from perihelion. t is a fraction
    // of a year, with t = 0 corresponding to when the FMS is at Aries.
    double longPeri = longPeri(jcentury);
    
    // The time must be adjusted because trueAnomalySun() expects the origin of time to be
    // at perihelion, not at Aries.
    double tau = timeFMSAtAries(jcentury);
    double tprime = t + tau;
    if (tprime > 1.0)
      tprime -= 1.0;
    double trueAnom = trueAnomalySun(tprime);
    
    // trueAnom is the true anomaly relative to perihelion. Adjust it to be relative 
    // to Aries.
    trueAnom -= (2.0 * Math.PI - longPeri);
    while (trueAnom < 0.0)
      trueAnom += 2.0 * Math.PI;
    
    return trueAnom;
  }
  
  public static double longToRA(double lambda,double jcentury) {
    
    // Given an ecliptic longitude, lambda, convert it to right ascension by projecting 
    // the point down to the equator. This amounts to solving 
    // tan alpha = cos epsilon tan lambda
    // for alpha.
    double epsilon = obliquity(jcentury);
    double k = Math.cos(epsilon);
    return GUtil.kTanToTan(lambda,k);
  }
  
  public static double RASun(double jcentury,double t) {
    
    // Return the right ascension of the sun for a given time. t should be in the 
    // range [0,1], with 0 corresponding to the time when DMS/FMS is at Aries. In 
    // principle, this method could take just the Julian day, but using two arguments is 
    // simpler to code.
    // 
    // Get the longitude of the true sun and convert to right ascension.
    double longSun = longTrueSun(jcentury,t);
    return longToRA(longSun,jcentury);
  }
  
  public static double redToEq(double jcentury,double t) {
    
    // Return the reduction equator: RA(TS) - Lo(TS). 
    // t = 0 is when the DMS/FMS is at Aries.
    double longSun = longTrueSun(jcentury,t);
    double RASun = RASun(jcentury,t);
    return RASun - longSun;
  }
  
  public static double eqnOfCenter(double jcentury,double t) {
    
    // By definition, this is true anomaly minus mean anomaly. 
    // t = 0 is when DMS/FMS is at Aries.
    double tau = timeFMSAtAries(jcentury);
    double tprime = t + tau;
    if (tprime > 1.0)
      tprime -= 1.0;
    double trueAnom = trueAnomalySun(tprime);
    double meanAnom = tprime * 2.0 * Math.PI;
    return trueAnom - meanAnom;
  }
  
  public static double RADMS(double jcentury,double t) {
    
    // Return the right ascension of the DMS, where is a fraction of a year after the FMS
    // reaches Aries. This is just the mean anomaly, after adjusting for the fact that the
    // angle is measured from Aries instead of from perihelion.
    double longPeri = longPeri(jcentury);
    double tau = timeFMSAtAries(jcentury);
    
    double tprime = t + tau;
    if (tprime > 1.0)
      tprime -= 1.0;
    
    double longDMS = tprime * 2.0 * Math.PI;
    longDMS -= (2.0 * Math.PI - longPeri);
    if (longDMS < 0.0)
      longDMS += 2.0 * Math.PI;
    
    // Return the corresponding right ascension.
    return longToRA(longDMS,jcentury);
  }
  
  public static double RAFMS(double jcentury,double t) {
    
    // Return the right ascension of the FMS (measured east from Aries).
    // This is very simple since the FMS is at Aries when t = 0.
    return t * 2.0 * Math.PI;
  }
  
  public static double eqnOfTime(double jcentury,double t) {
    
    // Return the equation of time: RA(FMS) - RA(TS), in minutes of time, where t is the
    // fraction of year beyond perihelion.
    double Eradians = -redToEq(jcentury,t) - eqnOfCenter(jcentury,t);
    double Edegrees = Math.toDegrees(Eradians);
    
    // Convert degrees to minutes of time. This is just
    // E * (24 hr/360 degrees) * (60 min / 1 hr)
    return Edegrees * 24.0 * 60.0 / 360.0;
  }
  
  public static void main(String[] args) {
    
    // This will report RA(TS), RA(DMS) and RA(FMS) over the course of a year.
    double jcentury = 0.0;
    for (double t = 0.0; t <= 1.0; t += 0.001)
      System.out.println(t + "\t" +RASun(jcentury,t)+ "\t" +RADMS(jcentury,t) + "\t" 
                         +RAFMS(jcentury,t));
  }
}