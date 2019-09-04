  package rsf.graphics;

// A grab-bag of routines for matrix and vector algebra, conversion of values, etc.,
// plus one method for drawing circles. All of these methods are static.

import java.awt.Graphics;


public class GUtil {
  
  public static final int X_AXIS = 1;
  public static final int Y_AXIS = 2;
  public static final int Z_AXIS = 3;
  
  public static void fillCircle(int x,int y,int r,Graphics g) {
    
    // Draw a circle with center at (x,y) with radius r. This is needed because fillOval()
    // is based on a bounding rectangle rather than the center and radius of the circle.
    g.fillOval(x - r,y - r,2*r,2*r);
  }
  
  public static String toString(double[][] A) {
    
    // Convert the matrix A into a String suitable for printing.
    String answer = new String();
    for (int i = 0; i < A.length; i++)
      {
        for (int j = 0; j < A[i].length; j++)
          answer += A[i][j]+ "\t";
        answer += "\n";
      } 
    return answer;
  }
  
  public static void print(double[][] A) {
    
    // Print the matrix A.
    System.out.println(toString(A));
  }
  
  public static String toString(double[] x) {
    
    // Convert the vector x to a String suitable for printing.
    String answer = new String();
    for (int i = 0; i < x.length; i++)
      answer += x[i]+ "\t";
    return answer;
  }
  public static void print(double[] x) {
    
    // Print the vector x.
    System.out.println(toString(x));
  }
  
  public static boolean between(double a,double min,double max) {
    // Return true if a between min and max, inclusive.
    if ((a >= min) && (a <= max))
      return true;
    return false;
  }
  
  public static double[] duplicate(double[] x) {
    
    // Create and return a new copy of x.
    double[] answer = new double[x.length];
    for (int i = 0; i < x.length; i++)
      answer[i] = x[i];
    return answer;
  }
  
  public static void copy(double[] src,double[] dest) {
    
    // Set dest[i] equal to src[i]. Both must have been allocated by the caller.
    for (int i = 0; i < src.length; i++)
      dest[i] = src[i];
  }
  
  public static double[] arrayStart(double[] x,int n) {
    
    // Return the first n entries of x.
    double[] answer = new double[n];
    for (int i = 0; i < n; i++)
      answer[i] = x[i];
    return answer;
  }
  
  public static double dmsToDeg(double deg,double min,double sec) {
    
    // Convert degrees, minutes and seconds to decimal degrees.
    return deg + (min / 60.0) + (sec/3600.0);
  }
  
  public static double[] degToDMS(double deg) {
    
    // Convert decimal degrees to degrees, minutes and seconds.
    double x = deg;
    if (x < 0.0) x = -deg;
    double[] answer = new double[3];
    answer[0] = (int) x;
    answer[1] = (int) ((x - answer[0]) * 60.0);
    answer[2] = (x - answer[0] - answer[1]/60.0) * 3600.0;
    if (deg < 0.0) answer[0] = -answer[0];
    return answer;
  }
  
  public static String degToDMSString(double deg) {
    
    // Convert decimal degrees to a string of the form "xxd yym zzs", where xx, yy and zz
    // are degrees, minutes and seconds. The \u00b0 character is for degrees. If this
    // doesn't print correctly on you computer, replace it with 'd' for degrees. The string 
    // argument to format takes the form %x.yf. The f means that it's to format a floating
    // point number. The x means that the result should take at least x characters, and 
    // the y means that there should be y characters after the decimal.
    double[] dms = degToDMS(deg);
    return String.format("%4.0f",dms[0])+"\u00b0 "+String.format("%2.0f",dms[1])+"' "+
                         String.format("%5.2f",dms[2])+"''";
  }
  
