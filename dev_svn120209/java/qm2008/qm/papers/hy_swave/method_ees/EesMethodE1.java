package papers.hy_swave.method_ees;
import atom.energy.part_wave.PotH;
import atom.energy.part_wave.PotHMtrx;
import atom.wf.WFQuadr;
import atom.wf.log_cr.WFQuadrLcr;
import flanagan.complex.Cmplx;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.func.arr.IFuncArr;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.Scatt;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.e1.ScattMethodBaseE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.partial.wf.CosRegPWaveLcr;
import scatt.partial.wf.SinKLcr;
import scatt.partial.wf.SinPWaveLcr;

import javax.utilx.log.Log;
import javax.utilx.pair.Dble2;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 28/02/12, 11:26 AM
 */
public class EesMethodE1 extends ScattMethodBaseE1 {   // E1 - one electron
public static Log log = Log.getLog(EesMethodE1.class);
public static final int IDX_REG = 0;
public static final int IDX_IRR = 1;
public static final int IDX_P_REG = 2;
public static final int IDX_P_IRR = 3;
private static final boolean OPER_P_ON = true;
public EesMethodE1(CalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScattRes calcSysEngs() {
  ScattRes res = new ScattRes();
  int chNum = getChNum();
  Vec engs = getSysEngs();
  int eN = engs.size();
  Mtrx mCrss = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCrss);
  IFuncArr basis = potH.getBasis();
  for (int i = 0; i < engs.size(); i++) {              log.dbg("i = ", i);
    double scattE = engs.get(i);                           log.dbg("E = ", scattE);
    mCrss.set(i, IDX_ENRGY, scattE);
    FuncArr psi = calcPsi(scattE, orthonN);
    Dble2 sc = calcSC(psi, scattE, i);
    double R = -sc.a / sc.b;                               log.dbg("R = ", R);
    double sysA = calcSysA(psi, scattE, i, R);
    double newR = calcRFromPsiE(psi, scattE, i, sysA, R);   log.dbg("newR = ", newR);
    Cmplx S = Scatt.calcSFromR(R);                                          log.dbg("S = ", S);
//    double sigma = Scatt.calcSigmaPiFromS(S, scattE);
//    double sigma = R;
    double sigma = newR;
    log.dbg("sigma = ", sigma).eol();
    mCrss.set(i, IDX_ENRGY + 1, sigma);     // NOTE +1; first column has incident energies
  }
  return res;
}
@Override
public ScattRes calc(Vec engs) {
  throw new IllegalArgumentException(log.error("use calcSysEngs()"));
}
protected double calcHE(FuncVec wf, PotHMtrx potH, FuncVec wf2, double scattE) {
  int L = 0;
  FuncVec pot = potH.getPot();
  WFQuadr quadr = potH.getQuadr();
  double res = quadr.calcInt(wf, pot, wf2);
  PotH calcH = potH.makePotH();

  double kin = calcH.calcKin(L, wf, wf2);      log.dbg("kin=", kin);
  double engInt = quadr.calcInt(wf, wf2);      log.dbg("engInt=", engInt); // energy integral
  double HE = kin - scattE * engInt;           log.dbg("HE=", HE);
  res += HE;
  return res;
}
protected Dble2 calcSC(FuncArr psi, double scattE, int sysIdx) {
  Dble2 res = new Dble2();
  FuncArr sysWFuncs = potH.getEigFuncArr();  log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  FuncVec psiS = psi.get(IDX_P_REG);          log.dbg("resS=", psiS);
  FuncVec psiC = psi.get(IDX_P_IRR);          log.dbg("resC=", psiC);
  FuncVec sysPsi = sysWFuncs.get(sysIdx);            log.dbg("sysPsi=", sysPsi);
  res.a = calcHE(sysPsi, potH, psiS, scattE);    log.dbg("S_i=", res.a);
  res.b = calcHE(sysPsi, potH, psiC, scattE);    log.dbg("C_i=", res.b);
  return res;
}
// this has 1./sqrt(momP)
public static FuncVec calcChPsiReg(double chScattE, LgrrOrthLcr orthN) {  // channel scattering eng
  int L = 0;
  double momP = Scatt.calcMomFromE(chScattE);
  WFQuadrLcr quadr = orthN.getQuadr();
  FuncVec res = new SinKLcr(quadr, momP, L);   //log.dbg("sinL=", sinL);
  return res;
}
public static FuncArr calcPsi(double scattE, LgrrOrthLcr orthN) {
  int L = 0;
  double momP = Scatt.calcMomFromE(scattE);
  IFuncArr basis = orthN;
  WFQuadrLcr quadr = orthN.getQuadr();
  Vec x = quadr.getX();
  FuncArr res = new FuncArr(x);
  FuncVec sinL = new SinPWaveLcr(quadr, momP, L);   log.dbg("sinL=", sinL);
  FuncVec cosL = new CosRegPWaveLcr(quadr, momP, L
    , orthN.getLambda());   log.dbg("cosL=", cosL);

  res.add(sinL.copyY());     // IDX_REG
  res.add(cosL.copyY());     // IDX_IRR
  res.add(sinL.copyY());     // IDX_P_REG
  res.add(cosL.copyY());     // IDX_P_IRR
  FuncVec resS = res.get(IDX_P_REG);          log.dbg("resS=", resS);
  FuncVec resC = res.get(IDX_P_IRR);          log.dbg("resC=", resC);

//  if (OPER_P_ON) {
    for (int i = 0; i < basis.size(); i++) {
      FuncVec fi = basis.getFunc(i);          log.dbg("fi=", fi);
      double dS = quadr.calcInt(sinL, fi);    log.dbg("dS=", dS);
      double dC = quadr.calcInt(cosL, fi);    log.dbg("dC=", dC);
      resS.addMultSafe(-dS, fi);              log.dbg("resS=", resS);
      resC.addMultSafe(-dC, fi);              log.dbg("resC=", resC);
    }
    for (int i = 0; i < basis.size(); i++) {
      FuncVec fi = basis.getFunc(i);            log.dbg("fi=", fi);
      double testS = quadr.calcInt(resS, fi);    log.dbg("testS=", testS);
      assertEquals("testS_" + i, testS, 0d);
      assertEquals(0, testS, MAX_INTGRL_ERR_E11);

      double testC = quadr.calcInt(resC, fi);    log.dbg("testC=", testC);
      assertEquals("testC_" + i, testC, 0d);
      assertEquals(0, testC, MAX_INTGRL_ERR_E11);
    }
//  }
//  FileX.writeToFile(sinL.toTab(), calcOpt.getHomeDir(), "wf", "sin_" + idxCount + ".txt");
//  FileX.writeToFile(cosL.toTab(), calcOpt.getHomeDir(), "wf", "cos_" + idxCount + ".txt");
//  FileX.writeToFile(resS.toTab(), calcOpt.getHomeDir(), "wf", "psi_sin_" + idxCount + ".txt");
//  FileX.writeToFile(resC.toTab(), calcOpt.getHomeDir(), "wf", "psi_cos_" + idxCount + ".txt");
  return res;
}
protected double calcSysA(FuncArr psi, double scattE, int engIdx, double R) {
  WFQuadrLcr quadr = (WFQuadrLcr)potH.getQuadr();    // CASTING!!! NOT GOOD
  IFuncArr basis = potH.getBasis();

  FuncArr sysWFuncs = potH.getEigFuncArr();  log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  FuncVec sysPsi = sysWFuncs.get(engIdx);            log.dbg("sysPsi=", sysPsi);
  FuncVec psiS = psi.get(IDX_REG);          log.dbg("resS=", psiS);
  FuncVec psiC = psi.get(IDX_IRR);          log.dbg("resC=", psiC);
  FuncVec last = basis.getFunc(basis.size() - 1);

  double b = quadr.calcInt(last, sysPsi);
  double s = quadr.calcInt(last, psiS);
  double c = quadr.calcInt(last, psiC);
  double res =  (s + R * c) / b;
  return res;
}
protected double calcRFromPsiE(FuncArr psi, double scattE, int engIdx, double sysA, double R) {
  double momP = Scatt.calcMomFromE(scattE);

  WFQuadrLcr quadr = (WFQuadrLcr)potH.getQuadr();    // CASTING!!! NOT GOOD
  IFuncArr basis = potH.getBasis();

  FuncArr sysWFuncs = potH.getEigFuncArr();  //log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  FuncVec sysPsi = sysWFuncs.get(engIdx);            //log.dbg("sysPsi=", sysPsi);
  FuncVec psiS = psi.get(IDX_REG);          //log.dbg("psiS=", psiS);
  FuncVec psiPS = psi.get(IDX_P_REG);          //log.dbg("psiPS=", psiPS);
  FuncVec psiPC = psi.get(IDX_P_IRR);          //log.dbg("psiPC=", psiPC);

// NOTE!!! Using psiPS is WRONG!!!
//  double b = calcV(psiPS, sysPsi);
//  b *= sysA;
//  double s = calcV(psiPS, psiPS);
//  double c = calcV(psiPS, psiPC);

  // THIS IS CORRECT; use psiS
  double b = calcV(psiS, sysPsi);
  b *= sysA;
  double s = calcV(psiS, psiPS);
  double c = calcV(psiS, psiPC);

  double res = b + s + R * c;
  double norm =  -2. / momP;       // WHY -2, not 2?
  res *= norm;
  return res;
}
protected double calcV(FuncVec wf, FuncVec wf2) {
  FuncVec pot = potH.getPot();
  WFQuadr quadr = potH.getQuadr();
  double res = quadr.calcInt(wf, pot, wf2);
  return res;
}
}