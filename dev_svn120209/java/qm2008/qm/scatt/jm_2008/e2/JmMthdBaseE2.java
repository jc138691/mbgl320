package scatt.jm_2008.e2;
import flanagan.complex.Cmplx;
import math.Mathx;
import math.complex.CmplxMtrx;
import math.complex.CmplxMtrxDbgView;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.mtrx.MtrxFactory;
import math.vec.Vec;
import math.vec.VecDbgView;
import scatt.Scatt;
import scatt.eng.EngGridFactory;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.target.JmCh;

import javax.utilx.log.Log;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,1/04/11,4:34 PM
 */
public abstract class JmMthdBaseE2 extends ScttMthdBaseE2 {
public static Log log = Log.getLog(JmMthdBaseE2.class);
public static final int SDCS_ENG_OFFSET = 1;
public static final int SDCS_CH_OFFSET = 1;
protected static final int CS_CH_OFFSET = 1;
private int exclSysIdx = -1;
public Mtrx jmX;

public JmMthdBaseE2(CalcOptE1 calcOpt) {
  super(calcOpt);
}
protected abstract Mtrx calcX();
public Vec calcSysScatEngs() {
  Vec scttEngs = sysEngs.copy();
  scttEngs.add(-trgtE2.getInitTrgtEng());
  return scttEngs;
}
private boolean isValidD(Vec overD, int nt) {
//  if (overD.size() <= 2 || nt <= 2)   // something is wrong!
//    return false;
  if (overD.size() <= 2) //[120529] nt=0; Using in JmMthdE1 with empty target
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
public ScttRes calc(Vec scttEngs) {          //JmMethodJmBasisE3.log.setDbg();
  return calcV3_best(scttEngs);
}
private ScttRes calcV3_best(Vec scttEngs) { //log.setDbg();
  int eN = scttEngs.size();
  EngModel engModel = calcOpt.getGridEng();
  ScttRes res = new ScttRes();
  int prntNum = calcPrntChNum();
  jmX = calcX();                              log.dbg("X=", new MtrxDbgView(jmX));

  new JmResonE2(this).calc(res, jmX);
  Mtrx mCs = new Mtrx(eN, prntNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  Mtrx mR = new Mtrx(eN, prntNum + 1);
  Mtrx mTics = new Mtrx(eN, 2);// ionisation cross section
  res.setSdcs(new Mtrx(prntNum + 1, eN + 1));
  res.setMtrxR(mR);
  res.setCrossSecs(mCs);
  res.setTics(mTics);

  for (int scttIdx = 0; scttIdx < eN; scttIdx++) {     log.info("i = ", scttIdx);
    scttE = scttEngs.get(scttIdx);             log.info("scttE = ", scttE);
    mR.set(scttIdx, IDX_ENRGY, scttE);
    mCs.set(scttIdx, IDX_ENRGY, scttE);
    mTics.set(scttIdx, IDX_ENRGY, scttE);                 // first column is for the energies
    if (scttE <= 0  ||  engModel.getFirst() > scttE ||  scttE > engModel.getLast()) {
      continue;
    }

    openChN = calcOpenChNum(scttE);
    calcChN = calcCalcChNum(scttE);

    sysTotE = scttE + trgtE2.getInitTrgtEng();          log.info("sysTotE = ", sysTotE);
    chArr = loadChArr(sysTotE);
    int sysIdx = matchSysTotE();
    if (sysIdx == -1) {
      jmR = calcR(calcChN, openChN);
//      jmR = calcR_v1_ok(calcN, openN);
    }
    else {
//      jmR = calcRSysIdx_bad(calcN, openN, sysIdx);
      continue; // ignore
    }
    jmR = calcCorrR(); // calc any corrections to the R-matrix

//    if (calcOpt.getJmTailN() > 1) { // DEBUG
//      jmR = calcCorrR(); // DEBUG
//    }

    jmS = Scatt.calcSFromK(jmR, openChN);               log.dbg("S matrix=\n", new CmplxMtrxDbgView(jmS));
    saveCrossSecs(scttIdx, res, jmS, openChN);
    saveMtrxR(scttIdx, res, jmR, openChN);
    if (calcOpt.getCalcSdcs()) {
      calcSdcs(scttIdx, res, prntNum);
    }
  }
  return res;
}
protected Mtrx calcCorrR() {
  return jmR;
//  JmKatoBasisHyE2 kato = new JmKatoBasisHyE2(this);
//      jmR = kato.calcKatoR_todo(jmR);     log.dbg("kato.calc(jmR)=\n", new MtrxDbgView(jmR));
}
protected Mtrx calcR(int calcN, int openN) {
  Mtrx W = null;
  if (calcOpt.getUseClosed()) {
    W = calcW(calcN);
//    W = calcW_DBG(calcN);
  } else {
    W = calcW(openN);
//    W = calcW_DBG(openN);
  }                                                log.dbg("W=\n", new MtrxDbgView(W));
  Mtrx WSJS = calcWsjs(W, openN);                  log.dbg("WSJS=\n", new MtrxDbgView(WSJS));
  Mtrx WCJC = calcWcjc(W);                         log.dbg("WCJC=\n", new MtrxDbgView(WCJC));
  Mtrx res = calcR(WCJC, WSJS);                    log.dbg("R=\n", new MtrxDbgView(res));
  MtrxFactory.makeSymmByAvr(res, openN);           log.dbg("MtrxFactory.makeSymmByAvr(R)=\n", new MtrxDbgView(res));
  return res;
}
private Mtrx calcR_v1_ok(int calcN, int openN) {
  Mtrx W = null;
  if (calcOpt.getUseClosed()) {
    W = calcW(calcN);
  } else {
    W = calcW(openN);
  }                                                 log.dbg("W=\n", new MtrxDbgView(W));
  Mtrx WSJS = calcWsjs(W, openN);                   log.dbg("WSJS=\n", new MtrxDbgView(WSJS));
  CmplxMtrx WCJC = calcWCJC_v1_ok(W);               log.dbg("WCJC=\n", new CmplxMtrxDbgView(WCJC));
  Mtrx res = calcR_v1_ok(WCJC, WSJS);               log.dbg("R=\n", new MtrxDbgView(res));
  return res;
}
protected void calcSdcs(int scttIdx, ScttRes res, int prntN) {
  loadSdcsW(chArr);
  //    loadSdcsW_Simpson(chArr);
  calcSdcsFromW(scttIdx, res, prntN);
}
protected void loadSdcsW(JmCh[] chArr) {
  Vec ws = trgtE2.getSdcsW();
  for (int i = 0; i < chArr.length; i++) {
    JmCh ch = chArr[i];
    ch.setSdcsW(ws.get(i));
  }
}
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
protected void calcSdcsFromW(int scttIdx, ScttRes res, int showNum) {
  int tN = showNum;
  Mtrx mCs = res.getCrossSecs();
  Mtrx mSdcs = res.getSdcs();
  for (int to = 0; to < tN; to++) {log.dbg("to = ", to);  // Target channels
    JmCh ch = chArr[to];
    double sigma = mCs.get(scttIdx, to + CS_CH_OFFSET);
    if (scttIdx == 0) { // store channels energies
      mSdcs.set(0, 0, 0);
      mSdcs.set(to + SDCS_CH_OFFSET, 0, ch.getEng());
    }
    mSdcs.set(to + SDCS_CH_OFFSET, scttIdx + SDCS_ENG_OFFSET, 0);
    if (ch.isOpen() && ch.getEng() > 0) {
      double w = ch.getSdcsW();
      double sdcs = sigma / w;
      mSdcs.set(to + SDCS_CH_OFFSET, scttIdx + SDCS_ENG_OFFSET, sdcs);
    }
  }
}
protected CmplxMtrx calcWCJC_v1_ok(Mtrx mW) {
  double[][] W = mW.getArray();
  int tN = mW.getNumRows();
  CmplxMtrx res = new CmplxMtrx(tN, tN);
  for (int t = 0; t < tN; t++) {
    for (int t2 = 0; t2 < tN; t2++) {
      JmCh ch2 = chArr[t2];
      log.dbg("W[t="+t+"][t2="+t2+"]", W[t][t2]);
      log.dbg("ch2.getJnn()=", ch2.getJnn());
      log.dbg("ch2.getCn()=", ch2.getCn());
      log.dbg("ch2.getCn1()=", ch2.getCn1());
      log.dbg("ch2.getCnn1()=", ch2.getCnn1());
      Cmplx wcj = ch2.getJnn().times(W[t][t2]).mult(ch2.getCnn1());   log.dbg("wcj=", wcj);
      Cmplx wcjc = wcj.add(new Cmplx(Mathx.dlt(t, t2)));              log.dbg("wcjc[t="+t+"][t2="+t2+"]=", wcjc);
      res.set(t, t2, wcjc);
    }
  }
  return res;
}
protected Mtrx calcWcjc(Mtrx mW) {
  double[][] W = mW.getArray();
  int n = mW.getNumRows();
  Mtrx res = new Mtrx(n, n);
  for (int r = 0; r < n; r++) {
    for (int c = 0; c < n; c++) {
      JmCh cCh = chArr[c];
      // NOTE! using *c_N / c_{N-1}; i.e. 1/c_{N-1} needs to be corrected when calculating R
      double wcj = cCh.getJnn().getRe() * W[r][c] * cCh.getCnn1().getRe();
      double wcjc = wcj + Mathx.dlt(r, c);
//      log.dbg("W[r="+r+"][c="+c+"]", W[r][c]);
//      log.dbg("cCh.getJnn()=", cCh.getJnn());
//      log.dbg("cCh.getCn()=", cCh.getCn());
//      log.dbg("cCh.getCn1()=", cCh.getCn1());
//      log.dbg("cCh.getCnn1()=", cCh.getCnn1());
//      log.dbg("wcj=", wcj);
//      log.dbg("wcjc[r=" + r + "][c=" + c + "]=", wcjc);
      res.set(r, c, wcjc);
    }
  }
  return res;
}
protected Mtrx calcR_v1_ok(CmplxMtrx WCJC, Mtrx WSJS) {
  int n3 = WCJC.getNumCols();
  int openN = WSJS.getNumCols();
  CmplxMtrx invWC = WCJC.inverse();                   log.dbg("WCJC^{-1}=\n", new CmplxMtrxDbgView(invWC));
//  Mtrx R = invWC.timesSelf(WSJS);                    log.dbg("R=\n", new CmplxMtrxDbgView(R));
  Mtrx R = new Mtrx(openN, openN);
  for (int t = 0; t < openN; t++) {
    for (int t2 = 0; t2 < openN; t2++) {
      double sum = 0;
      for (int t3 = 0; t3 < n3; t3++) {  // NOTE!!! n3=WCJC.getNumCols() is correct
//        for (int t3 = 0; t3 < openN; t3++) { // NOTE!!! openN is WRONG
        Cmplx wc = invWC.get(t, t3);               log.dbg("wc=", wc);
//        if (!Calc.isZero(wc.getIm())) {
//          throw new IllegalArgumentException(log.error("!Calc.isZero(wc.getIm())"));
//        }
        log.dbg("WSJS[t3="+t3+"][t2"+t2+"]=", WSJS.get(t3, t2));
        sum += wc.getRe() * WSJS.get(t3, t2);      log.dbg("sum=", sum);
      }
      R.set(t, t2, sum);             log.dbg("R[t="+t+"][t2="+t2+"]=", R.get(t, t2));
    }
  }
  R.timesEquals(-1.);                          log.dbg("R.timesEquals(-1.)=\n", new MtrxDbgView(R));
  loadCorrSqrt(R);                               log.dbg("loadConsts(R)=\n", new MtrxDbgView(R));
  loadCorrCn1(R);                               log.dbg("loadCorrCn1(R)=\n", new MtrxDbgView(R));
  MtrxFactory.makeSymmByAvr(R, openN);           log.dbg("MtrxFactory.makeSymmByAvr(R)=\n", new MtrxDbgView(R));
  return R;
}
protected Mtrx calcR(Mtrx WCJC, Mtrx WSJS) {
//  int calcN = WCJC.getNumCols();
//  int openN = WSJS.getNumCols();
  Mtrx invWC = WCJC.inverse();     log.dbg("WCJC^{-1}=\n", new MtrxDbgView(invWC));
  Mtrx R = invWC.times(WSJS);      log.dbg("R = invWC.timesSelf(WSJS)=\n", new MtrxDbgView(R));

// DEBUGGING
//  Mtrx R = new Mtrx(openN, openN);
//  for (int t = 0; t < openN; t++) {
//    for (int t2 = 0; t2 < openN; t2++) {
//      double sum = 0;
//      for (int t3 = 0; t3 < calcN; t3++) {  // NOTE!!! n3=WCJC.getNumCols() is correct
////        for (int t3 = 0; t3 < openN; t3++) { // NOTE!!! openN is WRONG
//        double wc = invWC.get(t, t3);
//        sum += wc * WSJS.get(t3, t2);
////        log.dbg("wc=", wc);
////        log.dbg("WSJS[t3="+t3+"][t2"+t2+"]=", WSJS.get(t3, t2));
////        log.dbg("sum=", sum);
//      }
//      R.set(t, t2, sum);             log.dbg("R[t="+t+"][t2="+t2+"]=", R.get(t, t2));
//    }
//  }

  R.timesEquals(-1.);                    log.dbg("R.timesEquals(-1.)=\n", new MtrxDbgView(R));
  loadCorrSqrt(R);                     log.dbg("loadCorrSqrt(R)=\n", new MtrxDbgView(R));
  loadCorrCn1(R);                      log.dbg("loadCorrCn1(R)=\n", new MtrxDbgView(R));
  return R;
}
protected void loadCorrSqrt(Mtrx mK) {
  for (int r = 0; r < mK.getNumRows(); r++) {
    double rK = chArr[r].getSqrtAbsMom(); // k_gamma
    for (int c = 0; c < mK.getNumCols(); c++) {
      double cK = chArr[c].getSqrtAbsMom(); // k_{gamma_0}
      double oldR = mK.get(r, c);
      double newR = oldR * (rK / cK);
      mK.set(r, c, newR);
    }
  }
}
protected void loadCorrCn1(Mtrx mK) {
  // NOTE!!! Closed channels stored as * cn1.getRe();
  int openN = mK.getNumCols();
  for (int r = 0; r < openN; r++) {
    JmCh rCh = chArr[r];
    Cmplx rCn1 = rCh.getCn1();
    if (!rCh.isOpen()) {
      throw new IllegalArgumentException(log.error("!ch2.isOpen()"));
    }
    for (int c = 0; c < openN; c++) {
      double oldR = mK.get(r, c);
      double newR = oldR / rCn1.getRe();
      mK.set(r, c, newR);
    }
  }
}
protected Mtrx calcWsjs(Mtrx mW, int openN) {
  double[][] W = mW.getArray();
  int tN = mW.getNumRows();
  Mtrx res = new Mtrx(tN, openN);
  for (int t = 0; t < tN; t++) {
    for (int t2 = 0; t2 < openN; t2++) {
      JmCh ch2 = chArr[t2];
      if (!ch2.isOpen()) {
        throw new IllegalArgumentException(log.error("!ch2.isOpen()"));
      }
      double wsj = ch2.getJnn().getRe() * W[t][t2] * ch2.getSn();
      double s = ch2.getSn1() * Mathx.dlt(t, t2);
//      if (ch2.getEng() > 0) { // DEBUG
//        s = 0;
//      }
      double wsjs = (wsj + s); // / ch2.getSqrtAbsMom()   MOVED to where R-matrix is calc-ed
      res.set(t, t2, wsjs);
    }
  }
  return res;
}
protected double calcDeltaSysIdx_bad(Mtrx mW) {
  double[][] W = mW.getArray();
  int tN = mW.getNumRows();
  double res = 0;
  for (int t = 0; t < tN; t++) {
    JmCh ch = chArr[t];
    double d = ch.getJnn().getRe() * W[t][t] * ch.getCnn1().getRe();
    res += d;
  }
  return res;
}
protected Vec calcAVecSysIdx_bad(int sysIdx, int tN) {
  double[][] X = jmX.getArray();
  Vec res = new Vec(tN);
  for (int t = 0; t < tN; t++) {
    JmCh ch = chArr[t];
    double a = ch.getJnn().getRe() * X[t][sysIdx] * ch.getQn1();
    double b = ch.getSnn1() - ch.getCnn1().getRe();
    double c = a * b;
    res.set(t, c);
  }
  return res;
}
protected Mtrx calcW(int calcNum) {
  double[][] X = jmX.getArray();
  int sN = getSysBasisSize();
  int tN = calcNum;
  Mtrx res = new Mtrx(tN, tN);
  double[] sysE = getSysEngs().getArr();        //log.dbg("sysConfH=", getSysEngs());
  for (int t = 0; t < tN; t++) {     //log.dbg("t = ", t);  // Target channels
    for (int t2 = t; t2 < tN; t2++) {
      double G = 0;
      for (int i = 0; i < sN; i++) {
//        if (exclSysIdx == i) { // DEBUG
//          continue;
//        }
        double ei = sysE[i];
        double xx = X[t][i] * X[t2][i];     //log.dbg("xx=", xx);
        if (Double.compare(ei, sysTotE) == 0) {
//          double eps = calcZeroG(i, sysE);
          throw new IllegalArgumentException(log.error("sysE[i="+i+"]=sysTotE=" + sysTotE));
//          G += (xx / eps);
        } else {
          G += (xx / (ei - sysTotE));       //log.dbg("G=", G);
        }
      }
      res.set(t, t2, G);
      res.set(t2, t, G);
    }
  }
  return res;
}
protected Mtrx calcW_DBG(int calcNum) {
  double[][] X = jmX.getArray();
  int sN = getSysBasisSize();
  int tN = calcNum;
  Mtrx res = new Mtrx(tN, tN);
  double[] sysE = getSysEngs().getArr();        //log.dbg("sysConfH=", getSysEngs());
  for (int t = 0; t < tN; t++) {     //log.dbg("t = ", t);  // Target channels
    for (int t2 = t; t2 < tN; t2++) {
      double G = 0;
      for (int i = 0; i < sN; i++) {
//        if (exclSysIdx == i) { // DEBUG
//          continue;
//        }
        double ei = sysE[i];
        double xx = X[t][i] * X[t2][i];     //log.dbg("xx=", xx);
        if (Double.compare(ei, sysTotE) == 0) {
//          double eps = calcZeroG(i, sysE);
          throw new IllegalArgumentException(log.error("sysE[i="+i+"]=sysTotE=" + sysTotE));
//          G += (xx / eps);
        } else {
          G += ((ei - sysTotE) / xx);       //log.dbg("G=", G);
        }
      }
      G = 1. / G;
      res.set(t, t2, G);
      res.set(t2, t, G);
    }
  }
  return res;
}


public int getExclSysIdx() {
  return exclSysIdx;
}
public void setExclSysIdx(int exclSysIdx) {
  this.exclSysIdx = exclSysIdx;
}
public ScttRes calcForMidSysEngs() {  log.setDbg();
  Vec scttEngs = EngGridFactory.makeMidPoints(sysEngs);  log.dbg("sysEngs=", new VecDbgView(sysEngs));
                                                         log.dbg("scttEngs=", new VecDbgView(scttEngs));
  double initTrgtE = trgtE2.getInitTrgtEng();            log.dbg("initTrgtE=", initTrgtE);
  scttEngs.add(-initTrgtE);                              log.dbg("scttEngs-initTrgtE=", new VecDbgView(scttEngs));
  return calc(scttEngs);
}
}
