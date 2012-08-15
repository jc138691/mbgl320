package atom.energy.pw.lcr;
import atom.energy.HOvMtrx;
import atom.energy.pw.OvMtrx;
import atom.wf.WFQuadrD1;
import math.func.FuncVec;
import math.func.arr.IFuncArr;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 15/05/12, 1:04 PM
 */
public class PotHOvMLcr extends HOvMtrx {
public static Log log = Log.getLog(PotHOvMLcr.class);
public PotHOvMLcr(int L, IFuncArr basis, FuncVec pot, WFQuadrD1 quadr) {
  super(new PotHMtrxLcr(L, basis, pot, quadr));
  setOv(new OvMtrx(basis, quadr));
}
}