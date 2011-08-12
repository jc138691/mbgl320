package atom.wf;
import math.func.arr.FuncArr;
import math.func.arr.NormFuncArr;
import math.integral.Quadr;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 23/07/2008, Time: 13:55:27
 */
public class WFArrL extends NormFuncArr {
  public static Log log = Log.getLog(WFArrL.class);
  private int L;
  public WFArrL(int L, Quadr w, FuncArr from) {
    super(w, from);
    this.L = L;
  }
  public WFArrL(int L, Quadr w) {
    super(w);
    this.L = L;
  }
  public int getL() {
    return L;
  }
}
