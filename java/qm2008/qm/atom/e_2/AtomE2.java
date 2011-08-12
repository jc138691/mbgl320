package atom.e_2;

import atom.AtomFano1965;
import atom.AtomLcr;
import atom.energy.slater.Slater;
import atom.energy.slater.SlaterLcr;
import atom.shell.Shell;
import atom.wf.log_cr.RkLcr;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/11/2010,10:31:35 AM
 */
public abstract class AtomE2 extends AtomLcr {
  private final static int TWO_ELEC = 2;

  public AtomE2(double z, SlaterLcr si) {
    super(z, si);
  }

  @Override
  final public int getNumElec() {
    return TWO_ELEC;
  }

}
