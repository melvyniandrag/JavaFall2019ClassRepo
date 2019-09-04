package rsf.graphics.sphere;

// This is a place-holder for classes that know how to handle changes to the eye position.

public interface EyeAdjuster {
  public void rotEyeRight(double angle);
  public void rotEyeUp(double angle);
  public void twistEye(double angle);
}