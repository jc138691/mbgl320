package atom.energy;

import atom.AtomFano1965;
import javax.utilx.arraysx.TArr;

import atom.shell.ConfArr;
import math.mtrx.Mtrx;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 28/05/2010, 11:09:54 AM
 */
public class ConfHConf extends Mtrx {
  protected final AtomFano1965 atom;
  protected final ConfArr basisL;
  protected final ConfArr basisR;
  public ConfHConf(ConfArr basisL, final AtomFano1965 atom, ConfArr basisR) {
    super(basisL.size(), basisR.size());
    this.atom = atom;
    this.basisL = basisL;
    this.basisR = basisR;
    load();
  }
  public TArr getBasisL() {
    return basisL;
  }
  public TArr getBasisR() {
    return basisR;
  }
  public AtomFano1965 getAtom() {
    return atom;
  }
  protected void load() {
    for (int r = 0; r < basisL.size(); r++) {
      for (int c = 0; c < basisR.size(); c++) {
        // Atomic system should know how to calculate itself
        Energy res = atom.calcH(basisL.get(r), basisR.get(c));
        set(r, c, res.kin + res.pot);
      }
    }
  }
}
