package scatt.partial.wf;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.simple.FuncExp2;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 29/02/12, 8:42 AM
 */
public class SinRegPWaveLcr  extends FuncVec {
public static Log log = Log.getLog(CosPWaveLcr.class);
public SinRegPWaveLcr(WFQuadrLcr w, final double p, final int L) {
  super(w.getR(), new SinPWaveFunc(p, L));
  if (L > 0) {
    throw new IllegalArgumentException(log.error("todo L>0"));
  }
  FuncVec expF = new FuncVec(w.getR(), new FuncExp2(-p)); // NOTE! Exp2
  expF.mult(-1);
  expF.add(1);// (1-exp(-p r^2))
  mult(expF);
  mult(w.getDivSqrtCR());                          // NOTE!!!  /qsrt(c+r)
  setX(w.getX());             // NOTE!!! but stores LCR as x
}
}