package math.func.deriv;
import math.func.FuncVec;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 16:23:46
 */
public class DerivFactory {
  public static FuncVec makeDeriv(FuncVec f) {
    FuncVec res = null;
    if (f.size() >= 9) {
      res = new DerivPts9(f);
    } else if (f.size() >= 5) {
      res = new DerivPts5(f);
    } else if (f.size() >= 3) {
      res = new DerivPts3(f);
    }
    return res;
  }
}
