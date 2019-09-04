package rsf.astron.place;

// These methods can be used to adjust celestial coordinates for general precession
// and nutation.
// 
// As with the Coord class, the observer's local sidereal time (lst), geographic latitude
// (phi) and the obliquity (epsilon) may be needed. There are four types of coordinate. 
// The table below shows whether each of lst, phi and epsilon is actually used in 
// transforming from one time to another. To make this class easier to use, to2000() and
// from2000() are also defined in forms that don't take lst or phi as arguments. As in
// Coord, if either of these is needed for the requested operation, an error message
// appears and the program exits.
//
//          lst phi epsilon
// Dec/RA    N   N    N
// Dec/HA    Y   N    N
// Horizon   Y   Y    N
// Ecliptic  N   N    Y
//
// Epsilon is always relevant to the transformation, since the obliquity changes over 
// time, even if epsilon is not used in making the transformation. Therefore,
// Coord.epsilon is always set to be equal to the obliquity at the time at which the 
// coordinates are given.

import rsf.graphics.GUtil;


public class PlaceUtil0 {
  
  public static double meanObliquity(double jd) {

    // Returns the mean obliquity of the ecliptic for the given Julian day, in radians.
    double t = (jd - 2451545.0) / 36525.0;
    double p0 = GUtil.dmsToDeg(23.0,26.0,21.448);
    return Math.toRadians(p0 - (46.8150 * t - 0.00059 * t * t) / 3600.0);
  }

  public static double[][] precessionMatrix(double jd) {
    
    // Return a 3 x 3 precession matrix for the given Julian day.
    double t = (jd - 2451545.0) / 36525.0;
    double zetaA = Math.toRadians((2306.2181 * t + 0.30188 * t * t + 0.017998 * t * t * t)
                                    / 3600.0);
    double zA = Math.toRadians((2306.2181 * t + 1.09468 * t * t + 0.018203 * t * t * t)
                                    / 3600.0);
    double thetaA = Math.toRadians((2004.3109 * t - 0.42665 * t * t 
                                      - 0.041833 * t * t * t) / 3600.0);
    
    // Convert these angles to rotation matrices.
    double[][] rotZzA = GUtil.rotationMatrix3(-zA,GUtil.Z_AXIS);
    double[][] rotYthetaA = GUtil.rotationMatrix3(thetaA,GUtil.Y_AXIS);
    double[][] rotZzetaA = GUtil.rotationMatrix3(-zetaA,GUtil.Z_AXIS);
    
    // The product of these matrices is the precession matrix.
    return GUtil.times(rotZzA,GUtil.times(rotYthetaA,rotZzetaA));
  }

  public static double[][] inversePrecessionMatrix(double jd) {
    
    // As above, but it returns P^{-1} rather than P. The only difference is that the 
    // signs of the angles are reversed, and the rotation matrices are multiplied in
    // reverse order. That is,
    // P = R_{-z_A,z} R_{theta,y} R_{-zeta,z}, so
    // P^{-1} = R_{zeta,z} R_{-theta,y} R_{z_A,z}.
    // A much shorter way to implement this method results from noting that because 
    // rotation matrices are orthogonal, their inverse and transpose are the same thing.
    // If you don't know what that means, don't worry about it.
    double t = (jd - 2451545.0) / 36525.0;
    double zetaA = Math.toRadians((2306.2181 * t + 0.30188 * t * t + 0.017998 * t * t * t)
                                    / 3600.0);
    double zA = Math.toRadians((2306.2181 * t + 1.09468 * t * t + 0.018203 * t * t * t)
                                    / 3600.0);
    double thetaA = Math.toRadians((2004.3109 * t - 0.42665 * t * t 
                                      - 0.041833 * t * t * t) / 3600.0);
    
    double[][] rotZzA = GUtil.rotationMatrix3(zA,GUtil.Z_AXIS);
    double[][] rotYthetaA = GUtil.rotationMatrix3(-thetaA,GUtil.Y_AXIS);
    double[][] rotZzetaA = GUtil.rotationMatrix3(zetaA,GUtil.Z_AXIS);
    
    return GUtil.times(rotZzetaA,GUtil.times(rotYthetaA,rotZzA));
  }
  
  public static double[][] nutationMatrix(double jd) {
    
    // This uses an approximation from Meeus, p. 144. It returns the nutation matrix,
    // which is defined to be
    // N = R_{-epsilon,x} R_{deltaPsi,z} R_{epsilon_0,x},
    // where epsilon = mean obliquity and epsilon_0 = epsilon + deltaEpsilon. 
    double T = (jd - 2451545.0) / 36525.0;
    double Omega = Math.toRadians(125.04452 - 1934.136261 * T + 0.0020708 * T * T +
                                      T * T * T / 450000.0);
    double L = Math.toRadians(280.4665 + 36000.7698 * T);
    double Lprime = Math.toRadians(218.3165 + 481267.8813 * T);
    
    // These are in arc-seconds.
    double deltaPsi = -17.20 * Math.sin(Omega) - 1.32 * Math.sin(2.0 * L) 
                      - 0.23 * Math.sin(2.0 * Lprime) + 0.21 * Math.sin(2.0 * Omega);
    double deltaEps = 9.20 * Math.cos(Omega) + 0.57 * Math.cos(2.0 * L) 
                      + 0.10 * Math.cos(2.0 * Lprime) - 0.09 * Math.cos(2.0 * Omega);
    
    // Convert to radians.
    deltaPsi = Math.toRadians(deltaPsi / 3600.0);
    deltaEps = Math.toRadians(deltaEps / 3600.0);
    
    // Obliquity values, in radians.
    double eps0 = meanObliquity(jd);
    double eps = deltaEps + eps0;
    
    // Compose the final matrix.
    double[][] rotEps = GUtil.rotationMatrix3(-eps,GUtil.X_AXIS);
    double[][] rotDeltaPsi = GUtil.rotationMatrix3(-deltaPsi,GUtil.Z_AXIS);
    double[][] rotEps0 = GUtil.rotationMatrix3(eps0,GUtil.X_AXIS);
    return GUtil.times(rotEps,GUtil.times(rotDeltaPsi,rotEps0));
  }
  
