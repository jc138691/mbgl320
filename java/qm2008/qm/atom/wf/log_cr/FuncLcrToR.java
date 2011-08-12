package atom.wf.log_cr;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:24:15
 */
public class FuncLcrToR extends FuncLcr {
  // f(x) = r,
  // x = ln(CR), CR=c+r; c > 0
  // exp(x) = c + r;  r = exp(x) - c
  public FuncLcrToR(final double c) {
    super(c);
  }
  public double calc(double x) {
    return Math.exp(x) - c;
  }
}
