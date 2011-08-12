package atom.wf.log_cr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:42:52
 */
public class FuncLcrToDivCr extends FuncLcrToCr {
  // f(x)=1/CR
  public double calc(double x) {
    double cr = super.calc(x);
    return 1. / cr;
  }
}
