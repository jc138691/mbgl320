package scatt.jm_2008.e2;
import atom.energy.LsConfHMtrx;
import flanagan.complex.Cmplx;
import math.Mathx;
import math.complex.CmplxMtrx;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.mtrx.api.Mtrx;
import math.vec.IntVec;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.VecFactory;
import scatt.eng.EngOpt;
import scatt.eng.auto.AutoEngFactory;
import scatt.jm_2008.e1.JmCalcOptE1;
import scatt.jm_2008.e1.ScttMthdBaseE1;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.jm_2008.jm.target.JmCh;
import scatt.jm_2008.jm.target.ScttTrgtE2;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 13/03/12, 11:36 AM
 */
public class ScttMthdBaseE2 extends ScttMthdBaseE1 {
public static Log log = Log.getLog(ScttMthdBaseE2.class);
protected static final int IDX_IONIZ = 1;
public ScttTrgtE2 trgtE2;
protected LsConfHMtrx sysConfH;
protected LgrrOrthLcr orthNt;
//protected FuncArr lgrrN;  // use trgtE2.statesE1
protected CmplxMtrx jmS;
protected int calcChN;
protected int openChN;

public int getCalcChN() {
  return calcChN;
}
public int getOpenChN() {
  return openChN;
}
public LgrrOrthLcr getOrthNt() {
  return orthNt;
}
public void setOrthNt(LgrrOrthLcr orthNt) {
  this.orthNt = orthNt;
}

public ScttMthdBaseE2(JmCalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScttRes calcAutoScttEngs(IntVec points) {   log.setDbg();
  log.dbg("tEngs=", new VecDbgView(getTrgtE2().getEngs()));
  Vec tEngs = AutoEngFactory.makeEngs(getTrgtE2().getEngs(), points);   log.dbg("tEngs=", new VecDbgView(tEngs));
  tEngs.add(-getTrgtE2().getInitTrgtEng());

  Vec sEngs = AutoEngFactory.makeEngs(getSysEngs(), new IntVec(new int[]{10}));   log.dbg("sEngs=", new VecDbgView(sEngs));
  sEngs.add(-getTrgtE2().getInitTrgtEng());
  tEngs.append(sEngs);
  tEngs.sortSelf();

  EngOpt engModel = calcOpt.getGridEng();
  tEngs = VecFactory.crop(tEngs, engModel.getFirst(), engModel.getLast());  log.dbg("crop(tEngs)=", new VecDbgView(tEngs));

  return calc(tEngs);
}
@Override
public ScttRes calc(Vec engs) {
  throw new IllegalArgumentException(log.error("call relevant implementation of calc(Vec engs)"));
}
public ScttTrgtE2 getTrgtE2() {
  return trgtE2;
}
public void setTrgtE2(ScttTrgtE2 trgtE2) {
  this.trgtE2 = trgtE2;
}
public void setSysConfH(LsConfHMtrx sysConfH) {
  this.sysConfH = sysConfH;
//  setPotH(sysConfH);
//    setSysEngs(sysConfH.getEigEngs(H_OVERWRITE));
}
public int getChNum() { // number of target channels
  return trgtE2.getEngs().size();
}
public void setWfsE1(FuncArr basisN) {
  trgtE2.setWfsE1(basisN);
}
public FuncArr getWfsE1() {
  return trgtE2.getWfsE1();
}
protected void saveCrossSecs(int i, ScttRes res, CmplxMtrx mS, int openNum) {
  Mtrx mCrss = res.getCrossSecs();
  Mtrx mTics = res.getTics();
  double ionSum = 0;
  int initChIdx = trgtE2.getInitTrgtIdx();
  for (int to = 0; to < openNum; to++) {          //log.dbg("to = ", to);  // Target channels
    JmCh ch = chArr[to];
    double k0 = chArr[initChIdx].getAbsMom();
    double k02 = k0 * k0;

    Cmplx S = mS.get(to, initChIdx);
    S = S.minus(Mathx.dlt(to, initChIdx));

    double sigma = Math.PI * S.abs2() / k02;    //log.dbg("sigma = ", sigma).eol();
    mCrss.set(i, to + 1, sigma);  // NOTE +1; first column has incident energies
    if (trgtE2.getEngs().get(to) > trgtE2.getIonGrndEng()) {  // sum up all positive energy target states
      ionSum += sigma;
    }
  }
  mTics.set(i, IDX_IONIZ, ionSum);
}
protected void saveMtrxR(int i, ScttRes res, Mtrx fromR, int openNum) {
  // THIS also makes & loads mRk
  Mtrx mR = res.getMtrxR();
  Mtrx mRk = res.getMtrxRk();
  if (mRk == null) {
    mRk = mR.copy();
    res.setMtrxRk(mRk);
  }
  int initChIdx = trgtE2.getInitTrgtIdx();
  for (int to = 0; to < openNum; to++) {    log.dbg("to = ", to);  // Target channels
    double k0 = chArr[initChIdx].getAbsMom();
    double r = fromR.get(to, initChIdx);
    double rk = r / k0;
    mR.set(i, to + 1, r);  // NOTE +1; first column has incident energies
    mRk.set(i, to + 1, rk);

    double ei = mR.get(i, IDX_ENRGY);
    mRk.set(i, IDX_ENRGY, ei);
  }
}
protected JmCh[] loadChArr(double sysEng) {
  LgrrOpt jmModel = calcOpt.getBasisOpt();
  Vec tEngs = trgtE2.getEngs();
  JmCh[] res = new JmCh[tEngs.size()];
  for (int i = 0; i < tEngs.size(); i++) {    //log.dbg("i = ", i);
    // NOTE!!! minus in "-trgtE2.getScreenZ()"
    res[i] = new JmCh(sysEng, tEngs.get(i), jmModel, -trgtE2.getScreenZ());
    //log.dbg("res[i]=", res[i]);
  }
  return res;
}
protected int calcPrntChNum() {
  EngOpt engModel = calcOpt.getGridEng();
  double maxScattE = engModel.getLast();
  return calcOpenChNum(maxScattE);
}
protected int calcOpenChNum(double scattE) {
  double maxTotSysE = trgtE2.getInitTrgtEng() + scattE;

//  if (maxTotSysE > 0) {// DEBUG
//    maxTotSysE /= 2;  log.dbg("TEST maxTotSysE /= 2=", maxTotSysE);
//  }

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
protected int calcNumTrgtCont() {
  int count = 0;
  Vec tEngs = trgtE2.getEngs();
  for (int chIdx = 0; chIdx < tEngs.size(); chIdx++) {
    double chE = tEngs.get(chIdx); // channel eng
    if (chE > trgtE2.getIonGrndEng()) {
      count++;
    }
  }
  return count;
}
protected int calcCalcChNum(double scattE) {
  int openN = calcOpenChNum(scattE);
//  return openN;  // DEBUG
  int res = openN + calcOpt.getUseClosedNum();
  int tN = getChNum();
  return Math.min(tN, res);
}

public FuncArr makeSinWfs(double sTotE, int chNum) {
  Vec x = quadr.getX();
  FuncArr res = new FuncArr(x);
  Vec tEngs = trgtE2.getEngs();
  for (int t = 0; t < chNum; t++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(t);     // target state eng
    double tScattE = sTotE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec wf = calcChSinWf(tScattE, quadr);
    res.add(wf);
  }
  return res;
}
// Delete orthonormal from given
public FuncArr makeSinDelN(double sTotE, int chNum) {
  Vec x = quadr.getX();
  FuncArr res = new FuncArr(x);
  Vec tEngs = trgtE2.getEngs();
  for (int t = 0; t < chNum; t++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(t);     // target state eng
    double tScattE = sTotE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec wf = calcSinDelN(tScattE, quadr, orth);
    res.add(wf);
  }
  return res;
}
public FuncArr makeSinKeepN(double sTotE, int chNum) {
  Vec x = quadr.getX();
  FuncArr res = new FuncArr(x);
  Vec tEngs = trgtE2.getEngs();
  for (int t = 0; t < chNum; t++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(t);     // target state eng
    double tScattE = sTotE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec wf = calcSinKeepN(tScattE, quadr, orth);
    res.add(wf);
  }
  return res;
}
// Delete Lgrr from given
public FuncArr makeSinDLgrr(double sTotE, int chNum) {
  Vec x = quadr.getX();
  FuncArr res = new FuncArr(x);
  Vec tEngs = trgtE2.getEngs();
  for (int t = 0; t < chNum; t++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(t);     // target state eng
    double tScattE = sTotE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec wf = calcSinDelBi(tScattE, quadr, lgrr, lgrrBi);
    res.add(wf);
  }
  return res;
}
public FuncArr makeCosDelN(double sTotE, int chNum) {
  Vec x = quadr.getX();
  FuncArr res = new FuncArr(x);
  Vec tEngs = trgtE2.getEngs();
  for (int t = 0; t < chNum; t++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(t);     // target state eng
    double tScattE = sTotE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec wf = calcCosDelN(tScattE, quadr, orth, orth.getLambda());
    res.add(wf);
  }
  return res;
}
public FuncArr makeCosDLgrr(double sTotE, int chNum) {
  Vec x = quadr.getX();
  FuncArr res = new FuncArr(x);
  Vec tEngs = trgtE2.getEngs();
  for (int t = 0; t < chNum; t++) {     //log.dbg("t = ", t);  // Target channels
    double tE = tEngs.get(t);     // target state eng
    double tScattE = sTotE - tE;
    if (tScattE <= 0) {
      break;
    }
    FuncVec wf = calcCosDelBi(tScattE, quadr, lgrr, lgrrBi, orth.getLambda());
    res.add(wf);
  }
  return res;
}
}