  public static double[] radToHMS(double rad) {
    
    // Convert radians to hours, minutes and seconds, at the ratio of 24 hours
    // to 2 pi radians. It is assumed that rad > 0.
    double[] answer = new double[3];
    double hours = rad * 24.0 / (2.0 * Math.PI);
    answer[0] = (int) hours;
    answer[1] = (int) ((hours - answer[0]) * 60.0);
    answer[2] = (hours - answer[0] - answer[1] / 60.0) * 3600.0;
    return answer;
  }
  
  public static String radToHMSString(double rad) {
    // Convert radians to a string of the form "xxh yym zzs", where xx, yy and
    // zz are in hours, minutes and seconds of time.
    double[] hms = radToHMS(rad);
    return String.format("%2.0f",hms[0])+"h "+String.format("%2.0f",hms[1])+
            "m "+String.format("%5.2f",hms[2])+"s";
  }
  
  public static String radToDMSString(double rad) {
    return degToDMSString(Math.toDegrees(rad));
  }
  
  public static double length(double[] x) {
    
    // Returns the length of the vector x.
    double answer = 0.0;
    for (int i = 0; i < x.length; i++)
      answer += x[i] * x[i];
    answer = Math.sqrt(answer);
    return answer;
  }
  
  public static double[] times(double[] x,double c) {
    
    // Returns the term-wise product c x.
    double[] answer = new double[x.length];
    for (int i = 0; i < x.length; i++)
      answer[i] = c * x[i];
    return answer;
  }
  
  public static double[] minus(double[] x,double[] y) {
    
    // Returns the vector x - y.
    double[] answer = new double[x.length];
    for (int i = 0; i < answer.length; i++)
      answer[i] = x[i] - y[i];
    return answer;
  }

  public static double[] plus(double[] x,double[] y) {
    
    // Returns the vector x + y.
    double[] answer = new double[x.length];
    for (int i = 0; i < answer.length; i++)
      answer[i] = x[i] + y[i];
    return answer;
  }
  
  public static void addTo(double[] x,double[] y) {
    
    // Add the vector x to y. That is, y = y + x.
    for (int i = 0; i < x.length; i++)
      y[i] += x[i];
  }
  
  public static double dot(double[] x,double[] y) {
    
    // Return the dot product of the vectors: x dot y.
    double answer = 0.0;
    for (int i = 0; i < x.length; i++)
      answer += x[i] * y[i];
    return answer;
  }
  
  public static double[] cross(double[] x,double[] y) {
    
    // Return the cross product: x cross y. This only makes sense for vectors
    // in three dimensions.
    double[] answer = new double[3];
    answer[0] = x[1] * y[2] - x[2] * y[1];
    answer[1] = x[2] * y[0] - x[0] * y[2];
    answer[2] = x[0] * y[1] - x[1] * y[0];
    return answer;
  }
  
  public static double[] times(double[][] A,double[] x) {
    
    // Returns the product Ax as a vector.
    // NOTE: it is assumed that the dimensions of A and x are consistent.
    double[] answer = new double[A.length];
    
    for (int i = 0; i < A.length; i++)
      {
        answer[i] = 0;
        for (int j = 0; j < x.length; j++)
          answer[i] += A[i][j] * x[j];
      }
    
    return answer;
  }

  public static double[] unitVector(double[] x) {
    
    // Convert x to a unit vector and return it.
    double length = length(x);
    double[] answer = new double[x.length];
    for (int i = 0; i < x.length; i++)
      answer[i] = x[i] / length;
    return answer;
  }
  
  public static double[][] identityMatrix(int n) {
    
    // Return an n x n identity matrix.
    double[][] answer = new double[n][n];
    
    for (int i = 0; i < n; i++)
      {
        for (int j = 1; j < n; j++)
          answer[i][j] = 0.0;
        answer[i][i] = 1.0;
      }
    
    return answer;
  }
  
