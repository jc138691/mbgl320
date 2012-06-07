package atom.e_1;
import atom.LsFermiSysH;
import atom.energy.Energy;
import atom.energy.slater.Slater;
import atom.shell.LsConf;
import d1.IConf;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 14:02:44
*/
public class SysE1 extends LsFermiSysH {
private final static int ONE_ELEC = 1;
public SysE1(double z
  , Slater si) { // grid/lgrrN dependent!!!
  super(z, si);
}
public int getNumElec() {
  return ONE_ELEC;
}
public Energy calcLsH(LsConf fc, LsConf fc2) {
  assertLS(fc, fc2);
  Energy res = new Energy();
  res.kin = calcOneKin(fc.getSh(0), fc2.getSh(0));
  res.pt = calcOnePotZ(fc.getSh(0), fc2.getSh(0)); // potential only
  return res;
}
public double calcLsOver(LsConf fc, LsConf fc2) {
  assertLS(fc, fc2);
  double res = calcOverlap(fc.getSh(0), fc2.getSh(0));
  return res;
}
}

