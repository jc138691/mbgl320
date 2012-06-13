package atom.e_3;

import atom.data.AtomLi;
import atom.energy.slater.SlaterLcr;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,07/12/2010,11:49:57 AM
 */
public class SysLi extends SysE3 {
  public SysLi(SlaterLcr si) {
    super(AtomLi.Z, si);
  }
}
