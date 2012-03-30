package scatt.jm_2008.e2;
import atom.energy.ConfHMtrx;
import flanagan.complex.Cmplx;
import math.Mathx;
import math.complex.CmplxMtrx;
import math.func.arr.FuncArr;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.e1.ScttMthdBaseE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.jm_2008.jm.target.JmCh;
import scatt.jm_2008.jm.target.ScattTrgtE2;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 13/03/12, 11:36 AM
 */
public class ScttMthdBaseE2 extends ScttMthdBaseE1 {
public static Log log = Log.getLog(ScttMthdBaseE2.class);
protected static final int IDX_IONIZ = 1;
protected ScattTrgtE2 trgtE2;
protected ConfHMtrx sysConfH;
private FuncArr trgtBasisN;
protected JmCh[] chArr;
public LgrrOrthLcr getOrthonNt() {
  return orthonNt;
}
public void setOrthonNt(LgrrOrthLcr orthonNt) {
  this.orthonNt = orthonNt;
}
protected LgrrOrthLcr orthonNt;

public ScttMthdBaseE2(CalcOptE1 calcOpt) {
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
protected void calcCrossSecs(int i, ScattRes res, CmplxMtrx mS, int chNum) {
//  int chNum = getChNum();
  Mtrx mCrss = res.getCrossSecs();
  Mtrx mTics = res.getTics();
  double ionSum = 0;
  int initChIdx = trgtE2.getInitTrgtIdx();
  for (int to = 0; to < chNum; to++) {
    log.dbg("to = ", to);  // Target channels
    JmCh ch = chArr[to];
    double k0 = chArr[initChIdx].getAbsMom();
    double k02 = k0 * k0;

    Cmplx S = mS.get(to, initChIdx);
    S = S.minus(Mathx.dlt(to, initChIdx));

    double sigma = Math.PI * S.abs2() / k02;

    log.dbg("sigma = ", sigma).eol();
    //      if (i == 0) { // store channels energies
    //        mCs.set(0, 0, 0); // init the corner
    //        mCs.set(0, to + 1, ch.getEng());  // NOTE +1; first row has incident energies
    //      }
    mCrss.set(i, to + 1, sigma);  // NOTE +1; first column has incident energies
    if (trgtE2.getEngs().get(to) > trgtE2.getIonGrndEng()) {  // sum up all positive energy target states
      ionSum += sigma;
    }
  }
  mTics.set(i, IDX_IONIZ, ionSum);
}
protected JmCh[] loadChArr(double sysEng) {
  LgrrModel jmModel = calcOpt.getLgrrModel();
  Vec tEngs = trgtE2.getEngs();
  JmCh[] res = new JmCh[tEngs.size()];
  for (int i = 0; i < tEngs.size(); i++) {
    log.dbg("i = ", i);
    // NOTE!!! minus in "-trgtE2.getScreenZ()"
    res[i] = new JmCh(sysEng, tEngs.get(i), jmModel, -trgtE2.getScreenZ());
    log.dbg("res[i]=", res[i]);
  }
  return res;
}
protected int calcShowChNum() {
  EngModel engModel = calcOpt.getGridEng();
  double maxScattE = engModel.getLast();
  return calcOpenChNum(maxScattE);
}
protected int calcOpenChNum(double scattE) {
  double maxTotSysE = trgtE2.getInitTrgtEng() + scattE;
  int tN = getChNum();
  int chIdx = 0;
  Vec tEngs = trgtE2.getEngs();
  for (chIdx = 0; chIdx < tN; chIdx++) {     //log.dbg("t = ", t);  // Target channels
    double chE = tEngs.get(chIdx); // channel eng
    if (chE > maxTotSysE) {
      return chIdx;
    }
  }
  return tN;
}

}
