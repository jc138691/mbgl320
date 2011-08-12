package math.func.simple;
import math.Mathx;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 16:23:50
 */
public class FuncPowInt implements Func {
  private final double a;
  private final int k;
  public FuncPowInt(double a, int k) {
    this.a = a;
    this.k = k;
  }
  public double calc(double x) {
    return a * Mathx.pow(x, k);
  }
}