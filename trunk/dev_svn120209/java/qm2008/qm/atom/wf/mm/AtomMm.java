package atom.wf.mm;
import atom.energy.slater.SlaterLcr;
import atom.shell.Shell;
import atom.wf.lcr.WFQuadrLcr;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 10/05/12, 10:12 AM
 */
public class AtomMm {
public static Log log = Log.getLog(AtomMm.class);
private WFQuadrLcr quadr;
public AtomMm(SlaterLcr si) {
  quadr = si.getQaudrLcr();
}
public double calcRk(Shell a, Shell b, Shell a2, Shell b2, int kk) {
  double res = RkMm.calc(quadr
    , a.getWf(), b.getWf(), a2.getWf(), b2.getWf(), kk);
//  log.dbg("RkMm.calc=", res);
  return res;
}
}
