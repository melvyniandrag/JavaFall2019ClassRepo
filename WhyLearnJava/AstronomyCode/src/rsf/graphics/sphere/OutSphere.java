package rsf.graphics.sphere;

// A class that can draw a shaded sphere. This is something like 
// rsf.graphics.sphere.intro.MobileSphere, but without the user-interface aspects. Unlike
// MobileSphere, this class is entirely self-contained -- it does not extend or implement
// any other class.

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import rsf.graphics.GUtil;


public class OutSphere {
  
  // This will hold an image of the sphere.
  protected BufferedImage image = null;
  
  // Making these private/protected is a bit of a nuisance, but we want to make
  // sure that these values are not changed when they shouldn't be.

  // Sphere with default radius one, centered at origin.
  private double sphereR = 1.0;
  protected double[] center = {0.0,0.0,0.0};
  private double[] centerq = null;
  
  // Distance of eye from the origin.
  private double eyeDistance = 10.0;
  
  // The location of the viewport between the eye and the origin. The larger this value,
  // the closer the viewport is to the eye and the smaller the sphere will appear to be.
  // This should be between 0.0 and 1.0.
  private double viewportDistance = 0.50;
  
  // These two perpendicular vectors determine the eye's location, and u2 x u1 points to
  // the eye. The observer is assumed to look toward the origin.
  // u1 is the x-axis of the viewport and u2 is the y-axis.
  private double[] u1 = {0.0,1.0,0.0};
  private double[] u2 = {0.0,0.0,1.0};
  
  // These are u1 and u2 values that have been *requested* but not yet applied to image.
  private double[] u1q = null;
  private double[] u2q = null;
  
  // Whether the sphere needs to be redrawn due to requested changes.
  private boolean dirty = true;
  
  // Eye coordinates (E), determined as eyeDistance (u2 x u1). This is recalculated
  // as needed. It's here to avoid having to pass it as an argument to various methods.
  private double[] eye = null;
  
  // This points from the origin to the plane of the viewport. Should be equal to
  // eye * viewportDistance. 
  private double[] vpVector = null;
  
  // Light coordinates (B).
  private double[] light = {0.0,20.0,-5.0};
  
  // New, requested light location.
  private double[] lightq = null;
  
  // These are used by the shading model in shadeIntensity().
  // The balance between ambient, diffuse and specular light and the specularity that
  // give the best appearance may depend on the particular monitor.
  public float ambient = 0.10f;
  public double specularity = 10.0;
  public double specularFraction = 0.40;
  
  // The width and height of the sphere's image, in pixels. Adjusted by setSize().
  protected int width;
  protected int height;
  
  // The size of the sphere's image in the same scale as the eye and light.
  // There's an interplay between portWidth, eyeDistance and viewportDistance.
  protected double portWidth = 1.5;
  protected double portHeight;
  
  // Used for shading the sphere in a particular color.
  private float redMax = 1.0f;
  private float greenMax = 1.0f;
  private float blueMax = 1.0f;
  
  // Occasionally, all of the geometric machinery that goes with this class is needed,
  // but without actually drawing the sphere. When noDraw is true, the sphere
  // isn't drawn, although everything else still works.
  private boolean noDraw = false;
  
  
  public OutSphere(int width,int height) {
    
    initQVariables();
    resize(width,height);
  }
  
  public OutSphere(int width,int height,double radius,double[] light,Color baseColor,
                   float ambient,double specularity,double specFraction) {
    
    // This constructor gives greater control over the shading parameters, and
    // the size of the sphere.
    sphereR = radius;
    this.light = GUtil.duplicate(light);
    
    float[] colorParts = baseColor.getRGBColorComponents(null);
    redMax = colorParts[0];
    greenMax = colorParts[1];
    blueMax = colorParts[2];
    
    initQVariables();
    resize(width,height);
    
    this.ambient = ambient;
    this.specularity = specularity;
    this.specularFraction = specFraction;
  }
  
  public OutSphere(int width,int height,boolean noDraw) {
    
    // Use this constructor (with noDraw = true) when you want a sphere that's
    // never shaded. This is useful because the class can still be used to keep
    // track of the eye, etc., but the sphere itself is not drawn. 
    // Calling with noDraw = false, then it's the same as the first constructor.
    this.noDraw = noDraw;
    initQVariables();
    resize(width,height);
  }
  
