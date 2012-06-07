package d1;
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
private int[] momN; // momentum number
private double[] mom; // momenta
private double[] engs; // engs
public BoxTrigOrth(WFQuadrD1 quadr, BoxTrigOpt model) {
  super(quadr.getX());
  this.quadr = quadr;
  this.opt = model;
  int size = 2 * model.getMomN() + 1;
  mom = new double[size];
  engs = new double[size];
  momN = new int[size];
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
  mom[i] = 0;
  engs[i] = 0;
  momN[i++] = 0;
  if (opt.getMomN() == 0)
    return;
  for (int n = 1; n <= opt.getMomN(); n++) {
    double p = 2. * Math.PI * n / opt.getBoxLen();
    mom[i] = p;
    engs[i] = 0.5 * p * p;
    momN[i++] = n;
    add(new FuncVec(getX(), new FuncCos2(p)));
    mom[i] = p;
    engs[i] = 0.5 * p * p;
    momN[i++] = n;
    add(new FuncVec(getX(), new FuncSin2(p)));
  }
}
private void norm() {
  double L = opt.getBoxLen();
  for (int i = 0; i < momN.length; i++) {
    int n = momN[i];
    double normN = Math.sqrt((2. - Mathx.dlt(0, n)) / L);
    get(i).mult(normN);
  }
}
}
