package func.d1;
import atom.wf.WFQuadrD1;
import math.Mathx;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.simple.FuncConst;
import math.func.simple.FuncCos2;
import math.func.simple.FuncSin2;
import scatt.jm_2008.jm.laguerre.IWFuncArr;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 5/06/12, 10:08 AM
 */
//  sin & cos trigonometric functions
// as basis of a Periodic Box
public class BoxTrigOrth extends FuncArr implements IWFuncArr {
private WFQuadrD1 quadr;
private BoxTrigOpt opt;
private int[] idxToN;
public BoxTrigOrth(WFQuadrD1 quadr, BoxTrigOpt model) {
  super(quadr.getX());
  this.quadr = quadr;
  this.opt = model;
  idxToN = new int[2 * model.getMomN() + 1];
  load();
  norm();
}
public WFQuadrD1 getQuadr() {
  return quadr;
}
private void load() {
  if (opt.getMomN() < 0)
    return;
  add(new FuncVec(getX(), new FuncConst(1.)));
  int i = 0;
  idxToN[i++] = 0;
  if (opt.getMomN() == 0)
    return;
  for (int n = 1; n <= opt.getMomN(); n++) {
    double k = 2. * Math.PI * n / opt.getBoxLen();
    idxToN[i++] = n;
    add(new FuncVec(getX(), new FuncCos2(k)));
    idxToN[i++] = n;
    add(new FuncVec(getX(), new FuncSin2(k)));
  }
}
private void norm() {
  double L = opt.getBoxLen();
  for (int i = 0; i < idxToN.length; i++) {
    int n = idxToN[i];
    double normN = Math.sqrt((2. - Mathx.dlt(0, n)) / L);
    get(i).mult(normN);
  }
}
}
