package scatt.partial.wf;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 19/11/2008, Time: 12:34:34
 */
public class KMtrxFuncArrR extends FuncArr {
  public static String HELP = "Partial K-matrix K(E, r) (by numerical match)";
  public static Log log = Log.getLog(KMtrxEngR.class);
  public KMtrxFuncArrR(EngFuncArr arr) {  // TODO L
    super(arr.getX(), arr.size());
    load(arr);
  }
  protected void load(EngFuncArr arr) {
    Vec eng = arr.getEng();
    Vec x = getX();
    for (int ie = 0; ie < eng.size(); ie++) {
      double E = eng.get(ie);                     log.dbg("E = ", E);
      FuncVec res = getFunc(ie);
      FuncVec f = arr.getFunc(ie);                log.dbg("f = ", f);
      for (int ix = 0; ix < x.size(); ix++) {
        double K = 0;
        if (ix > 0) {
          K = KMtrxR.calc(f, E, ix-1);
        }
        res.set(ix, K);
      }
      log.dbg("res = ", res);
    }
  }
}
