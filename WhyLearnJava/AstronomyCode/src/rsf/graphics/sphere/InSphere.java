package rsf.graphics.sphere;

// Keeps track of the geometry relative to a sphere where the observer is inside the sphere,
// at the origin looking out to the surface of the sphere. The sphere is assumed to be 
// centered at the origin and to have radius 1. 
//
// The over-all organization is similar to OutSphere, but this class can handle different 
// methods of projection to the screen. Also, this class doesn't include any code for 
// drawing. Use makePerspectiveProjection(), makeParallelProjection(), makeRadialAxisU1()
// or makeRadialAxisU2() to set the method of projection. The class can also handle zooming
// in/out on the screen. Call zoomIn() and zoomOut() to do this.

import rsf.graphics.GUtil;


public class InSphere {
  
  // These are the types of projection that the class can handle.
  private static final int PARALLEL = 1;
  private static final int PERSPECTIVE = 2;
  private static final int RADIAL = 3;
  
  // This is the particular projection being used. It must be equal to one of the three 
  // values above.
  private int projChoice = 1;
  
  // If the projection type is RADIAL, then this variable indicates which of the two methods
  // to use. The cylinder used for a radial projection has either u1 or u2 as its axis.
  // A third possible choice would be to use a cylinder with axis equal to u1 x u2, but that
  // gives visually strange output because it maximizes the distortion near the center
  // of the window.
  private boolean axisU1 = true;
  
  // These values have the same meaning that they did in OutSphere.
  private double[] u1 = {0.0,1.0,0.0};
  private double[] u2 = {0.0,0.0,1.0};
  private double[] u1q = null;
  private double[] u2q = null;
  private boolean dirty = true;
  protected int width;
  protected int height;
  
  // These two values, vpd and vpTrim, are used for zooming in and out. The method used for
  // zooming in the parallel and perspective cases is different than the method used with 
  // radial projection. vpd is used for parallel and perspective projection; vpTrim is used
  // for radial projection.
  //
  // vpd stands for "ViewPort Distance". Under perspective projection, it is the 
  // distance of the viewport from the eye. Under parallel projection, the distance
  // of the plane from the eye is unimportant, so vpd is used as a magnification factor
  // when converting to/from screen coordinates. In either case, vpd must be between 
  // 0 and 1; the larger the value, the more magnification. 
  private double vpd = 0.01;
  
  // vpTrim is used for zooming in/out under radial projection. It should be in the range 
  // [0,pi/2). It's the amount to trim, in radians, from each end of a strip that goes
  // along the cylinder. What's left is projected to the screen. vpTrim stands for
  // "viewport trim".
  private double vpTrim = 0.00;
  
  
  public InSphere(int width,int height) {
    
    u1q = GUtil.duplicate(u1);
    u2q = GUtil.duplicate(u2);
    setSize(width,height);
  }
  
  synchronized public void makePerspectiveProjection() {
    this.projChoice = PERSPECTIVE;
    this.dirty = true;
  }
  
  synchronized public void makeParallelProjection() {
    this.projChoice = PARALLEL;
    this.dirty = true;
  }

  synchronized public void makeRadialProjection() {
    this.projChoice = RADIAL;
    this.dirty = true;
  }
  
  synchronized public void makeRadialAxisU1() {
    this.projChoice = RADIAL;
    this.axisU1 = true;
    this.dirty = true;
  }
  
  synchronized public void makeRadialAxisU2() {
    this.projChoice = RADIAL;
    this.axisU1 = false;
    this.dirty = true;
  }
  
  synchronized public void setSize(int width,int height) {
    
    // Call this whenever the size of the image changes.
    this.width = width;
    this.height = height;
    dirty = true;
    update();
  }
  
  private void screenToViewportParallel(int x,int z,double[] V) {

    // Convert screen coordinates to the 2-d coordinates within the plane.
    // This is the reverse of what was done near the end of sphereToScreen().
    double len = Math.sqrt(1.0 - vpd * vpd);
    double planeX = len * (2.0 * (double) x - (double) width) / (double) width;
    double planeZ = len * (2.0 * (double) (height - 1 - z) - (double) height) / 
                      (double) height;
    
    V[0] = planeX * u1[0] + planeZ * u2[0];
    V[1] = planeX * u1[1] + planeZ * u2[1];
    V[2] = planeX * u1[2] + planeZ * u2[2];
    
    // Offset the viewport by vpd. The viewport is a plane perpendicular to the line from 
    // the origin through u1 x u2. So the origin of this plane is at vpd * (u1 x u2).
    double[] planeOrigin = GUtil.times(GUtil.cross(u1,u2),vpd);
    GUtil.addTo(planeOrigin,V);
  }
  
