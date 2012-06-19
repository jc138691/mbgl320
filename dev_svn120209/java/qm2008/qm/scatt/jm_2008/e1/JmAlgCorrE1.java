package scatt.jm_2008.e1;
import math.Calc;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.mtrx.api.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import scatt.jm_2008.e2.JmMthdBaseE2;
import scatt.jm_2008.jm.target.JmCh;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 29/05/12, 1:16 PM
 */
public class JmAlgCorrE1 extends JmTailE2 {
public static Log log = Log.getLog(JmAlgCorrE1.class);
protected FuncArr sinWfs;
protected FuncArr sinDelN;
protected FuncArr cosDelN;
public Mtrx newR;
public JmAlgCorrE1(JmMthdBaseE2 mthd) {
  super(mthd);
}
public Mtrx calcNewR() {
  log.setDbg();
  jmF = calcFFromR();  log.dbg("jmF[0]=\n", new MtrxDbgView(new Mtrx(jmF[0])));
  jmA = calcVecA();    log.dbg("jmA=", new MtrxDbgView(new Mtrx(jmA)));
  loadKatoLgrr();  // todo: del once sinDelN is working

  int chNum = mthd.getChNum();  // it is ONE but preparing for E2
  sinWfs = mthd.makeSinWfs(mthd.sysTotE, chNum);
//  sinWfs = mthd.makeSinKeepN(mthd.sysTotE, chNum);//DEBUG
//  sinWfs = mthd.makeSinDelN(mthd.sysTotE, chNum);//DEBUG

//  FuncArr sWfs = mthd.getWfsE1();
//  sinDelN = mthd.makeSinDelN(mthd.sysTotE, chNum);
//  cosDelN = mthd.makeCosDelN(mthd.sysTotE, chNum);
//  sinDelN = mthd.makeSinDLgrr(mthd.sysTotE, chNum);
//  cosDelN = mthd.makeCosDLgrr(mthd.sysTotE, chNum);

  log.dbg("\n oldR=\n", new MtrxDbgView(mthd.jmR));
  newR = calcSumA();
  log.dbg("\n newR=calcSumA()=\n", new MtrxDbgView(newR));

  addSumJmTail(newR);
//  addSumJmAll_BAD(newR);
  log.dbg("\n newR=addSumJmTail()=\n", new MtrxDbgView(newR));

  loadNorms(newR);
  log.dbg("\n newR=loadNorms()=\n", new MtrxDbgView(newR));

  return newR;
}

private void addSumJmTail(Mtrx mR) {
  int rN = mthd.jmR.getNumRows();
  int cN = mthd.jmR.getNumCols();
  int jN = jmF.length;
  int N = mthd.getN();

  double[][] res = mR.getArray();
  for (int r = 0; r < rN; r++) {
    Vec psiR = sinWfs.get(r);
    for (int c = 0; c < cN; c++) {
      double sum = 0;
      for (int R2 = 0; R2 < rN; R2++) {
        for (int j = 0; j < jN; j++) {
          double f = jmF[j][R2][c];        //log.dbg("f=", f);
          FuncVec tailF = tailLgrr.get(N + j);  // NOTE! (N+j+1) is wrong
          double v = mthd.calcPotInt(psiR, tailF);
          sum += (f * v);
        }
      }
      res[r][c] += sum;
    }
  }
}
private void addSumJmAll_BAD(Mtrx mR) {
  int rN = mthd.jmR.getNumRows();
  int cN = mthd.jmR.getNumCols();
  double[][] res = mR.getArray();
  for (int r = 0; r < rN; r++) {
    Vec psiR = sinWfs.get(r);
    for (int c = 0; c < cN; c++) {
      double sum = 0;
      for (int R2 = 0; R2 < rN; R2++) {
        Vec sinWf = sinDelN.get(R2);
//        Vec cosWf = cosDelN.get(R2);
//        double fb = mthd.calcPotInt(psiR, psiR);
        double sinV = mthd.calcPotInt(psiR, sinWf);
//        double sinV = mthd.calcPotInt(psiR, psiR);
//        double cosV = mthd.calcPotInt(psiR, cosWf);

        double tanX = mthd.jmR.get(R2, c);
        double cosX = Calc.cosFromTan(tanX);
        double sinX = Calc.sinFromCos(cosX);
        double f = sinV * cosX;
//        double f = sinV * cosX + cosV * sinX;
        sum += f;
      }
      res[r][c] += sum;
    }
  }
}
private Mtrx calcSumA() {
  FuncArr sWfs = mthd.getWfsE1();

  int rN = mthd.jmR.getNumRows();
  int cN = mthd.jmR.getNumCols();
  int sN = mthd.getSysBasisSize();
  double[][] res = new double[rN][cN];
  for (int r = 0; r < rN; r++) { // channel ROWS
    Vec psiR = sinWfs.get(r);
    for (int c = 0; c < cN; c++) {  // channel COLS
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
