package rsf.astron.graphics.incs;

// Like BaseSphereMgr, but for an InSphere.

import rsf.graphics.sphere.InsideEyeAdjuster;
import rsf.graphics.sphere.InSphere;


public class InsideSphereMgr extends Thread implements InsideEyeAdjuster {
  
  InSphere baseSphere = null;
  
  
  public InsideSphereMgr(InSphere theSphere) {
    this.baseSphere = theSphere;
  }
  
  public void rotEyeRight(double angle) {
    baseSphere.requestRotEyeRight(angle);
  }
  
  public void rotEyeUp(double angle) {
    baseSphere.requestRotEyeUp(angle);
  }
  
  public void twistEye(double angle) {
    baseSphere.requestTwistEye(angle);
  }
  
  public void zoomIn() {
    baseSphere.zoomIn();
  }
  
  public void zoomOut() {
    baseSphere.zoomOut();
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