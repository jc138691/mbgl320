package scatt.jm_2008.e1;
import math.mtrx.Mtrx;
import scatt.jm_2008.e2.JmMthdBaseE2;
import scatt.jm_2008.e2.JmSdcsBasisHyE2;
import scatt.jm_2008.jm.ScttRes;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 29/05/12, 1:53 PM
 */
// [120529] converted JmMthdE1 to use JmMthdBaseE2: nice!
public class JmMthdE1 extends JmMthdBaseE2 {
public static Log log = Log.getLog(JmMthdE1.class);
public JmMthdE1(CalcOptE1 calcOpt) {
  super(calcOpt);
}

@Override
protected Mtrx calcX() {   // very nice!! E1,E2,E3 are now all the same
  double[] D = getOverD().getArr();  log.dbg("D_j2=", getOverD());
  int sN = getSysBasisSize();
  int cN = 1;  // ONLY ONE CHANNEL
  Mtrx res = new Mtrx(cN, sN);
  for (int t = 0; t < cN; t++) {  // target channel/state
    for (int i = 0; i < sN; i++) {  // system states
      // X is just D for pot-scattering
      res.set(t, i, D[i]);  //log.dbg("X[" + t + ", " + i +"]=", sum);
    }
  }
  return res;
}

protected void calcSdcs(int i, ScttRes res, int prntN) {
  // nothing to do; target does not have an electron
}
}