  public static double[][] inverseNutationMatrix(double jd) {
    
    // As above, but it returns N^{-1}. Like the inverse of the precession matrix, this 
    // could be implemented more briefly by noting that U^t = U^{-1}, when U is 
    // orthogonal.
    double T = (jd - 2451545.0) / 36525.0;
    double Omega = Math.toRadians(125.04452 - 1934.136261 * T + 0.0020708 * T * T +
                                    T * T * T / 450000.0);
    double L = Math.toRadians(280.4665 + 36000.7698 * T);
    double Lprime = Math.toRadians(218.3165 + 481267.8813 * T);
    double deltaPsi = -17.20 * Math.sin(Omega) - 1.32 * Math.sin(2.0 * L) 
                      - 0.23 * Math.sin(2.0 * Lprime) + 0.21 * Math.sin(2.0 * Omega);
    double deltaEps = 9.20 * Math.cos(Omega) + 0.57 * Math.cos(2.0 * L) 
                      + 0.10 * Math.cos(2.0 * Lprime) - 0.09 * Math.cos(2.0 * Omega);
    deltaPsi = Math.toRadians(deltaPsi / 3600.0);
    deltaEps = Math.toRadians(deltaEps / 3600.0);
    double eps0 = meanObliquity(jd);
    double eps = deltaEps + eps0;
    
    // This is the difference from nutationMatrix().
    double[][] rotEps = GUtil.rotationMatrix3(eps,GUtil.X_AXIS);
    double[][] rotDeltaPsi = GUtil.rotationMatrix3(deltaPsi,GUtil.Z_AXIS);
    double[][] rotEps0 = GUtil.rotationMatrix3(-eps0,GUtil.X_AXIS);
    return GUtil.times(rotEps0,GUtil.times(rotDeltaPsi,rotEps));
  }
  
  private static void error(int badType) {
    
    // Called when an attempt is made to convert to a given type of coordinates
    // without sufficient information -- lst or phi is needed but not given.
    System.err.println("Unable to precess type " +badType+ " without lst/phi");
    (new Exception()).printStackTrace();
    System.exit(-1);
  }
  
  public static Coord from2000(Coord orig,double toJD,double lst,double phi) {
    
    // Express the orig Coord object relative to the given Julian day. It is assumed
    // that orig is given relative to 2000.0.
    
    // Convert orig to DecRA. This is why lst and phi may be needed.
    Coord decRACoords = orig.toDecRA(lst,phi);
    
    // Convert to rectangular coordinates, apply the precession matrix,
    // then convert back to the original coordinate system.
    double[] rect0 = decRACoords.toRectangular();
    double[][] P = precessionMatrix(toJD);
    double[][] N = nutationMatrix(toJD);
    double[] rect1 = GUtil.times(GUtil.times(N,P),rect0);
    Coord precessed = Coord.fromRectangular(rect1[0],rect1[1],rect1[2],Coord.DecRA);
    Coord answer = precessed.to(orig.type,lst,phi);
    answer.epsilon = meanObliquity(toJD);
    return answer;
  }
  
  public static Coord from2000(Coord orig,double toJD) {
    
    if ((orig.type == Coord.DecHA) || (orig.type == Coord.Horizon))
      error(orig.type);
    return from2000(orig,toJD,0.0,0.0);
  }
  
  public static Coord to2000(Coord orig,double fromJD,double lst,double phi) {
    
    // The reverse of from2000(). fromJD is the Julian day for which orig is given.
    Coord decRACoords = orig.toDecRA(lst,phi);
    
    double[] rect0 = decRACoords.toRectangular();
    double[][] P = inversePrecessionMatrix(fromJD);
    double[][] N = inverseNutationMatrix(fromJD);
    double[] rect1 = GUtil.times(GUtil.times(P,N),rect0);
    Coord decRA2000 = Coord.fromRectangular(rect1[0],rect1[1],rect1[2],Coord.DecRA);
    return decRA2000.to(orig.type,lst,phi);
  }
  
  public static Coord to2000(Coord orig,double fromJD) {
    
    if ((orig.type == Coord.DecHA) || (orig.type == Coord.Horizon))
      error(orig.type);
    return to2000(orig,fromJD,0.0,0.0);
  }
}