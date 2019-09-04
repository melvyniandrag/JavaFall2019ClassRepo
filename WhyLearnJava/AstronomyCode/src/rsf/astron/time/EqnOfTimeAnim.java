package rsf.astron.time;

// Shows an animation of the equation of time.

import rsf.graphics.graphing.GraphAnimWindow;


public class EqnOfTimeAnim extends GraphAnimWindow {
  
  public EqnOfTimeAnim(double minx,double maxx,double mint,double maxt,
                       double miny,double maxy,double tstep) {
    super(minx,maxx,mint,maxt,miny,maxy,tstep);    
  }
  
  public double f(double x,double t) {
    return EqnOfTime.eqnOfTime(t,x); 
  }
  
  public static void main(String[] args) {   
    EqnOfTimeAnim theWindow = new EqnOfTimeAnim(0.0,1.0,-100.0,100.0,-20.0,20.0,0.5);
    theWindow.theTimer.start();
  }
}