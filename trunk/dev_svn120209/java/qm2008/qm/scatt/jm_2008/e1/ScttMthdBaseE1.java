package scatt.jm_2008.e1;
import atom.energy.HMtrx;
import atom.energy.part_wave.PotHMtrx;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.vec.Vec;
import project.workflow.task.test.FlowTest;
import scatt.eng.EngGrid;
import scatt.eng.EngGridFactory;
import scatt.eng.EngModel;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,1/04/11,3:43 PM
 */
public abstract class ScttMthdBaseE1 extends FlowTest {
protected static final int IDX_ENRGY = 0;
private Vec overD;      // overlap coefficients D
protected Vec sysEngs;
protected PotHMtrx potH;
protected LgrrOrthLcr orthonN;
protected final CalcOptE1 calcOpt;
public ScttMthdBaseE1(CalcOptE1 calcOpt) {
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
public ScattRes calcSysEngs() {
  throw new IllegalArgumentException(log.error("call relevant implementation of calcSysEngs"));
//  return calc(sysEngs);
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
//public ScattRes calcWithMidSysEngs() {
//  Vec scttEngs = EngGridFactory.makeWithMidPoints(sysEngs);
//  return calc(scttEngs);
//}
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
}
