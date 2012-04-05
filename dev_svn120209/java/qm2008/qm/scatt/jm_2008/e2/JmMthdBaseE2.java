package scatt.jm_2008.e2;
import flanagan.complex.Cmplx;
import math.Mathx;
import math.complex.CmplxMtrx;
import math.complex.CmplxMtrxDbgView;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import scatt.eng.EngGridFactory;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.target.JmCh;

import javax.utilx.log.Log;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,1/04/11,4:34 PM
 */
public abstract class JmMthdBaseE2 extends ScttMthdBaseE2 {
public static Log log = Log.getLog(JmMthdBaseE2.class);
private static double MAGIC_MAX_EPS = 0.00000001; // MAGIC NUMBERS!!!!!!!!!!!!!!!!!!!!!!!
private static int MAGIC_EPS_N = 1000; // MAGIC NUMBERS!!!!!!!!!!!!!!!!!!!!!!!
//protected static final int IDX_IONIZ = 1;
protected static final int SDCS_ENG_OFFSET = 1;
//  protected static final int SC_SYS_ENG_OFFSET = 1;
protected static final int SDCS_CH_OFFSET = 1;
//  protected static final int CS_ENG_OFFSET = 1;
protected static final int CS_CH_OFFSET = 1;
private int exclSysIdx = -1;
//protected JmCh[] chArr;
public JmMthdBaseE2(CalcOptE1 calcOpt) {
  super(calcOpt);
}
protected abstract Mtrx calcX();
public ScattRes calcSysEngs() {
  throw new IllegalArgumentException(log.error("TODO: sysEngs - trgtGrndEng"));
//  return calc(sysEngs);
}
private boolean isValidD(Vec overD, int nt) {
  if (overD.size() <= 2 || nt <= 2)   // something is wrong!
    return false;
  if (overD.size() <= nt)   // something is wrong!
    return false;
  double maxErr = calcOpt.getTestModel().getMaxIntgrlErr();
  for (int i = 0; i < overD.size(); i++) {
    double d_i = overD.get(i);
    if (i < nt && Math.abs(d_i) > maxErr) { // must be zero
      return false;
    }
    if (i == (overD.size() - 1) && Math.abs(d_i) < maxErr) { // [28Apr2011] ONLY THE LAST must NOT be zero
      return false;
    }
  }
  return true;
}
@Override
public void setOverD(Vec overD) {
  if (trgtE2 == null) {
    throw new IllegalArgumentException(log.error("trgtE2 == null; first call setTrgtE3(jmTrgt);"));
  }
  int nt = trgtE2.getNt();
  if (!isValidD(overD, nt)) {
    throw new IllegalArgumentException(log.error("!isValidD(overD, nt, N)"));
  }
  super.setOverD(overD);
}
@Override
public ScattRes calc(Vec engs) {          //JmMethodJmBasisE3.log.setDbg();
  ScattRes res = new ScattRes();
  int chNum = getChNum();
  int eN = engs.size();
  Mtrx X = calcX();
  log.dbg("X=", new MtrxDbgView(X));
  new JmResonE2(this).calc(res, X);
  Mtrx mCrss = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCrss);
  Mtrx mTics = new Mtrx(eN, 2);// ionisation cross section
  res.setTics(mTics);
  res.setSdcs(new Mtrx(chNum + 1, eN + 1));
  for (int engIdx = 0; engIdx < eN; engIdx++) {       log.info("i = ", engIdx);
    double scattE = engs.get(engIdx);    log.info("scattE = ", scattE);
    mCrss.set(engIdx, IDX_ENRGY, scattE);
    mTics.set(engIdx, IDX_ENRGY, scattE);                 // first column is for the energies
    double sysTotE = scattE + trgtE2.getInitTrgtEng();
    chArr = loadChArr(sysTotE);
    Mtrx W = calcW(sysTotE, X.getArray());    log.dbg("W=\n", new MtrxDbgView(W));
    CmplxMtrx zp = new CmplxMtrx(chNum, chNum); // Z^+
    CmplxMtrx zm = new CmplxMtrx(chNum, chNum); // Z^-
    loadZ(zp, zm, W, chArr);
    log.dbg("Z^+=\n", new CmplxMtrxDbgView(zp));
    log.dbg("Z^-=\n", new CmplxMtrxDbgView(zm));
    CmplxMtrx zpInv = null;
    try {
      zpInv = zp.inverse();      log.dbg("(Z^+)^{-1}=\n", new CmplxMtrxDbgView(zpInv));
    } catch (java.lang.ArithmeticException ae) {
      log.info("W=\n", new MtrxDbgView(W));
      log.info("WCJC=\n", new CmplxMtrxDbgView(zpInv));
      log.info("WSJS=\n", new CmplxMtrxDbgView(zp));
      log.info("(1-iR)=\n", new CmplxMtrxDbgView(zm));
      throw ae;
    }
    CmplxMtrx mS = zpInv.times(zm);    log.dbg("S matrix=\n", new CmplxMtrxDbgView(mS));
    mS = mS.transpose();
    calcCrossSecs(engIdx, res, mS, chNum);
    calcSdcs(engIdx, res);
  }
  return res;
}
public ScattRes calc_OLD(Vec engs) {          //JmMethodJmBasisE3.log.setDbg();
  ScattRes res = new ScattRes();
  int chNum = getChNum();
  int eN = engs.size();
  Mtrx X = calcX();
  log.dbg("X=", new MtrxDbgView(X));
  new JmResonE2(this).calc(res, X);
  Mtrx mCs = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  Mtrx mTics = new Mtrx(eN, 2);// ionisation cross section
  res.setSdcs(new Mtrx(chNum + 1, eN + 1));
  res.setCrossSecs(mCs);
  res.setTics(mTics);
  for (int i = 0; i < eN; i++) {
    log.info("i = ", i);
    double scattE = engs.get(i);
    log.info("scattE = ", scattE);
    mCs.set(i, IDX_ENRGY, scattE);
    mTics.set(i, IDX_ENRGY, scattE);                 // first column is for the energies
    double sysE = scattE + trgtE2.getInitTrgtEng();
    chArr = loadChArr(sysE);
    Mtrx W;
    CmplxMtrx WCJC;
    CmplxMtrx WSJS;
    if (calcOpt.getUseClosed()) {
      W = calcW(sysE, X.getArray());
      log.dbg("W=\n", new MtrxDbgView(W));
      WCJC = calcWCJC(W, chArr);
      log.dbg("WCJC=\n", new CmplxMtrxDbgView(WCJC));
      WSJS = calcWSJS(W, chArr);
      log.dbg("WSJS=\n", new CmplxMtrxDbgView(WSJS));
    } else {
      W = calcOpenW(chArr, sysE, X.getArray());
      log.dbg("W=\n", new MtrxDbgView(W));
      WCJC = calcOpenWCJC(W, chArr);
      log.dbg("WCJC=\n", new CmplxMtrxDbgView(WCJC));
      WSJS = calcOpenWSJS(W, chArr);
      log.dbg("WSJS=\n", new CmplxMtrxDbgView(WSJS));
    }
    CmplxMtrx R = calcR(WCJC, WSJS, chArr);
    log.dbg("R=\n", new CmplxMtrxDbgView(R));
    CmplxMtrx iRm = calcIR(R, chArr, -1);
    log.dbg("(1-iR)=\n", new CmplxMtrxDbgView(iRm)); // (1-iR) open channels only
    CmplxMtrx iRp = calcIR(R, chArr, +1);
    log.dbg("(1+iR)=\n", new CmplxMtrxDbgView(iRp));// (1+iR) open channels only
    CmplxMtrx iRmInv = null;
    try {
      iRmInv = iRm.inverse();
      log.dbg("(1-iR)^{-1}=\n", new CmplxMtrxDbgView(iRm));
    } catch (java.lang.ArithmeticException ae) {
      log.info("W=\n", new MtrxDbgView(W));
      log.info("WCJC=\n", new CmplxMtrxDbgView(WCJC));
      log.info("WSJS=\n", new CmplxMtrxDbgView(WSJS));
      log.info("(1-iR)=\n", new CmplxMtrxDbgView(iRm));
      throw ae;
    }
    CmplxMtrx mS = iRp.times(iRmInv);
    log.dbg("S matrix=\n", new CmplxMtrxDbgView(mS));
    calcCrossSecs(i, res, mS, chNum);
    calcSdcs(i, res);
    //      calcSdcsCurveFit(i, res);  // step-function SDCS
  }
  return res;
}
protected void calcSdcs(int i, ScattRes res) {
  loadSdcsW(chArr);
  //    loadSdcsW_Simpson(chArr);
  calcSdcsFromW(i, res);
}
protected void loadSdcsW(JmCh[] chArr) {
  Vec ws = trgtE2.getSdcsW();
  for (int i = 0; i < chArr.length; i++) {
    JmCh ch = chArr[i];
    ch.setSdcsW(ws.get(i));
  }
}
//// load E/2-symmetric channel energies
//protected void loadSymmChEnrgsV2(JmCh[] chArr) {
//  // all chE must be <= sysE/2
//  for (int i = 0; i < chArr.length; i++) {
//    JmCh ch = chArr[i];
//    if (!ch.isOpen() || ch.getEng() <= 0)
//      continue; // nothing to do: not open, or not continuum
//    double chE = ch.getEng();
//    double scattE = ch.getScattEng();
//    double sysE = ch.getSysEng();
//    double E2 = sysE / 2.;
//    if (chE <= E2)
//      continue; // all good
//    ch.setScattEng(chE); // swap energies
//    ch.setEng(scattE);
//  }
//  //NOTE!!! the channels are no longer sorted by chE
//}
protected void loadSdcsW_Simpson(JmCh[] chArr) {
  boolean first = true;  // first open ionization channel
  boolean last = false;  // last open ionization channel
  for (int i = 1; i < chArr.length - 1; i++) {
    log.dbg("i = ", i);   // NOTE from 1 and "-1"
    JmCh ch = chArr[i];
    JmCh ch2 = chArr[i + 1];
    JmCh chPrev = chArr[i - 1];
    if (!ch.isOpen() || ch.getEng() <= 0)
      continue; // nothing to do: not open, or not continuum
    last = !ch2.isOpen();
    double e = ch.getEng();
    double e2 = ch2.getEng();
    double ePrev = chPrev.getEng();
    double sysE = ch.getSysEng();
    if (first) {
      first = false;
      double w = 0.5 * (e + e2);
      ch.setSdcsW(w);
      continue;
    }
    if (last) {
      first = false;
      double w = sysE - 0.5 * (e + ePrev);
      ch.setSdcsW(w);
      break;
    }
    double t = Mathx.pow(e2 - ePrev, 3);
    double w = t / (8. * (e2 - e) * (e - ePrev));
    ch.setSdcsW(w);
  }
}
protected void calcSdcsFromW(int i, ScattRes res) {
  int tN = getChNum();
  Mtrx mCs = res.getCrossSecs();
  Mtrx mSdcs = res.getSdcs();
  for (int to = 0; to < tN; to++) {
    log.dbg("to = ", to);  // Target channels
    JmCh ch = chArr[to];
    double sigma = mCs.get(i, to + CS_CH_OFFSET);
    if (i == 0) { // store channels energies
      mSdcs.set(0, 0, 0);
      mSdcs.set(to + SDCS_CH_OFFSET, 0, ch.getEng());
    }
    mSdcs.set(to + SDCS_CH_OFFSET, i + SDCS_ENG_OFFSET, 0);
    if (ch.isOpen() && ch.getEng() > 0) {
      double w = ch.getSdcsW();
      double sdcs = sigma / w;
      mSdcs.set(to + SDCS_CH_OFFSET, i + SDCS_ENG_OFFSET, sdcs);
    }
  }
}
protected CmplxMtrx calcWCJC(Mtrx mW, JmCh[] chArr) {
  double[][] W = mW.getArray();
  int tN = getChNum();
  CmplxMtrx res = new CmplxMtrx(tN, tN);
  for (int t = 0; t < tN; t++) {
    for (int t2 = 0; t2 < tN; t2++) {
      JmCh ch2 = chArr[t2];
      Cmplx wcjc = new Cmplx(Mathx.dlt(t, t2));
      //        if (ch.isOpen()  && ch2.isOpen()) {   // for openOnly-version
      Cmplx wcj = ch2.getJnn().times(W[t][t2]).mult(ch2.getCn());
      Cmplx c = ch2.getCn1().times(Mathx.dlt(t, t2));
//        wcjc = wcj.add(c).div(ch2.getSqrtAbsMom());
      wcjc = wcj.add(c);
      //        }
      res.set(t, t2, wcjc);
    }
  }
  return res;
}
protected void loadZ(CmplxMtrx zp, CmplxMtrx zm, Mtrx mW, JmCh[] chArr) {
  double[][] W = mW.getArray();
  int tN = chArr.length;
  CmplxMtrx res = new CmplxMtrx(tN, tN);
  for (int t = 0; t < tN; t++) {
    for (int t2 = 0; t2 < tN; t2++) {
      JmCh ch2 = chArr[t2];
      Cmplx qp = ch2.getCsn1().times(Mathx.dlt(t, t2));
      Cmplx qm = qp.conjugate();
      Cmplx Qp = ch2.getJnn().times(ch2.getCsn());
      Cmplx Qm = Qp.conjugate();
      Cmplx vp = (Qp.times(W[t][t2])).add(qp);
      Cmplx vm = (Qm.times(W[t][t2])).add(qm);
      vp = vp.div(ch2.getSqrtAbsMom());
      vm = vm.div(ch2.getSqrtAbsMom());
      zp.set(t, t2, vp);
      zm.set(t, t2, vm);
    }
  }
}
protected CmplxMtrx calcR(CmplxMtrx WCJC, CmplxMtrx WSJS, JmCh[] chArr) {
//      CmplxMtrx invWC = WCJC.inverse();                     log.dbg("WCJC^{-1}=\n", new CmplxMtrxDbgView(invWC));
//      CmplxMtrx R = invWC.times(WSJS);            log.dbg("R=\n", new CmplxMtrxDbgView(R));
//      R.timesEquals(-1.);                        log.dbg("R=\n", new CmplxMtrxDbgView(R));
  CmplxMtrx invWC = WCJC.inverse();
  log.dbg("WCJC^{-1}=\n", new CmplxMtrxDbgView(invWC));
  CmplxMtrx R = invWC.times(WSJS);
  log.dbg("R=\n", new CmplxMtrxDbgView(R));
  R.timesEquals(-1.);
  log.dbg("R=\n", new CmplxMtrxDbgView(R));
  int tN = chArr.length;
  for (int t = 0; t < tN; t++) {
    double kg = chArr[t].getSqrtAbsMom(); // k_gamma
    for (int t2 = 0; t2 < tN; t2++) {
      JmCh ch2 = chArr[t2];
      if (ch2.isOpen()) {   // for openOnly-version
        double kg0 = ch2.getSqrtAbsMom(); // k_{gamma_0}
        Cmplx newR = R.get(t, t2);
        newR = newR.times(kg / kg0);
        R.set(t, t2, newR);
      }
    }
  }
  return R;
}
protected CmplxMtrx calcOpenWCJC(Mtrx mW, JmCh[] chArr) {
  double[][] W = mW.getArray();
  int tN = getChNum();
  CmplxMtrx res = new CmplxMtrx(tN, tN);
  for (int t = 0; t < tN; t++) {
    JmCh ch = chArr[t];
    for (int t2 = 0; t2 < tN; t2++) {
      JmCh ch2 = chArr[t2];
      Cmplx wcjc = new Cmplx(Mathx.dlt(t, t2));
      if (ch.isOpen() && ch2.isOpen()) {   // v2
        Cmplx wcj = ch2.getJnn().times(W[t][t2]).mult(ch2.getCn());
        Cmplx c = ch2.getCn1().times(Mathx.dlt(t, t2));
//          wcjc = wcj.add(c).div(ch2.getSqrtAbsMom());
        wcjc = wcj.add(c);
      }
      res.set(t, t2, wcjc);
    }
  }
  return res;
}
protected CmplxMtrx calcWSJS(Mtrx mW, JmCh[] chArr) {
  double[][] W = mW.getArray();
  int tN = getChNum();
  CmplxMtrx res = new CmplxMtrx(tN, tN);
  for (int t = 0; t < tN; t++) {
    for (int t2 = 0; t2 < tN; t2++) {
      JmCh ch2 = chArr[t2];
      Cmplx wsjs = Cmplx.ZERO;
      if (ch2.isOpen()) {
        //        if (ch.isOpen()  &&  ch2.isOpen()) {     // for openOnly-version
        Cmplx wsj = ch2.getJnn().times(W[t][t2]).times(ch2.getSn());
        double s = ch2.getSn1() * Mathx.dlt(t, t2);
//          wsjs = wsj.add(s).div(ch2.getSqrtAbsMom());
        wsjs = wsj.add(s);
      }
      res.set(t, t2, wsjs);
    }
  }
  return res;
}
protected CmplxMtrx calcOpenWSJS(Mtrx mW, JmCh[] chArr) {
  double[][] W = mW.getArray();
  int tN = getChNum();
  CmplxMtrx res = new CmplxMtrx(tN, tN);
  for (int t = 0; t < tN; t++) {
    JmCh ch = chArr[t];
    for (int t2 = 0; t2 < tN; t2++) {
      JmCh ch2 = chArr[t2];
      Cmplx wsjs = Cmplx.ZERO;
      if (ch.isOpen() && ch2.isOpen()) {     //v2
        Cmplx wsj = ch2.getJnn().times(W[t][t2]).times(ch2.getSn());
        double s = ch2.getSn1() * Mathx.dlt(t, t2);
//          wsjs = wsj.add(s).div(ch2.getSqrtAbsMom());
        wsjs = wsj.add(s);
      }
      res.set(t, t2, wsjs);
    }
  }
  return res;
}
protected CmplxMtrx calcIR(CmplxMtrx mR, JmCh[] chArr, double c) {
  Cmplx[][] R = mR.getArr();
  int tN = getChNum();
  CmplxMtrx res = new CmplxMtrx(tN, tN);
  for (int t = 0; t < tN; t++) {
    JmCh ch = chArr[t];
    for (int t2 = 0; t2 < tN; t2++) {
      JmCh ch2 = chArr[t2];
      Cmplx deltaOne = Cmplx.ONE.times(Mathx.dlt(t, t2));
      if (ch.isOpen() && ch2.isOpen()) {
        Cmplx iR = Cmplx.i.times(R[t][t2]);   // i * R
        res.set(t, t2, deltaOne.plus(iR.times(c)));
      } else
        res.set(t, t2, deltaOne);
    }
  }
  return res;
}
protected Mtrx calcW(double E, double[][] X) {
  int sN = getSysBasisSize();
  int tN = getChNum();
  Mtrx res = new Mtrx(tN, tN);
  double[] sysE = getSysEngs().getArr();
  log.dbg("sysConfH=", getSysEngs());
  for (int t = 0; t < tN; t++) {     //log.dbg("t = ", t);  // Target channels
    for (int t2 = t; t2 < tN; t2++) {
      double G = 0;
      for (int i = 0; i < sN; i++) {
        if (exclSysIdx == i) { // TODO:
          continue;
        }
        double ei = sysE[i];
        double xx = X[t][i] * X[t2][i];
        if (Double.compare(ei, E) == 0) {
          double eps = calcZeroG(i, sysE);
          //throw new IllegalArgumentException(log.error("E=e_i=" + (float) E));
          // [10May2011] trying hard to resolve this problem. TODO: exact theoretical solution would be nice
          G += (xx / eps);
        } else {
          G += (xx / (ei - E));
        }
      }
      res.set(t, t2, G);
      res.set(t2, t, G);
    }
  }
  return res;
}
protected Mtrx calcOpenW(JmCh[] chArr, double E, double[][] X) {
  int sN = getSysBasisSize();
  int tN = getChNum();
  Mtrx res = new Mtrx(tN, tN);
  double[] sysE = getSysEngs().getArr();
  log.dbg("sysConfH=", getSysEngs());
  for (int t = 0; t < tN; t++) {     //log.dbg("t = ", t);  // Target channels
    JmCh ch = chArr[t];
    for (int t2 = t; t2 < tN; t2++) {
      JmCh ch2 = chArr[t2];
      double G = 0;
      if (ch.isOpen() && ch2.isOpen()) {
        for (int i = 0; i < sN; i++) {
          double ei = sysE[i];
          double xx = X[t][i] * X[t2][i];
          if (Double.compare(ei, E) == 0) {
            //throw new IllegalArgumentException(log.error("E=e_i=" + (float) E));
            // [10May2011] trying hard to resolve this problem. TODO: exact theoretical solution would be nice
            double eps = calcZeroG(i, sysE);
            G += (xx / eps);
          } else {
            G += (xx / (ei - E));
          }
        }
      }
      res.set(t, t2, G);
      res.set(t2, t, G);
    }
  }
  return res;
}
private double calcZeroG(int i, double[] sysE) {
  int sN = sysE.length;
  double eps = 0;
  if (i < sN - 1 && i > 0) {
    eps = (sysE[i + 1] - sysE[i - 1]) / MAGIC_EPS_N;
  } else if (i == 0) {
    eps = (sysE[1] - sysE[0]) / MAGIC_EPS_N;
  } else if (i == sN - 1) {
    eps = (sysE[sN - 1] - sysE[sN - 2]) / MAGIC_EPS_N;
  }
  eps = Math.min(eps, MAGIC_MAX_EPS);
  return eps;
}
public int getExclSysIdx() {
  return exclSysIdx;
}
public void setExclSysIdx(int exclSysIdx) {
  this.exclSysIdx = exclSysIdx;
}
public ScattRes calcMidSysEngs() {
  throw new IllegalArgumentException(log.error("[19Sep2011] This idea does not work. The convergence is slow in Nt, not in N!!!"));
//    Vec scttEngs = EngGridFactory.makeMidPoints(sysEngs);
//    double initTrgtE = trgtE2.getInitTrgtEng();
//    scttEngs.add(-initTrgtE);
//    return calc(scttEngs);
}
public ScattRes calcWithMidSysEngs() {
  Vec scttEngs = EngGridFactory.makeWithMidPoints(sysEngs);
  double initTrgtE = trgtE2.getInitTrgtEng();
  scttEngs.add(-initTrgtE);
  return calc(scttEngs);
}
}
