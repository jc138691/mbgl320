package papers.hy_swave.ees_bad;
import atom.wf.lcr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.IFuncArr;
import math.vec.Vec;
import scatt.Scatt;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.partial.wf.CosPWaveLcr;
import scatt.partial.wf.SinPWaveLcr;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 5/03/12, 4:04 PM
 */
public class EaMethodE1_G_db extends EesMethodE1 {   // E1 - one electron
public static Log log = Log.getLog(EaMethodE1_G_db.class);
public EaMethodE1_G_db(CalcOptE1 calcOpt) {
  super(calcOpt);
}
protected FuncArr calcPsi(double scattE, int engIdx) {  log.setDbg();
  int L = 0;
  double momP = Scatt.calcMomFromE(scattE);
  WFQuadrLcr quadr = (WFQuadrLcr) potH.getQuadr();    // CASTING!!! NOT GOOD
  IFuncArr basis = potH.getBasis();
  Vec x = quadr.getX();
  FuncArr res = new FuncArr(x);
  FuncVec psi1 = new SinPWaveLcr(quadr, momP, L);   log.dbg("sinL=", psi1);

  FuncVec gr = psi1.copyY();
  gr.mult(potH.getPot());
//  FuncArr sysWFuncs = potH.getEigFuncArr();  log.dbg("sysWFuncs=", new FuncArrDbgView(sysWFuncs));
//  FuncVec sysPsi = sysWFuncs.get(engIdx);            log.dbg("sysPsi=", sysPsi);
//  gr.mult(sysPsi);
  gr.mult(psi1);
  gr.mult(quadr.getLcrToR().getCR2());
  gr = quadr.calcFuncIntOK(gr); log.dbg("gr=int_0^r=", gr); // int_0^r
  gr.mult(1. / gr.getLast()); log.dbg("gr[0:1]=", gr); // normalize [0:1]

  FuncVec gPsi2 = new CosPWaveLcr(quadr, momP, L);   log.dbg("cosL=", gPsi2);
  gPsi2.mult(gr);   log.dbg("cosL=", gPsi2); // g(r)cos(kr)

  res.add(psi1.copyY());     // IDX_REG
  res.add(psi1.copyY());     // IDX_P_REG
  res.add(gPsi2.copyY());     // IDX_P_IRR
  FuncVec pPsi1 = res.get(IDX_P_REG);          log.dbg("resS=", pPsi1);
  FuncVec pPsi2 = res.get(IDX_P_IRR);          log.dbg("resC=", pPsi2);

  for (int i = 0; i < basis.size(); i++) {
    FuncVec fi = basis.getFunc(i);            log.dbg("fi=", fi);
    double dS = quadr.calcInt(psi1, fi);    log.dbg("dS=", dS);
    double dC = quadr.calcInt(gPsi2, fi);    log.dbg("dC=", dC);
    pPsi1.addMultSafe(-dS, fi);              log.dbg("resS=", pPsi1);
    pPsi2.addMultSafe(-dC, fi);              log.dbg("resC=", pPsi2);
  }
  for (int i = 0; i < basis.size(); i++) {
    FuncVec fi = basis.getFunc(i);            log.dbg("fi=", fi);
    double testS = quadr.calcInt(pPsi1, fi);    log.dbg("testS=", testS);
    assertEquals("testS_" + i, testS, 0d);
    assertEquals(0, testS, MAX_INTGRL_ERR_E11);

    double testC = quadr.calcInt(pPsi2, fi);    log.dbg("testC=", testC);
    assertEquals("testC_" + i, testC, 0d);
    assertEquals(0, testC, MAX_INTGRL_ERR_E11);
  }
//  FileX.writeToFile(sinL.toTab(), calcOpt.getHomeDir(), "wf", "sin_" + idxCount + ".txt");
//  FileX.writeToFile(cosL.toTab(), calcOpt.getHomeDir(), "wf", "cos_" + idxCount + ".txt");
//  FileX.writeToFile(resS.toTab(), calcOpt.getHomeDir(), "wf", "psi_sin_" + idxCount + ".txt");
//  FileX.writeToFile(resC.toTab(), calcOpt.getHomeDir(), "wf", "psi_cos_" + idxCount + ".txt");
  return res;
}
}