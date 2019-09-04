package rsf.graphics.sphere.intro;

// This allows the user to move the eye about the sphere.

import java.awt.Graphics;

import rsf.graphics.GUtil;
import rsf.ui.FocusTaker;
import rsf.ui.SimpleWindow;


public class MobilePane extends SpherePane {
  
  // Distance of eye from the center of the sphere.
  protected double eyeDistance = 10.0;

  // These two perpendicular vectors determine a plane tangent to the sphere, and
  // u2 x u1 points to the eye. The observer is assumed to look toward the origin.
  // u1 is the x-axis of the viewport and u2 is the y-axis.
  public double[] u1 = {1.0,0.0,0.0};
  public double[] u2 = {0.0,1.0,0.0};
  
  
  public MobilePane() {
    this.addKeyListener(new EyeKeyListener(this));
    this.addMouseListener(new FocusTaker(this));
  }
 
  public void screenToViewport(int x,int z,double[] V) {
    
    // Given screen coordinates, (x,z), return the viewport coordinates in V.
    double planeX = ((double) x / (double) width - 0.5) * portWidth;
    double planeZ = ((double) z / (double) height - 0.5) * portHeight;
    V[0] = planeX * u1[0] + planeZ * u2[0];
    V[1] = planeX * u1[1] + planeZ * u2[1];
    V[2] = planeX * u1[2] + planeZ * u2[2];
    
    // Offset the viewport by the normal vector.
    GUtil.addTo(GUtil.cross(u2,u1),V);
  }
 
  protected void paintComponent(Graphics g) {
    
    // Draw it...
    eye = GUtil.cross(u2,u1);
    eye = GUtil.times(eye,eyeDistance);
    super.paintComponent(g);
  }
  
  public static void main(String[] args) {
    
    // This is exactly as in SpherePane.
    SimpleWindow theWindow = new SimpleWindow("Mobile Sphere",new MobilePane());
  }
}