  private void screenToViewportU1(int x,int z,double[] V) {
    
    // Convert screen coordinates to the 2-d coordinates within the plane. This is the
    // reverse of the tail-end of sphereToScreenU1().
    // BEWARE: There's a scaling issue in the radial case because the vertical coordinate is
    // really an angle. In particular, only the first two coordinates of V are set. The
    // viewport is not embedded in 3-space in a meaningful way.
    
    // This inverts the relations at the end of sphereToScreenU1(). Take the formulas
    // for answer[i] and solve them for r[i]. 
    V[0] = Math.PI * (0.5 - (double) z /(double)(height - 1));
    V[1] = 2.0 * (double) x / (double)(width -1) - 1;
    
    // This gives the "unscaled" values so that the results are in the range [-1,+1] 
    // and [-pi/2,+pi/2].
    double M = Math.PI / (Math.PI - 2.0 * vpTrim);
    V[0] /= M;
    V[1] /= M;
  }
  
  private void screenToViewportU2(int x,int z,double[] V) {
    
    // Similar to the u1 case.
    V[0] = Math.PI * ((double) x /(double)(width - 1) - 0.5);
    V[1] = 1.0 - 2.0 * (double) z / (double)(height -1);
    double M = Math.PI / (Math.PI - 2.0 * vpTrim);
    V[0] /= M;
    V[1] /= M;
  }
  
  private void screenToViewportPerspective(int x,int z,double[] V) {
    
    // This is the same as the parallel case. In both methods the plane of projection and 
    // the screen bear the same relationship. The viewport is the screen, as it is 
    // embedded in 3d space.
    screenToViewportParallel(x,z,V);
  }
  
  synchronized public void screenToViewport(int x,int z,double[] V) {
    
    // Similar to OutSphere.screenToViewport(). Given screen coordinates, (x,z), return the 
    // viewport coordinates in V. These are the coordinates of the point on the plane of 
    // projection. V must have been allocated by the caller.
    if (this.projChoice == PARALLEL)
      screenToViewportParallel(x,z,V);
    else if (projChoice == PERSPECTIVE)
      screenToViewportPerspective(x,z,V);
    else if (axisU1 == true)
      screenToViewportU1(x,z,V);
    else
      screenToViewportU2(x,z,V);
  }
  
  private boolean isSpherePointParallel(double[] V,double[] s) {
    
    // Return true iff V (a 3-d point on the plane of projection) is the
    // projection of a point on the sphere, in which case set s equal to the point
    // on the sphere. The only situation in which this will not return true is when
    // the user is zoomed out so far that an entire hemisphere is visible. In this
    // case the corners of the window are off the sphere.
    
    // s is equal to the intersection of the line L = V + <u1 x u2> with the sphere.
    // L is parameterized by L(t) = V + t(u1 x u2), and L(t) is on the sphere whenever
    // t satisfies |L(t)|^2 = 1. Set e = u1 x u2. To solve for t, observe that
    // |L(t)|^2 = 1
    // (V + t e) dot (V + t e) = 1
    // V dot V + 2t V dot e + t^2 e dot e = 1
    // V dot V + 2t V dot e + t^2 = 1
    // The last step follows because e dot e = 1. Apply the quadratic formula to 
    // find t. s is then equal to L(t).
    double[] e = GUtil.cross(u1,u2);
    double a = 1.0; // This is e dot e.
    double b = 2.0 * GUtil.dot(V,e);
    double c = GUtil.dot(V,V) - 1.0;
    double residue = b * b - 4.0 * a * c;
    if (residue < 0.0)
      // Not the projection of a point on the sphere.
      return false;
    
    // No need to consider the -b - sqrt() case because b must be positive.
    double t = (-b + Math.sqrt(residue)) / (2.0 * a);
    s[0] = V[0] + t * e[0];
    s[1] = V[1] + t * e[1];
    s[2] = V[2] + t * e[2];
    return true;
  }
  