  private void initQVariables() {
    
    u1q = GUtil.duplicate(u1);
    u2q = GUtil.duplicate(u2);
    lightq = GUtil.duplicate(light);
    centerq = GUtil.duplicate(center);
  }
  
  synchronized public void resize(int width,int height) {
    
    // Call this whenever the size of the image changes.
    this.width = width;
    this.height = height;
    this.portHeight = portWidth * (double) height/ (double) width;
    
    // Allocate a new buffer to hold the sphere's image.
    this.image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    
    dirty = true;
    update();
  }
  
  synchronized public void screenToViewport(int x,int z,double[] V) {
    
    // Given screen coordinates, (x,z), return the viewport coordinates in V.
    // Notice the adjustment to z that must be made since the orientation of the
    // y-axis on the monitor is the reverse of the usual orientation.
    double planeX = ((double) x / (double) width - 0.5) * portWidth;
    double planeZ = ((double) (height - 1 - z) / (double) height - 0.5) * portHeight;
    
    V[0] = planeX * u1[0] + planeZ * u2[0];
    V[1] = planeX * u1[1] + planeZ * u2[1];
    V[2] = planeX * u1[2] + planeZ * u2[2];
    
    // Offset the viewport by the eye. The viewport is a plane perpendicular
    // to the line from eye to origin.
    GUtil.addTo(vpVector,V);
  }
  
  synchronized public boolean isSpherePoint(double[] V,double[] s) {
   
    // Return true iff the point, V, assumed to lie on the plane of projection, is the 
    // projection of a point on the sphere. If V is such a point, then s is set equal to 
    // the coordinates of that point (i.e., the coordinates of a point on the sphere).
    // s must have been allocated by the caller.

    // See if the line L(t) = (1-t)E + t V intersects the sphere. 
    // NOTE: This could be expressed more briefly using vector notation:
    // a = |eye - V|^2, etc.
    double a = (eye[0] - V[0]) * (eye[0] - V[0]) +
               (eye[1] - V[1]) * (eye[1] - V[1]) +
               (eye[2] - V[2]) * (eye[2] - V[2]);
    double b = 2.0 * (eye[0] - center[0]) * (V[0] - eye[0]) +
               2.0 * (eye[1] - center[1]) * (V[1] - eye[1]) +
               2.0 * (eye[2] - center[2]) * (V[2] - eye[2]);
    double c = Math.pow(GUtil.length(GUtil.minus(eye,center)),2.0)
               - sphereR * sphereR;
    double residue = b * b - 4.0 * a * c;
    
    if (residue < 0.0)
      // Not the projection of a point on the sphere.
      return false;
    
    // Got here, so V is the projection of a point on the Sphere. Determine s. This point
    // is given by taking the smaller (closer) of the two t values and plugging it into
    // L(t) = (1-t)E + t V.
    double t1 = (-b + Math.sqrt(residue)) / (2.0 * a);
    double t2 = (-b - Math.sqrt(residue)) / (2.0 * a);
    double t = t1;
    if (t2 < t) t = t2;
    
    s[0] = eye[0] * (1.0 - t) + V[0] * t;
    s[1] = eye[1] * (1.0 - t) + V[1] * t;
    s[2] = eye[2] * (1.0 - t) + V[2] * t;
    
    return true;
  }
  
  synchronized public boolean isRearSpherePoint(double[] V,double[] s) {
    
    // Very much like SpherePane.isSpherePoint(), but it returns true when a point is on
    // the sphere, but is NOT visible to the eye. That is, it's on the back of the sphere.
    double a = (eye[0] - V[0]) * (eye[0] - V[0]) +
               (eye[1] - V[1]) * (eye[1] - V[1]) +
               (eye[2] - V[2]) * (eye[2] - V[2]);
    double b = 2.0 * (eye[0] - center[0]) * (V[0] - eye[0]) +
               2.0 * (eye[1] - center[1]) * (V[1] - eye[1]) +
               2.0 * (eye[2] - center[2]) * (V[2] - eye[2]);
    double c = Math.pow(GUtil.length(GUtil.minus(eye,center)),2.0)
               - sphereR * sphereR;
    double residue = b * b - 4.0 * a * c;
   
    if (residue < 0.0)
      return false;
   
    // Take the LARGER (further) of the two t values and plug it into 
    // L(t) = (1-t)E + t V. This is where it differs from isSpherePoint().
    double t1 = (-b + Math.sqrt(residue)) / (2.0 * a);
    double t2 = (-b - Math.sqrt(residue)) / (2.0 * a);
    double t = t1;
    if (t2 > t) t = t2;
   
    s[0] = eye[0] * (1.0 - t) + V[0] * t;
    s[1] = eye[1] * (1.0 - t) + V[1] * t;
    s[2] = eye[2] * (1.0 - t) + V[2] * t;
   
    return true;
  }

