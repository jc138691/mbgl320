package atom.energy.slater;
import atom.shell.Shell;
import atom.wf.log_r.TransLrToR;
import atom.wf.log_r.WFQuadrLr;
import math.vec.Vec;
import math.func.FuncVec;

import javax.utilx.log.Log;
/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 16:38:40
*/
// NOTE!!! In the following transformation all radial extremes have been removed, eg z/r or L(L+1)/r2
//
// After sqrt(r)*Rn(r) = Fn(r)
// and r = exp(x);   dr = r dx
//                        2              2
//                     1 d        (L+1/2)
// r^2 * Hamiltonian = - - --   + -------- + r*atomZ = r^2 * H(X)
//                     2 dx^2        2
//  oo  2
//  |  r dx Fn'(exp(x)) H Fn(exp(x)) = delta_n'n
// -oo
// w are for 'x' integral
//
public class SlaterLr extends Slater {
public static Log log = Log.getLog(SlaterLr.class);
final private TransLrToR xToR; // r = exp(x)
final private WFQuadrLr wLogR;
public SlaterLr(WFQuadrLr w) {
  super(w);
  this.xToR = w.getLrToR();
  wLogR = w;
}
/**
 * THIS FUNCTION IS BASIS DEPENDANT !!!!!!!!!!!
 * //                 1 d^2    (L+1/2)^2
 * // Hamiltonian = - - --   + -------- + r*atomZ = H(X)
 * //                 2 dx^2      2
 * //  oo
 * //  |  dx Fn'(exp(x)) H Fn(exp(x)) = delta_n'n
 * // -oo
 * // 1/r**2 cancels r**2
 */
public double calcOneKin(Shell sh, Shell SH2 /* make it stand out*/) {
  return calcKinDrv(sh, SH2) + calcKinL(sh, SH2);
}
public double calcKin(int L, FuncVec wf, FuncVec wf2) {
  return calcKinDrv(wf, wf2) + calcKinL(L, wf, wf2);
}
private double calcKinL(Shell sh, Shell SH2 /* make it stand out*/) {
  int L = sh.getWfL();
  if (L != SH2.getWfL())
    return 0.;
  return calcKinL(L, sh.getWf(),  SH2.getWf());
}

private double calcKinL(int L, FuncVec wf, FuncVec wf2) {
  double L2 = 0.5 * (0.5 + L) * (0.5 + L);
  double int_L2 = calc(wf, wf2);
  double res = L2 * int_L2;
  return res;
}

// NOTE!!! This is the same for SlaterLcr
private double calcKinDrv(Shell sh, Shell SH2 /* make it stand out*/) {
  int L = sh.getWfL();
  if (L != SH2.getWfL())
    return 0.;
  return calcKinDrv(sh.getWf(), SH2.getWf());
}
private double calcKinDrv(FuncVec wf, FuncVec wf2) {
  Vec drv = wf.getDrv();
  Vec drv2 = wf2.getDrv();
  double int_d = calc(drv, drv2);
  // note 0.5 instead of (-0.5), that is because U' * U' replaced U*U"
  // x            x               |x
  // I dr RF" = - I dr R'F' + RF' |  = - drR'F' + RF'(x) - RF'(y)
  // y            y               |y
  double firstCorrection = wf.getFirst() * drv2.getFirst();
  double lastCorr = wf.getLast() * drv2.getLast();
  double res = 0.5 * (int_d + firstCorrection - lastCorr);
  return res;
}

public double calcOneZPot(double z, Shell sh, Shell sh2) {
  if (sh.getWfL() != sh2.getWfL())
    return 0.;
  if (xToR == null) {
    throw new IllegalArgumentException(log.error("xToR == null"));
  }
  //  oo
  //  |  r**2 dx Fn'(r) atomZ * 1/r Fn(r) =
  // -oo
  double int_z = calc(sh.getWf(), sh2.getWf(), xToR);
  double res = z * int_z;
  return res;
}
public double calcOverlap(Shell sh, Shell sh2) {
  if (sh.getWfL() != sh2.getWfL())
    return 0.;
  if (xToR == null) {
    throw new IllegalArgumentException(log.error("xToR == null"));
  }
  double res = calc(sh.getWf(), sh2.getWf(), xToR.getR2());
  return res;
}
}
