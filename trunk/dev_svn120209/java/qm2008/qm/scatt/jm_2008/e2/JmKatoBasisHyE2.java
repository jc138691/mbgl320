package scatt.jm_2008.e2;
import atom.shell.Conf;
import atom.shell.ConfArr;
import atom.wf.log_cr.WFQuadrLcr;
import math.Mathx;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.LagrrLcr;
import scatt.jm_2008.jm.target.JmCh;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 18/04/12, 9:40 AM
 */
public class JmKatoBasisHyE2 {
public static Log log = Log.getLog(JmKatoBasisHyE2.class);
protected JmMthdBaseE2 mthd;
protected double[][][] jmF;  // katoIdx, K-rows, K-cols(open-only)
protected double[][] jmA;    // K-cols (open-only), sysIdx
private double[][] mBK; // matrix basis-Xi
protected LagrrLcr katoLgrr;

public JmKatoBasisHyE2(JmMthdBaseE2 jmMthdBaseE2) {      log.setDbg();
  mthd = jmMthdBaseE2;
}
public Mtrx calcKatoR(Mtrx mtR) { //mt-matrix trial
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
  double aa = calcAA(r, c);
  double ak = calcAK(r, c); // TODO

//  double ka = calcKA(r, c);
//  double kk = calcKK(r, c);

//  double res = aa + ak + ka + kk;
  double res = aa;
  return res;
}

private double calcAK(int r, int c) {
  //TODO
  ConfArr sB = mthd.sysConfH.getBasis();     // sBasis
  Vec res = new Vec(sB.size());
  for (int sbi = 0; sbi < sB.size(); sbi++) {   // system basis index
    Conf basisConf = sB.get(sbi);
    double a = calcTwoPot(clmbE2, basisConf);         //log.dbg("eng=", eng);
    res.set(sbi, a);
  }
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

protected void loadKatoLgrr() {
  if (katoLgrr != null)
    return;
  WFQuadrLcr quadr = mthd.getQuadrLcr();

  CalcOptE1 calcOpt = mthd.getCalcOpt();
  int katoN = calcOpt.getKatoN();

  LgrrModel jmModel = calcOpt.getLgrrModel();
  int N = jmModel.getN();
  LgrrModel katoModel = new LgrrModel(jmModel);
  katoModel.setN(N + katoN);     log.dbg("Kato Lgrr model =", katoModel);
  katoLgrr = new LagrrLcr(quadr, katoModel);    log.dbg("katoLgrr =\n", katoLgrr);
}
protected double[][][] calcFFromR() {
  CalcOptE1 calcOpt = mthd.getCalcOpt();
  int rN = mthd.jmR.getNumRows();
  int cN = mthd.jmR.getNumCols();
  int katoN = calcOpt.getKatoN();
//  int initChIdx = mthd.trgtE2.getInitTrgtIdx();
  LgrrModel jmModel = calcOpt.getLgrrModel();
  int N = jmModel.getN();
  LgrrModel xiModel = new LgrrModel(jmModel);
  Vec tEngs = mthd.trgtE2.getEngs();
  double[][][] res = new double[katoN][rN][cN];
  for (int r = 0; r < rN; r++) {
    for (int xiIdx = 0; xiIdx < katoN; xiIdx++) {
      xiModel.setN(N + xiIdx);                //log.dbg("N + xiIdx=", N + xiIdx);
      JmCh ch = new JmCh(mthd.getSysTotE(), tEngs.get(r), xiModel
        , -mthd.trgtE2.getScreenZ());
      for (int c = 0; c < cN; c++) {
        double sg = Mathx.dlt(r, c) * ch.getSn();  //log.dbg("sg=", sg);
        double cg = 0;
        if (ch.isOpen()) {
          cg = ch.getCn().getRe();
        } else {
          // "closed" is stored as c_{N-1} R_{\gamma \gamma_0}
          cg = ch.getCnn1().getRe();
          //log.dbg("ch.getCn()=", ch.getCn()); log.dbg("cg=", cg);
  //        JmCh dbg = new JmCh(mthd.getSysTotE(), tEngs.get(r), xiModel
  //          , -mthd.trgtE2.getScreenZ());
        }
        double scR = sg + cg * mthd.jmR.get(r, c); //log.dbg("scR=", scR);
        res[xiIdx][r][c] = scR;
      }
    }
  }
  return res;
}
protected double[][] calcVecA() {
  int cN = mthd.jmR.getNumCols();
  int sN = mthd.getSysBasisSize();
  double[][] res = new double[cN][sN] ;
  double[] sysE = mthd.getSysEngs().getArr();
  for (int sysIdx = 0; sysIdx < sN; sysIdx++) {
    double ei = sysE[sysIdx];
    for (int c = 0; c < cN; c++) {
      double ai = calcAi(sysIdx, c);
      if (Double.compare(ei, mthd.getSysTotE()) == 0) {
        throw new IllegalArgumentException(log.error("E=e_i=" + (float)mthd.getSysTotE()));
    //      double eps = calcZeroG(i, sysE);
    //      ai /= eps;
      } else {
        ai /= (ei - mthd.getSysTotE());      //log.dbg("ai=", ai);
      }
      res[c][sysIdx] = ai;
    }
  }
  return res;
}
private double calcAi(int sysIdx, int c) {
  double[][] X = mthd.jmX.getArray();
  int rN = mthd.jmR.getNumRows();
  double res = 0;
  int IDX_N = 0; // N'th value is stored in the first column
  for (int r = 0; r < rN; r++) {     //log.dbg("t = ", t);  // Target channels
    JmCh ch = mthd.chArr[r];
    double xx = X[r][sysIdx];
    double f = jmF[IDX_N][r][c];
    double xjf = -xx * ch.getJnn().getRe() * f;
    res += xjf;       //if (t == tN-1) { log.dbg("xjf=", xjf); log.dbg("res=", res);}
  }
  return res;
}
}
