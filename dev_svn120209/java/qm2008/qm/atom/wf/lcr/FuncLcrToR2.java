package atom.wf.lcr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:28:09
 */
public class FuncLcrToR2 extends FuncLcrToR {
  // f(x) = r*r
  public FuncLcrToR2(double c) {
    super(c);
  }

  public double calc(double x) {
    double r = super.calc(x);
    return r * r;
  }
}
