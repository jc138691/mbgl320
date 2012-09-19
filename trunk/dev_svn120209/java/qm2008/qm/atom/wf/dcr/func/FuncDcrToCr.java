package atom.wf.dcr.func;
import math.func.Func;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 17/09/12, 10:41 AM
 */
public class FuncDcrToCr implements Func {
  // x=-1/CR; CR = -1/x
  public double calc(double x) {
    return -1./x;
  }
}
