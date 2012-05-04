package math.interpol;
import math.func.FuncVec;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 14:23:18
 */
public class PolynIntrp {
  public static Log log = Log.getLog(PolynIntrp.class);

  // CFuncPow      at small r,  f(r) = a * pow(r, b);
  public static double calcPowerSLOW(FuncVec func, int idx) {
    Vec xf = func.getX();
    if (idx >= func.size() - 1) {
      String mssg = "idx >= func.size() - 1";
      throw new IllegalArgumentException(log.error(mssg));
    }
    if (xf.get(idx) == 0 && xf.get(idx + 1) == 0) {
      String mssg = "func.x.getLine(idx) == 0 && func.x.getLine(idx++) == 0";
      throw new IllegalArgumentException(log.error(mssg));
    }
    int i = idx;
    if (xf.get(idx) == 0) {
      i++; // try to recover
      if (i >= func.size() - 1) {
        String mssg = "i >= func.size() - 1";
        throw new IllegalArgumentException(log.error(mssg));
      }
    }
    double f = func.get(i);
    double f2 = func.get(i + 1);
    if (f2 / f < 1.) {
      String mssg = "f2 / f < 1.  " + (float) f2 + "/" + (float) f;
      throw new IllegalArgumentException(log.error(mssg));
    }
    double x = xf.get(i);
    double x2 = xf.get(i + 1);
    if (x2 / x < 1.) {
      String mssg = "x2/x < 1.  " + (float) x2 + "/" + (float) x;
      throw new IllegalArgumentException(log.error(mssg));
    }
    double res = Math.log(f2 / f) / Math.log(x2 / x);
    return res;
  }
}