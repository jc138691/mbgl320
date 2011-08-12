package atom.wf.log_cr;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:25:13
 */
public class FuncRToLcr implements Func {
  // f(x) = ln(CR), CR=c+r; c > 0
  private final double c;
  public FuncRToLcr(double c) {
    this.c = c;
  }
  public FuncRToLcr(double x0, double r0) {
    this.c = Math.exp(x0) - r0;
  }

  public double calc(double r) {
    return Math.log(c + r);
  }
}