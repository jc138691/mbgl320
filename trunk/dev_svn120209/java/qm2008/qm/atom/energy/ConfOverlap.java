package atom.energy;

import atom.AtomFano1965;
import atom.shell.ConfArr;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 31/05/2010, 1:38:37 PM
 */
public class ConfOverlap extends ConfHConf {
  public ConfOverlap(ConfArr basisL, final AtomFano1965 atom, ConfArr basisR) {
    super(basisL, atom, basisR);
  }
  protected void load() {
    for (int r = 0; r < basisL.size(); r++) {
      for (int c = 0; c < basisR.size(); c++) {
        // Atomic system should know how to calculate itself
        double res = atom.calcOverlap(basisL.get(r), basisR.get(c));
        set(r, c, res);
      }
    }
  }
}