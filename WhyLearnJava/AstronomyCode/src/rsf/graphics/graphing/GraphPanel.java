package rsf.graphics.graphing;

// Use this to graph some (x,y) data.

import javax.swing.JPanel;
import java.text.DecimalFormat;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;


public class GraphPanel extends JPanel {
  
  // The data. Points of the function to be graphed are given by ydata[i] = f(xdata[i]).
  private double[] xdata = null;
  private double[] ydata = null;
  
  // A message that may be displayed at the top of the graph.
  private String msg = null;
  
  // Values to make the graph pretty.
  private final int leftMargin = 60;
  private final int rightMargin = 20;
  private final int topMargin = 60;
  private final int bottomMargin = 40;
  
  // The max/min x and y values on the graph. These values could be determined from the 
  // data, but if this class is used in an animation, it will look better if the range
  // of values shown is fixed over the entire animation.
  private double maxx = 0.0;
  private double minx = 0.0;
  private double maxy = 0.0;
  private double miny = 0.0;
  
  
  public GraphPanel(double minx,double maxx,double miny,double maxy) {
    this.minx = minx;
    this.maxx = maxx;
    this.miny = miny;
    this.maxy = maxy;
  }
  
  public void setXY(double[] x,double[] y) {
    
    // This sets the data to be displayed.
    xdata = x;
    ydata = y;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  protected Rectangle axisRect() {
    
    // Returns the rectangle that forms the axes of the graph.
    Rectangle		answer;    
    Dimension drawRect = this.getSize();

    // x,y,width,height
    answer = new Rectangle(leftMargin,topMargin,
                           drawRect.width - rightMargin - leftMargin,
                           drawRect.height - bottomMargin - topMargin);
    return answer;
  }

  protected double niceNumber(double x,boolean round) {
    
    // This is from Graphics Gems I, p. 61-2. It takes a number, x, and return a number 
    // "close to" x that has the form a x 10^n, where a is 1, 2 or 5. So the number will 
    // have the form 1.0, 2.0, 5.0, 10.0, 0.10, 0.50, 0.20 etc. The round argument is 
    // whether to take the closest such number (round = true) or the next largest
    // such number after x.
    
    // Take x and put in in scientific form. That is, if x = 102, then f will be 1.02.
    int exp = (int) Math.floor(Math.log(x) / Math.log(10.0));
    double f = x / (float) Math.pow(10.0,exp);
    
    double nf;
    if (round == true)
      if (f < 1.5) nf = 1.0;
      else if (f < 3.0) nf = 2.0;
      else if (f < 7.0) nf = 5.0;
      else nf = 10.0;
    else
      if (f <= 1.0) nf = 1.0;
      else if (f <= 2.0) nf = 2.0;
      else if (f <= 5.0) nf = 5.0;
      else nf = 10.0;
    
    return nf * Math.pow(10.0,exp);
  }
  
  public void paintComponent(Graphics g) {
    
    Graphics2D g2 = (Graphics2D) g;
    
    // Clear the panel to white.
    int width = this.getSize().width;
    int height = this.getSize().height;
    g.setColor(Color.WHITE);
    g.fillRect(0,0,width,height);
    
    if ((xdata == null) || (ydata == null)) return;
    
    // Draw the axes.
    g.setColor(Color.BLACK);
    Rectangle axRect = axisRect();
    g.drawLine(axRect.x,axRect.y + axRect.height,
               axRect.x + axRect.width,axRect.y+axRect.height);
    g.drawLine(axRect.x,axRect.y,axRect.x,axRect.y + axRect.height);
    
    // Draw the hash marks and labels. This is taken from Graphics Gems I, p. 61.
    // I have modified the code to make what that book calls "loose labels".
    
    // Number of tick marks.
    int numTicks = 8;
    
    double range = maxx - minx;
    double d = niceNumber(range / (numTicks - 1),true);
    double graphMin = Math.floor(minx/d) * d;
    double graphMax = Math.ceil(maxx/d) * d;
    
    // Determine the number of digits to show after the decimal.
    // Use this to specify the string format.
    int nfrac = (int) -Math.floor(Math.log(d)/Math.log(10));
    if (nfrac < 0.0)
      nfrac = 0;
    
    String formatString = null;
    if (nfrac <= 0)
      formatString = new String("#0");
    else
      {
        formatString = new String("#0.");
        for (int i = 0; i < nfrac; i++)
          formatString += "0";
      }

    DecimalFormat numForm = new DecimalFormat(formatString);

    for (double x = graphMin; x < graphMax + 0.5 * d; x += d)
      {
        String numString = numForm.format(x);

        // Convert the number where the tick is to appear to x and y coordinates 
        // in window terms.
        int yCoord = axRect.y + axRect.height;
        int xCoord = (int) (axRect.x + axRect.width * (x - minx) / (maxx - minx));

        // Make sure this tick is on the visible portion of the axis.
        if ((xCoord < axRect.x) || (xCoord > (axRect.x + axRect.width)))
          continue;

        // Draw the tick mark.
        g.drawLine(xCoord,yCoord,xCoord,yCoord + 4);
        
        // Draw the number label.
        FontRenderContext frc = g2.getFontRenderContext();
        Rectangle2D bounds = g2.getFont().getStringBounds(numString,frc);
        int w = (int) bounds.getWidth();
        int h = (int) bounds.getHeight();
        g.drawString(numString,xCoord - w / 2,yCoord + h + 2);
      }

    // Do the same thing for the y-axis.
    numTicks = 8;
    range = maxy - miny;
    d = niceNumber(range / (numTicks - 1),true);
    graphMin = Math.floor(miny/d) * d;
    graphMax = Math.ceil(maxy/d) * d;
    nfrac = (int) -Math.floor(Math.log(d)/Math.log(10));
    if (nfrac < 0.0)
      nfrac = 0;
    if (nfrac <= 0)
      formatString = new String("#0");
    else
      {
        formatString = new String("#0.");
        for (int i = 0; i < nfrac; i++)
          formatString += "0";
      }
    
    numForm = new DecimalFormat(formatString);
    
    for (double y = graphMin; y < graphMax + 0.5 * d; y += d)
      {
        String numString = numForm.format(y);
          
        int xCoord = axRect.x;
        int yCoord = (int) ((axRect.y + axRect.height) - 
                            (axRect.height) * (y - miny) / (maxy - miny));
        if ((yCoord < axRect.y) || (yCoord > axRect.y + axRect.height))
          continue;
        
        g.drawLine(xCoord,yCoord,xCoord - 4,yCoord);
        
        FontRenderContext frc = g2.getFontRenderContext();
        Rectangle2D bounds = g2.getFont().getStringBounds(numString,frc);
        g.drawString(numString,(int)(xCoord - 10 - bounds.getWidth()),yCoord + 4);
      }
    
    // Draw the data.
    int x1 = (int) (axRect.x - axRect.width * (xdata[0] - minx) / (maxx - minx));
    int y1 = (int) ((axRect.y + axRect.height) - (axRect.height) * 
                    (ydata[0] - miny) / (maxy - miny));		
    for (int i = 1; i < ydata.length; i++)
      {
        int x2 = (int) (axRect.x + axRect.width * (xdata[i] - minx) / (maxx - minx));
        int y2 = (int) ((axRect.y + axRect.height) - (axRect.height) * 
                        (ydata[i] - miny) / (maxy - miny));
        
        g.setColor(Color.RED);
        g.drawLine(x1,y1,x2,y2);
        
        x1 = x2;
        y1 = y2;
      }
    
    // Display the message, if any.
    g.setColor(Color.BLACK);
    if (msg != null)
      g.drawString(msg,(axRect.width/2),20);
  }
}