package atom.wf.mm;
import atom.data.AtomHe;
import atom.data.AtomHy;
import atom.energy.slater.SlaterLcr;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 14/05/12, 10:41 AM
 */
public class SysHyMmE2 extends SysMmE2 {
public SysHyMmE2(SlaterLcr si) {
  super(AtomHy.Z, si);
}
}