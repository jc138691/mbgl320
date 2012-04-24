package papers.hy_swave.ees_bad;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.func.arr.IFuncArr;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.Scatt;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.ScttRes;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 29/02/12, 11:49 AM
 */
public class EaMethodE1_FBorn_db extends EesMethodE1 {   // E1 - one electron
public static Log log = Log.getLog(EaMethodE1_FBorn_db.class);
public EaMethodE1_FBorn_db(CalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScttRes calcSysEngs() {
  ScttRes res = new ScttRes();
  int chNum = getChNum();
  Vec engs = getSysEngs();
  int eN = engs.size();
  Mtrx mCrss = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCrss);
  IFuncArr basis = potH.getBasis();
  EesMethodE1 methodE1 = new EesMethodE1(calcOpt);
  for (int i = 0; i < engs.size(); i++) {              log.dbg("i = ", i);
    double scattE = engs.get(i);                           log.dbg("E = ", scattE);
    double momP = Scatt.calcMomFromE(scattE);
    mCrss.set(i, IDX_ENRGY, scattE);
    FuncArr psi = methodE1.calcPsi(scattE, orthonN);
    double a = calcA(psi, scattE, i);
//    double f = calcF(a, psi, scttE, i);
//    double f = calcBornF(psi, scttE);
    double R = calcBornR(psi, scattE);
//    Cmplx S = Scatt.calcSFromF(f, momP);                                          log.dbg("S = ", S);
//    double R = Scatt.calcKFromS(S);                               log.dbg("R = ", R);
//    double sigma = Scatt.calcSigmaPiFromS(S, scttE);
    double sigma = R;
    log.dbg("sigma = ", sigma).eol();
    mCrss.set(i, IDX_ENRGY + 1, sigma);     // NOTE +1; first column has incident energies
  }
  return res;
}
@Override
public ScttRes calc(Vec engs) {
  throw new IllegalArgumentException(log.error("use calcSysEngs()"));
}
private double calcA(FuncArr psi, double scattE, int engIdx) {
  FuncArr sysWFuncs = potH.getEigFuncArr();  log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  FuncVec psiS = psi.get(IDX_P_REG);          log.dbg("resS=", psiS);
  FuncVec sysPsi = sysWFuncs.get(engIdx);            log.dbg("sysPsi=", sysPsi);
  double a11 = calcHE(psiS, potH, psiS, scattE);    log.dbg("a11=", a11);
  double ai = calcHE(sysPsi, potH, psiS, scattE);    log.dbg("ai=", ai);
  double res = -0.5 * a11 / ai;    log.dbg("S_i=", res);
  return res;
}
private double calcF(double a, FuncArr psi, double scattE, int engIdx) {
  FuncArr sysWFuncs = potH.getEigFuncArr();  log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  FuncVec psiReg = psi.get(IDX_REG);          log.dbg("resS=", psiReg);
  FuncVec psiS = psi.get(IDX_P_REG);          log.dbg("resS=", psiS);
  FuncVec sysPsi = sysWFuncs.get(engIdx);            log.dbg("sysPsi=", sysPsi);

  double sysA = calcV(psiReg, sysPsi);    log.dbg("sysA=", sysA);
  sysA *= a;                                    log.dbg("sysA * a=", sysA);

  double sysB = calcV(psiReg, psiS);    log.dbg("sysB=", sysB);
  double res = - (sysA + sysB) / scattE;    log.dbg("res=", res);
  return res;
}
private double calcBornR(FuncArr psi, double scattE) {
  double momP = Scatt.calcMomFromE(scattE);
  FuncArr sysWFuncs = potH.getEigFuncArr();  log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  FuncVec psi1 = psi.get(IDX_REG);          log.dbg("resS=", psi1);
  double sysA = calcV(psi1, psi1);    log.dbg("sysA=", sysA);
//  double res = - sysA / (scttE * 4.0 * Math.PI);    log.dbg("res=", res);
  double res = -2 * sysA / momP;    log.dbg("res=", res);
  return res;
}
}