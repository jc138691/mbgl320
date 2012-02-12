package math.func.simple;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 26/09/2008, Time: 16:23:05
 */
public class FuncLinear implements Func {
  private final double a;
  private final double b;
  public FuncLinear(double a, double b) {
    this.a = a;
    this.b = b;
  }
  public double calc(double x) {
    return a + b * x;
  }
}