  synchronized public int[] sphereToScreen(double[] p) {
    
    // Project p, which should be a point on the sphere, to screen coordinates.
    // answer[0] will be the x coordinate and answer[1] will be the y coordinate.
    
    // The plane of projection is <u1,u2> + v.
    double[] v = vpVector;
    double[] eyeTon = GUtil.minus(eye,v);
    double[] pToEye = GUtil.minus(p,eye);
    double[] u2cross = GUtil.cross(u2,pToEye);
    double[] u1cross = GUtil.cross(u1,pToEye);
    double alpha1 = GUtil.dot(eyeTon,u2cross) / GUtil.dot(u1,u2cross);
    double alpha2 = GUtil.dot(eyeTon,u1cross) / GUtil.dot(u2,u1cross);
    
    // Convert (alpha1,alpha2), which is relative to the plane of the viewport, to 
    // screen coordinates.
    int[] answer = new int[2];
    answer[0] = (int)((alpha1 / portWidth + 0.5) * (double) width);
    answer[1] = (int)((alpha2 / portHeight + 0.5) * (double) height);
    
    // Adjust the vertical coordinate since the vertical orientation of the monitor
    // is the opposite of what's usual.
    answer[1] = height - 1 - answer[1];
    
    return answer;
  }

  synchronized public boolean isFrontPoint(double[] p) {
      
    // Return true iff p is a point on the visible side of the sphere. p is assumed
    // to be a point somewhere on the sphere.
    double dp = GUtil.dot(p,eye);
    if (dp > 0.0)
      return true;
    else
      return false;
  }
  
  synchronized public float shadeIntensity(double[] n,double[] p) {
    
    // Return the intensity of shading, in [0,1], for the given point on the surface, p, and 
    // the normal vector, n. This will work for any surface, not just a sphere.
    double[] L = GUtil.minus(light,p);
    float diffuse = (float)(GUtil.dot(L,n) / (GUtil.length(L) * GUtil.length(n)));
    
    if (diffuse < 0.0) 
      // The point is on the dark side of the sphere. Just show ambient light.
      return ambient;
    else
      {
        // Determine the amount of specular reflection.
        double[] R = GUtil.rotate3(Math.PI,n,light);
        
        double[] eyeVector = GUtil.minus(eye,p);
        double[] reflectVector = GUtil.minus(R,p);
        double dotprod = GUtil.dot(eyeVector,reflectVector);
        float specular = 0.0f;
        if (dotprod > 0)
          specular = (float)(Math.pow(dotprod / 
              (GUtil.length(eyeVector) * GUtil.length(reflectVector)),specularity));
        
        // Combine the three sources of light.
        float intensity = (float) (ambient + (1.0 - ambient) * 
            ((1.0 - specularFraction) * diffuse + specularFraction * specular));
        return intensity;
      }
  }
  
  synchronized public void drawSphere() {
    
    // Draw the sphere to this.image. It is assumed that the dimensions of image match 
    // this.width and this.height.
    drawSphere(image);
  }
  
