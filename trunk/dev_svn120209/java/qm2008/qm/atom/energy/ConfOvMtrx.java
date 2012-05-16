package atom.energy;

import atom.AtomFano1965;
import javax.utilx.arraysx.TArr;

import atom.shell.ConfArr;
import math.mtrx.Mtrx;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 28/05/2010, 11:09:54 AM
 */
public class ConfOvMtrx extends Mtrx {
protected final ISysH atom;
protected final ConfArr basisL;
protected final ConfArr basisR;
public ConfOvMtrx(ConfArr basisL, final ISysH atom, ConfArr basisR) {
  super(basisL.size(), basisR.size());
  this.atom = atom;
  this.basisL = basisL;
  this.basisR = basisR;
  load();
}
public ConfOvMtrx(ConfArr basis, final ISysH atom) {
  super(basis.size(), basis.size());
  this.atom = atom;
  this.basisL = basis;
  this.basisR = basis;
  load();
}
public TArr getBasisL() {
  return basisL;
}
public TArr getBasisR() {
  return basisR;
}
public ISysH getAtom() {
  return atom;
}
protected void load() {
  for (int r = 0; r < basisL.size(); r++) {
    for (int c = 0; c < basisR.size(); c++) {
      double res = atom.calcOverlap(basisL.get(r), basisR.get(c));
      set(r, c, res);
    }
  }
}
//  protected void load2() {
//    for (int r = 0; r < basisL.size(); r++) {
//      for (int c = 0; c < basisR.size(); c++) {
//        // Atomic system should know how to calculate itself
//        Energy res = atom.calcH(basisL.get(r), basisR.get(c));
//        set(r, c, res.kin + res.pt);
//      }
//    }
//  }
}
