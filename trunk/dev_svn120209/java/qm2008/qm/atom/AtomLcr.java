package atom;

import atom.energy.slater.SlaterLcr;
import atom.shell.Shell;
import atom.wf.lcr.RkLcr;

import javax.utilx.log.Log;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/11/2010,3:18:10 PM
 */
public abstract class AtomLcr extends AtomFano1965 {
public static Log log = Log.getLog(AtomLcr.class);
protected final SlaterLcr slaterLcr;

public AtomLcr(double z, SlaterLcr si) {
  super(z, si);
  slaterLcr = si;
}
public AtomLcr(AtomLcr from) {
  super(from.Z, from.slaterLcr);
  this.slaterLcr = from.slaterLcr;
}
@Override
public double calcRk(Shell a, Shell b, Shell a2, Shell b2, int kk) {
  double res = RkLcr.calc(slaterLcr.getQaudrLcr()
    , a.getWf(), b.getWf(), a2.getWf(), b2.getWf(), kk);
//  log.dbg("RkLcr.calc=", res);
  return res;
}
public SlaterLcr getSlaterLcr() {
  return slaterLcr;
}
}
