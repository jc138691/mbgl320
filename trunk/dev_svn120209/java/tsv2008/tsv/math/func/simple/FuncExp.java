package math.func.simple;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 17:03:35
 */
public class FuncExp implements Func {
  private final double c;
  public FuncExp(final double c) {
    this.c = c;
  }
  public double calc(double x) {
    return Math.exp(c * x);
  }
}
