package scatt.jm_2008.e1;
import atom.energy.part_wave.PotHMtrx;
import atom.wf.lcr.WFQuadrLcr;
import math.vec.Vec;
import project.workflow.task.test.FlowTest;
import scatt.eng.EngGrid;
import scatt.eng.EngModel;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,1/04/11,3:43 PM
 */
public abstract class ScttMthdBaseE1 extends FlowTest {
protected static final int IDX_ENRGY = 0;
private Vec overD;      // overlap coefficients D
protected Vec sysEngs;
protected WFQuadrLcr quadrLcr;
protected PotHMtrx potH;
protected LgrrOrthLcr orthonN;
protected final CalcOptE1 calcOpt;
protected double scttE;
protected double sysTotE;
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
public int getChNum() { // number of target channels
  return 1; // only one for pt-scattering
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
}