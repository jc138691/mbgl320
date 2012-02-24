package scatt.partial.wf;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 23/02/12, 1:55 PM
 */
public class SinPWaveLcr extends FuncVec {
public static Log log = Log.getLog(SinPWaveLcr.class);
public SinPWaveLcr(WFQuadrLcr w, final double p, final int L) {
  super(w.getR(), new SinPWaveFunc(p, L));
  if (L > 0) {
    throw new IllegalArgumentException(log.error("todo L>0"));
  }
  mult(w.getDivSqrtCR());                          // NOTE!!!  /qsrt(c+r)
  setX(w.getX());             // NOTE!!! but stores LCR as x
}
}
