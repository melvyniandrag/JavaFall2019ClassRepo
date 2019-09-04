package rsf.astron.jpl.anim;

// Shows a sphere with the planets projected to the sphere at their positions as of a 
// particular Julian day. This isn't meant to be used on it's own (it would be pretty 
// boring). It's used by JPLSphereAnim. This is very much like StarSphere. It takes the 
// existing code that generates a shaded sphere, and pastes the planets onto it.

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import rsf.astron.graphics.intro.EqEcSphere;
import rsf.astron.jpl.JPLData;
import rsf.astron.place.Coord;
import rsf.graphics.GUtil;
import rsf.graphics.sphere.OutSphere;


public class JPLSphere {
  
  // These fields are as in earlier programs.
  EqEcSphere baseSphere = null;
  private BufferedImage planetImage = null;
  private String message = null;
  JPLData eph = null;
  
  // The Julian day to use.
  public double jd = 0.0;
  
  public JPLSphere(int width,int height,JPLData eph) {
    
    // This stuff should be clear. Earlier sphere-drawing programs are similar.
    this.eph = eph;
    baseSphere = new EqEcSphere(width,height);
    planetImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    this.jd = eph.startJD;
  }
  
  public void setMsg(String msg) {
    this.message = msg;
  }
  
  synchronized public void resize(int width,int height) {
    
    if ((planetImage.getWidth() == width) && (planetImage.getHeight()== height))
      // No need to resize.
      return;
    
    // Have to allocate new image buffers because the window size has changed.
    synchronized(baseSphere) {
    synchronized(planetImage) {
      baseSphere.resize(width,height);
      planetImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      update();
    }
    }
  }
  
  public void addToJD(double x) {

    // Move the JD forward by x days.
    if (this.jd + x < eph.endJD)
      this.jd += x;  
    
    // Adjust the displayed data.
    message = String.format("JD = %.2f",jd);
  }
  
  synchronized public void update() {

    synchronized (baseSphere) {
    synchronized (planetImage) {
      boolean dirty = baseSphere.update();
      
      // Copy the baseSphere to planetImage, then add the planets at their positions as 
      // of this.JD.
      Graphics g = planetImage.getGraphics();
      BufferedImage baseImage = baseSphere.getImage();
      g.drawImage(baseImage,0,0,null);
      
      // Add any message.
      if (message != null)
        {
          g.setColor(Color.red);
          g.drawString(message,20,20);
        }
      
      if (eph == null)
        // No JPL data loaded.
        return;
      
      addPlanets(this.eph,this.jd,this.baseSphere,this.planetImage);
    }
    }
  }
  
  synchronized public void draw(Graphics g) {
    synchronized (baseSphere) {
    synchronized (planetImage) {
      g.drawImage(planetImage,0,0,null);
    }
    }
  }
  
  public static void addPlanets(JPLData eph,double jd,OutSphere theSphere,
                                BufferedImage destImage) {
    
    // Add the planets to the given destImage, at the position for time jd from the given
    // JPLData and OutSphere. This is static to make it easier to reuse the code.
    drawPlanet(JPLData.Mercury,Color.yellow,0.01,eph,jd,theSphere,destImage);
    drawPlanet(JPLData.Venus,Color.PINK,0.02,eph,jd,theSphere,destImage);
    drawPlanet(JPLData.Earth,Color.blue,0.03,eph,jd,theSphere,destImage);
    drawPlanet(JPLData.Mars,Color.RED,0.03,eph,jd,theSphere,destImage);
    drawPlanet(JPLData.Jupiter,Color.GRAY,0.06,eph,jd,theSphere,destImage);
    drawPlanet(JPLData.Saturn,Color.green,0.05,eph,jd,theSphere,destImage);
  }
  
  private static void drawPlanet(int whichPlanet,Color c,double r,JPLData eph,
                                 double jd,OutSphere theSphere,BufferedImage destImage) {
    
    // Draw whichPlanet on the sphere with the given Color, using this.jd
    // to determine the position of the planet.
    int origin = JPLData.SSB;
    double[] statevector = eph.getPosition(jd,whichPlanet,origin);
    
    double xx = statevector[0];
    double yy = statevector[1];
    double zz = statevector[2];
    
    // Convert from (x,y,z) to coordinates on the sphere, then to screen coordinates.
    // This is a bit round-about. The point is to convert the rectangular coordinates to
    // another set of rectangular coordinates that lie on the sphere. This amounts to a
    // radial projection of the planet's position to the celestial sphere.
    Coord decRA = Coord.fromRectangular(xx,yy,zz,Coord.DecRA);
    double[] rect = decRA.toRectangular();
    double sphereR = theSphere.getRadius();
    GUtil.times(rect,sphereR);
    
    // Draw the planet as a disk on the sphere.
    theSphere.addDisk(destImage,rect,r,c);
  }
}