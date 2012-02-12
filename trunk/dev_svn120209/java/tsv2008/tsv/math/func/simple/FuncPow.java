package math.func.simple;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 16:24:50
 */
public class FuncPow implements Func {
  private final double a;
  private final double b;
  public FuncPow(double a, double b) {
    this.a = a;
    this.b = b;
  }
  public double calc(double x) {
    return a * Math.pow(x, b);
  }
}
