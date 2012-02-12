package math.func.simple;
import math.vec.FastLoop;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 17:00:44
 */
// f(x) = c[0] + c[1]*x + c[2]*x*x+...
public class FuncPolynom implements Func {
  final private double[] c;
  public FuncPolynom(double[] c) {
    this.c = c;
  }
  public double calc(double x) {
    return FastLoop.polynom(c, x);
  }
}