  public static double[][] times(double[][] B,double[][] C) {
    
    // Return the matrix product A = B C. If B is an n x m matrix (n rows
    // and m columns), and C is an m x r matrix, then A must be an n x r matrix.
    // The (i,j)-th entry of A is sum_k (b_ik c_kj).
    int n = B.length;
    int m = B[0].length; // Also equal to C.length;
    int r = C[0].length;
    double[][] A = new double[n][r];
    
    for (int i = 0; i < n; i++)
      {
        for (int j = 0; j < r; j++)
          for (int k = 0; k < m; k++)
            A[i][j] += B[i][k] * C[k][j];
      }
    
    return A;
  }
  
  public static double[] sphericalToRectangular(double r,double theta,double phi) {

    // Convert x = (r,theta,phi) to rectangular coordinates. This assumes "standard" 
    // spherical coordinates: 0 < theta < 2 pi and 0 < phi < pi.
    double x = r * Math.sin(theta) * Math.cos(phi);
    double y = r * Math.sin(theta) * Math.sin(phi);
    double z = r * Math.cos(theta);
    
    double[] answer = new double[3];
    answer[0] = x;
    answer[1] = y;
    answer[2] = z;
    return answer;
  }
  
  public static double[] sphericalToRectangular(double[] spherical) {
    // Convenience method.
    return sphericalToRectangular(spherical[0],spherical[1],spherical[2]);
  }
  
  public static double[] changeBasis(double[] a,double[] b,double[] c,double[] u) {
    
    // If a, b and c are three linearly independent vectors, and u is an arbitrary vector, 
    // this finds scalars r, s and t so that
    // u = r a + s b + t c.
    // These values are returned as the array (r,s,t). Effectively, what this does is take 
    // the vector u, expressed under the coordinate system with axes in the directions
    // (1,0,0), (0,1,0) and (0,0,1); and change the expression to one under axes that point
    // in the a, b and c directions.
    //
    // This is here primarily as an illustration.
    double[] bxc = GUtil.cross(b,c);
    double[] cxa = GUtil.cross(c,a);
    double[] axb = GUtil.cross(a,b);
    
    double[] answer = new double[3];
    answer[0] = GUtil.dot(u,bxc) / GUtil.dot(a,bxc);
    answer[1] = GUtil.dot(u,cxa) / GUtil.dot(b,cxa);
    answer[2] = GUtil.dot(u,axb) / GUtil.dot(c,axb);
    
    return answer;
  }
  
  public static double[] rectangularToSpherical(double[] rectangular) {
    
    // Convert rectangular = (x,y,z) to spherical coordinates, (r,theta,phi), where theta is
    // in [0,pi] and phi in [0,2pi]. This is not the system of spherical
    // coordinates most useful in astronomy; it's the "normal" system.
    double x = rectangular[0];
    double y = rectangular[1];
    double z = rectangular[2];
    double r = length(rectangular);
    
    double theta = Math.atan2(Math.sqrt(x*x + y*y),z);
    if (theta < 0.0) theta += 2.0 * Math.PI;
    
    double phi = Math.atan2(y,x);
    if (phi < 0.0) phi += 2.0 * Math.PI;
    
    double[] a = new double[3];
    a[0] = r;
    a[1] = theta;
    a[2] = phi;
    return a;
  }
  
  public static double[][] rotationMatrix3(double angle,int axis) {
    
    // Returns a 3x3 rotation matrix. axis = 1, 2 or 3 means that the matrix
    // should rotate about the x, y or z-axis, respectively. Use the values
    // this.X_AXIS, etc. for greater clarity.
    return rotationMatrix3(Math.cos(angle),Math.sin(angle),axis);
  }
  
