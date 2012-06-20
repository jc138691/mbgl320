package scatt.jm_2008.e2;
import atom.shell.*;
import math.mtrx.api.Mtrx;
import math.mtrx.MtrxDbgView;
import scatt.jm_2008.e1.JmCalcOptE1;
import scatt.jm_2008.jm.ScttRes;

import javax.utilx.log.Log;
/**
* Created by Dmitry.A.Konovalov@gmail.com, 16/02/2010, 1:53:49 PM
*/
public class JmMthdBasisHyE2 extends JmMthdBaseE2 {  // two-electrons
public static Log log = Log.getLog(JmMthdBasisHyE2.class);
private JmSdcsBasisHyE2 scdsHyE2;

public JmMthdBasisHyE2(JmCalcOptE1 calcOpt) {
  super(calcOpt);
}

/*
Load channel projections
X_i^{\alpha} = \sum_{j}  \delta_{j_1,\alpha} C_{ij} A_j D_{j_2,N-1},
*/
@Override
protected Mtrx calcX() {
  double[][] C = sysConfH.getEigVec().getArr2D();      log.dbg("C_ij=", new MtrxDbgView(sysConfH.getEigVec()));
  double[] D = getOverD().getArr();
  log.dbg("D_j2=", getOverD());
  LsConfs sysBasis = sysConfH.getConfs();
  int sN = getSysBasisSize();
  int cN = getChNum();
  Mtrx res = new Mtrx(cN, sN);
  for (int t = 0; t < cN; t++) {  // target channel/state
    for (int i = 0; i < sN; i++) {  // system states
      double sum = 0;
      for (int j = 0; j < sN; j++) {  // system states
        LsConf sysConf = sysBasis.get(j);      //log.dbg("sysBasis["+j+"]=", sysConf);
        ShPair sp = (ShPair) sysConf;
        int j1 = sp.a.getIdx();
        int j2 = sp.b.getIdx();                       //log.dbg("(j1,j2) = ("+j1 + ", ", j2);
        if (t != j1 && t != j2)
          continue;
        double dir = 0;  // direct
        if (j1 == t) {
          dir = C[j][i] * D[j2];   // NOTE!!! [j, i]
          sum += dir;                                     //log.dbg("sum = ", sum);
        }                                           //log.dbg("di = ", di);
        if (j2 == t) {
          dir = C[j][i] * D[j1];   // NOTE!!! [j, i]
          sum += dir;                                     //log.dbg("sum = ", sum);
        }                                           //log.dbg("di = ", di);
      }
      res.set(t, i, sum);                           //log.dbg("X[" + t + ", " + i +"]=", sum);
    }
  }
  return res;
}

protected void calcSdcs(int scttIdx, ScttRes res, int prntN) {
  if (scdsHyE2 == null) {
    scdsHyE2 = new JmSdcsBasisHyE2(this);
  }
  scdsHyE2.calcScds(scttIdx, res, prntN);
}
}
