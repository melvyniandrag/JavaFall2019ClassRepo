package rsf.astron.jpl.anim;

// Used to hold data about a planet and how it is to be drawn.

import java.awt.Color;

public class PlanetState {
  double[] position = null;
  Color theColor = null;
  int pixelRadius = 0;
  double distance = 0.0; // distance of the planet from the observer.
}