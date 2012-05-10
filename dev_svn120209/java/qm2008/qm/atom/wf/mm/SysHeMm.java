package atom.wf.mm;
import atom.e_2.SysHe;
import atom.energy.slater.SlaterLcr;
import atom.shell.Shell;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 10/05/12, 10:51 AM
 */
public class SysHeMm extends SysHe {
private AtomMm atomMm;
public SysHeMm(SlaterLcr si) {
  super(si);
  atomMm = new AtomMm(si);
}
@Override
public double calcRk(Shell a, Shell b, Shell a2, Shell b2, int kk) {
  return atomMm.calcRk(a, b, a2, b2, kk);
}
}
