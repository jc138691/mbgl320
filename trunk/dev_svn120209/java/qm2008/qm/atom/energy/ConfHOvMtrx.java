package atom.energy;
import atom.shell.Conf;
import atom.shell.ConfArr;
import math.mtrx.Mtrx;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 16/05/12, 1:17 PM
 */
public class ConfHOvMtrx extends HOvMtrx {
public static Log log = Log.getLog(ConfHOvMtrx.class);
private final ISysH atom;
private final ConfArr basis;
private final Mtrx mOv;
public ConfHOvMtrx(ConfArr basis, final ISysH atom) {
  super(basis.size(), basis.size());
  this.atom = atom;
  this.basis = basis;
  mOv = new Mtrx(basis.size(), basis.size());
  loadNormAndDiag();
  loadNonDiag();
  setOv(mOv);
//  super(new ConfHMtrx(basis, atom));
//  setOv(new ConfOvMtrx(basis, atom));
}

private void loadNormAndDiag() {     log.setDbg();
  int len = basis.size();
  for (int i = 0; i < len; i++) {
    Conf ci = basis.get(i);
    double ovDiag = atom.calcOverlap(ci, ci);  log.dbg("\n \n ovDiag[i="+i+"] = " + ovDiag);
//    mOv.set(i, i, ovDiag); // DEBUG
    mOv.set(i, i, 1.);

    double ni = 1. / Math.sqrt(ovDiag);   log.dbg("ni = " + ni);
    basis.get(i).setNorm(ni);
    Energy res = atom.calcH(ci, ci);
    double hii = res.kin + res.pt;      log.dbg("hii = " + hii);
    hii *= (ni * ni);                   log.dbg("ni*ni*hii = " + hii);
    set(i, i, hii);
  }
}
private void loadNonDiag() {
  int len = basis.size();
  for (int i = 0; i < len; i++) {
    Conf ci = basis.get(i);
    double ni = ci.getNorm();
    if ((10 * len)%(i+1) == 0) {
      log.info("ConfHOvMtrx row=" + i + ", " + (int)(100.* i / len) + "%");
    }
    for (int j = i+1; j < basis.size(); j++) { // NOTE c=r+1
//    for (int j = 0; j < basis.size(); j++) { // DEBUG
      if (j == i)
        continue;
      Conf cj = basis.get(j);
      double nj = cj.getNorm();

      double ov = atom.calcOverlap(ci, cj);  log.dbg("\n \n ov[i="+i+", j="+j+"] = " + ov);
      ov *= (ni * nj);                       log.dbg("\n ni * nj * ov = " + ov);
//      ov = 0; // DEBUG
      mOv.set(i, j, ov);
      mOv.set(j, i, ov);

      Energy res = atom.calcH(ci, cj);
      double hij = res.kin + res.pt;      log.dbg("\n \n hij[i="+i+", j="+j+"] = " + hij);
      hij *= (ni * nj);                   log.dbg("\n ni * nj * hij = " + hij);
      set(i, j, hij);
      set(j, i, hij);
    }
  }
}
}