package atom.e_1;
import atom.data.AtomHy;
import atom.energy.slater.SlaterLcr;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,14/04/11,1:50 PM
 */
public class SysHy extends SysE1 {
  public SysHy(SlaterLcr si) {
    super(-AtomHy.Z, si);
  }
}
