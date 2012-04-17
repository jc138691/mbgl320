package scatt.jm_2008.e2;
import flanagan.complex.Cmplx;
import math.Calc;
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
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.target.JmCh;

import javax.utilx.log.Log;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,1/04/11,4:34 PM
 */
public abstract class JmMthdBaseE2 extends ScttMthdBaseE2 {
public static Log log = Log.getLog(JmMthdBaseE2.class);
//private static double MAGIC_MAX_EPS = 0.00000001; // MAGIC NUMBERS!!!!!!!!!!!!!!!!!!!!!!!
//private static int MAGIC_EPS_N = 1000; // MAGIC NUMBERS!!!!!!!!!!!!!!!!!!!!!!!
//protected static final int IDX_IONIZ = 1;
protected static final int SDCS_ENG_OFFSET = 1;
//  protected static final int SC_SYS_ENG_OFFSET = 1;
protected static final int SDCS_CH_OFFSET = 1;
//  protected static final int CS_ENG_OFFSET = 1;
protected static final int CS_CH_OFFSET = 1;
private int exclSysIdx = -1;
private int incSysIdx = -1;
protected Mtrx jmX;

public JmMthdBaseE2(CalcOptE1 calcOpt) {
  super(calcOpt);
}
protected abstract Mtrx calcX();
public Vec calcSysScatEngs() {
  Vec scttEngs = sysEngs.copy();
  scttEngs.add(-trgtE2.getInitTrgtEng());
  return scttEngs;
}
//public ScattRes calcSysEngs() {
//  Vec scttEngs = sysEngs.copy();
//  scttEngs.add(-trgtE2.getInitTrgtEng());
//  return calc(scttEngs);
//}
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
public ScattRes calc(Vec scttEngs) {          //JmMethodJmBasisE3.log.setDbg();
  return calcV3_best(scttEngs);
}
private ScattRes calcV3_best(Vec scttEngs) { log.setDbg();
  EngModel engModel = calcOpt.getGridEng();
  ScattRes res = new ScattRes();
  int showNum = calcShowChNum();
//  int chNum = getChNum();
  int eN = scttEngs.size();
  jmX = calcX();                              log.dbg("X=", new MtrxDbgView(jmX));
  new JmResonE2(this).calc(res, jmX);
  Mtrx mCs = new Mtrx(eN, showNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  Mtrx mTics = new Mtrx(eN, 2);// ionisation cross section
  res.setSdcs(new Mtrx(showNum + 1, eN + 1));
  res.setCrossSecs(mCs);
  res.setTics(mTics);
  for (int engIdx = 0; engIdx < eN; engIdx++) {
    log.info("i = ", engIdx);
    scattE = scttEngs.get(engIdx);             log.info("scattE = ", scattE);
    mCs.set(engIdx, IDX_ENRGY, scattE);
    mTics.set(engIdx, IDX_ENRGY, scattE);                 // first column is for the energies
    if (scattE <= 0  ||  engModel.getFirst() > scattE ||  scattE > engModel.getLast()) {
      continue;
    }
    int openN = calcOpenChNum(scattE);
    int calcN = calcCalcChNum(scattE);
    sysTotE = scattE + trgtE2.getInitTrgtEng();          log.info("sysTotE = ", sysTotE);
    chArr = loadChArr(sysTotE);
    int sysIdx = matchSysTotE();
    if (sysIdx == -1) {
      jmR = calcR(calcN, openN);
//      jmR = calcR_v1_ok(calcN, openN);
    }
    else {
//      jmR = calcRSysIdx_bad(calcN, openN, sysIdx);
      jmR = calcRSysIdx(calcN, openN, sysIdx);
    }
    CmplxMtrx mS = Scatt.calcSFromK(jmR);               log.dbg("S matrix=\n", new CmplxMtrxDbgView(mS));
    calcCrossSecs(engIdx, res, mS, openN);
    if (calcOpt.getCalcSdcs()) {
      calcSdcs(engIdx, res, showNum);
    }
  }
  return res;
}
private Mtrx calcR(int calcN, int openN) {
  Mtrx W = null;
  if (calcOpt.getUseClosed()) {
    W = calcW(calcN);
  } else {
    W = calcW(openN);
  }                                                log.dbg("W=\n", new MtrxDbgView(W));
  Mtrx WSJS = calcWsjs(W, openN);                  log.dbg("WSJS=\n", new MtrxDbgView(WSJS));
  Mtrx WCJC = calcWcjc(W);                         log.dbg("WCJC=\n", new MtrxDbgView(WCJC));
  Mtrx res = calcR(WCJC, WSJS);                    log.dbg("R=\n", new MtrxDbgView(res));
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
private Mtrx calcRSysIdx_bad(int calcN, int openN, int sysIdx) {
  Mtrx mW = null;
  if (calcOpt.getUseClosed()) {
    mW = calcWSysIdx_bad(calcN, sysIdx);
  } else {
    mW = calcWSysIdx_bad(openN, sysIdx);
  }                                                   log.dbg("W=\n", new MtrxDbgView(mW));
  double dlt = calcDeltaSysIdx_bad(mW);                   log.dbg("dlt_i=", dlt);
  Vec A = calcAVecSysIdx_bad(sysIdx, openN);              log.dbg("A=\n", new VecDbgView(A));
  Mtrx res = calcRSysIdx_bad(sysIdx, mW, dlt, A);         log.dbg("R=\n", new MtrxDbgView(res));
  return res;
}
private Mtrx calcRSysIdx(int calcN, int openN, int sysIdx) {
  return res;
}
private Mtrx calcRSysIdx_bad(int sysIdx, Mtrx mW, double dlt, Vec vA) {
  double[][] X = jmX.getArray();
  int openN = vA.size();
  Mtrx R = new Mtrx(openN, openN);
  for (int t = 0; t < openN; t++) {
    JmCh ch = chArr[t];
    for (int t2 = 0; t2 < openN; t2++) {
      double a = -Mathx.dlt(t, t2) * ch.getQn1();
      double b = X[t][sysIdx] * vA.get(t2) / dlt;
      double res = a + b;
      R.set(t, t2, res);             log.dbg("R[t="+t+"][t2="+t2+"]=", R.get(t, t2));
    }
  }
  loadConstsSqrt(R);                               log.dbg("loadConsts(R)=\n", new MtrxDbgView(R));
  MtrxFactory.makeSymmByAvr(R);                log.dbg("MtrxFactory.makeSymmByAvr(R)=\n", new MtrxDbgView(R));
  return R;
}
protected void calcSdcs(int i, ScattRes res, int showNum) {
  loadSdcsW(chArr);
  //    loadSdcsW_Simpson(chArr);
  calcSdcsFromW(i, res, showNum);
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
protected void calcSdcsFromW(int i, ScattRes res, int showNum) {
  int tN = showNum;
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
  int tN = mW.getNumRows();
  Mtrx res = new Mtrx(tN, tN);
  for (int t = 0; t < tN; t++) {
    for (int t2 = 0; t2 < tN; t2++) {
      JmCh ch2 = chArr[t2];
      double wcj = ch2.getJnn().getRe() * W[t][t2] * ch2.getCnn1().getRe();
      double wcjc = wcj + Mathx.dlt(t, t2);
//      log.dbg("W[t="+t+"][t2="+t2+"]", W[t][t2]);
//      log.dbg("ch2.getJnn()=", ch2.getJnn());
//      log.dbg("ch2.getCn()=", ch2.getCn());
//      log.dbg("ch2.getCn1()=", ch2.getCn1());
//      log.dbg("ch2.getCnn1()=", ch2.getCnn1());
//      log.dbg("wcj=", wcj);
//      log.dbg("wcjc[t=" + t + "][t2=" + t2 + "]=", wcjc);
      res.set(t, t2, wcjc);
    }
  }
  return res;
}
protected Mtrx calcR_v1_ok(CmplxMtrx WCJC, Mtrx WSJS) {
  int n3 = WCJC.getNumCols();
  int openN = WSJS.getNumCols();
  CmplxMtrx invWC = WCJC.inverse();                   log.dbg("WCJC^{-1}=\n", new CmplxMtrxDbgView(invWC));
//  Mtrx R = invWC.times(WSJS);                    log.dbg("R=\n", new CmplxMtrxDbgView(R));
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
  loadConstsSqrt(R);                               log.dbg("loadConsts(R)=\n", new MtrxDbgView(R));
  loadConstsCn1(R);                               log.dbg("loadConstsCn1(R)=\n", new MtrxDbgView(R));
  MtrxFactory.makeSymmByAvr(R);                log.dbg("MtrxFactory.makeSymmByAvr(R)=\n", new MtrxDbgView(R));
  return R;
}
protected Mtrx calcR(Mtrx WCJC, Mtrx WSJS) {
  int n3 = WCJC.getNumCols();
  int openN = WSJS.getNumCols();
  Mtrx invWC = WCJC.inverse();     log.dbg("WCJC^{-1}=\n", new MtrxDbgView(invWC));
//  Mtrx R = invWC.times(WSJS);                    log.dbg("R=\n", new CmplxMtrxDbgView(R));
  Mtrx R = new Mtrx(openN, openN);
  for (int t = 0; t < openN; t++) {
    for (int t2 = 0; t2 < openN; t2++) {
      double sum = 0;
      for (int t3 = 0; t3 < n3; t3++) {  // NOTE!!! n3=WCJC.getNumCols() is correct
//        for (int t3 = 0; t3 < openN; t3++) { // NOTE!!! openN is WRONG
        double wc = invWC.get(t, t3);
        sum += wc * WSJS.get(t3, t2);
//        log.dbg("wc=", wc);
//        log.dbg("WSJS[t3="+t3+"][t2"+t2+"]=", WSJS.get(t3, t2));
//        log.dbg("sum=", sum);
      }
      R.set(t, t2, sum);             log.dbg("R[t="+t+"][t2="+t2+"]=", R.get(t, t2));
    }
  }
  R.timesEquals(-1.);                          log.dbg("R.timesEquals(-1.)=\n", new MtrxDbgView(R));
  loadConstsSqrt(R);                               log.dbg("loadConstsSqrt(R)=\n", new MtrxDbgView(R));
  loadConstsCn1(R);                               log.dbg("loadConstsCn1(R)=\n", new MtrxDbgView(R));
  MtrxFactory.makeSymmByAvr(R);                log.dbg("MtrxFactory.makeSymmByAvr(R)=\n", new MtrxDbgView(R));
  return R;
}
protected void loadConstsSqrt(Mtrx mK) {
  for (int t = 0; t < mK.getNumRows(); t++) {
    JmCh ch = chArr[t];
    double kg = ch.getSqrtAbsMom(); // k_gamma
    Cmplx cn1 = ch.getCn1();
    for (int t2 = 0; t2 < mK.getNumCols(); t2++) {
      JmCh ch2 = chArr[t2];
      double kg2 = ch2.getSqrtAbsMom(); // k_{gamma_0}
      double oldR = mK.get(t, t2);
      double newR = oldR * (kg / kg2);
      mK.set(t, t2, newR);
    }
  }
}
protected void loadConstsCn1(Mtrx mK) {
  for (int t = 0; t < mK.getNumRows(); t++) {
    JmCh ch = chArr[t];
    Cmplx cn1 = ch.getCn1();
    for (int t2 = 0; t2 < mK.getNumCols(); t2++) {
      double oldR = mK.get(t, t2);
      double newR = oldR / cn1.getRe();
      mK.set(t, t2, newR);
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
//        exclSysIdx = 10; // DEBUG
//        if (exclSysIdx == i) { // DEBUG
//          continue;
//        }
        incSysIdx = 10; // DEBUG
        if (incSysIdx != i) { // DEBUG
          continue;
        }
        double ei = sysE[i];
        double xx = X[t][i] * X[t2][i];     log.dbg("xx=", xx);
        if (Double.compare(ei, sysTotE) == 0) {
//          double eps = calcZeroG(i, sysE);
          throw new IllegalArgumentException(log.error("sysE[i="+i+"]=sysTotE=" + sysTotE));
//          G += (xx / eps);
        } else {
          G += (xx / (ei - sysTotE));       log.dbg("G=", G);
        }
      }
      res.set(t, t2, G);
      res.set(t2, t, G);
    }
  }
  return res;
}
protected Mtrx calcWSysIdx_bad(int calcNum, int sysIdx) {
//  log.dbg("jmX=\n", new MtrxDbgView(jmX));
//  Mtrx Xt = jmX.transpose();
//  log.dbg("Xt=\n", new MtrxDbgView(Xt));
//  Mtrx invX = Xt.inverse();
//  log.dbg("invX=\n", new MtrxDbgView(invX));
//
//  Mtrx xx = invX.times(Xt);
//  log.dbg("xx=\n", new MtrxDbgView(xx));
//
//  Mtrx xx2 = Xt.times(invX);
//  log.dbg("xx2=\n", new MtrxDbgView(xx2));


  double[][] X = jmX.getArray();
  int tN = calcNum;
  Mtrx res = new Mtrx(tN, tN);
  for (int t = 0; t < tN; t++) {     //log.dbg("t = ", t);  // Target channels
    for (int t2 = t; t2 < tN; t2++) {
//      log.dbg("X[t][sysIdx-1]=", X[t][sysIdx-1]);
//      log.dbg("X[t][sysIdx]=", X[t][sysIdx]);
//      log.dbg("X[t][sysIdx+1]=", X[t][sysIdx+1]);

//      log.dbg("X[t2][sysIdx-1]=", X[t2][sysIdx-1]);
//      log.dbg("X[t2][sysIdx]=", X[t2][sysIdx]);
//      log.dbg("X[t2][sysIdx+1]=", X[t2][sysIdx+1]);

      double G = X[t][sysIdx] * X[t2][sysIdx];
      res.set(t, t2, G);
      res.set(t2, t, G);
    }
  }
  return res;
}

protected int matchSysTotE() {
  double[] sysE = getSysEngs().getArr();
  for (int i = 0; i < sysE.length; i++) {
    double ei = sysE[i];
    if (Calc.isZero(ei - sysTotE)) {
      log.dbg("sysE[i="+i+"]=sysTotE=", sysTotE);
      return i;
    }
  }
  return -1;
}
//protected double calcZeroG(int i, double[] sysE) {
//  int sN = sysE.length;
//  double eps = 0;
//  if (i < sN - 1 && i > 0) {
//    eps = (sysE[i + 1] - sysE[i - 1]) / MAGIC_EPS_N;
//  } else if (i == 0) {
//    eps = (sysE[1] - sysE[0]) / MAGIC_EPS_N;
//  } else if (i == sN - 1) {
//    eps = (sysE[sN - 1] - sysE[sN - 2]) / MAGIC_EPS_N;
//  }
//  eps = Math.min(eps, MAGIC_MAX_EPS);
//  return eps;
//}
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
