package atom.wf.lcr.func;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 14/05/2010, 5:04:17 PM
 */
public class FuncLcrToSqrtCr extends FuncLcrToCr {
  // f(x) = sqrt(CR)
  public double calc(double x) {
    double cr = super.calc(x);
    return Math.sqrt(cr);
  }
}