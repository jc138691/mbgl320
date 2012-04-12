package scatt.jm_2008.e2;
import atom.shell.*;
import math.Mathx;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.target.JmCh;

import javax.utilx.log.Log;
/**
* Created by Dmitry.A.Konovalov@gmail.com, 16/02/2010, 1:53:49 PM
*/
public class JmMthdBasisHyE2 extends JmMthdBaseE2 {  // two-electrons
public static Log log = Log.getLog(JmMthdBasisHyE2.class);
private Mtrx jmF;
private static final double SQRT_PI2 = Math.sqrt(2. / Math.PI);

public JmMthdBasisHyE2(CalcOptE1 calcOpt) {
  super(calcOpt);
}

/*
Load channel projections
X_i^{\alpha} = \sum_{j}  \delta_{j_1,\alpha} C_{ij} A_j D_{j_2,N-1},
*/
@Override
protected Mtrx calcX() {
  double[][] C = sysConfH.getEigVec().getArray();      log.dbg("C_ij=", new MtrxDbgView(sysConfH.getEigVec()));
  double[] D = getOverD().getArr();
  log.dbg("D_j2=", getOverD());
  ConfArr sysBasis = sysConfH.getBasis();
  int sN = getSysBasisSize();
  int cN = getChNum();
  Mtrx res = new Mtrx(cN, sN);
  for (int t = 0; t < cN; t++) {  // target channel/state
    for (int i = 0; i < sN; i++) {  // system states
      double sum = 0;
      for (int j = 0; j < sN; j++) {  // system states
        Conf sysConf = sysBasis.get(j);      //log.dbg("sysBasis["+j+"]=", sysConf);
        ShPair sp = (ShPair) sysConf;
        int j1 = sp.a.getIdx();
        int j2 = sp.b.getIdx();                       //log.dbg("(j1,j2) = ("+j1 + ", ", j2);
        if (t != j1 && t != j2)
          continue;
        double dir = 0;  // direct
        if (j1 == t) {
          dir = C[j][i] * D[j2];   // NOTE!!! [j, i]
          sum += dir;                                     //log.dbg("sum = ", sum);
        }                                           //log.dbg("dir = ", dir);
        if (j2 == t) {
          dir = C[j][i] * D[j1];   // NOTE!!! [j, i]
          sum += dir;                                     //log.dbg("sum = ", sum);
        }                                           //log.dbg("dir = ", dir);
      }
      res.set(t, i, sum);                           //log.dbg("X[" + t + ", " + i +"]=", sum);
    }
  }
  return res;
}

protected void calcSdcs_TODO(int i, ScattRes res) {
  //log.setDbg();
  jmF = calcFFromR();     log.dbg("jmF=\n", new MtrxDbgView(jmF));
  Vec vA = calcVecA();    log.dbg("vA=", new VecDbgView(vA));

  int stoppedHere = 0;
//  loadSdcsW(chArr);
////    loadSdcsW_Simpson(chArr);
//  calcSdcsFromW(i, res);
}
private Mtrx calcFFromR() {
  int tN = getChNum();
  Mtrx res = new Mtrx(tN, tN);
  for (int r = 0; r < tN; r++) {     //log.dbg("t = ", t);  // Target channels
    JmCh ch = chArr[r];
    if (!ch.isOpen()) {
      break;
    }
    for (int c = 0; c < tN; c++) {     //log.dbg("t = ", t);  // Target channels
      JmCh ch2 = chArr[c];
      if (!ch2.isOpen()) {
        break;
      }
      double sg = Mathx.dlt(r, c) * ch.getSn();
      double cg = ch2.getCn().getRe();
      double scR = sg + cg * jmR.get(r, c);
      scR *= ( SQRT_PI2 / ch2.getSqrtAbsMom());
      res.set(r, c, scR);
    }
  }
  return res;
}
protected Vec calcVecA() {
  int sN = getSysBasisSize();
  Vec res = new Vec(sN);
  double[] sysE = getSysEngs().getArr();
  for (int i = 0; i < sN; i++) {
    double ei = sysE[i];
    double ai = calcAi(i);
    if (Double.compare(ei, sysTotE) == 0) {
      throw new IllegalArgumentException(log.error("E=e_i=" + (float)sysTotE));
  //      double eps = calcZeroG(i, sysE);
  //      ai /= eps;
    } else {
      ai /= (ei - sysTotE);      log.dbg("ai=", ai);
    }
    res.set(i, ai);
  }
  return res;
}
private double calcAi(int i) {
  double[][] X = jmX.getArray();
  int tN = getChNum();
  double res = 0;
  int initChIdx = trgtE2.getInitTrgtIdx();
  for (int t = 0; t < tN; t++) {     //log.dbg("t = ", t);  // Target channels
    JmCh ch = chArr[t];
    if (!ch.isOpen()) {
      break;
    }
    double xx = X[t][i];
    double f = jmF.get(t, initChIdx);
    double xjf = -xx * ch.getJnn().getRe() * f;
    res += xjf;
  }
  return res;
}
}
