package atom.wf.mm;
import atom.wf.lcr.WFQuadrLcr;
import math.func.FuncVec;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 3/05/12, 9:45 AM
 */
//                     oo oo
// Rmm(a, b; a2, b2) = I  I  dr ds    P(a; y) P(a2; y) U^k(r,s) P(b; x) P(b2; x)
//                     0  0
// = 2 * INTL dr P(a; y) P(a2; y) 1/y Ymm(b, b2, K, y); NOTE 1/y!!!  and *2.
//  U^k(r,s) = y^k/x^(k+1)   y = min(r,s)   x = max(r,s)
public class RkMm {
public static Log log = Log.getLog(RkMm.class);
public static double calc(WFQuadrLcr w, Vec a, Vec b, Vec a2, Vec b2, int K) {
//  log.setDbg();
  FuncVec yk = new YkMm(w, a, a2, K).calcYk();
//  log.dbg("yk=\n", new VecDbgView(yk));
  double res = 2. * w.calcPotDivR(b, b2, yk);  // NOTE!!!! *2.
  return res;
}
}
