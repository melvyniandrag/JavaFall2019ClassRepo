package rsf.graphics.sphere;

// This is like EyeAdjuster, but it also has methods to let you zoom in and out.

public interface InsideEyeAdjuster extends EyeAdjuster {
  public void zoomIn();
  public void zoomOut();
}