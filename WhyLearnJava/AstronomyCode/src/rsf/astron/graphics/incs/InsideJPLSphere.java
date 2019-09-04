package rsf.astron.graphics.incs;

// Similar to JPLSphere, but the planets are shown from inside the sphere. It is assumed 
// that the origin is the earth, so the earth itself is not drawn.

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import rsf.graphics.GUtil;
import rsf.astron.jpl.JPLData;
import rsf.astron.place.Coord;
import rsf.graphics.sphere.InSphere;


public class InsideJPLSphere {
  
  // These are as in earlier cases.
  InSphere baseSphere = null;
  private BufferedImage planetImage = null;
  private String message = null;
  JPLData eph = null;
  public double jd = 0.0;
  
  public InsideJPLSphere(int width,int height,JPLData eph) {
    
    // This stuff should be clear. Earlier sphere-drawing programs are similar.
    this.eph = eph;
    baseSphere = new InSphere(width,height);
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
      baseSphere.setSize(width,height);
      planetImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
      update();
    }
    }
  }
  
  public void addToJD(double x) {

    // Move the JD forward by x days.
    if (this.jd + x < eph.endJD)
      this.jd += x;  
    
    // Adjust the displayed message.
    message = String.format("JD = %.2f",jd);
  }
  
  synchronized public void update() {

    synchronized (baseSphere) {
    synchronized (planetImage) {
      boolean dirty = baseSphere.update();
      
      // Erase the image.
      Graphics g = planetImage.getGraphics();
      g.clearRect(0,0,planetImage.getWidth(),planetImage.getHeight()); 
      
      // Add any message.
      if (message != null)
        {
          g.setColor(Color.red);
          g.drawString(message,20,20);
        }
      
      if (eph == null)
        // No JPL data loaded.
        return;

      // Add the planets at their positions as of this.JD.
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
  
  public static void addPlanets(JPLData eph,double jd,InSphere theSphere,
                                BufferedImage destImage) {
    
    // Add the planets to the given destImage, at the position for time jd from the given
    // JPLData and OutSphere. This is static to make it easier to reuse the code.
    drawPlanet(JPLData.Sun,Color.orange,15,eph,jd,theSphere,destImage);
    drawPlanet(JPLData.Mercury,Color.yellow,3,eph,jd,theSphere,destImage);
    drawPlanet(JPLData.Venus,Color.PINK,4,eph,jd,theSphere,destImage);
    drawPlanet(JPLData.Mars,Color.RED,5,eph,jd,theSphere,destImage);
    drawPlanet(JPLData.Jupiter,Color.GRAY,4,eph,jd,theSphere,destImage);
    drawPlanet(JPLData.Saturn,Color.green,4,eph,jd,theSphere,destImage);
    drawPlanet(JPLData.Moon,Color.white,12,eph,jd,theSphere,destImage);
  }
  
  private static void drawPlanet(int whichPlanet,Color c,int r,JPLData eph,double jd,
                                 InSphere theSphere,BufferedImage destImage) {
    
    // Draw whichPlanet on the sphere with the given Color, using this.jd to determine the 
    // position of the planet.
    int origin = JPLData.Earth;
    double[] statevector = eph.getPosition(jd,whichPlanet,origin);
    
    double xx = statevector[0];
    double yy = statevector[1];
    double zz = statevector[2];
    
    // Convert from (x,y,z) to coords on the sphere, then to screen coords.
    Coord decRA = Coord.fromRectangular(xx,yy,zz,Coord.DecRA);
    double[] rect = decRA.toRectangular();
    

    int[] screen = theSphere.sphereToScreen(rect);
    if (screen == null)
      return;
    
    Graphics g = destImage.getGraphics();
    g.setColor(c);
    GUtil.fillCircle(screen[0],screen[1],r,g);
  }
}