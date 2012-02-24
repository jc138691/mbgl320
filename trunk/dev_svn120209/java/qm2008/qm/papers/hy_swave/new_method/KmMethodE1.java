package papers.hy_swave.new_method;
import atom.wf.log_cr.WFQuadrLcr;
import flanagan.complex.Cmplx;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.Scatt;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.e1.ScattMethodBaseE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.jm_2008.jm.theory.JmTheory;

import javax.utilx.log.Log;
import javax.utilx.pair.Dble2;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 23/02/12, 11:18 AM
 */
public class KmMethodE1 extends ScattMethodBaseE1 {   // E1 - one electron
public static Log log = Log.getLog(KmMethodE1.class);
private static final int SC_N_ROWS = 2;
private static final int IDX_S = 0;
private static final int IDX_C = 1;
public KmMethodE1(CalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScattRes calc(Vec engs) {
  WFQuadrLcr quadr = orthonN.getQuadr();
  Vec r = quadr.getR();

  ScattRes res = new ScattRes();
  int chNum = getChNum();
  int eN = engs.size();
  Mtrx mCrss = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCrss);
  Mtrx sc = new Mtrx(SC_N_ROWS, orthonN.size());
  FuncArr psi = new FuncArr(r, SC_N_ROWS);
  for (int i = 0; i < engs.size(); i++) {              log.dbg("i = ", i);
    double scattE = engs.get(i);                           log.dbg("E = ", scattE);
    mCrss.set(i, IDX_ENRGY, scattE);
    loadPsi(scattE, psi, orthonN);
//    loadSC(scattE);
//    Dble2 g = calcG(sc, scattE);                                         log.dbg("g = ", g);
    Dble2 w = calcW(psi, psi2, scattE);                                         log.dbg("w = ", w);
    double R = -(w.a + g.a) / (w.b + g.b);                               log.dbg("R = ", R);
    Cmplx S = new Cmplx(1., R).div(new Cmplx(1., -R));
    log.dbg("S = ", S);
    S = S.add(-1);
    double k = Scatt.calcMomFromE(scattE);
    log.dbg("k = ", k);
    double k2 = k * k;
//    double sigma = Math.PI * S.abs2() / k2;
    double sigma = 0;
    log.dbg("sigma = ", sigma).eol();
    mCrss.set(i, IDX_ENRGY + 1, sigma);     // NOTE +1; first column has incident energies
  }
  return res;
}
private void loadPsi(double scattE, Mtrx psi, LgrrOrthLcr orthonN) {
  double momP = Scatt.calcMomFromE(scattE);

}
private Dble2 calcG(Mtrx mSC, double E) {
  Dble2 res = new Dble2();
  double[][] arr = mSC.getArray();
  for (int i = 0; i < sysEngs.size(); i++) {
    double ei = sysEngs.get(i);
    double ss = arr[IDX_S][i] * arr[IDX_S][i];
    double sc = arr[IDX_S][i] * arr[IDX_C][i];
    if (Float.compare((float) ei, (float) E) == 0)
      throw new IllegalArgumentException(log.error("E=e_i=" + (float) E));
    res.a += (ss / (ei - E));
    res.b += (sc / (ei - E));
  }
  return res;
}
}
