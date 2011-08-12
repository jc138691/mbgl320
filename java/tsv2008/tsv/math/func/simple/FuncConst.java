package math.func.simple;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 10/07/2008, Time: 16:54:10
 */
public class FuncConst implements Func {
  final private double c0;
  public FuncConst(double v) {
    c0 = v;
  }
  final public double calc(double x) {
    return c0;
  }
}
