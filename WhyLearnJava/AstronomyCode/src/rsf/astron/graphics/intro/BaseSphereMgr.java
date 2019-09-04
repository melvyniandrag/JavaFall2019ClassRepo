package rsf.astron.graphics.intro;

// This class is it's own thread that periodically checks whether SunMotion.baseSphere
// needs to be updated. Changes to the scene (the eye's position) are reported to this 
// thread, which will update baseSphere when convenient. This prevents the animation 
// from being jerky.

import rsf.graphics.sphere.EyeAdjuster;
import rsf.graphics.sphere.OutSphere;


public class BaseSphereMgr extends Thread implements EyeAdjuster {
  
  OutSphere baseSphere = null;
  
  
  public BaseSphereMgr(OutSphere theSphere) {
    this.baseSphere = theSphere;
  }
  
  public void rotEyeRight(double angle) {
   
    // Rotate the eye to the right by angle (in Radians).
    baseSphere.requestRotEyeRight(angle);
  }
  
  public void rotEyeUp(double angle) {
   
    // Rotate the eye upwards by angle (in Radians).
    baseSphere.requestRotEyeUp(angle);
  }
  
  public void twistEye(double angle) {
   
    // Twist the orientation of the eye clock-wise.
    baseSphere.requestTwistEye(angle);
  }
  
  public void run() {
   
    // Loop, continuously updating baseSphere.
    while (true)
      {
        baseSphere.update();
        
        try {
          Thread.sleep(100);
        } catch (Exception e) { ;; }
      }
  }
}