package scatt.jm_2008.e1;
import math.vec.Vec;
import scatt.eng.EngGrid;
import scatt.eng.EngGridFactory;
import scatt.eng.EngModel;
import scatt.jm_2008.jm.JmRes;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,1/04/11,3:43 PM
 */
public abstract class JmMethodBaseE1 {
protected static final int IDX_ENRGY = 0;
private Vec overD;      // overlap coefficients D
protected Vec sysEngs;
protected final JmOptE1 jmOpt;
public JmMethodBaseE1(JmOptE1 jmOpt) {
  this.jmOpt = jmOpt;
}
public JmOptE1 getJmOpt() {
  return jmOpt;
}
public JmRes calcEngGrid() {
  EngModel eng = jmOpt.getGridEng();    //log.dbg("Incident Energies =", eng);
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
public JmRes calcMidSysEngs() {
  Vec scttEngs = EngGridFactory.makeMidPoints(sysEngs);
  return calc(scttEngs);
}
public JmRes calcWithMidSysEngs() {
  Vec scttEngs = EngGridFactory.makeWithMidPoints(sysEngs);
  return calc(scttEngs);
}
public abstract JmRes calc(Vec engs);
public void setSysEngs(Vec engs) {
  this.sysEngs = engs;
}
public void setOverD(Vec overD) {
  this.overD = overD;
}
public Vec getOverD() {
  return overD;
}
}
