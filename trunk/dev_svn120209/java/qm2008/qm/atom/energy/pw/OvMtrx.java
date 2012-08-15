package atom.energy.pw;
import atom.wf.WFQuadrD1;
import math.func.FuncVec;
import math.func.arr.IFuncArr;
import math.mtrx.MtrxDbgView;
import math.mtrx.api.Mtrx;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 15/05/12, 2:26 PM
 */
public class OvMtrx extends Mtrx {
public static Log log = Log.getLog(OvMtrx.class);
private final IFuncArr basis;
private final WFQuadrD1 quadr;
public OvMtrx(IFuncArr basis, WFQuadrD1 quadr) {
  super(basis.size(), basis.size());
  this.basis = basis;
  this.quadr = quadr;
  calc();
}
public void calc() {   log.setDbg();
  for (int r = 0; r < basis.size(); r++) {      //log.dbg("row=", r);
    FuncVec fr = basis.getFunc(r);
    for (int c = r; c < basis.size(); c++) {    //log.dbg("col=", c);
      FuncVec fc = basis.getFunc(c);
      double res = quadr.calcInt(fr, fc);  log.dbg("potE=", res);
      set(r, c, res);
      set(c, r, res);
    }
  }
  log.dbg("OvMtrx=", new MtrxDbgView(this));
}
}
