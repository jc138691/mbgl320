package scatt.jm_2008.e1;
import atom.energy.part_wave.PotH;
import atom.energy.part_wave.PotHMtrx;
import atom.wf.WFQuadrD1;
import atom.wf.lcr.WFQuadrLcr;
import math.Calc;
import math.func.FuncVec;
import math.func.arr.IFuncArr;
import math.mtrx.Mtrx;
import math.vec.Vec;
import project.workflow.task.test.FlowTest;
import scatt.Scatt;
import scatt.eng.EngGrid;
import scatt.eng.EngModel;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.jm_2008.jm.laguerre.lcr.LagrrBiLcr;
import scatt.jm_2008.jm.laguerre.lcr.LagrrLcr;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.jm_2008.jm.target.JmCh;
import scatt.partial.wf.CosRegWfLcr;
import scatt.partial.wf.SinWfLcr;

import javax.utilx.log.Log;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,1/04/11,3:43 PM
 */
public abstract class ScttMthdBaseE1 extends FlowTest {
public static Log log = Log.getLog(ScttMthdBaseE1.class);
private static final double MIN_SYS_ENG_DELTA = Calc.EPS_10;
protected static final int IDX_ENRGY = 0;

public Mtrx jmR;    // NOTE!!! Closed channels stored as * cn1.getRe();
public JmCh[] chArr;
protected Vec sysEngs;
protected WFQuadrLcr quadr;
protected PotHMtrx potH;
protected LgrrOrthLcr orth;
protected LagrrLcr lgrr;
protected LagrrBiLcr lgrrBi;
protected final JmCalcOptE1 calcOpt;
protected double scttE;
protected double sysTotE;
protected int L = 0;
private Vec overD;      // overlap coefficients D

public double getSysTotE() {
  return sysTotE;
}
public double getScttE() {
  return scttE;
}
public ScttMthdBaseE1(JmCalcOptE1 calcOpt) {
  this.calcOpt = calcOpt;
}
public JmCalcOptE1 getCalcOpt() {
  return calcOpt;
}
public ScttRes calcForScttEngModel() {
  EngGrid engs = calcScattEngs();
  return calc(engs);
}
public EngGrid calcScattEngs() {
  EngModel eng = calcOpt.getGridEng();    //log.dbg("Incident Energies =", eng);
  EngGrid engs = new EngGrid(eng);
  return engs;
}
public int getSysBasisSize() {
  return sysEngs.size();
}
public Vec getSysEngs() {
  return sysEngs;
}
protected int calcOpenChNum(double scattE) {
  return 1; // only one for pot-scattering
}
public int getChNum() { // number of target channels
  return 1; // only one for pot-scattering
}
//public ScttRes calcWithMidSysEngs() {
//  Vec scttEngs = EngGridFactory.makeWithMidPoints(sysEngs);
//  return calc(scttEngs);
//}
public abstract ScttRes calc(Vec engs);
public void setSysEngs(Vec engs) {
  this.sysEngs = engs;
}
public void setOverD(Vec overD) {
  this.overD = overD;
}
public Vec getOverD() {
  return overD;
}
public PotHMtrx getPotH() {
  return potH;
}
public void setPotH(PotHMtrx potH) {
  this.potH = potH;
}
public LgrrOrthLcr getOrth() {
  return orth;
}
public void setOrth(LgrrOrthLcr orth) {
  this.orth = orth;
}
public WFQuadrLcr getQuadr() {
  return quadr;
}
public void setQuadr(WFQuadrLcr quadr) {
  this.quadr = quadr;
}
public int getN() {
  return calcOpt.getBasisOpt().getN();
}

protected double calcHE(FuncVec wf, PotHMtrx potH, FuncVec wf2, double scattE) {
  int L = 0;
  FuncVec pot = potH.getPot();
  WFQuadrD1 quadr = potH.getQuadr();
  double res = quadr.calcInt(wf, pot, wf2);
  PotH calcH = potH.makePotH();

  double kin = calcH.calcKin(L, wf, wf2);      log.dbg("kin=", kin);
  double engInt = quadr.calcInt(wf, wf2);      log.dbg("engInt=", engInt); // energy integral
  double HE = kin - scattE * engInt;           log.dbg("HE=", HE);
  res += HE;
  return res;
}

protected double calcPotInt(Vec wf, Vec wf2) {
  FuncVec pot = potH.getPot();
  WFQuadrD1 quadr = potH.getQuadr();
  double res = quadr.calcInt(wf, pot, wf2);
  return res;
}

protected int matchSysTotE() {
  double[] sysE = getSysEngs().getArr();
  for (int i = 0; i < sysE.length; i++) {
    double ei = sysE[i];
    if (Math.abs(ei - sysTotE) < MIN_SYS_ENG_DELTA) {
      log.dbg("sysE[i="+i+"]=sysTotE=", sysTotE);
      return i;
    }
  }
  return -1;
}
protected JmCh[] loadChArr(double sysEng) {
  LgrrOpt jmModel = calcOpt.getBasisOpt();
  JmCh[] res = new JmCh[1];
    // NOTE!!! minus in "-trgtE2.getScreenZ()"
  res[0] = new JmCh(sysEng, 0, jmModel, 0);
  log.dbg("res[i]=", res[0]);
  return res;
}
protected Mtrx calcR(int calcN, int openN) {
  return null;
}
public void setLgrrBi(LagrrBiLcr lgrrBi) {
  this.lgrrBi = lgrrBi;
}
public void setLgrr(LagrrLcr lgrr) {
  this.lgrr = lgrr;
}
public LagrrLcr getLgrr() {
  return lgrr;
}
public LagrrBiLcr getLgrrBi() {
  return lgrrBi;
}
public static FuncVec calcCosDelN(double chScattE
  , WFQuadrD1 quadr, IFuncArr basis, double regLambda) {  // channel scattering eng
  int L = 0;
  double momP = Scatt.calcMomFromE(chScattE);
  FuncVec wf = new CosRegWfLcr((WFQuadrLcr)quadr, momP, L, regLambda);   //log.dbg("sinL=", sinL);
  FuncVec res = delN(wf, quadr, basis);          //log.dbg("resS=", resS);
  return res;
}
public static FuncVec calcCosDelBi(double chScattE
  , WFQuadrD1 quadr, IFuncArr basis, IFuncArr basisBi, double regLambda) {  // channel scattering eng
  int L = 0;
  double momP = Scatt.calcMomFromE(chScattE);
  FuncVec wf = new CosRegWfLcr((WFQuadrLcr)quadr, momP, L, regLambda);   //log.dbg("sinL=", sinL);
  FuncVec res = delBi(wf, quadr, basis, basisBi);          //log.dbg("resS=", resS);
  return res;
}
public static FuncVec delN(FuncVec wf, WFQuadrD1 quadr, IFuncArr basis) {
  Vec x = quadr.getX();
  FuncVec res = wf.copyY();
  for (int i = 0; i < basis.size(); i++) {
    FuncVec fi = basis.getFunc(i);         //log.dbg("fi=", fi);
    double dS = quadr.calcInt(wf, fi);     //log.dbg("d=", dS);
    res.addMultSafe(-dS, fi);              //log.dbg("res=", res);
  }
  for (int i = 0; i < basis.size(); i++) {
    FuncVec fi = basis.getFunc(i);           //log.dbg("fi=", fi);
    double testInt = quadr.calcInt(res, fi);   //log.dbg("testS=", testS);
    assertEquals("testInt_" + i, testInt, 0d);
    assertEquals(0, testInt, MAX_INTGRL_ERR_E11);
  }
  return res;
}
public static FuncVec keepN(FuncVec wf, WFQuadrD1 quadr, IFuncArr basis) {
  Vec x = quadr.getX();
  FuncVec res = new FuncVec(x);
  for (int i = 0; i < basis.size(); i++) {
    FuncVec fi = basis.getFunc(i);         //log.dbg("fi=", fi);
    double dS = quadr.calcInt(wf, fi);     //log.dbg("d=", dS);
    res.addMultSafe(dS, fi);              //log.dbg("res=", res);
  }
//  for (int i = 0; i < basis.size(); i++) {
//    FuncVec fi = basis.getFunc(i);           //log.dbg("fi=", fi);
//    double testInt = quadr.calcInt(res, fi);   //log.dbg("testS=", testS);
//    assertEquals("testInt_" + i, testInt, 0d);
//    assertEquals(0, testInt, MAX_INTGRL_ERR_E11);
//  }
  return res;
}
public static FuncVec delBi(FuncVec wf, WFQuadrD1 quadr, IFuncArr basis, IFuncArr basisBi) {
  Vec x = quadr.getX();
  FuncVec res = wf.copyY();
  for (int i = 0; i < basis.size(); i++) {
    FuncVec bi = basisBi.getFunc(i);         //log.dbg("fi=", fi);
    FuncVec fi = basis.getFunc(i);         //log.dbg("fi=", fi);
    double dS = quadr.calcInt(wf, bi);     //log.dbg("d=", dS);
    res.addMultSafe(-dS, fi);              //log.dbg("res=", res);
  }
  for (int i = 0; i < basis.size(); i++) {
    FuncVec bi = basisBi.getFunc(i);           //log.dbg("fi=", fi);
    double testInt = quadr.calcInt(res, bi);   //log.dbg("testS=", testS);
    assertEquals("testInt_" + i, testInt, 0d);
    assertEquals(0, testInt, MAX_INTGRL_ERR_E11);
  }
  return res;
}
public static FuncVec calcChSinWf(double chScattE, WFQuadrLcr quadr) {  // channel scattering eng
  int L = 0;
  double momP = Scatt.calcMomFromE(chScattE);
  FuncVec res = new SinWfLcr(quadr, momP, L);   //log.dbg("sinL=", sinL);
  return res;
}
public static FuncVec calcSinDelN(double chScattE
  , WFQuadrD1 quadr, IFuncArr basis) {  // channel scattering eng
  int L = 0;
  double momP = Scatt.calcMomFromE(chScattE);
  FuncVec wf = new SinWfLcr((WFQuadrLcr)quadr, momP, L);   //log.dbg("wf=", wf);
  FuncVec res = delN(wf, quadr, basis);          //log.dbg("resS=", resS);
  return res;
}
public static FuncVec calcSinKeepN(double chScattE
  , WFQuadrD1 quadr, IFuncArr basis) {  // channel scattering eng
  int L = 0;
  double momP = Scatt.calcMomFromE(chScattE);
  FuncVec wf = new SinWfLcr((WFQuadrLcr)quadr, momP, L);   //log.dbg("wf=", wf);
  FuncVec res = keepN(wf, quadr, basis);          //log.dbg("resS=", resS);
  return res;
}
public static FuncVec calcSinDelBi(double chScattE
  , WFQuadrD1 quadr, IFuncArr basis, IFuncArr basisBi) {  // channel scattering eng
  int L = 0;
  double momP = Scatt.calcMomFromE(chScattE);
  FuncVec wf = new SinWfLcr((WFQuadrLcr)quadr, momP, L);   //log.dbg("wf=", wf);
  FuncVec res = delBi(wf, quadr, basis, basisBi);          //log.dbg("resS=", resS);
  return res;
}
}
