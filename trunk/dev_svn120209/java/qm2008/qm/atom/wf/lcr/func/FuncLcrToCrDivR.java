package atom.wf.lcr.func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:38:20
 */
public class FuncLcrToCrDivR extends FuncLcrToR {
  // f(x)=CR/r
  public FuncLcrToCrDivR(double p) {
    super(p);
  }

  public double calc(double x) {
    double r = super.calc(x);
    if (Double.compare(r, 0.) == 0) {
      return 0.;
    }
    return Math.exp(x) / r;
  }
}
