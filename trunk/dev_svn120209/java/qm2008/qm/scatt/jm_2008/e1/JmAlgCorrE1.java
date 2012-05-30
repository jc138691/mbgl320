package scatt.jm_2008.e1;
import math.Mathx;
import math.func.arr.FuncArr;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import scatt.jm_2008.e2.JmMthdBaseE2;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.target.JmCh;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 29/05/12, 1:16 PM
 */
public class JmAlgCorrE1 extends JmAlgKatoE2 {
public static Log log = Log.getLog(JmAlgCorrE1.class);
protected FuncArr freeS;
public Mtrx newR;
public JmAlgCorrE1(JmMthdBaseE2 mthd) {
  super(mthd);
}
public Mtrx calcNewR() {
  log.setDbg();
  jmF = calcFFromR();  log.dbg("jmF[0]=\n", new MtrxDbgView(new Mtrx(jmF[0])));
  jmA = calcVecA();    log.dbg("jmA=", new MtrxDbgView(new Mtrx(jmA)));
  loadKatoLgrr();

  int chNum = mthd.getChNum();  // it is ONE but preparing for E2
  freeS = mthd.makeFreeS(mthd.sysTotE, chNum);

  log.dbg("\n oldR=\n", new MtrxDbgView(mthd.jmR));
  newR = calcSumA();
  log.dbg("\n newR=calcSumA()=\n", new MtrxDbgView(newR));

//  addSumXiN_TODO(newR);
//  log.dbg("\n newR=addSumXiN_TODO()=\n", new MtrxDbgView(newR));

  return mthd.jmR; //TODO
}

private Mtrx addSumXiN_TODO(Mtrx mR) {
  CalcOptE1 calcOpt = mthd.getCalcOpt();
  int rN = mthd.jmR.getNumRows();
  int cN = mthd.jmR.getNumCols();
  int katoN = calcOpt.getKatoN();
//  int initChIdx = mthd.trgtE2.getInitTrgtIdx();
  LgrrModel jmModel = calcOpt.getLgrrModel();
  int N = jmModel.getN();
  LgrrModel xiModel = new LgrrModel(jmModel);
  Vec tEngs = mthd.trgtE2.getEngs();
  double[][] res = mR.getArray();
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
        res[r][c] = scR;
      }
    }
  }
  return new Mtrx(res);
}
private Mtrx calcSumA() {
  FuncArr sWfs = mthd.getBasisN();

  int rN = mthd.jmR.getNumRows();
  int cN = mthd.jmR.getNumCols();
  int sN = mthd.getSysBasisSize();
  double[][] res = new double[rN][cN];
  for (int r = 0; r < rN; r++) { // channel ROWS
    JmCh chR = mthd.chArr[r];
    double sqR = chR.getSqrtAbsMom();
    Vec psiR = freeS.get(r);

    for (int c = 0; c < cN; c++) {  // channel COLS
      JmCh chC = mthd.chArr[c];
      double sqC = chC.getSqrtAbsMom();

      double sum = 0;
      for (int s = 0; s < sN; s++) { // sys states
        Vec sWf = sWfs.get(s);
        double v = mthd.calcPotInt(psiR, sWf);
        double a = jmA[c][s];
        double fromA = -2. * v / (sqR * sqC);
        sum += fromA;
      }
      res[r][c] = sum;
    }
  }
  return new Mtrx(res);
}
}
