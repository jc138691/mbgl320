package scatt.partial.wf.k_matrix_idea;
import javax.utilx.log.Log;

import math.vec.Vec;
import math.func.FuncVec;
import scatt.partial.wf.eng_arr_not_used.EngFuncArr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 19/11/2008, Time: 10:07:06
 */
public class KMtrxEngR extends FuncVec {
  public static String HELP = "Partial KMatrix (by numerical match)";
  public static Log log = Log.getLog(KMtrxEngR.class);
  public KMtrxEngR(EngFuncArr arr) {  // TODO L
    super(arr.getEng());
    load(arr);
  }
  protected void load(EngFuncArr arr) {
    Vec eng = arr.getEng();
    for (int i = 0; i < eng.size(); i++) {
      double E = eng.get(i);                     log.dbg("E = ", E);
      FuncVec f = arr.getFunc(i);
      double K = KMtrxR.calc(f, E);              log.dbg("K(E) = ", K);
      set(i, K);
    }
  }
}
