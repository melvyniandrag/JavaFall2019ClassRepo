package rsf.graphics.sphere.intro;

// Shows a shaded sphere with one light source and the observer at a fixed position.

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import rsf.graphics.GUtil;
import rsf.ui.MyPanel;
import rsf.ui.SimpleWindow;


public class SpherePane extends MyPanel {

  // Sphere of radius one implicitly assumed to be at the origin.
  protected static double sphereR = 1.0;
  
  // Eye coordinates (E).
  public double[] eye = {0.0,-10.0,0.0};
  
  // Light coordinates (B).
  public double[] light = {20.0,-10.0,0.0};
  
  // These are used by the shading model in shadeIntensity().
  // The balance between ambient, diffuse and specular light and the specularity that
  // give the best appearance may depend on the particular monitor.
  public static float Ambient = 0.10f;
  public static double Specularity = 10.0;
  public static double SpecularFraction = 0.40;
  
  // These are declared here so that they can be used by screenToViewport()
  // without passing them as arguments.
  protected int width;
  protected int height;
  protected double portWidth = 3.0;
  protected double portHeight;
  
  
  public boolean isSpherePoint(double[] V,double[] s) {
    
    // Return true iff the point, V, assumed to lie on the plane of projection, is the 
    // projection of a point on the sphere. If V is such a point, then s is set to be 
    // the coordinates of that point. s must have been allocated by the caller.

    // See if the line L(t) = (1-t)E + t V intersects the sphere. 
    double a = (eye[0] - V[0]) * (eye[0] - V[0]) +
               (eye[1] - V[1]) * (eye[1] - V[1]) +
               (eye[2] - V[2]) * (eye[2] - V[2]);
    double b = 2.0 * eye[0] * (V[0] - eye[0]) +
               2.0 * eye[1] * (V[1] - eye[1]) +
               2.0 * eye[2] * (V[2] - eye[2]);
    double c = eye[0] * eye[0] + eye[1] * eye[1] + eye[2] * eye[2]
               - sphereR * sphereR;
    double residue = b * b - 4.0 * a * c;
    
    if (residue < 0.0)
      // Not the projection of a point on the sphere.
      return false;
    
    // Got here, so V is the projection of a point on the Sphere.
    // Determine s. This point is given by taking the smaller (closer) of 
    // the two t values and plugging it into L(t) = (1-t)E + t V.
    double t1 = (-b + Math.sqrt(residue)) / (2.0 * a);
    double t2 = (-b - Math.sqrt(residue)) / (2.0 * a);
    double t = t1;
    if (t2 < t) t = t2;
    
    s[0] = eye[0] * (1.0 - t) + V[0] * t;
    s[1] = eye[1] * (1.0 - t) + V[1] * t;
    s[2] = eye[2] * (1.0 - t) + V[2] * t;
    
    return true;
  }
  
  public float shadeIntensity(double[] n,double[] p) {
    
    // Return the intensity of shading, in [0,1], for the given point on the surface, p,
    // and the normal vector, n. This will work for any surface, not just a sphere.
    double[] L = GUtil.minus(light,p);
    float diffuse = (float)(GUtil.dot(L,n) / 
                (GUtil.length(L) * GUtil.length(n)));
    
    if (diffuse < 0.0) 
      // The point is on the dark side of the sphere. Just show ambient light.
      return Ambient;
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
              (GUtil.length(eyeVector) * GUtil.length(reflectVector)),
              Specularity));
        
        // Combine the three sources of light.
        float intensity = (float) (Ambient + (1.0 - Ambient) * 
            ((1.0 - SpecularFraction) * diffuse + SpecularFraction * specular));
        return intensity;
      }
  }
  
  public void screenToViewport(int x,int z,double[] V) {
    
    // Given screen coordinates, (x,z), return the viewport coordinates in V.
    V[0] = ((double) x / (double) width - 0.5) * portWidth;
    V[2] = ((double) z / (double) height - 0.5) * portHeight;
  }
  
  public void drawSphere(BufferedImage image) {
    
    // Draw the sphere to the given BufferedImage. It is assumed that the dimensions
    // of image match this.width and this.height.
    
    // Viewport is a plane at y = constant.
    double[] V = new double[3];
    V[1] = -1.0;
    
    // Will be a point on the surface of the sphere. Allocated here for speed (rather
    // than repeatedly reallocating it).
    double[] s = new double[3];
    
    // The screen coordinates do not match the spatial coordinates. Specify the 
    // viewport's dimensions in the same scale as the eye, sphere, etc.
    this.portHeight = portWidth * (double) height/ (double) width;
    
    // Loop over each pixel and determine its shade.
    // Coordinates are set up so that z increases as you go down on the screen, x 
    // increases as you move right and y increases as you move into the screen. This 
    // is a little unnatural, but it matches the computer's coordinate system. The 
    // center of the screen should be at x=0, z=0.
    for (int x = 0; x < width; x++)
      {
        for (int z = 0; z < height; z++)
          {
            // Convert (x,z) in screen coordinates to viewport coordinates.
            screenToViewport(x,z,V);
            
            // See whether this is a point on the sphere or the background.
            if (isSpherePoint(V,s))
              {
                // On the sphere. Get the surface normal at this point. Since the 
                // sphere is centered at the origin, the s vector is also the normal.
                double[] n = s;
                
                // Determine intensity of shading and set the pixel.
                float intensity = shadeIntensity(n,s);
                int rgb = (new Color(intensity,intensity,intensity)).getRGB();
                image.setRGB(x,z,rgb);
              }
            else
              {
                // Background point that does appear on the sphere.
                int rgb = (Color.black).getRGB();
                image.setRGB(x,z,rgb);
              }
          }
      }
  }
  
  protected void paintComponent(Graphics g) {
    
    // Size of JPanel in pixels. getSize() is a method from JPanel.
    this.width = this.getSize().width;
    this.height = this.getSize().height;
    
    // Draw the sphere to a buffer, then copy it to the screen.
    BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    drawSphere(image);
    g.drawImage(image,0,0,null);
  }
  
  public static void main(String[] args) {
    
    // This main() will invoke SimpleWindow rather than SphereWindow0.
    SimpleWindow theWindow = new SimpleWindow("Shaded Sphere",new SpherePane());
  }
}