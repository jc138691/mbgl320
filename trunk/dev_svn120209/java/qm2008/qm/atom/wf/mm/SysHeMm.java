package atom.wf.mm;
import atom.data.AtomHe;
import atom.e_2.SysHe;
import atom.energy.slater.SlaterLcr;
import atom.shell.Shell;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 10/05/12, 10:51 AM
 */
public class SysHeMm extends SysMmE2 {
public SysHeMm(SlaterLcr si) {
  super(-AtomHe.Z, si);
}
}
