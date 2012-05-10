package atom.e_2;

import atom.data.AtomHe;
import atom.energy.slater.SlaterLcr;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,07/12/2010,11:51:29 AM
 */
public class SysHeOldOk extends SysE2OldOk {
  public SysHeOldOk(SlaterLcr si) {
    super(-AtomHe.Z, si);
  }
}