  private boolean isSpherePointU1(double[] V,double[] s) {
    
    // First check whether the point is on the visible side of the sphere.
    // Any point with the x-coordinate in the range [-pi/2,pi/2] and the 
    // y-coordinate in the range [-1,+1] is the projection of some point on the sphere.
    // This returns true even if the point is not visible in the window due to zooming.
    if ((V[1] > 1.0) || (V[1] < -1.0))
      return false;
    if ((V[0] > Math.PI/2.0) || (V[0] < -Math.PI/2.0))
      return false;
    
    // Recover the decomposition of p into
    // p = (p dot u1) u1 + (p dot u2) u2 + (p dot (u1xu2)) (u1xu2).
    // V[1] = p dot u1 is known, and tan(V[0]) = (p dot u2)/(p dot(u1xu2)). 
    // Set alpha = p dot u2 and beta = p dot (u1xu2). Then 
    // p = V[1] u1 + alpha u2 + beta (u1xu2).
    // This can be solved for alpha and beta because p must have length 1.
    // 
    // |p|^2 = V[1]^2 + alpha^2 + beta^2 since u1, u2 and u1xu2 are orthonormal.
    // tan(V[0]) = alpha/beta, or alpha = beta tan(V[0]), and this makes
    // |p|^2 = 1 = V[1]^2 + beta^2 tan^2(V[0]) + beta^2. Solve this for beta:
    // beta = sqrt((1-V[1]^2)/(1+tan^2(V[0]))). Take the positive square root because that's
    // the visible side of the sphere. The result is p.
    double tan = Math.tan(V[0]);
    double beta = Math.sqrt((1.0 - V[1]*V[1]) / (1.0 + tan * tan));
    double alpha = beta * tan;
    double[] u1xu2 = GUtil.cross(u1,u2);
    double[] answer = GUtil.times(u1,V[1]);
    answer = GUtil.plus(answer,GUtil.times(u2,alpha));
    answer = GUtil.plus(answer,GUtil.times(u1xu2,beta));
    GUtil.copy(answer,s);
    return true;
  }
  
  private boolean isSpherePointU2(double[] V,double[] s) {
    
    // Similar to the u1 case.
    if ((V[1] > 1.0) || (V[1] < -1.0))
      return false;
    if ((V[0] > Math.PI/2.0) || (V[0] < -Math.PI/2.0))
      return false;
    
    double tan = Math.tan(V[0]);
    double beta = Math.sqrt((1.0 - V[1]*V[1]) / (1.0 + tan * tan));
    double alpha = beta * tan;
    double[] u1xu2 = GUtil.cross(u1,u2);
    double[] answer = GUtil.times(u1,alpha);
    answer = GUtil.plus(answer,GUtil.times(u2,V[1]));
    answer = GUtil.plus(answer,GUtil.times(u1xu2,beta));
    GUtil.copy(answer,s);
    return true;
  }
  
  private boolean isSpherePointPerspective(double[] V,double[] s) {
    
    // This is easy with perspective projection -- although V should be on the plane of 
    // projection, it could be any point along the line from the eye at (0,0,0) through
    // a point on the plane of projection. The desired point is then s = V/|V| since 
    // the sphere is assumed to have radius 1.
    double t = 1.0 / GUtil.length(V);
    GUtil.copy(GUtil.times(V,t),s);
    
    // See if the eye points in the same direction as s. In other words, is this point on 
    // the hemisphere to which the eye points? This will still return true even when the 
    // user has zoomed in so that the point is pushed off the edge of the screen.
    double[] eye = GUtil.cross(u1,u2);
    if (GUtil.dot(eye,s) > 0.0)
      return true;
    else
      return false;
  }
  
  synchronized public boolean isSpherePoint(double[] V,double[] s) {
    
    // Return true iff V (a point on the plane of projection) is the projection of a point
    // on the sphere. For parallel and perspective projection, V is a 3-d point; for radial
    // projection, it's a 2-d point on the "plane" given by half a cylinder that's been
    // flattened out. s must have been allocated by the caller.
    if (this.projChoice == PARALLEL)
      return isSpherePointParallel(V,s);
    else if (this.projChoice == PERSPECTIVE)
      return isSpherePointPerspective(V,s);
    else if (axisU1 == true)
      return isSpherePointU1(V,s);
    else
      return isSpherePointU2(V,s);
  }
  
