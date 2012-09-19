package atom.wf.dcr.func;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 17/09/12, 10:40 AM
 */
public class FuncDcrToCR2 extends FuncDcrToCr {
  // f(x)=CR^2
  public double calc(double x) {
    double cr = super.calc(x);
    return cr * cr;
  }
}