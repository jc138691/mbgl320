package atom.wf.lcr.func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:39:26
 */
public class FuncLcrToDivR extends FuncLcrToR {
  // f(x)=1/r
  public FuncLcrToDivR(double p) {
    super(p);
  }

  public double calc(double x) {
    double r = super.calc(x);
    if (r == 0)
      return 0;
    return 1. / r;
  }
}
