package scatt.jm_2008.e2;
import atom.e_2.SysE2;
import atom.energy.Energy;
import atom.shell.*;
import flanagan.complex.Cmplx;
import math.Calc;
import math.Mathx;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import scatt.jm_2008.e1.JmCalcOptE1;
import scatt.jm_2008.e1.JmTailE2;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.jm_2008.jm.target.JmCh;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 18/04/12, 9:40 AM
 */
public class JmKatoBasisHyE2 extends JmTailE2 {
public static Log log = Log.getLog(JmKatoBasisHyE2.class);
//protected static final boolean DEBUG_JM1 = false;
protected int ID_XI_OFFSET = 3;
protected Cmplx[][][] jmCF;  // katoIdx, K-rows, K-cols(open-only)
protected Cmplx[][] jmCA;    // K-cols (open-only), sysIdx
private double[][] mBK; // matrix basis-Xi
protected Vec vHEBasis;

public JmKatoBasisHyE2(JmMthdBaseE2 jmMthdBaseE2) {
  super(jmMthdBaseE2);
  log.setDbg();
}
public Mtrx calcKatoR_todo(Mtrx mtR) { //mt-matrix trial
  log.setDbg();
  jmF = calcFFromR();  log.dbg("jmF[0]=\n", new MtrxDbgView(new Mtrx(jmF[0])));
  jmA = calcVecA();    log.dbg("jmA=", new MtrxDbgView(new Mtrx(jmA)));
  loadKatoLgrr();
  Mtrx res = mtR.copy();
  Mtrx mKato = calcKato(); log.dbg("mKato=", new MtrxDbgView(mKato));
  res.minusEquals(mKato);
  return res;
}
private Mtrx calcKato() {
  int rN = mthd.jmR.getNumRows();
  int cN = mthd.jmR.getNumCols();
  Mtrx res = new Mtrx(rN, cN);
  int n = cN; // open only
  for (int r = 0; r < n; r++) { // NOTE 'n'
    for (int c = 0; c < n; c++) { // NOTE 'n'
      double v = calcKato(r, c);
      res.set(r, c, v);
    }
  }
  return res;
}
private double calcKato(int r, int c) {
  // TODO: try using calcSumA and calcSumK

  double aa = calcAA(r, c);
//  double ak = calcAK(r, c);

//  double ka = calcKA(r, c);
//  double kk = calcKK(r, c);

//  double res = aa + ak + ka + kk;
  double res = aa;
  return res;
}


private double calcAA(int r, int c) {
  double[] sysEngs = mthd.getSysEngs().getArr();
  double sysTotE = mthd.getSysTotE();
  int sN = mthd.getSysBasisSize();
  double res = 0;
  for (int s = 0; s < sN; s++) {
    double ei = sysEngs[s];
    double ah = jmA[r][s] * jmA[c][s];
    ah *= (ei - sysTotE);
    res += ah;
  }
  return res;
}

protected Cmplx[][][] calcCFFromR() {
  JmCalcOptE1 calcOpt = mthd.getCalcOpt();
  int rN = mthd.jmS.getNumRows();
  int cN = mthd.jmS.getNumCols();
  int katoN = calcOpt.getJmTailN();
//  int initChIdx = mthd.trgtE2.getInitTrgtIdx();
  LgrrOpt jmModel = calcOpt.getLgrrModel();
  int N = jmModel.getN();
  LgrrOpt xiModel = new LgrrOpt(jmModel);
  Vec tEngs = mthd.trgtE2.getEngs();
  Cmplx[][][] res = new Cmplx[katoN][rN][cN];
  for (int r = 0; r < rN; r++) {
    for (int xiIdx = 0; xiIdx < katoN; xiIdx++) {
      xiModel.setN(N + xiIdx);                //log.dbg("N + xiIdx=", N + xiIdx);
      JmCh ch = new JmCh(mthd.getSysTotE(), tEngs.get(r), xiModel
        , -mthd.trgtE2.getScreenZ());
      for (int c = 0; c < cN; c++) {
        Cmplx sg = ch.getCsn().conjugate().times(Mathx.dlt(r, c));
         //log.dbg("sg=", sg);
        Cmplx cg = ch.getCsn();
        Cmplx scR = cg.mult(mthd.jmS.get(r, c));
        scR = sg.minus(scR);
//        scR = sg.add(scR); // VERY wrong
        //log.dbg("scR=", scR);
        res[xiIdx][r][c] = scR;
      }
    }
  }
  return res;
}
protected Cmplx[][] calcVecCA() {
  int cN = mthd.jmS.getNumCols();
  int sN = mthd.getSysBasisSize();
  Cmplx[][] res = new Cmplx[cN][sN] ;
  double[] sysE = mthd.getSysEngs().getArr();
  for (int sysIdx = 0; sysIdx < sN; sysIdx++) {
    double ei = sysE[sysIdx];
    for (int c = 0; c < cN; c++) {
      Cmplx ai = calcCAi(sysIdx, c);
      if (Double.compare(ei, mthd.getSysTotE()) == 0) {
        throw new IllegalArgumentException(log.error("E=e_i=" + (float)mthd.getSysTotE()));
      } else {
//        ai /= (ei - mthd.getSysTotE());
        ai.timesEquals(1./(ei - mthd.getSysTotE()));
        //log.dbg("ai=", ai);
      }
      res[c][sysIdx] = ai;
    }
  }
  return res;
}
private Cmplx calcCAi(int sysIdx, int c) {
  double[][] X = mthd.jmX.getArray();
  int rN = mthd.jmS.getNumRows();
  Cmplx res = new Cmplx();
  int IDX_N = 0; // N'th value is stored in the first column
  for (int r = 0; r < rN; r++) {     //log.dbg("t = ", t);  // Target channels
    JmCh ch = mthd.chArr[r];
    double xx = X[r][sysIdx];
    Cmplx f = jmCF[IDX_N][r][c];
//    Cmplx xjf = -xx * ch.getJnn().getRe() * f;
    Cmplx xjf = f.times(-xx * ch.getJnn().getRe());
    res.addToSelf(xjf);
  }
  return res;
}
private double calcHESys(Conf leftConf, int sysIdx) {
  double[][] sV = mthd.sysConfH.getEigArr(); // sysEigVec
  ConfArr sB = mthd.sysConfH.getBasis();     // sBasis
  double res = 0;
  for (int sbi = 0; sbi < sB.size(); sbi++) {   // system basis index
    double term = sV[sbi][sysIdx];     //log.dbg("term=", term);
    if (Calc.isZero(term))
      continue;
    double a = vHEBasis.get(sbi);         //log.dbg("eng=", eng);
    res += ( term * a );
  }
  return res;
}
protected Vec calcHEBasis(Conf leftConf) {
  ConfArr sB = mthd.sysConfH.getBasis();     // sBasis
  Vec res = new Vec(sB.size());
  for (int sbi = 0; sbi < sB.size(); sbi++) {   // system basis index
    Conf basisConf = sB.get(sbi);
    double a = calcHE(leftConf, basisConf);         //log.dbg("eng=", eng);
    res.set(sbi, a);
  }
  return res;
}
protected double calcSumA(Conf leftConf) {
  int initChIdx = mthd.trgtE2.getInitTrgtIdx();
  int sN = mthd.getSysBasisSize();
  double res = 0;  // amplitude
  for (int s = 0; s < sN; s++) {
    double ch = calcHESys(leftConf, s);  //log.dbg("calcScds ch=", ch);
    double ah = ch * jmA[initChIdx][s];          //log.dbg("calcScds ah=", ah);
    res += ah;
  }
  return res;
}
protected Cmplx calcSumCA(Conf leftConf) {
  int initChIdx = mthd.trgtE2.getInitTrgtIdx();
  int sN = mthd.getSysBasisSize();
  Cmplx res = new Cmplx();  // amplitude
  for (int s = 0; s < sN; s++) {
    double ch = calcHESys(leftConf, s);
    Cmplx ah = (jmCA[initChIdx][s]).times(ch);
    res.addToSelf(ah);
  }
  return res;
}
protected double calcSumK(Conf leftConf) {
  int L = 0;
  Ls LS = mthd.sysConfH.getBasis().getLs();

  JmCalcOptE1 calcOpt = mthd.getCalcOpt();
  int rN = mthd.jmR.getNumRows();
  int katoN = calcOpt.getJmTailN();
  int initChIdx = mthd.trgtE2.getInitTrgtIdx();
  int N = mthd.getN();
  FuncArr trgtStates = mthd.trgtE2.getWfsE1();
  int ID_E2_XI = ID_XI_OFFSET + N;

  double res = 0;  // amplitude
  for (int r = 0; r < rN; r++) {     //log.dbg("t = ", t);  // Target channels
    FuncVec tWf = trgtStates.get(r);
    Shell tSh = new Shell(r, tWf, L);

    for (int j = 0; j < katoN; j++) {
      double f = jmF[j][r][initChIdx];        //log.dbg("f=", f);
      FuncVec xi = tailLgrr.get(N + j);
      ShPair xiConf = ShPairFactory.makePair(tSh, xi, ID_E2_XI, L, LS);
      double v2 = calcHE(leftConf, xiConf); //log.dbg("v2=", v2);
      res += (f * v2);       //log.dbg("res=", res);
    }  log.dbg("res(katoN=" + katoN + ")=", res);
  }
  return res;
}
protected Cmplx calcSumCK(Conf leftConf) {
  int L = 0;
  Ls LS = mthd.sysConfH.getBasis().getLs();

  JmCalcOptE1 calcOpt = mthd.getCalcOpt();
  int rN = mthd.jmS.getNumRows();
  int katoN = calcOpt.getJmTailN();
  int initChIdx = mthd.trgtE2.getInitTrgtIdx();
  int N = mthd.getN();
  FuncArr trgtStates = mthd.trgtE2.getWfsE1();
  int ID_E2_XI = ID_XI_OFFSET + N;

  Cmplx res = new Cmplx();  // amplitude
  for (int r = 0; r < rN; r++) {     //log.dbg("t = ", t);  // Target channels
    FuncVec tWf = trgtStates.get(r);
    Shell tSh = new Shell(r, tWf, L);

    for (int j = 0; j < katoN; j++) {
      Cmplx f = jmCF[j][r][initChIdx];        //log.dbg("f=", f);
      FuncVec xi = tailLgrr.get(N + j);
      ShPair xiConf = ShPairFactory.makePair(tSh, xi, ID_E2_XI, L, LS);
      double v2 = calcHE(leftConf, xiConf); //log.dbg("v2=", v2);
//      res += (f * v2);
      res.addToSelf(f.times(v2));
    }  //log.dbg("res(katoN=" + katoN + ")=", res);
  }
  return res;
}
protected double calcHE(Conf leftConf, Conf conf) {
  SysE2 sysE2 = (SysE2)mthd.sysConfH.getAtom();
  Energy res = sysE2.calcH(leftConf, conf);   //log.dbg("calcHE res=", res);

//  if (DEBUG_JM1) {
//    SysE2OldOk sys = new SysE2OldOk(sysE2.getAtomZ(), sysE2.getSlaterLcr());
//    double x = sys.calcOverlap(leftConf, conf);
//    double sysTotE = mthd.getSysTotE();
//    double he = res.kin + res.pt - sysTotE * x;
//  }
  return res.p2;
}
}