  private int[] sphereToScreenParallel(double[] p) {
    
    // The point on the screen to which p, a point on the sphere, projects could fail 
    // to be visible for two reasons: it's on the side of the sphere behind the eye,
    // or it's off the edge of the visible rectangle.
    // 
    // The plane of projection is <u1,u2> + v, where v = u1 x u2. According to Chapter 2, 
    // the projection of the point p to the plane is given by the coordinates
    // alpha_i = (p-v) dot u_i.
    // v is perpendicular to u1 and u2, so v dot u1 = v dot u2 = 0, and the equation can be 
    // simplified to alpha_i = p dot u_i.
    
    // Check if on the visible side.
    double[] v = GUtil.cross(u1,u2);
    if (GUtil.dot(p,v) < 0.0)
      return null;
    
    // Get coordinates of the projection in the same coordinate space as the sphere.
    double[] alpha = new double[2];
    alpha[0] = GUtil.dot(p,u1);
    alpha[1] = GUtil.dot(p,u2);
    
    // Scale to the screen in accord with width, height and vpd. When vpd = 0, an entire 
    // hemisphere is barely visible. When vpd = 1, then only one point should be visible.
    // The alpha[i] will range over [-1,1] since they must be points somewhere
    // within the sphere.
    //
    // Think of a circle and a vertical chord at distance d (= vpd) from the origin. Then 
    // the upper half of the chord has length L = sqrt(R^2 - d^2), where R is the radius of 
    // the sphere. The interval [-L,+L] is mapped to [0,width] or to [0,height].
    double len = Math.sqrt(1.0 - vpd * vpd);
    int[] answer = new int[2];
    answer[0] = (int) ((alpha[0] + len) * width / (2.0 * len));
    answer[1] = (int) ((alpha[1] + len) * height / (2.0 * len));
    
    // Adjust the vertical coordinate since the vertical orientation of the monitor
    // is the opposite of what's usual.
    answer[1] = height - 1 - answer[1];
    
    if ((answer[0] < 0) || (answer[0] >= width))
      return null;
    if ((answer[1] < 0) || (answer[1] >= height))
      return null;
    return answer;
  }

  private int[] sphereToScreenU2(double[] p) {
    
    // The vector p can be written as
    // p = (p dot u1) u1 + (p dot u2) u2 + (p dot (u1 x u2)) (u1 x u2).
    // The vertical coordinate is just p dot u2 and the angle of the horizontal coordinate 
    // is given by tan(alpha) = p dot u1 / (p dot (u1 x u2)).
    
    // Check if the point is visible.
    double[] u1xu2 = GUtil.cross(u1,u2);
    if (GUtil.dot(p,u1xu2) < 0.0)
      // p is a point behind the eye.
      return null;
      
    // Get the coordinates on the flattened half of the cylinder. This makes r[1] (the
    // vertical coordinate) a value in [-1,+1] and r[0] (the horizontal coordinate) a value
    // in [-pi/2,+pi/2].
    double[] r = new double[2];
    r[1] = GUtil.dot(p,u2);
    r[0] = Math.atan(GUtil.dot(p,u1)/GUtil.dot(p,u1xu2));
    
    // Scale according to vpTrim, which is the angle to trim from the sides of the visible 
    // cylinder. This determines the stretch ratio in the horizontal direction. Use the same
    // ratio in the vertical direction.
    double M = Math.PI / (Math.PI - 2.0 * vpTrim);
    r[0] *= M;
    r[1] *= M;
    
    // Scale r to fit on the screen. r[0] is in the range M [-pi/2,+pi/2], and we want a
    // linear map that takes [-pi/2,pi/2] to [0,width-1]. r[1] is in the range 
    // M [-1,+1] and to map [-1,+1] to [0,height-1].
    int[] answer = new int[2];
    answer[0] = (int) ((double)(width-1) * (r[0] + Math.PI / 2.0)/Math.PI);
    answer[1] = (int) ((double)(height-1)*(r[1]+1.0)/2.0);
    
    // Adjust the vertical coordinate since the vertical orientation of the monitor
    // is the opposite of what's usual.
    answer[1] = height - 1 - answer[1];
    
    // Return null if this point is off the edge of the screen.
    if ((answer[0] < 0) || (answer[0] >= width))
      return null;
    if ((answer[1] < 0) || (answer[1] >= height))
      return null;
    
    return answer;
  }
  
  private int[] sphereToScreenU1(double[] p) {
    
    // This is similar to the U2 case, but with u1 and u2 interchanged.
    
    // Check if the point is visible.
    double[] u1xu2 = GUtil.cross(u1,u2);
    if (GUtil.dot(p,u1xu2) < 0.0)
      // p is a point behind the eye.
      return null;
        
    // Get the coordinates on the flattened half of the cylinder.
    double[] r = new double[2];
    r[1] = GUtil.dot(p,u1);
    r[0] = Math.atan(GUtil.dot(p,u2)/GUtil.dot(p,u1xu2));
    
    // Scale according to vpTrim.
    double M = Math.PI / (Math.PI - 2.0 * vpTrim);
    r[0] *= M;
    r[1] *= M;
    
    // Scale r to fit on the screen.
    int[] answer = new int[2];
    answer[1] = (int) ((double)(height-1) * (r[0] + Math.PI / 2.0)/Math.PI);
    answer[0] = (int) ((double)(width-1)*(r[1]+1.0)/2.0);
    
    // Adjust the vertical coordinate.
    answer[1] = height - 1 - answer[1];
    
    // Return null if this point is off the edge of the screen.
    if ((answer[0] < 0) || (answer[0] >= width))
      return null;
    if ((answer[1] < 0) || (answer[1] >= height))
      return null;
    
    return answer;
  }
  
