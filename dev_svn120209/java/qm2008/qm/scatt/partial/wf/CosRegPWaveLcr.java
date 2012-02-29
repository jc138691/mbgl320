package scatt.partial.wf;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.simple.FuncExp;
import math.func.simple.FuncExp2;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 24/02/12, 12:02 PM
 */
public class CosRegPWaveLcr extends FuncVec {
public static Log log = Log.getLog(CosPWaveLcr.class);
public CosRegPWaveLcr(WFQuadrLcr w, final double p, final int L
  , final double lambda) {
  super(w.getR(), new CosPWaveFunc(p, L));
  if (L > 0) {
    throw new IllegalArgumentException(log.error("todo L>0"));
  }
//  FuncVec expF = new FuncVec(w.getR(), new FuncExp2(-p*p*0.5));
  FuncVec expF = new FuncVec(w.getR(), new FuncExp(-lambda*0.5));
  addMultSafe(-1, expF);
  mult(w.getDivSqrtCR());                          // NOTE!!!  /qsrt(c+r)
  setX(w.getX());             // NOTE!!! but stores LCR as x
}
}