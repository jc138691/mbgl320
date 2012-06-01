package scatt.partial.wf;
import atom.wf.lcr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.simple.FuncExp;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 24/02/12, 12:02 PM
 */
public class CosRegWfLcr extends FuncVec {
public static Log log = Log.getLog(CosPWaveLcr.class);
public CosRegWfLcr(WFQuadrLcr w, final double p, final int L
  , final double lambda) {
  super(w.getR(), new CosLFunc(p, L));
  if (L > 0) {
    throw new IllegalArgumentException(log.error("todo L>0"));
  }

  // TESTED!!! THIS WORKS PERFECTLY
  // This is very stable: tested with -lambda and -lambda/4; no noticeable differences
  FuncVec expF = new FuncVec(w.getR(), new FuncExp(-lambda*0.5));

  addMultSafe(-1, expF);
  multSelf(w.getDivSqrtCR());                          // NOTE!!!  /qsrt(c+r)
  setX(w.getX());             // NOTE!!! but stores LCR as x
}
}