package rsf.astron.graphics.surface;

// This shows a view of the sky from the perspective of an observer on the surface of the 
// earth from a fixed lat/long and at a given time.

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import rsf.ui.MyPanel;
import rsf.astron.jpl.JPLData;
import rsf.astron.place.Coord;
import rsf.astron.place.PlaceUtil;
import rsf.astron.stars.StarList;
import rsf.graphics.GUtil;
import rsf.graphics.sphere.InSphere;
import rsf.astron.time.TimeUtil;


public class SurfacePane extends MyPanel {
  
  // The function of these variable should be clear.
  InSphere baseSphere = null;
  private BufferedImage finalImage = null;
  private StarList theStars = null;
  private JPLData eph = null;
  private String message = null;
  public double jd = 0.0;
  public double longitude = 0.0;
  public double latitude = 0.0;
  
  // Whether to draw altitude/azimuth lines relative to the horizon.
  public boolean drawAltAzLines = false;
  
  
  public SurfacePane(StarList theStars,JPLData eph) {
    
    // This stuff should be clear. Earlier sphere-drawing programs are similar.
    this.theStars = theStars;
    this.eph = eph;
    baseSphere = new InSphere(this.initialWidth,this.initialWidth);
    finalImage = new BufferedImage(initialWidth,initialWidth,BufferedImage.TYPE_INT_RGB);
    this.jd = eph.startJD+1.01;
  }
  
  public void setMsg(String msg) {
    this.message = msg;
  }
  
