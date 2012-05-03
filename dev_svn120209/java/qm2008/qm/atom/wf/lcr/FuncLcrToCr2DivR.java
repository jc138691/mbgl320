package atom.wf.lcr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:32:22
 */
public class FuncLcrToCr2DivR extends FuncLcrToR {
  // f(x)=CR^2/r
  public FuncLcrToCr2DivR(double p) {
    super(p);
  }

  public double calc(double x) {
    double r = super.calc(x);
    if (r == 0)
      return 0;
    double cr = r + getC();
    return cr * cr / r;
  }
}