  synchronized public void addSphere(BufferedImage destImage) {
    
    // Draw the sphere to destImage, *without* drawing anything that's not part of the
    // sphere. The background is left alone so the sphere is "pasted" onto destImage.
    // It is assumed that destImage has the correct height and width.
    //
    // This is more clever than previous versions. Its scans a rectangle that's no bigger
    // than necessary. The rectangle is large enough to enclose the sphere but no larger.
    // The easiest way to do is to scan along from the top center down till you hit the 
    // sphere, then scan along from the left center till you hit the sphere. This could be
    // calculated algebraically, but it's easier to let the computer do it, and it will be
    // relatively fast since it's only scanning one row and one column.
    
    // First, make sure that this sphere is to be drawn at all.
    if (this.noDraw == true)
      return;
    
    int xStart = 0;
    int xEnd = width;
    int zStart = 0;
    int zEnd = 0;
    
    double[] V = new double[3];
    double[] s = new double[3];
    
    // Determine the x-range.
    int z = height / 2;
    for (int x = 0; x < width; x++)
      {
        screenToViewport(x,z,V);
        if (isSpherePoint(V,s))
          {
            // Hit the sphere. This tells us xStart and xEnd.
            xStart = x - 1;
            xEnd = width - x + 2;
            if (xStart < 0) xStart = 0;
            if (xEnd >= width) xEnd = width;
            break;
          }
      }
    
    // Same deal to determine the z-range.
    int x = width / 2;
    for (z = 0; z < height; z++)
      {
        screenToViewport(x,z,V);
        if (isSpherePoint(V,s))
          {
            zStart = z - 1;
            zEnd = height - z + 2;
            if (zStart < 0) zStart = 0;
            if (zEnd > height) zEnd = height;
            break;
          }
      }
    
    // Loop over each pixel and determine its shade.
    for (x = xStart; x < xEnd; x++)
      {
        for (z = zStart; z < zEnd; z++)
          {
            // Convert (x,z) in screen coordinates to viewport coordinates.
            screenToViewport(x,z,V);
            
            // See whether this is a point on the sphere or the background.
            if (isSpherePoint(V,s))
              {
                // On the sphere. Get the surface normal at this point.
                double[] n = GUtil.minus(s,center);
                
                // Determine intensity of shading and set the pixel.
                float intensity = shadeIntensity(n,s);
                int rgb = (new Color(intensity*redMax,intensity*greenMax,
                    intensity*blueMax)).getRGB();
                destImage.setRGB(x,z,rgb);
              }
          }
      }
  }
  
  private int diskRadius(double r) {

    // Return the maximum possible number of pixels from the center of a disk of 
    // radius r on the sphere to the edge of the disk. This radius is expressed
    // as the arc-length of the disk (a better name would be cap since the "disk"
    // is an object on the surface of a sphere).
        
    // This is where the sphere intersects the x-axis.
    double[] p = GUtil.plus(center,new double[] {sphereR,0.0,0.0});
    
    // Determine the equivalent value for vpVector (which is set to v in sphereToScreen).
    // This is as in implementRequests().
    double[] u1 = {0.0,1.0,0.0};
    double[] u2 = {0.0,0.0,1.0};
    double[] eye = {1.0,0.0,1.0};
    eye = GUtil.times(eye,eyeDistance);
    double[] v = GUtil.times(eye,viewportDistance);
    
    // Now exactly as in sphereToScreen().
    double[] eyeTon = GUtil.minus(eye,v);
    double[] pToEye = GUtil.minus(p,eye);
    double[] u2cross = GUtil.cross(u2,pToEye);
    double[] u1cross = GUtil.cross(u1,pToEye);
    double alpha1 = GUtil.dot(eyeTon,u2cross) / GUtil.dot(u1,u2cross);
    double alpha2 = GUtil.dot(eyeTon,u1cross) / GUtil.dot(u2,u1cross);
    
    // Convert (alpha1,alpha2), which is relative to the plane of the viewport, to 
    // screen coordinates. Unlike in sphereToScreen(), this is not cast to ints.
    double[] m0 = new double[2];
    m0[0] = (alpha1 / portWidth + 0.5) * (double) width;
    m0[1] = (alpha2 / portHeight + 0.5) * (double) height;
    
    // Shift p=s_0 off to the side by arc-length r, and project that point to the
    // screen too. Strictly speaking, this isn't right either since this is vertical
    // distance and addDisk() sets pixels based on direct distance. But this
    // will give a slightly larger value, so it's ok.
    p[0] = sphereR * Math.sqrt(1.0 - (r/sphereR)*(r/sphereR));
    p[1] = r; 
    
    // Exactly as above.
    pToEye = GUtil.minus(p,eye);
    u2cross = GUtil.cross(u2,pToEye);
    u1cross = GUtil.cross(u1,pToEye);
    alpha1 = GUtil.dot(eyeTon,u2cross) / GUtil.dot(u1,u2cross);
    alpha2 = GUtil.dot(eyeTon,u1cross) / GUtil.dot(u2,u1cross);
    
    double[] m1 = new double[2];
    m1[0] = (alpha1 / portWidth + 0.5) * (double) width;
    m1[1] = (alpha2 / portHeight + 0.5) * (double) height;
    
    // Get distance from m0 to m1. This is in pixels. Adding 1 because of
    // occasional rounding error.
    return (int) GUtil.length(GUtil.minus(m0,m1))+1;
  }
  
