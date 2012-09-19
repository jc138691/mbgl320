package atom.wf.lcr.func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:41:25
 */
public class FuncLcrToCR2 extends FuncLcrToCr {
  // f(x)=CR^2
  public double calc(double x) {
    double cr = super.calc(x);
    return cr * cr;
  }
}
