package scatt.jm_2008.e1;
import flanagan.complex.Cmplx;
import math.func.FuncVec;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.Scatt;
import scatt.eng.EngModel;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.theory.JmTheory;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/08/2008, Time: 16:59:55
 */
public class JmMthdE1_OLD extends ScttMthdBaseE1 {   // E1 - one electron
public static Log log = Log.getLog(JmMthdE1_OLD.class);
public JmMthdE1_OLD(CalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScttRes calc(Vec scttEngs) {
  int eN = scttEngs.size();
  EngModel engModel = calcOpt.getGridEng();
  ScttRes res = new ScttRes();
  int chNum = getChNum();
  LgrrModel model = calcOpt.getLgrrModel();
  int N = model.getN();
  double lambda = model.getLambda();
  FuncVec arrShift = new FuncVec(scttEngs);
  Mtrx mCs = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCs);
  for (int scttIdx = 0; scttIdx < eN; scttIdx++) {    log.dbg("scttIdx = ", scttIdx);
    scttE = scttEngs.get(scttIdx);                     log.dbg("E = ", scttE);
    mCs.set(scttIdx, IDX_ENRGY, scttE);
    if (scttE <= 0  ||  engModel.getFirst() > scttE ||  scttE > engModel.getLast()) {
      continue;
    }
    sysTotE = scttE;          log.info("sysTotE = ", sysTotE);
    chArr = loadChArr(sysTotE);
    int sysIdx = matchSysTotE();
    if (sysIdx == -1) {
//      jmR = calcR(1, 1);  //converting to E2
    }
    else {
      continue; // ignore
    }

    //todo: old code; test and del --------------
    double Jnn = JmTheory.calcJnnL0byE(N, scttE, lambda);           log.dbg("Jnn = ", Jnn);
    Cmplx sc = JmTheory.calcSCnL0byE(N, scttE, lambda);                 log.dbg("sc_N = ", sc);
    Cmplx sc1 = JmTheory.calcSCnL0byE(N - 1, scttE, lambda);             log.dbg("sc_{N-1} = ", sc1);
    double G = calcG(scttE);                                            log.dbg("G = ", G);
    double g = Jnn * G;                                                 log.dbg("g = ", g);
    double sN1 = sc1.getIm();
    double cN1 = sc1.getRe();
    double sN = sc.getIm();
    double cN = sc.getRe();
    double R = -(sN1 + g * sN) / (cN1 + g * cN);                        log.dbg("R = ", R);
    //--------------

    R = calcCorr(R);

    double shift = Math.atan(R);
    Cmplx S = Scatt.calcSFromK(R);                                          log.dbg("S = ", S);
//    double sigma = Scatt.calcSigmaPiFromS(S, scttE);
    double sigma = R;
    log.dbg("sigma = ", sigma).eol();
    mCs.set(scttIdx, IDX_ENRGY + 1, sigma);     // NOTE +1; first column has incident energies
    arrShift.set(scttIdx, shift);
  }
  res.setShift(arrShift);
  return res;
}
protected double calcCorr(double jmR) {
  return jmR; // no correction in the standard JM method
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