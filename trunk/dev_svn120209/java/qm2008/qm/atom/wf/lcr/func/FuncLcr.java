package atom.wf.lcr.func;

import math.func.Func;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 04/03/2010, 9:38:37 AM
 */
public abstract class FuncLcr implements Func {
  // f(x) = r,
  // x = ln(CR), CR=c+r; c > 0
  // exp(x) = c + r;  r = exp(x) - c
  protected final double c;
  public FuncLcr(final double c) {
    this.c = c;
  }

  public FuncLcr(FuncLcr from) {
    this.c = from.c;
  }

  public final double getC() {
    return c;
  }
}