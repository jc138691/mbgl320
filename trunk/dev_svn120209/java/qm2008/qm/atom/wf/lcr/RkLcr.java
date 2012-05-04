package atom.wf.lcr;
import math.func.FuncVec;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 14:19:41
 */
// from F.Fisher "The hartree-fock method for atoms", p18
//                     oo oo
// R^k(a, b; a2, b2) = I  I  dr ds    P(a; r) P(a2; r) U^k(r,s) P(b; s) P(b2; s)
//                     0  0
// = INTL dr P(a; r) P(a2; r) 1/r Y(b, b2, K, r); NOTE 1/r!!!
//  U^k(r,s) = y^k/x^(k+1)   y = min(r,s)   x = max(r,s)
public class RkLcr {
public static Log log = Log.getLog(RkLcr.class);
public static double calc(WFQuadrLcr w, Vec a, Vec b, Vec a2, Vec b2, int K) {
//  log.setDbg();
  FuncVec yk = new YkLcr(w, b, b2, K).calcYk();
//  log.dbg("yk=\n", new VecDbgView(yk));
//    double res = FastLoop.dot(a, a2, yk, w.getWithCR2DivR());
  double res = w.calcPotDivR(a, a2, yk);
  return res;
}
}
