package scatt.jm_2008.e2;
import atom.energy.ConfHMtrx;
import math.func.arr.FuncArr;
import math.vec.Vec;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.e1.ScattMethodBaseE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.target.ScattTrgtE2;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 13/03/12, 11:36 AM
 */
public class ScattMethodBaseE2 extends ScattMethodBaseE1 {
public static Log log = Log.getLog(ScattMethodBaseE2.class);
protected ScattTrgtE2 trgtE2;
protected ConfHMtrx sysConfH;
private FuncArr trgtBasisN;
public ScattMethodBaseE2(CalcOptE1 calcOpt) {
  super(calcOpt);
}
@Override
public ScattRes calc(Vec engs) {
  throw new IllegalArgumentException(log.error("call relevant implementation of calc(Vec engs)"));
}
public ScattTrgtE2 getTrgtE2() {
  return trgtE2;
}
public void setTrgtE2(ScattTrgtE2 trgtE2) {
  this.trgtE2 = trgtE2;
}
public void setSysConfH(ConfHMtrx sysConfH) {
  this.sysConfH = sysConfH;
//  setPotH(sysConfH);
//    setSysEngs(sysConfH.getEigVal(H_OVERWRITE));
}
public int getChNum() { // number of target channels
  return trgtE2.getEngs().size();
}
public void setTrgtBasisN(FuncArr trgtBasisN) {
  this.trgtBasisN = trgtBasisN;
}
public FuncArr getTrgtBasisN() {
  return trgtBasisN;
}
}
