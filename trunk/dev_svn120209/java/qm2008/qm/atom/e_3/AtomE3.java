package atom.e_3;

import atom.AtomLcr;
import atom.energy.slater.SlaterLcr;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/11/2010,11:14:47 AM
 */
public abstract class AtomE3 extends AtomLcr {
  private final static int THREE_ELEC = 3;

  public AtomE3(double atomZ, SlaterLcr si) {
    super(atomZ, si);
  }
  final public int getNumElec() {
    return THREE_ELEC;
  }
}
