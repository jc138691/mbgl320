package atom.wf.log_cr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:45:42
 */
public class FuncLcrToDivSqrtCr extends FuncLcrToCr {
  // f(x) = 1./sqrt(CR) 
  public double calc(double x) {
    double cr = super.calc(x);
    return 1. / Math.sqrt(cr);
  }
}
