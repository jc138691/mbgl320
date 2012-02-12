package math.func.simple;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 26/09/2008, Time: 16:26:08
 */
public class FuncQuadr implements Func {
  private final double a;
  private final double b;
  private final double c;
  public FuncQuadr(double a, double b, double c) {
    this.a = a;
    this.b = b;
    this.c = c;
  }
  public double calc(double x) {
    return a * x * x + b * x + c;
  }
}
