package atom.wf.lcr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:31:20
 */
public class FuncLcrToCr2DivR2 extends FuncLcrToCrDivR {
  // f(x)=(CR/r)^2
  public FuncLcrToCr2DivR2(double p) {
    super(p);
  }

  public double calc(double x) {
    double yr = super.calc(x);
    if (Double.compare(yr, 0.) == 0) {
      return 0.;
    }
    return yr * yr;
  }
}
