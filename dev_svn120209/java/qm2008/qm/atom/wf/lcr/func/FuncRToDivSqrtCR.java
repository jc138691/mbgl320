package atom.wf.lcr.func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 10:20:29
 */
public class FuncRToDivSqrtCR extends FuncLcr {
  public FuncRToDivSqrtCR(final double c) {
    super(c);
  }
  public FuncRToDivSqrtCR(final FuncLcr from) {
    super(from);
  }
  // f(r)=1/sqrt(c+r)
  public double calc(double r) {
    return 1. / Math.sqrt(c + r);
  }
}
