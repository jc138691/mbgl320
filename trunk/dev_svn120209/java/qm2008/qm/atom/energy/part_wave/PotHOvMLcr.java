package atom.energy.part_wave;
import atom.energy.HOvMtrx;
import atom.wf.WFQuadr;
import math.func.FuncVec;
import math.func.arr.IFuncArr;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.mtrx.MtrxFactory;
import math.mtrx.jamax.EigenSymm;
import math.vec.Vec;
import math.vec.VecDbgView;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 15/05/12, 1:04 PM
 */
public class PotHOvMLcr extends HOvMtrx {
public static Log log = Log.getLog(PotHOvMLcr.class);
public PotHOvMLcr(int L, IFuncArr basis, FuncVec pot, WFQuadr quadr) {
  super(new PotHMtrxLcr(L, basis, pot, quadr));
  setOv(new OvMtrx(basis, quadr));
}
}