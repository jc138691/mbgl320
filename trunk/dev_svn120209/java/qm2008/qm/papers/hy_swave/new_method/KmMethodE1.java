package papers.hy_swave.new_method;
import atom.wf.WFQuadr;
import atom.wf.log_cr.WFQuadrLcr;
import flanagan.complex.Cmplx;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.mtrx.Mtrx;
import math.vec.Vec;
import scatt.Scatt;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.e1.ScattMethodBaseE1;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.partial.wf.CosRegPWaveLcr;
import scatt.partial.wf.SinPWaveLcr;

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

//  WFQuadrLcr quadr = orthonN.getQuadr();
//  Vec r = quadr.getR();

  ScattRes res = new ScattRes();
  int chNum = getChNum();
  int eN = engs.size();
  Mtrx mCrss = new Mtrx(eN, chNum + 1);   // NOTE!!! +1 for incident energies column; +1 for target channel eneries
  res.setCrossSecs(mCrss);
  Mtrx mSC = new Mtrx(SC_N_ROWS, orthonN.size());
  for (int i = 0; i < engs.size(); i++) {              log.dbg("i = ", i);
    double scattE = engs.get(i);                           log.dbg("E = ", scattE);
    mCrss.set(i, IDX_ENRGY, scattE);
    FuncArr psi = calcPsi(scattE, orthonN, i);
    loadSC(mSC, psi);
    Dble2 g = calcG(mSC, scattE);                                         log.dbg("g = ", g);
    Dble2 w = calcW(psi);                                         log.dbg("w = ", w);
    double R = -(w.a + g.a) / (w.b + g.b);                               log.dbg("R = ", R);
    Cmplx S = new Cmplx(1., R).div(new Cmplx(1., -R));    log.dbg("S = ", S);
    S = S.add(-1);
    double k = Scatt.calcMomFromE(scattE);                log.dbg("k = ", k);
    double k2 = k * k;
    double sigma = Math.PI * S.abs2() / k2;
//    double sigma = 0;
    log.dbg("sigma = ", sigma).eol();
    mCrss.set(i, IDX_ENRGY + 1, sigma);     // NOTE +1; first column has incident energies
  }
  return res;
}
private Dble2 calcW(FuncArr psi) {
  Dble2 res = new Dble2();
  FuncVec pot = potH.getPot();            log.dbg("pot=", pot);
  FuncVec psiS = psi.get(IDX_S);          log.dbg("resS=", psiS);
  FuncVec psiC = psi.get(IDX_C);          log.dbg("resC=", psiC);
  WFQuadr quadr = potH.getQuadr();
  res.a = quadr.calcInt(psiS, pot, psiS);
  res.b = quadr.calcInt(psiS, pot, psiC);    log.dbg("res=", res);
  return res;
}
private void loadSC(Mtrx mSC, FuncArr psi) {
  FuncArr sysWFuncs = potH.getEigFuncArr();  log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
  FuncVec pot = potH.getPot();            log.dbg("pot=", pot);
  FuncVec psiS = psi.get(IDX_S);          log.dbg("resS=", psiS);
  FuncVec psiC = psi.get(IDX_C);          log.dbg("resC=", psiC);
  WFQuadr quadr = potH.getQuadr();
  for (int i = 0; i < sysWFuncs.size(); i++) {
    FuncVec sysPsi = sysWFuncs.get(i);            log.dbg("sysPsi=", sysPsi);
    double S_i = quadr.calcInt(sysPsi, pot, psiS);    log.dbg("dS=", S_i);
    double C_i = quadr.calcInt(sysPsi, pot, psiC);    log.dbg("dC=", C_i);
    mSC.set(IDX_S, i, S_i);
    mSC.set(IDX_C, i, C_i);
  }
}
private FuncArr calcPsi(double scattE, LgrrOrthLcr orthonN, int idxCount) {
  int L = 0;
  double momP = Scatt.calcMomFromE(scattE);
  WFQuadrLcr quadr = orthonN.getQuadr();
  Vec x = quadr.getX();
  FuncArr res = new FuncArr(x);
  FuncVec sinL = new SinPWaveLcr(quadr, momP, L);   log.dbg("sinL=", sinL);
  FuncVec cosL = new CosRegPWaveLcr(quadr, momP, L);   log.dbg("cosL=", cosL);

  res.add(sinL.copyY());
  res.add(cosL.copyY());
  FuncVec resS = res.get(IDX_S);          log.dbg("resS=", resS);
  FuncVec resC = res.get(IDX_C);          log.dbg("resC=", resC);

  for (int i = 0; i < orthonN.size(); i++) {
    FuncVec fi = orthonN.get(i);            log.dbg("fi=", fi);
    double dS = quadr.calcInt(sinL, fi);    log.dbg("dS=", dS);
    double dC = quadr.calcInt(cosL, fi);    log.dbg("dC=", dC);
    resS.addMultSafe(-dS, fi);              log.dbg("resS=", resS);
    resC.addMultSafe(-dC, fi);              log.dbg("resC=", resC);
  }
  for (int i = 0; i < orthonN.size(); i++) {
    FuncVec fi = orthonN.get(i);            log.dbg("fi=", fi);
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
