package atom.energy.part_wave;
import atom.wf.WFQuadr;
import math.func.FuncVec;
import math.func.arr.IFuncArr;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.mtrx.MtrxFactory;
import math.mtrx.jamax.EigenSymm;
import math.vec.DbgView;
import math.vec.VecDbgView;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 15/05/12, 1:04 PM
 */
public class PotHAMtrxLcr extends PotHMtrxLcr {
public static Log log = Log.getLog(PotHAMtrxLcr.class);
private Mtrx ovM;
public PotHAMtrxLcr(int L, IFuncArr basis, FuncVec pot, WFQuadr quadr) {
  super(L, basis, pot, quadr);
}
public void calc() {  log.setDbg();
  super.calc();
  ovM = new OverHMtrx(getBasis(), getQuadr());
  log.dbg("OverHMtrx=\n", new MtrxDbgView(ovM));

  EigenSymm ovEig = new EigenSymm(ovM, false);
  Mtrx vec = ovEig.getV();
  log.dbg("ovEig.getVec()=\n", new MtrxDbgView(vec));

  double[] vals = ovEig.getRealEVals();
  log.dbg("ovEig.getVal()=\n", new VecDbgView(vals));

  Mtrx D = ovEig.getD();
  log.dbg("ovEig.getD()=\n", new MtrxDbgView(D));

  Mtrx test = vec.times(D).times(vec.transpose());
  log.dbg("test=\n", new MtrxDbgView(test));

  MtrxFactory.makeSqrt(D);
  Mtrx A = vec.times(D);
}
}