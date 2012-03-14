package scatt.partial.wf;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 14/03/12, 11:34 AM
 */
public class CosRegK2 extends CosRegPWaveLcr {
public CosRegK2(WFQuadrLcr w, final double p, final int L, final double lambda) {
  super(w, p, L, lambda);
  mult(Math.sqrt(2. / p));
}
}

