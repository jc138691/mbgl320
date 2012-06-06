package scatt.jm_2008.e1;
import atom.wf.lcr.WFQuadrLcr;
import flanagan.complex.Cmplx;
import math.func.FuncVec;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.Scatt;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.partial.wf.SinWfLcr;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 28/05/12, 11:29 AM
 */
// First Born
public class FbMthdE1 extends ScttMthdBaseE1 {   // E1 - one electron
public static Log log = Log.getLog(JmMthdE1_OLD.class);
public FbMthdE1(JmCalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScttRes calc(Vec engs) {
  ScttRes res = new ScttRes();
  int chNum = getChNum();
  int eN = engs.size();
  LgrrOpt model = calcOpt.getBasisOpt();
  int N = model.getN();
  double lambda = model.getLambda();
  FuncVec arrShift = new FuncVec(engs);
  Mtrx mCs = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCs);
  for (int i = 0; i < engs.size(); i++) {       log.dbg("i = ", i);
    double scattE = engs.get(i);             log.dbg("E = ", scattE);
    mCs.set(i, IDX_ENRGY, scattE);
    double momP = Scatt.calcMomFromE(scattE);
    WFQuadrLcr quadr = orth.getQuadr();
    FuncVec sinL = new SinWfLcr(quadr, momP, L);   log.dbg("sinL=", sinL);
    double v = calcPotInt(sinL, sinL);
    double R = -2. * v / momP;                        log.dbg("R = ", R);
    double shift = Math.atan(R);
    Cmplx S = Scatt.calcSFromK(R);                                          log.dbg("S = ", S);
//    double sigma = Scatt.calcSigmaPiFromS(S, scttE);
//    double sigma = R / momP;  // DEBUG
    double sigma = R; // DEBUG
    log.dbg("sigma = ", sigma).eol();
    mCs.set(i, IDX_ENRGY + 1, sigma);     // NOTE +1; first column has incident energies
    arrShift.set(i, shift);
  }
  res.setShift(arrShift);
  return res;
}
}
