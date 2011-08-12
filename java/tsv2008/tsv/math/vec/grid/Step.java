package math.vec.grid;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 14:54:18
 */
public class Step {
  private double step;
//  private final double oneOver;
  public Step(Range r, int size) {
    this(r.getRange() / (size - 1));
  }
  public Step(double step) {
    this.step = step;
//    oneOver = 1. / step;
  }
  public double getStep() {
    return step;
  }
  public void setStep(double step) {
    this.step = step;
  }
//  public double getOneOver() {
//    return oneOver;
//  }
}
