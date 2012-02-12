package scatt.partial.wf;

import math.func.Func;
import math.func.FuncVec;
import math.vec.Vec;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 14/05/2010, 9:06:26 AM
 */
public class CosPartPWaveR extends FuncVec {
  public static Log log = Log.getLog(CosPartPWaveR.class);
  public CosPartPWaveR(final Vec x, final double p, final int L) {
    super(x, new CosPartPWaveFunc(p, L));
    if (L > 0) {
      throw new IllegalArgumentException(log.error("todo L>0"));
    }
  }
}
class CosPartPWaveFunc implements Func {
  public static Log log = Log.getLog(CosPartPWaveFunc.class);
  private int L;
  private double p;
  public CosPartPWaveFunc(double p, int L) {
    this.p = p;
    this.L = L;
    if (L > 0) {
      throw new IllegalArgumentException(log.error("todo L>0"));
    }
  }
  public double calc(double r) {
    return Math.cos(p * r);
  }
}