package math.vec.grid;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 14:55:41
 */
public class Range {
  private final double left;
  private final double right;
  private final double range;
  private final double oneOver;
  public Range(double first, double last) {
    left = first;
    right = last;
    range = last - first;
    oneOver = 1. / range;
  }
  public double getLeft() {
    return left;
  }
  public double getRight() {
    return right;
  }
  public double getRange() {
    return range;
  }
  public double getOneOver() {
    return oneOver;
  }
}