  synchronized public void addDisk(BufferedImage destImage,double[] p,double r,Color c) {
    
    // Add a circular disk to destImage. The disk will be centered at p, where p is
    // assumed to be the rectangular coordinates of a point that is on the sphere.
    // The radius of the disk will be r, and color c. The radius is given in terms of
    // the 3-d coordinate system of the sphere. It is assumed that destImage is
    // consistent with this OutSphere object. Most likely it will be a copy of
    // this.image, possibly after drawing some extra stuff.
    
    // Convert to screen coordinates.
    int[] m = sphereToScreen(p);
    
    int width = destImage.getWidth();
    int height = destImage.getHeight();
    
    // Determine the rectangle inside of destImage to scan.
    int pixelRad = diskRadius(r);
    int lowx = m[0] - pixelRad; if (lowx < 0) lowx = 0;
    int hix = m[0] + pixelRad; if (hix >= width) hix = width - 1;
    int lowy = m[1] - pixelRad; if (lowy < 0) lowy = 0;
    int hiy = m[1] + pixelRad; if (hiy >= height) hiy = height - 1;
    
    // Scan the pixels.
    double[] V = new double[3];
    double[] s = new double[3];
    for (int x = lowx; x <= hix; x++)
      {
        for (int z = lowy; z <= hiy; z++)
          {
            screenToViewport(x,z,V);
            
            // See whether this is a point on the sphere or the background.
            if (isSpherePoint(V,s))
              {
                double dist = GUtil.length(GUtil.minus(p,s));
                if (dist < r)
                  // s is close to p, and it's on front side.
                  destImage.setRGB(x,z,c.getRGB());
              }
          }
      }
  }
  
  synchronized public void drawSphere(BufferedImage destImage) {
    
    // Draw the sphere *and* erase the background. This is the normal behavior.
    Graphics g = destImage.getGraphics();
    g.clearRect(0,0,width,height);
    addSphere(destImage);
  }
  
  synchronized public BufferedImage getImage() {
    return image; 
  }

  synchronized public double getRadius() {
    return sphereR;
  }
  
  synchronized public double[] getEyeDirection() {
    return GUtil.duplicate(eye);
  }
  
  synchronized public double[] getCenter() {
    return GUtil.duplicate(center);
  }
  
  synchronized public int getWidth() {
    return this.width;
  }
  
  synchronized public int getHeight() {
    return this.height;
  }
  
  synchronized public void requestCenterOffset(double[] offset) {
    centerq = GUtil.plus(centerq,offset);
    dirty = true;
  }
  
  synchronized public void requestRotEyeUp(double angle) {
    u2q = GUtil.rotate3(-angle,u1q,u2q);
    dirty = true;
  }
  
  synchronized public void requestRotEyeRight(double angle) {
    u1q = GUtil.rotate3(-angle,u2q,u1q);
    dirty = true;
  }

  synchronized public void requestTwistEye(double angle) {
    
    // Twist the orientation of the eye clock-wise.
    double[] eyeVector = GUtil.cross(u2q,u1q);
    u2q = GUtil.rotate3(angle,eyeVector,u2q);
    u1q = GUtil.rotate3(angle,eyeVector,u1q);
    dirty = true;
  }
  
  synchronized public void requestLight(double[] newlight) {
    this.lightq = GUtil.duplicate(newlight);
    dirty = true;
  }
  
  synchronized public void implementRequests() {
    
    // Make sure that any requested changes are implemented.
    u1 = GUtil.duplicate(u1q);
    u2 = GUtil.duplicate(u2q);
    this.light = GUtil.duplicate(this.lightq);
    center = GUtil.duplicate(centerq);
    
    eye = GUtil.cross(u2,u1);
    eye = GUtil.times(eye,eyeDistance);
    vpVector = GUtil.times(eye,viewportDistance);
  }
  
  synchronized public boolean update() {
    
    // Change the geometrically important values to reflect any change requests,
    // then update image.
    if (dirty == false)
      return false;
    
    implementRequests();
    
    // Draw the sphere to this.image.
    drawSphere();
    
    dirty = false;
    return true;
  }
  
  synchronized public void update(BufferedImage destImage) {
    
    // Draw to destImage, without drawing any of the background pixels.
    if (dirty == false)
      return;
    
    implementRequests();
    addSphere(destImage);
    
    dirty = false;
  }
}