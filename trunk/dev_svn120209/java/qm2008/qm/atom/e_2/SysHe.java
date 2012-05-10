package atom.e_2;

import atom.data.AtomHe;
import atom.energy.slater.SlaterLcr;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,07/12/2010,11:45:08 AM
 */
public class SysHe extends SysE2 {
  public SysHe(SlaterLcr si) {
    super(-AtomHe.Z, si);
  }
}
