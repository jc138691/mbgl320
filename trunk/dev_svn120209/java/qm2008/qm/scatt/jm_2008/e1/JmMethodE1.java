package scatt.jm_2008.e1;
import flanagan.complex.Cmplx;
import math.func.FuncVec;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.Scatt;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.theory.JmTheory;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/08/2008, Time: 16:59:55
 */
public class JmMethodE1 extends ScattMethodBaseE1 {   // E1 - one electron
public static Log log = Log.getLog(JmMethodE1.class);
public JmMethodE1(CalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScattRes calc(Vec engs) {
  ScattRes res = new ScattRes();
  int chNum = getChNum();
  int eN = engs.size();
  LgrrModel model = calcOpt.getLgrrModel();
  int N = model.getN();
  double lambda = model.getLambda();
  FuncVec arrShift = new FuncVec(engs);
  Mtrx mCs = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCs);
  for (int i = 0; i < engs.size(); i++) {
    log.dbg("i = ", i);
    double scattE = engs.get(i);
    log.dbg("E = ", scattE);
    mCs.set(i, IDX_ENRGY, scattE);
    double Jnn = JmTheory.calcJnnL0byE(N, scattE, lambda);           log.dbg("Jnn = ", Jnn);
    Cmplx sc = JmTheory.calcSCnL0byE(N, scattE, lambda);                 log.dbg("sc_N = ", sc);
    Cmplx sc1 = JmTheory.calcSCnL0byE(N - 1, scattE, lambda);             log.dbg("sc_{N-1} = ", sc1);
    double G = calcG(scattE);                                            log.dbg("G = ", G);
    double g = Jnn * G;                                                 log.dbg("g = ", g);
    double sN1 = sc1.getIm();
    double cN1 = sc1.getRe();
    double sN = sc.getIm();
    double cN = sc.getRe();
    double R = -(sN1 + g * sN) / (cN1 + g * cN);                        log.dbg("R = ", R);
    double shift = Math.atan(R);
    Cmplx S = Scatt.calcSFromR(R);                                          log.dbg("S = ", S);
//    double sigma = Scatt.calcSigmaPiFromS(S, scattE);
    double sigma = R;
    log.dbg("sigma = ", sigma).eol();
    mCs.set(i, IDX_ENRGY + 1, sigma);     // NOTE +1; first column has incident energies
    arrShift.set(i, shift);
  }
  res.setShift(arrShift);
  return res;
}
private double calcG(double E) {
  double res = 0;
  Vec d = getOverD();
  for (int i = 0; i < sysEngs.size(); i++) {
    double ei = sysEngs.get(i);
    double d2 = d.get(i) * d.get(i);
    if (Float.compare((float) ei, (float) E) == 0)
      throw new IllegalArgumentException(log.error("E=e_i=" + (float) E));
    res += (d2 / (ei - E));
  }
  return res;
}
}
