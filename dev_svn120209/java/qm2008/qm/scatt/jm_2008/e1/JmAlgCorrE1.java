package scatt.jm_2008.e1;
import math.Mathx;
import math.func.FuncVec;
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

//  addSumJmTail(newR);
//  log.dbg("\n newR=addSumJmTail()=\n", new MtrxDbgView(newR));

  loadNorms(newR);
  log.dbg("\n newR=loadNorms()=\n", new MtrxDbgView(newR));

  return newR;
}

private Mtrx addSumJmTail(Mtrx mR) {
  CalcOptE1 calcOpt = mthd.getCalcOpt();
  int rN = mthd.jmR.getNumRows();
  int cN = mthd.jmR.getNumCols();
  int katoN = calcOpt.getKatoN();

  LgrrModel jmModel = calcOpt.getLgrrModel();
  int N = jmModel.getN();
  LgrrModel xiModel = new LgrrModel(jmModel);
  Vec tEngs = mthd.trgtE2.getEngs();
  double[][] res = mR.getArray();
  for (int r = 0; r < rN; r++) {
    Vec psiR = freeS.get(r);
    for (int c = 0; c < cN; c++) {

    double sum = 0;
    for (int j = 0; j < katoN; j++) {
      double f = jmF[j][r][c];        //log.dbg("f=", f);
      FuncVec tailF = katoLgrr.get(N + j);
      double v = mthd.calcPotInt(psiR, tailF);
      }
      res[r][c] += sum;

    }
  }
  return new Mtrx(res);
}
private Mtrx calcSumA() {
  FuncArr sWfs = mthd.getWfsE1();

  int rN = mthd.jmR.getNumRows();
  int cN = mthd.jmR.getNumCols();
  int sN = mthd.getSysBasisSize();
  double[][] res = new double[rN][cN];
  for (int r = 0; r < rN; r++) { // channel ROWS
    JmCh chR = mthd.chArr[r];
    double rSq = chR.getSqrtAbsMom();
    Vec psiR = freeS.get(r);

    for (int c = 0; c < cN; c++) {  // channel COLS
      JmCh chC = mthd.chArr[c];
      double cSq = chC.getSqrtAbsMom();

      double sum = 0;
      for (int s = 0; s < sN; s++) { // sys states
        Vec sWf = sWfs.get(s);
        double v = mthd.calcPotInt(psiR, sWf);
        double av = jmA[c][s] * v;
        sum += av;          //log.dbg("sum=", sum);
      }     //log.dbg("final sum=", sum);
      res[r][c] = sum;
    }
  }
  return new Mtrx(res);
}

private Mtrx loadNorms(Mtrx mR) {
  int rN = mthd.jmR.getNumRows();
  int cN = mthd.jmR.getNumCols();
  double[][] res = mR.getArray();
  for (int r = 0; r < rN; r++) { // channel ROWS
    JmCh chR = mthd.chArr[r];
    double rSq = chR.getSqrtAbsMom();

    for (int c = 0; c < cN; c++) {  // channel COLS
      JmCh chC = mthd.chArr[c];
      double cSq = chC.getSqrtAbsMom();

      double norm = -2. / cSq;
      res[r][c] *= norm;
    }
  }
  return new Mtrx(res);
}
}
