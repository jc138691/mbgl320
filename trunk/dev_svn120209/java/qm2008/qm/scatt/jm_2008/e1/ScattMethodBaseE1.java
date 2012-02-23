package scatt.jm_2008.e1;
import atom.energy.ConfHMtrx;
import atom.energy.HMtrx;
import math.vec.Vec;
import scatt.eng.EngGrid;
import scatt.eng.EngGridFactory;
import scatt.eng.EngModel;
import scatt.jm_2008.jm.ScattRes;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,1/04/11,3:43 PM
 */
public abstract class ScattMethodBaseE1 {
protected static final int IDX_ENRGY = 0;
private Vec overD;      // overlap coefficients D
protected Vec sysEngs;
protected HMtrx sysH;
protected final CalcOptE1 calcOpt;
public ScattMethodBaseE1(CalcOptE1 calcOpt) {
  this.calcOpt = calcOpt;
}
public CalcOptE1 getCalcOpt() {
  return calcOpt;
}
public ScattRes calcEngGrid() {
  EngModel eng = calcOpt.getGridEng();    //log.dbg("Incident Energies =", eng);
  EngGrid engs = new EngGrid(eng);
  return calc(engs);
}
public int getSysBasisSize() {
  return sysEngs.size();
}
public Vec getSysEngs() {
  return sysEngs;
}
public int getChNum() { // number of target channels
  return 1; // only one for pot-scattering
}
public ScattRes calcMidSysEngs() {
  Vec scttEngs = EngGridFactory.makeMidPoints(sysEngs);
  return calc(scttEngs);
}
public ScattRes calcWithMidSysEngs() {
  Vec scttEngs = EngGridFactory.makeWithMidPoints(sysEngs);
  return calc(scttEngs);
}
public abstract ScattRes calc(Vec engs);
public void setSysEngs(Vec engs) {
  this.sysEngs = engs;
}
public void setOverD(Vec overD) {
  this.overD = overD;
}
public Vec getOverD() {
  return overD;
}
public HMtrx getSysH() {
  return sysH;
}
public void setSysH(HMtrx sysH) {
  this.sysH = sysH;
}
}
