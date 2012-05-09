package papers.hy_swave.ees_bad;
import atom.energy.part_wave.PotH;
import atom.energy.part_wave.PotHMtrx;
import atom.wf.WFQuadr;
import atom.wf.lcr.WFQuadrLcr;
import flanagan.complex.Cmplx;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.func.arr.IFuncArr;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.Scatt;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.e1.ScttMthdBaseE1;
import scatt.jm_2008.jm.ScttRes;
import scatt.partial.wf.CosRegPWaveLcr;
import scatt.partial.wf.SinPWaveLcr;

import javax.utilx.log.Log;
import javax.utilx.pair.Dble2;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 23/02/12, 11:18 AM
 */
public class EaMethodE1_all_engs_wrong extends ScttMthdBaseE1 {   // E1 - one electron
public static Log log = Log.getLog(EaMethodE1_all_engs_wrong.class);
private static final int SC_N_ROWS = 3;
private static final int IDX_S = 0;
private static final int IDX_C = 1;
private static final int IDX_D = 2;
public EaMethodE1_all_engs_wrong(CalcOptE1 calcOpt) {
  super(calcOpt);
}
public ScttRes calc(Vec engs) {
  ScttRes res = new ScttRes();
  int chNum = getChNum();
  int eN = engs.size();
  Mtrx mCrss = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCrss);
  IFuncArr basis = potH.getBasis();
  Mtrx mSC = new Mtrx(SC_N_ROWS, basis.size());
  for (int i = 0; i < engs.size(); i++) {              log.dbg("i = ", i);
    double scattE = engs.get(i);                           log.dbg("E = ", scattE);
    mCrss.set(i, IDX_ENRGY, scattE);
    FuncArr psi = calcPsi(scattE, potH, i);
    loadSC(mSC, psi, scattE);
    Dble2 g = calcG(mSC, scattE);                                         log.dbg("g = ", g);
    Dble2 w = calcW(psi, scattE);                                         log.dbg("w = ", w);
    double R = -(w.a + g.a) / (w.b + g.b);                               log.dbg("R = ", R);
    Cmplx S = Scatt.calcSFromK(R);                                          log.dbg("S = ", S);
//    double sigma = Scatt.calcSigmaPiFromS(S, scttE);
    double sigma = R;
//    double sigma = 0;
    log.dbg("sigma = ", sigma).eol();
    mCrss.set(i, IDX_ENRGY + 1, sigma);     // NOTE +1; first column has incident energies
  }
  return res;
}
private Dble2 calcW(FuncArr psi, double scattE) {
  Dble2 res = new Dble2();
  FuncVec psiS = psi.get(IDX_S);          log.dbg("resS=", psiS);
  FuncVec psiC = psi.get(IDX_C);          log.dbg("resC=", psiC);
  FuncVec xiN = orthonN.getLast();
  res.a = calcHE(xiN, potH, psiS, scattE);
  res.b = calcHE(xiN, potH, psiC, scattE);  log.dbg("res=", res);
  return res;
}
private double calcHE(FuncVec wf, PotHMtrx potH, FuncVec wf2, double scattE) {
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
private void loadSC(Mtrx mSC, FuncArr psi, double scattE) {
  FuncArr sysWFuncs = potH.getEigFuncArr();  log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  FuncVec psiS = psi.get(IDX_S);          log.dbg("resS=", psiS);
  FuncVec psiC = psi.get(IDX_C);          log.dbg("resC=", psiC);
  FuncVec xiN = orthonN.getLast();
  for (int i = 0; i < sysWFuncs.size(); i++) {
    FuncVec sysPsi = sysWFuncs.get(i);            log.dbg("sysPsi=", sysPsi);
    double S_i = calcHE(sysPsi, potH, psiS, scattE);    log.dbg("S_i=", S_i);
    double C_i = calcHE(sysPsi, potH, psiC, scattE);    log.dbg("C_i=", C_i);
    double D_i = calcHE(xiN, potH, sysPsi, scattE);    log.dbg("D_i=", D_i);
    mSC.set(IDX_S, i, S_i);
    mSC.set(IDX_C, i, C_i);
    mSC.set(IDX_D, i, D_i);
  }
}
private FuncArr calcPsi(double scattE, PotHMtrx potH, int idxCount) {
  int L = 0;
  double momP = Scatt.calcMomFromE(scattE);
  WFQuadrLcr quadr = (WFQuadrLcr)potH.getQuadr();    // CASTING!!! NOT GOOD
  IFuncArr basis = potH.getBasis();
  Vec x = quadr.getX();
  FuncArr res = new FuncArr(x);
  FuncVec sinL = new SinPWaveLcr(quadr, momP, L);   log.dbg("sinL=", sinL);
  FuncVec cosL = new CosRegPWaveLcr(quadr, momP, L, momP);   log.dbg("cosL=", cosL);

  res.add(sinL.copyY());
  res.add(cosL.copyY());
  FuncVec resS = res.get(IDX_S);          log.dbg("resS=", resS);
  FuncVec resC = res.get(IDX_C);          log.dbg("resC=", resC);

  for (int i = 0; i < basis.size(); i++) {
    FuncVec fi = basis.getFunc(i);            log.dbg("fi=", fi);
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
//  FileX.writeToFile(sinL.toTab(), calcOpt.getHomeDir(), "wf", "sin_" + idxCount + ".txt");
//  FileX.writeToFile(cosL.toTab(), calcOpt.getHomeDir(), "wf", "cos_" + idxCount + ".txt");
//  FileX.writeToFile(resS.toTab(), calcOpt.getHomeDir(), "wf", "psi_sin_" + idxCount + ".txt");
//  FileX.writeToFile(resC.toTab(), calcOpt.getHomeDir(), "wf", "psi_cos_" + idxCount + ".txt");
  return res;
}
private Dble2 calcG(Mtrx mSC, double E) {
  Dble2 res = new Dble2();
  double[][] arr = mSC.getArray();
  for (int i = 0; i < sysEngs.size(); i++) {
    double ei = sysEngs.get(i);
    double ss = arr[IDX_D][i] * arr[IDX_S][i];
    double sc = arr[IDX_D][i] * arr[IDX_C][i];
    if (Float.compare((float) ei, (float) E) == 0)
      throw new IllegalArgumentException(log.error("E=e_i=" + (float) E));
    res.a += (ss / (ei - E));
    res.b += (sc / (ei - E));
  }
  return res;
}

}