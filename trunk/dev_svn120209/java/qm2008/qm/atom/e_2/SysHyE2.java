package atom.e_2;
import atom.data.AtomHe;
import atom.data.AtomHy;
import atom.energy.slater.SlaterLcr;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 14/05/12, 10:42 AM
 */
public class SysHyE2  extends SysE2 {
  public SysHyE2(SlaterLcr si) {
    super(-AtomHy.Z, si);
  }
}