package scatt.partial.wf;
import math.func.FuncVec;
import math.func.Func;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 23/07/2008, Time: 16:32:32
 */
public class SinPartPWaveR extends FuncVec {
  public static Log log = Log.getLog(SinPartPWaveR.class);
  public SinPartPWaveR(final Vec x, final double p, final int L) {
    super(x, new SinPartPWaveFunc(p, L));
    if (L > 0) {
      throw new IllegalArgumentException(log.error("todo L>0"));
    }
  }
}
class SinPartPWaveFunc implements Func {
  public static Log log = Log.getLog(SinPartPWaveFunc.class);
  private int L;
  private double p;
  public SinPartPWaveFunc(double p, int L) {
    this.p = p;
    this.L = L;
    if (L > 0) {
      throw new IllegalArgumentException(log.error("todo L>0"));
    }
  }
  public double calc(double r) {
    return Math.sin(p * r);
  }
}