  synchronized public void resize(int width,int height) {
    
    if ((finalImage.getWidth() == width) && (finalImage.getHeight()== height))
      // No need to resize.
      return;
    
    // Have to allocate new image buffers because the window size has changed.
    synchronized(baseSphere) {
    synchronized(finalImage) {
      baseSphere.setSize(width,height);
      finalImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
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
  
  private void drawPlanet(int thePlanet,Color c,int r) {
    
    // Draw thePlanet to this.finalImage. Determine the horizon coordinates of the planet. 
    // This is similar to PlaceUtil.main().
    double gst = TimeUtil.jdToGST(jd,eph);
    double lst = TimeUtil.gstToLST(gst,longitude);
    double[] p = eph.getObservedPosition(jd,thePlanet,JPLData.Earth);
    double[] e = PlaceUtil.observerLocation(gst,latitude,longitude);
    p = GUtil.minus(p,e);
    
    Coord raDec = Coord.fromRectangular(p[0],p[1],p[2],Coord.DecRA);
    raDec.epsilon = PlaceUtil.meanObliquity(jd);
    Coord precessed = PlaceUtil.from2000(raDec,jd);
    Coord horizon = precessed.toHorizon(lst,latitude);
    double[] rect = horizon.toRectangular();
    
    int[] screen = baseSphere.sphereToScreen(rect);
    if (screen == null)
      return;
    
    Graphics g = finalImage.getGraphics();
    g.setColor(c);
    GUtil.fillCircle(screen[0],screen[1],r,g);
  }
  
  private void addPlanets() {
    
    // Draw the planets. This method, and drawPlanet() above, are very similar to the 
    // methods of the same name in InsideJPLSphere, but because the observer is on the 
    // surface of the earth, the code from InsideJPLSphere doesn't quite work here.
    drawPlanet(JPLData.Sun,Color.orange,15);
    drawPlanet(JPLData.Mercury,Color.yellow,3);
    drawPlanet(JPLData.Venus,Color.PINK,4);
    drawPlanet(JPLData.Mars,Color.RED,5);
    drawPlanet(JPLData.Jupiter,Color.GRAY,4);
    drawPlanet(JPLData.Saturn,Color.green,4);
    drawPlanet(JPLData.Moon,Color.white,12);
    drawPlanet(JPLData.Neptune,Color.cyan,3);
    drawPlanet(JPLData.Pluto,Color.magenta,3);
    drawPlanet(JPLData.Uranus,Color.darkGray,3);
  }
  
  private void drawAltAz(Graphics g) {

    // Add lines to the output image that show the alt/az angles. Also, draw N/S/E/W along
    // the perimeter of the window at the cardinal directions. This is crude, but it gives 
    // an idea of the nature of the distortion inherent in the different types of projection.
    double[] V = new double[3];
    double[] s = new double[3];
    for (int x = 0; x < baseSphere.getWidth(); x++)
      {
        for (int z = 0; z < baseSphere.getHeight(); z++)
          {
            baseSphere.screenToViewport(x,z,V);
            boolean onSphere = baseSphere.isSpherePoint(V,s);
            if (onSphere == true)
              {
                Coord horizon = Coord.fromRectangular(s[0],s[1],s[2],Coord.Horizon);
                
                // Convert coords to degrees.
                double lat = horizon.twoPiTerm * 180.0 / Math.PI;
                if (lat < 0.0) lat += 360.0;
                double lon = horizon.piTerm * 180.0 / Math.PI;
                if (lon < 0.0) lon += 360.0;
                
                // Now draw lines of altitude and azimuth every 10 degrees.
                if ((lat % 10.0) < 1.5)
                  finalImage.setRGB(x,z,Color.blue.getRGB());
                if ((lon % 10.0) < 1.0)
                  finalImage.setRGB(x,z,Color.red.getRGB());
              }
          }
      }
    
    // Add N/S/E/W.    
    seekCardinal(0.0,"N",g,Color.green);
    seekCardinal(Math.PI/2.0,"E",g,Color.green);
    seekCardinal(Math.PI,"S",g,Color.green);
    seekCardinal(3.0 * Math.PI/2.0,"W",g,Color.green);
  }
  
  private void seekCardinal(double az,String label,Graphics g,Color c) {
    
    // Find the point along the perimeter of the window with azimuth closest to az (in
    // radians) and label it.
    int[] closest = new int[2];
    double diff = 999.9;
    double[] V = new double[3];
    double[] s = new double[3];
    for (int i = 0; i < this.getWidth(); i++)
      {
        // Look along top edge. Convert screen coordinates to horizon coordinates and 
        // compare to the closest seen so far.
        baseSphere.screenToViewport(i,0,V);
        boolean onSphere = baseSphere.isSpherePoint(V,s);
        if (onSphere == true)
          {
            Coord horizon = Coord.fromRectangular(s[0],s[1],s[2],Coord.Horizon);
            if (Math.abs(horizon.twoPiTerm - az) < diff)
              {
                diff = Math.abs(horizon.twoPiTerm - az);
                closest[0] = i;
                closest[1] = 0;
              }
          }
        
        // Look along bottom edge.
        baseSphere.screenToViewport(i,this.getHeight()-1,V);
        onSphere = baseSphere.isSpherePoint(V,s);
        if (onSphere == true)
          {
            Coord horizon = Coord.fromRectangular(s[0],s[1],s[2],Coord.Horizon);
            if (Math.abs(horizon.twoPiTerm - az) < diff)
              {
                diff = Math.abs(horizon.twoPiTerm - az);
                closest[0] = i;
                closest[1] = this.getHeight() - 1;
              }
          }
      }
    
    // Same thing, but look along left and right edges.
    for (int i = 0; i < this.getHeight(); i++)
      {
        // Look along left edge.
        baseSphere.screenToViewport(0,i,V);
        boolean onSphere = baseSphere.isSpherePoint(V,s);
        if (onSphere == true)
          {
            Coord horizon = Coord.fromRectangular(s[0],s[1],s[2],Coord.Horizon);
            if (Math.abs(horizon.twoPiTerm - az) < diff)
              {
                diff = Math.abs(horizon.twoPiTerm - az);
                closest[0] = 0;
                closest[1] = i;
              }
          }
        
        // Look along right edge.
        baseSphere.screenToViewport(this.getWidth()-1,i,V);
        onSphere = baseSphere.isSpherePoint(V,s);
        if (onSphere == true)
          {
            Coord horizon = Coord.fromRectangular(s[0],s[1],s[2],Coord.Horizon);
            if (Math.abs(horizon.twoPiTerm - az) < diff)
              {
                diff = Math.abs(horizon.twoPiTerm - az);
                closest[0] = this.getWidth() - 1;
                closest[1] = i;
              }
          }
      }
    
    // Offset closest a bit from the edge of the screen and draw the label.
    if (closest[0] == 0)
      closest[0] += 15;
    else if (closest[0] == this.getWidth() - 1)
      closest[0] -= 15;
    if (closest[1] == 0)
      closest[1] += 15;
    else if (closest[1] == this.getHeight() - 1)
      closest[1] -= 15;
    
    g.setColor(c);
    g.drawString(label,closest[0],closest[1]);
  }
  
  protected void paintComponent(Graphics g) {
    draw(g);
  }
  
  synchronized public void update() {
        
    // The code in PlaceUtil already effectively converts from JPL data to dec/ra for a 
    // given time and place, and to horizon coords. These horizon coords are what is needed
    // here. That takes care of the planets. Handling the stars requires a way to convert 
    // from dec/ra (which is how data for the stars is given) to horizon coords.
    synchronized (baseSphere) {
    synchronized (finalImage) {
      
      // Allow the sphere to update its position.
      boolean dirty = baseSphere.update();
      
      // Erase the image.
      Graphics g = finalImage.getGraphics();
      g.clearRect(0,0,finalImage.getWidth(),finalImage.getHeight());
      
      // Add the stars. Do this by getting the Dec/RA coord for each star and converting
      // it to horizon coords. This is much line what is done in InsideStarSphere.addStars().
      Coord starPos = new Coord();
      starPos.type = Coord.DecRA;
      double gst = TimeUtil.jdToGST(jd,eph);
      double lst = TimeUtil.gstToLST(gst,longitude);
      synchronized (theStars) {
        for (int i = 0; i < theStars.ra.length; i++)
          {
            // Apply precession, then convert from RA/Dec to (x,y,z) coordinates on the 
            // sphere.
            starPos.piTerm = theStars.dec[i];
            starPos.twoPiTerm = theStars.ra[i];
            starPos.epsilon = PlaceUtil.meanObliquity(jd);
            Coord precessed = PlaceUtil.from2000(starPos,jd);
            Coord horizon = precessed.toHorizon(lst,latitude);
            double[] rect = horizon.toRectangular();
            
            // Convert to screen coordinates.
            int[] screen = baseSphere.sphereToScreen(rect);
            if (screen == null)
              continue;
            
            // Convert the (x,y,z) coordinates to screen coordinates, and draw it.
            double d0 = 0.3;
            double d1 = 1.0;
            double m0 = 4.0;
            double m1 = -1.1;
            float intensity = (float) (d0 - 1.0 + Math.pow(d1 - d0 + 1.0,
                                                          (m0 - theStars.mag[i])/(m0-m1)));
            if (intensity < 0.0)
              continue;
            
            int rgb = (new Color(intensity,intensity,intensity)).getRGB();
            finalImage.setRGB(screen[0],screen[1],rgb);
          }
      }
      
      // Add the planets.
      addPlanets();
      
      // Add the horizon. This is done by scanning every point of the window and
      // testing whether the altitude is more or less than zero.
      Color brown = new Color(0.4f,0.2f,0.0f); 
      double[] V = new double[3];
      double[] s = new double[3];
      for (int x = 0; x < baseSphere.getWidth(); x++)
        {
          for (int z = 0; z < baseSphere.getHeight(); z++)
            {
              // Convert (x,y) in screen coordinates to horizon coordinates.
              baseSphere.screenToViewport(x,z,V);
              boolean onSphere = baseSphere.isSpherePoint(V,s);
              if (onSphere == true)
                {
                  // Because the z-axis points to the zenith, this is easy. Convert the
                  // 3d coordinates on the sphere to spherical coordinates.
                  Coord horizon = Coord.fromRectangular(s[0],s[1],s[2],Coord.Horizon);
                  
                  // If the altitude is less than zero, then the point is below the horizon.
                  // Make it brown.
                  if (horizon.piTerm < 0.0)
                    finalImage.setRGB(x,z,brown.getRGB());
                }
            }
        }

      // Add alt/az lines if the user asked for them.
      if (drawAltAzLines == true)
        drawAltAz(g);
    }
    }
  }
  
  synchronized public void draw(Graphics g) {
    synchronized (baseSphere) {
    synchronized (finalImage) {
      g.drawImage(finalImage,0,0,null);
    }
    }
  }
}