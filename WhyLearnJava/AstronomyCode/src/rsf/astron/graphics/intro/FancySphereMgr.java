package rsf.astron.graphics.intro;

// Same idea as BaseSphereMgr, but for FancySunMotion. There are now two sphere objects
// that need to know when the eye changes. Because they must move together, every
// change to the eye must be synchronized.

import rsf.graphics.sphere.EyeAdjuster;
import rsf.graphics.sphere.OutSphere;


class FancySphereMgr extends Thread implements EyeAdjuster {
  
  OutSphere sphere1 = null;
  OutSphere sphere2 = null;
  
  public FancySphereMgr(OutSphere s1,OutSphere s2) {
   this.sphere1 = s1;
   this.sphere2 = s2;
  }
  
  public void rotEyeRight(double angle) {
   synchronized (sphere1) { synchronized(sphere2) {
     sphere1.requestRotEyeRight(angle);
     sphere2.requestRotEyeRight(angle);
   } }
  }
  
  public void rotEyeUp(double angle) {
   synchronized (sphere1) { synchronized(sphere2) {
     sphere1.requestRotEyeUp(angle);
     sphere2.requestRotEyeUp(angle);
   } }
  }
  
  public void twistEye(double angle) {
   synchronized (sphere1) { synchronized(sphere2) {
     sphere1.requestTwistEye(angle);
     sphere2.requestTwistEye(angle);
   } }
  }
  
  public void run() {
  
   // Continuously tell the two spheres to take changes into account.
   while (true)
     {
       synchronized (sphere1) { synchronized (sphere2) {
         sphere1.implementRequests();
         sphere2.implementRequests();
       } }
      
       try {
         Thread.sleep(100);
       } catch (Exception e) { ;; }
     }
  }
}