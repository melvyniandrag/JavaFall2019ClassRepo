package rsf.astron.kepler;

// Used to solve Kepler's Equation, M = alpha - e sin(alpha).
// Based on Meeus, p. 196 or Danby p. 198.

import rsf.ui.LineReader;


public class KeplersEqn {
  
  private static double accuracy = 1.0 / 1000000.0;
  
  public static double solve(double e,double M) {
    
    // Return alpha such that M = alpha - e sin(alpha). The idea is that
    // alpha_{i+1} = M + e sin(alpha_i) gives successively better approximations.
    double alpha_i = M;
    double alpha_ip1 = M + e * Math.sin(alpha_i);
    
    // Keep iterating until we have M to within this.accuracy.
    double error = Math.abs(M - apply(e,alpha_ip1));
    while(error > accuracy)
      {
        alpha_i = alpha_ip1;
        alpha_ip1 = M + e * Math.sin(alpha_i);
        error = Math.abs(M - apply(e,alpha_ip1));
      }
      
    return alpha_ip1;
  }
  
  public static double apply(double e,double alpha) {
    return alpha - e * Math.sin(alpha);
  }
  
  public static double solveApprox(double e,double M) {
    
    // Solution by closed-form approximation.
    double e2coef = Math.sin(2.0 * M);
    double e3coef = 9.0 * Math.sin(3.0 * M) - 3.0 * Math.sin(M);
    double e4coef = 64.0 * Math.sin(4.0 * M) - 32.0 * Math.sin(2.0 * M);
    double e5coef = 625.0 * Math.sin(5.0 * M) - 405.0 * Math.sin(3.0*M) 
                     + 10.0 * Math.sin(M);
    double e6coef = 7776.0 * Math.sin(6.0 * M) - 6144.0 * Math.sin(4.0 * M) 
                     + 480.0 * Math.sin(2.0 * M);
    
    double epow = e * e; e2coef *= epow / 2.0;
    epow *= e; e3coef *= epow / 24.0;
    epow *= e; e4coef *= epow / 102.0;
    epow *= e; e5coef *= epow / 1920.0;
    epow *= e; e6coef *= epow / 23040.0; 
    
    return M + e * Math.sin(M) + e2coef + e3coef + e4coef + e5coef + e6coef;
  }
  
  public static double MToTheta(double M,double e) {
    
    // The solve() method gives M --> alpha. Go one more step.
    return alphaToTheta(solve(e,M),e);
  }
  
  public static double alphaToTheta(double alpha,double e) {
    
    // Convert the alpha to a polar angle theta. e is the eccentricity.
    // Uses tan^2(theta/2) = ( (1+e)/(1-e) ) tan^2(alpha/2).
    if (alpha == Math.PI)
      return Math.PI;
    
    double alphaTerm = Math.sqrt((1.0 + e) / (1.0 - e)) * Math.tan(alpha / 2.0);
    if (alpha < Math.PI)
      return 2.0 * Math.atan2(alphaTerm,1.0);
    else
      return 2.0 * Math.atan2(-alphaTerm,-1.0);
  }
  
  public static double thetaToAlpha(double theta,double e) {
    
    // Reverse of alphaToThetha().
    if (theta == Math.PI)
      return Math.PI;
    double thetaTerm = Math.sqrt((1.0 - e) / (1.0 + e)) * Math.tan(theta / 2.0);
    if (theta < Math.PI)
      return 2.0 * Math.atan2(thetaTerm,1.0);
    else
      return 2.0 * Math.atan2(-thetaTerm,-1.0);
  }
  
  public static void main(String[] args) {
    
    // Compare iterative and approximate solutions of Kepler's equation.
    double e = LineReader.queryDouble("Eccentricity? ");
    if ((e > 1.0) || (e < 0.0))
      {
        System.err.println("The eccentricity should be between 0 and 1.");
        System.exit(0);
      }
    
    for (int i = 0; i < 360; i++)
      {
        double M = Math.PI * i/180.0;
        double iterSoltn = solve(e,M);
        double appSoltn = solveApprox(e,M);
        System.out.println(i+ "\t" +iterSoltn+ "\t" +appSoltn);
      }
  }
}