package rsf.graphics.sphere.intro;

// Like MobilePane, which it extends, but the user can also draw free-hand on the sphere.

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import rsf.graphics.GUtil;
import rsf.ui.SimpleWindow;


public class DrawPane extends MobilePane implements MouseListener,MouseMotionListener {
  
  // (lastx,lasty) is the most recently noted mouse position.
  // See mousePressed(), mouseDragged() and mouseReleased().
  int lastx = 0;
  int lasty = 0;
  
  // This holds all the points that have been drawn with the mouse. Each entry is a three
  // dimensional point on the surface of the sphere. The curve is drawn in connect-the-
  // dots fashion. When the user stops one curve and starts another (by releasing the 
  // mouse button, then clicking again), the last dot from one curve should not be 
  // connected to the first dot of the next curve. To solve this problem, there is a null
  // entry between each set of points from a single curve.
  ArrayList drawnPoints = new ArrayList();
  
  // A copy of the most recent sphere image, with surface curves.
  private BufferedImage image = null;
  
  public DrawPane() {  
    this.addKeyListener(new EyeKeyListener(this));
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
  }
  
  public void mouseEntered(MouseEvent e) { 
    
    // Need so that this hears keyboard events.
    this.requestFocus();
  }
  
  public void mouseDragged(MouseEvent e) {
    
    // Add a point to the current curve.
    Graphics g = getGraphics();
    int x = e.getX();
    int y = e.getY();
    
    // Make sure that (x,y) is on the sphere and not some point "in space."
    double[] V = new double[3];
    double[] s = new double[3];
    screenToViewport(x,y,V);
    if (isSpherePoint(V,s) == false)
      // Not a point on the sphere.
      return;
      
    // Got here, so s is a point on the sphere. Add it to the list of points to draw.
    drawnPoints.add(s);
    
    // Redraw points (with the new addition), then copy to the screen.
    drawPoints(image);
    g.drawImage(image,0,0,null);
  }
  
  public void drawPoints(BufferedImage destImage) {
    
    // Draw all the points in this.drawnPoints to destImage, which should 
    // already contain an image of the shaded sphere.
    
    // The curves are drawn in cyan.
    Graphics destG = destImage.getGraphics();
    destG.setColor(Color.cyan);
    
    // Whether we are in the middle of a series of points which should be
    // connected in dot-to-dot fashion.
    boolean inPointSeries = false;
    int prevX = 0;
    int prevY = 0;
    
    // Loop over the entries in drawnPoints.
    for (int i = 0; i < drawnPoints.size(); i++)
      {
        double[] s = (double[]) drawnPoints.get(i);
        if (s == null)
          {
            // Have reached the end of a connected series of points.
            inPointSeries = false;
            continue;
          }
        
        // See if s is visible -- similar to what's done in SpherePane.isSpherePoint().
        double b = 2.0 * s[0] * (eye[0] - s[0]) +
                   2.0 * s[1] * (eye[1] - s[1]) +
                   2.0 * s[2] * (eye[2] - s[2]);
        if (b > 0)
          {
            // The point is visible. Project it onto the viewport by finding the point
            // where L(t) intersects the viewport, then convert viewport coordinates to
            // screen coordinates.
            
            // Project the point s to the viewport.
            double[] n = GUtil.cross(u2,u1);
            double[] eyeTon = GUtil.minus(eye,n);
            double[] pToEye = GUtil.minus(s,eye);
            double[] u2cross = GUtil.cross(u2,pToEye);
            double[] u1cross = GUtil.cross(u1,pToEye);
            double alpha1 = GUtil.dot(eyeTon,u2cross) / GUtil.dot(u1,u2cross);
            double alpha2 = GUtil.dot(eyeTon,u1cross) / GUtil.dot(u2,u1cross);
            
            // Convert (alpha1,alpha2), which is relative to the plane of the viewport, to
            // screen coordinates.
            int x = (int)((alpha1 / portWidth + 0.5) * (double) width);
            int y = (int)((alpha2 / portHeight + 0.5) * (double) height);
            
            // Connect the dots in this wasn't the first point of the curve.
            if (inPointSeries == true)
              destG.drawLine(prevX,prevY,x,y);
            
            // Test: draw the individual point to see how dots are connected.
            //destG.setColor(Color.red);
            //destG.fillOval(x-2,y-2,5,5);
            //destG.setColor(Color.cyan);
            
            // We are definitely in the process of making a curve now.
            inPointSeries = true;
            prevX = x;
            prevY = y;
          }
      }
  }
  
  public void mouseReleased(MouseEvent e) {
    
    // No longer drawing on the sphere. Terminate the drag.
    drawnPoints.add(null);
  }
  
  public void mousePressed(MouseEvent e) {
    
    // Beginning to draw on the sphere.
    lastx = e.getX();
    lasty = e.getY();
  }
  
  // These methods are required by the MouseListener interface but are not used.
  public void mouseExited(MouseEvent e) { ;; }
  public void mouseClicked(MouseEvent e) { ;; }
  public void mouseMoved(MouseEvent e) { ;; }
  
  protected void paintComponent(Graphics g) {
    
    // As in SpherePane and MobilePane. 
    eye = GUtil.cross(u2,u1);
    eye = GUtil.times(eye,eyeDistance);
    this.width = this.getSize().width;
    this.height = this.getSize().height;
    
    // Draw the sphere to a buffer, add the drawn curves, and copy to screen.
    image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    drawSphere(image);
    drawPoints(image);
    g.drawImage(image,0,0,null);
  }
  
  public static void main(String[] args) {
    
    // This is exactly as in SpherePane.
    SimpleWindow theWindow = new SimpleWindow("Drawable Sphere",new DrawPane());
  }
}