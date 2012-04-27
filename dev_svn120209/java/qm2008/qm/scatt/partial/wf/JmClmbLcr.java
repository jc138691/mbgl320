package scatt.partial.wf;

import atom.wf.coulomb.CoulombNmrvLcr;
import atom.wf.log_cr.FuncRToDivSqrtCR;
import atom.wf.log_cr.TransLcrToR;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.vec.Vec;
import math.vec.grid.StepGrid;
import scatt.Scatt;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 13/05/2010, 4:43:37 PM
 */
public class JmClmbLcr extends FuncArr {
public static Log log = Log.getLog(JmClmbLcr.class);
private WFQuadrLcr quadr;
private Vec engs;

public JmClmbLcr(int L, double Z, Vec engs, double maxSysEng, WFQuadrLcr w) {
  super(w.getR(), engs.size());
  quadr = w;
  this.engs = engs;
  TransLcrToR trans = w.getLcrToR();
  if (size() == 0)
    return;
  if (!(w.getX() instanceof StepGrid)) {
    throw new IllegalArgumentException(log.error("w.getX() must be instanceof StepGrid"));
  }
  for (int i = 0; i < engs.size(); i++) {
    double E = engs.get(i);                         log.dbg("E = ", E);
    if (E < 0  ||  E > maxSysEng)
      continue; //ignore bound states  and closed channels
    FuncVec f = CoulombNmrvLcr.calc(L, Z, E, trans, false);
    set(i, f);
  }
  mult(new FuncRToDivSqrtCR(w.getLcrToRFunc())); // NOTE!!!  /qsrt(c+r)
  setX(w.getX());             // NOTE!!! but stores LCR as x
}
public WFQuadrLcr getQuadrLcr() {
  return quadr;
}
public void normToE() {
  normToE(engs);
}
private void normToE(Vec engs) {    // delta(E-E')
  for (int i = 0; i < size(); i++) {
    double E = engs.get(i);
    if (E <= 0)
      continue;
    double k = Scatt.calcMomFromE(E);
    double norm = Math.sqrt(2. / (Math.PI * k));
    get(i).mult(norm);
  }
}
}
