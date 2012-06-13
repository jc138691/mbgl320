package atom.e_2;

import atom.AtomLcr;
import atom.energy.slater.SlaterLcr;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/11/2010,10:31:35 AM
 */
public abstract class AtomE2 extends AtomLcr {
private final static int TWO_ELEC = 2;

public AtomE2(double atomZ, SlaterLcr si) {
  super(atomZ, si);
}

final public int getNumElec() {
  return TWO_ELEC;
}

}
