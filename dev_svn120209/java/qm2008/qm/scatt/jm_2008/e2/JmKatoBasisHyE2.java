package scatt.jm_2008.e2;
import atom.wf.log_cr.WFQuadrLcr;
import math.Mathx;
import math.mtrx.Mtrx;
import math.vec.Vec;
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
protected Mtrx jmF;
protected Vec jmA;
protected LagrrLcr katoLgrr;

public JmKatoBasisHyE2(JmMthdBaseE2 jmMthdBaseE2) {      log.setDbg();
  mthd = jmMthdBaseE2;
}
public Mtrx calcKatoR_todo(Mtrx mtR) { //mt-matrix trial

  return null;
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
protected Mtrx calcFFromR() {
  CalcOptE1 calcOpt = mthd.getCalcOpt();
  int rN = mthd.openChN;
  int katoN = calcOpt.getKatoN();
  int initChIdx = mthd.trgtE2.getInitTrgtIdx();
  LgrrModel jmModel = calcOpt.getLgrrModel();
  int N = jmModel.getN();
  LgrrModel xiModel = new LgrrModel(jmModel);
  Vec tEngs = mthd.trgtE2.getEngs();
  Mtrx res = new Mtrx(rN, katoN);
  for (int r = 0; r < rN; r++) {     //log.dbg("t = ", t);  // Target channels
    for (int xiIdx = 0; xiIdx < katoN; xiIdx++) {
      xiModel.setN(N + xiIdx);                //log.dbg("N + xiIdx=", N + xiIdx);
      JmCh ch = new JmCh(mthd.getSysTotE(), tEngs.get(r), xiModel
        , -mthd.trgtE2.getScreenZ());
      double sg = Mathx.dlt(r, initChIdx) * ch.getSn();  //log.dbg("sg=", sg);
      double cg = ch.getCn().getRe();                    //log.dbg("ch.getCn()=", ch.getCn()); log.dbg("cg=", cg);
      double scR = sg + cg * mthd.jmR.get(r, initChIdx); //log.dbg("scR=", scR);
      res.set(r, xiIdx, scR);
    }
  }
  return res;
}
protected Vec calcVecA() {
  int sN = mthd.getSysBasisSize();
  Vec res = new Vec(sN);
  double[] sysE = mthd.getSysEngs().getArr();
  for (int sysIdx = 0; sysIdx < sN; sysIdx++) {
    double ei = sysE[sysIdx];
    double ai = calcAi(sysIdx);
    if (Double.compare(ei, mthd.getSysTotE()) == 0) {
      throw new IllegalArgumentException(log.error("E=e_i=" + (float)mthd.getSysTotE()));
  //      double eps = calcZeroG(i, sysE);
  //      ai /= eps;
    } else {
      ai /= (ei - mthd.getSysTotE());      //log.dbg("ai=", ai);
    }
    res.set(sysIdx, ai);
  }
  return res;
}
private double calcAi(int sysIdx) {
  double[][] X = mthd.jmX.getArray();
  int tN = jmF.getNumRows();
  double res = 0;
  int IDX_N = 0; // N'th value is stored in the first column
  for (int t = 0; t < tN; t++) {     //log.dbg("t = ", t);  // Target channels
    JmCh ch = mthd.chArr[t];
    double xx = X[t][sysIdx];
    double f = jmF.get(t, IDX_N);
    double xjf = -xx * ch.getJnn().getRe() * f;
    res += xjf;       //log.dbg("res=", res);
  }
  return res;
}
}
