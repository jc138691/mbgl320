package atom.energy.part_wave;
import atom.wf.WFQuadr;
import math.func.FuncVec;
import math.func.arr.IFuncArr;
import math.mtrx.Mtrx;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 15/05/12, 2:26 PM
 */
public class OverHMtrx extends Mtrx {
public static Log log = Log.getLog(OverHMtrx.class);
private final IFuncArr basis;
private final WFQuadr quadr;
public OverHMtrx(IFuncArr basis, WFQuadr quadr) {
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
}
}
