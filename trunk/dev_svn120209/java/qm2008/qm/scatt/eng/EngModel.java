package scatt.eng;
import math.vec.grid.StepGridOpt;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 29/09/2008, Time: 14:51:24
 */
public class EngModel extends StepGridOpt {
  public EngModel() {
  }
  public EngModel(double first, double last, int n) {
    super(first, last, n);
  }
  public void loadDefault() {
    loadDefault(this);
  }
  public static void loadDefault(EngModel gridEng) {
    gridEng.setFirst(0.1f);
    gridEng.setLast(1);
    gridEng.setNumPoints(10);
  }
}
