package atom;

import atom.energy.slater.SlaterLcr;
import atom.shell.Shell;
import atom.wf.lcr.rk.RkLcr;
import atom.wf.lcr.rk.RkLcrMap;
import atom.wf.lcr.yk.YkLcrMap;
import math.func.FuncVec;

import javax.utilx.log.Log;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/11/2010,3:18:10 PM
 */
public abstract class AtomLcr extends LsFermiSysH {
public static Log log = Log.getLog(AtomLcr.class);
protected final SlaterLcr slaterLcr;
private RkLcrMap mapRk;
private YkLcrMap mapYk;

public AtomLcr(double atomZ, SlaterLcr si) {
  super(atomZ, si);
  slaterLcr = si;
  init();
}
public AtomLcr(AtomLcr from) {
  super(from.atomZ, from.slaterLcr);
  this.slaterLcr = from.slaterLcr;
}
public void init() {
  mapRk = new RkLcrMap(slaterLcr.getQaudrLcr());
  mapYk = new YkLcrMap(slaterLcr.getQaudrLcr());
}

@Override
public double calcRk(Shell a, Shell b, Shell a2, Shell b2, int kk) {
  double res;
  if (isMapRk()) {
    res = mapRk.calc(a.getWf(), b.getWf(), a2.getWf(), b2.getWf(), kk);
  }
  else if (isMapYk()) {
    FuncVec yk = mapYk.calcYk(b.getWf(), b2.getWf(), kk);
    res = RkLcr.calc(slaterLcr.getQaudrLcr(), a.getWf(), a2.getWf(), yk, kk);
  }
  else {
    res = RkLcr.calc(slaterLcr.getQaudrLcr()
    , a.getWf(), b.getWf(), a2.getWf(), b2.getWf(), kk);
  }
//  log.dbg("RkLcr.calc=", res);
  return res;
}
public SlaterLcr getSlaterLcr() {
  return slaterLcr;
}
}
