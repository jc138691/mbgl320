package scatt.partial.wf;
import math.func.Func;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 23/02/12, 1:44 PM
 */
public class CosPWaveFunc implements Func {
  public static Log log = Log.getLog(CosPWaveFunc.class);
  private int L;
  private double p;
  public CosPWaveFunc(double p, int L) {
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