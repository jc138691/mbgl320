package scatt.jm_2008.e1;
import atom.energy.part_wave.PotH;
import atom.energy.part_wave.PotHMtrx;
import atom.wf.WFQuadr;
import atom.wf.lcr.WFQuadrLcr;
import math.Calc;
import math.func.FuncVec;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.mtrx.MtrxFactory;
import math.vec.Vec;
import project.workflow.task.test.FlowTest;
import scatt.eng.EngGrid;
import scatt.eng.EngModel;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.jm_2008.jm.target.JmCh;

import javax.utilx.log.Log;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,1/04/11,3:43 PM
 */
public abstract class ScttMthdBaseE1 extends FlowTest {
public static Log log = Log.getLog(ScttMthdBaseE1.class);
private static final double MIN_SYS_ENG_DELTA = Calc.EPS_10;
protected static final int IDX_ENRGY = 0;

public Mtrx jmR;     // NOTE!!! Only open part is corrected by / cn1.getRe();
public JmCh[] chArr;
protected Vec sysEngs;
protected WFQuadrLcr quadrLcr;
protected PotHMtrx potH;
protected LgrrOrthLcr orthonN;
protected final CalcOptE1 calcOpt;
protected double scttE;
protected double sysTotE;
protected int L = 0;
private Vec overD;      // overlap coefficients D

//public JmCh[] getChArr() {
//  return chArr;
//}
//public Mtrx getJmR() {
//  return jmR;
//}
public double getSysTotE() {
  return sysTotE;
}
public double getScttE() {
  return scttE;
}
public ScttMthdBaseE1(CalcOptE1 calcOpt) {
  this.calcOpt = calcOpt;
}
public CalcOptE1 getCalcOpt() {
  return calcOpt;
}
public ScttRes calcForScatEngModel() {
  EngGrid engs = calcScattEngs();
  return calc(engs);
}
public EngGrid calcScattEngs() {
  EngModel eng = calcOpt.getGridEng();    //log.dbg("Incident Energies =", eng);
  EngGrid engs = new EngGrid(eng);
  return engs;
}
//public ScttRes calcSysEngs() {
//  throw new IllegalArgumentException(log.error("call relevant implementation of calcSysEngs"));
////  return calc(sysEngs);
//}
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
public LgrrOrthLcr getOrthonN() {
  return orthonN;
}
public void setOrthonN(LgrrOrthLcr orthonN) {
  this.orthonN = orthonN;
}
public WFQuadrLcr getQuadrLcr() {
  return quadrLcr;
}
public void setQuadrLcr(WFQuadrLcr quadrLcr) {
  this.quadrLcr = quadrLcr;
}
public int getN() {
  return calcOpt.getLgrrModel().getN();
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

protected double calcV(FuncVec wf, FuncVec wf2) {
  FuncVec pot = potH.getPot();
  WFQuadr quadr = potH.getQuadr();
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
  LgrrModel jmModel = calcOpt.getLgrrModel();
  JmCh[] res = new JmCh[1];
    // NOTE!!! minus in "-trgtE2.getScreenZ()"
  res[0] = new JmCh(sysEng, 0, jmModel, 0);
  log.dbg("res[i]=", res[0]);
  return res;
}
protected Mtrx calcR(int calcN, int openN) {
  return null;
}
}