  private int[] sphereToScreenPerspective(double[] p) {
    
    // Similar to what was done in OutSphere.sphereToScreen(). The plane of projection is 
    // <u1,u2> + (1.0 - vpd) * u1 x u2. The eye is at (0,0,0). The equations for alpha_1 and
    // alpha_2 in chapter 2 say that
    // alpha_1 = -v dot (u2 x p) / u1 dot (u2 x p),
    // where v = vpd * (u1 x u2), as E = (0,0,0). And
    // alpha_2 = -v dot (u1 x p) / u2 dot (u1 x p)
    double[] u1xu2 = GUtil.cross(u1,u2);
    
    // Make sure that p is on the visible side of the sphere.
    if (GUtil.dot(p,u1xu2) < 0.0)
      return null;
    
    double[] v = GUtil.times(u1xu2,vpd);
    double[] u1xp = GUtil.cross(u1,p);
    double[] u2xp = GUtil.cross(u2,p);
    double[] alpha = new double[2];
    alpha[0] = -GUtil.dot(v,u2xp) / GUtil.dot(u1,u2xp); 
    alpha[1] = -GUtil.dot(v,u1xp) / GUtil.dot(u2,u1xp);
    
    // Convert to screen coordinates. The logic is the same as in the parallel projection
    // case because the plane bears the same relation to the sphere in both cases.
    double len = Math.sqrt(1.0 - vpd * vpd);
    int[] answer = new int[2];
    answer[0] = (int) ((alpha[0] + len) * width / (2.0 * len));
    answer[1] = (int) ((alpha[1] + len) * height / (2.0 * len));

    // Adjust the vertical coordinate since the vertical orientation of the monitor
    // is the opposite of what's usual.
    answer[1] = height - 1 - answer[1];
    
    if ((answer[0] < 0) || (answer[0] >= width))
      return null;
    if ((answer[1] < 0) || (answer[1] >= height))
      return null;
    
    return answer;
  }
  
  synchronized public int[] sphereToScreen(double[] p) {
    
    // Project p, which should be a point on the sphere, to screen coordinates.
    // answer[0] will be the x coordinate and answer[1] will be the y coordinate.
    // If this point would not be visible, then return null. 
    if (this.projChoice == PARALLEL)
      return sphereToScreenParallel(p);
    else if (projChoice == PERSPECTIVE)
      return sphereToScreenPerspective(p);
    else if (axisU1 == true)
      return sphereToScreenU1(p);
    else
      return sphereToScreenU2(p);
  }
  
  synchronized public double[] getEyeDirection() {
    return GUtil.cross(u1,u2);
  }
  
  synchronized public int getWidth() {
    return this.width;
  }
  
  synchronized public int getHeight() {
    return this.height;
  }
  
  synchronized public void requestRotEyeUp(double angle) {
    u2q = GUtil.rotate3(angle,u1q,u2q);
    dirty = true;
  }
  
  synchronized public void requestRotEyeRight(double angle) {
    u1q = GUtil.rotate3(angle,u2q,u1q);
    dirty = true;
  }

  synchronized public void requestTwistEye(double angle) {
    
    // Twist the orientation of the eye clock-wise.
    double[] eyeVector = GUtil.cross(u2q,u1q);
    u2q = GUtil.rotate3(angle,eyeVector,u2q);
    u1q = GUtil.rotate3(angle,eyeVector,u1q);
    dirty = true;
  }
  
  synchronized public void zoomIn() {
    
    vpd += 0.01;
    if (vpd > 0.99)
      vpd = 0.99;
    vpTrim += 0.01;
    if (vpTrim > Math.PI/2.0 - 0.02)
      vpTrim = Math.PI/2.0 - 0.02;
    
    dirty = true;
  }
  
  synchronized public void zoomOut() {
    
    vpd -= 0.01;
    if (vpd < 0.01)
      vpd = 0.01;
    vpTrim -= 0.01;
    if (vpTrim < 0.0)
      vpTrim = 0.0;
    dirty = true;
  }
  
  synchronized public void implementRequests() {
    
    // Make sure that any requested changes are implemented.
    u1 = GUtil.duplicate(u1q);
    u2 = GUtil.duplicate(u2q);
  }
  
  synchronized public boolean update() {
    
    // Change the geometrically important values to reflect any change requests.
    if (dirty == false)
      return false;
    implementRequests();
    dirty = false;
    return true;
  }
}