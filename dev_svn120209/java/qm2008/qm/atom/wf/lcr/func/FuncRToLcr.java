package atom.wf.lcr.func;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:25:13
 */
public class FuncRToLcr extends FuncLcr {
  // f(x) = ln(CR), CR=c+r; c > 0
  public FuncRToLcr(double x0, double r0) {
    super(Math.exp(x0) - r0);
  }

  public double calc(double r) {
    return Math.log(c + r);
  }
}