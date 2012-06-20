package scatt.jm_2008.e1;
import atom.wf.lcr.WFQuadrLcr;
import math.Calc;
import math.Mathx;
import scatt.jm_2008.e2.JmMthdBaseE2;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.jm_2008.jm.laguerre.lcr.LagrrLcr;
import scatt.jm_2008.jm.target.JmCh;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 29/05/12, 10:58 AM
 */
public class JmTailE2 {
public static Log log = Log.getLog(JmTailE2.class);
protected double[][][] jmF;  // katoIdx, K-rows, K-cols(open-only)
protected double[][] jmA;    // K-cols (open-only), sysIdx
protected LagrrLcr tailLgrr;
protected JmMthdBaseE2 mthd;

public JmTailE2(JmMthdBaseE2 mthd) {      log.setDbg();
  this.mthd = mthd;
}
protected void loadKatoLgrr() {
  if (tailLgrr != null)
    return;
  WFQuadrLcr quadr = mthd.getQuadr();

  JmCalcOptE1 calcOpt = mthd.getCalcOpt();
  int tailN = calcOpt.getJmTailN();

  LgrrOpt lgrrN = calcOpt.getBasisOpt();
  int N = lgrrN.getN();
  LgrrOpt lgrrTail = new LgrrOpt(lgrrN);
  lgrrTail.setN(N + tailN);     log.dbg("Kato Lgrr model =", lgrrTail);
  tailLgrr = new LagrrLcr(quadr, lgrrTail);    log.dbg("tailLgrr =\n", tailLgrr);
}

protected double[][][] calcFFromR() {
  JmCalcOptE1 calcOpt = mthd.getCalcOpt();
  int rN = mthd.jmR.getNumRows();
  int cN = mthd.jmR.getNumCols();
  int jN = calcOpt.getJmTailN();

  LgrrOpt lgrrN = calcOpt.getBasisOpt();
  int N = lgrrN.getN();
  LgrrOpt lgrrTail = new LgrrOpt(lgrrN);
  double[] chE = mthd.trgtE2.getEngs().getArr(); // channel energies

  double[][][] res = new double[jN][rN][cN];
  for (int r = 0; r < rN; r++) {
    JmCh rCh = mthd.chArr[r];
    for (int j = 0; j < jN; j++) {  // JM-lgrrTail
      lgrrTail.setN(N + j);                //log.dbg("N + j=", N + j);
      JmCh jCh = new JmCh(mthd.getSysTotE(), chE[r], lgrrTail
        , -mthd.trgtE2.getScreenZ());

      for (int c = 0; c < cN; c++) {
        JmCh cCh = mthd.chArr[c];
        double rSj = Mathx.dlt(r, c) * jCh.getSn();  //log.dbg("rSn=", rSn);
        double rCj = 0;
        if (jCh.isOpen()) {
          rCj = jCh.getCn().getRe();
//          rCj = jCh.getSn(); // DEBUG
//          rCj = jCh.getCn1().getRe();  // DEBUG
        } else {
          // "closed" is stored as c_{N-1} R_{\gamma \gamma_0}
          rCj = jCh.getCnn1().getRe();
          //log.dbg("jCh.getCn()=", jCh.getCn()); log.dbg("rCj=", rCj);
  //        JmCh dbg = new JmCh(mthd.getSysTotE(), tEngs.get(r), lgrrTail
  //          , -mthd.trgtE2.getScreenZ());
        }
        double tanX = mthd.jmR.get(r, c);
        double cosX = Calc.cosFromTan(tanX);
        double sinX = Calc.sinFromCos(cosX);
        double f = rSj + rCj * tanX; //log.dbg("f=", f);
//        double f = rSj * cosX + rCj * sinX; //log.dbg("f=", f);    // WRONG!!!
        double rSq = rCh.getSqrtAbsMom();
        res[j][r][c] = f / rSq;
      }
    }
  }
  return res;
}
protected double[][] calcVecA() {
  int cN = mthd.jmR.getNumCols();
  int sN = mthd.getSysBasisSize();
  double[][] res = new double[cN][sN] ;
  double[] sE = mthd.getSysEngs().getArr();
  for (int s = 0; s < sN; s++) {
    double es = sE[s];
    for (int c = 0; c < cN; c++) { // target channels
      double scA = calcAsc(s, c);
      if (Float.compare((float)es, (float)mthd.sysTotE) == 0) {
        throw new IllegalArgumentException(log.error("E=e_i=" + (float)mthd.getSysTotE()));
      } else {
        scA /= (es - mthd.sysTotE);      //log.dbg("scA=", scA);
      }
      res[c][s] = scA;
    }
  }
  return res;
}
private double calcAsc(int s, int c) {
  double[][] X = mthd.jmX.getArr2D();
  int rN = mthd.jmR.getNumRows();
  double sum = 0;
  int IDX_N = 0; // N'th value is stored in the first column
  for (int r = 0; r < rN; r++) {     //log.dbg("r = ", r);
    JmCh rCh = mthd.chArr[r];
    double xx = X[r][s];             //log.dbg("xx = ", xx);
    double f = jmF[IDX_N][r][c];
    double xjf = -xx * rCh.getJnn().getRe() * f;  //log.dbg("xjf = ", xjf);
    sum += xjf;       //log.dbg("sum=", sum);
  } //log.dbg("res sum=", sum);
  return sum;
}
}
