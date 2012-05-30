package scatt.jm_2008.e1;
import atom.wf.lcr.WFQuadrLcr;
import math.Mathx;
import math.vec.Vec;
import scatt.jm_2008.e2.JmMthdBaseE2;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.LagrrLcr;
import scatt.jm_2008.jm.target.JmCh;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 29/05/12, 10:58 AM
 */
public class JmAlgKatoE2 {
public static Log log = Log.getLog(JmAlgKatoE2.class);
protected double[][][] jmF;  // katoIdx, K-rows, K-cols(open-only)
protected double[][] jmA;    // K-cols (open-only), sysIdx
protected LagrrLcr katoLgrr;
protected JmMthdBaseE2 mthd;

public JmAlgKatoE2(JmMthdBaseE2 mthd) {      log.setDbg();
  this.mthd = mthd;
}
protected void loadKatoLgrr() {
  if (katoLgrr != null)
    return;
  WFQuadrLcr quadr = mthd.getQuadr();

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
  double[] sEngs = mthd.getSysEngs().getArr();
  for (int s = 0; s < sN; s++) {
    double ei = sEngs[s];
    for (int c = 0; c < cN; c++) { // target channels
      double ai = calcAi(s, c);
      if (Double.compare(ei, mthd.getSysTotE()) == 0) {
        throw new IllegalArgumentException(log.error("E=e_i=" + (float)mthd.getSysTotE()));
      } else {
        ai /= (ei - mthd.getSysTotE());      //log.dbg("ai=", ai);
      }
      res[c][s] = ai;
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