  public static double[][] rotationMatrix3(double cos,double sin,int axis) {
    
    // Returns a 3x3 rotation matrix. axis = 1, 2 or 3 means that the matrix should rotate 
    // about the x, y or z-axis, respectively. In this case, the method does not have to 
    // calculate the sin and cos. It is passed in by the caller. The class includes final 
    // values (X_AXIS, etc.) that can be used to make the code clearer.
    double[][] answer = identityMatrix(3);

    switch (axis)
      {
        case X_AXIS : answer[1][1] = cos;
                      answer[1][2] = sin;
                      answer[2][1] = -sin;
                      answer[2][2] = cos;
                      break;
        case Y_AXIS : answer[0][0] = cos;
                      answer[0][2] = -sin;
                      answer[2][0] = sin;
                      answer[2][2] = cos;
                      break;
        case Z_AXIS : answer[0][0] = cos;
                      answer[0][1] = sin;
                      answer[1][0] = -sin;
                      answer[1][1] = cos;
                      break;
      }
    
    return answer;
  }
  
  public static double[][] rotationMatrix3Inv(double cos,double sin,int axis) {
    
    // This is here for convenience, and to help avoiding a mix-up of the
    // signs of sine and cosine.
    return rotationMatrix3(cos,-sin,axis);
  }
  
  public static double[] rotate3(double angle,double[] u,double[] p) {
    
    // Rotate the point p about the line in the u direction through the given
    // angle (in radians), all in 3 dimensions.  
    // It is assumed that the line passes through the origin -- that is, L = <u>
    double[] answer = new double[3];
    
    double h = Math.sqrt(u[0]*u[0] + u[2]*u[2]);
    double lengthu = length(u);
    
    double[][] A = null;
    double[][] Ainv = null;
    if (h != 0)
      {
        A = GUtil.rotationMatrix3(u[0]/h,-u[2]/h,Y_AXIS);
        Ainv = GUtil.rotationMatrix3Inv(u[0]/h,-u[2]/h,Y_AXIS);
      }
    else
      {
        A = identityMatrix(3);
        Ainv = identityMatrix(3);
      }
    double[][] B = GUtil.rotationMatrix3(h/lengthu,u[1]/lengthu,Z_AXIS);
    double[][] Binv = GUtil.rotationMatrix3Inv(h/lengthu,u[1]/lengthu,Z_AXIS);
    double[][] R = GUtil.rotationMatrix3(angle,X_AXIS);
    
    answer = GUtil.times(A,p);
    answer = GUtil.times(B,answer);
    answer = GUtil.times(R,answer);
    answer = GUtil.times(Binv,answer);
    answer = GUtil.times(Ainv,answer);
    return answer;
  }
  
  public static double[] rotate3(double angle,double[] p,int axis) {
    
    // Rotate p about the given axis, which should be one of X_AXIS, Y_AXIS or Z_AXIS,
    // by the given angle, in radians.
    double[][] R = rotationMatrix3(angle,axis);
    return times(R,p);
  }
  
  public static double[] parallelProject(double[] u1,double[] u2,double[] p) {
    
    // Project p onto the plane defined by u1 and u2 using parallel projection.
    // It is assumed that the plane passes through the origin and that u1 and
    // u2 are perpendicular.
    double[] answer = new double[2];
    answer[0] = dot(p,u1);
    answer[1] = dot(p,u2);
    return answer;
  }
  
  public static double kTanToTan(double beta,double k) {
    
    // Use this to solve an equation of the form tan(alpha) = k tan(beta).
    // Give beta and k to get alpha, with alpha and beta in radians.
    // It is assumed that beta is in the range [0,2 pi].

    if (beta == Math.PI)
      return Math.PI;
    
    // Remember, Java uses a method that returns the angle in [-pi,+pi], whereas
    // the result should be in [0,2pi].
    if (beta <= Math.PI/2.0)
      return Math.atan2(k * Math.tan(beta),1.0);
    else if (beta <= Math.PI)
      return Math.atan2(-k * Math.tan(beta),-1.0);
    else if (beta <= 3.0 * Math.PI/2.0)
      return Math.atan2(-k * Math.tan(beta),-1.0) + 2.0 * Math.PI;
    else
      return Math.atan2(k * Math.tan(beta),1.0) + 2.0 * Math.PI;
  }
}