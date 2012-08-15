package atom.energy.slater;
import atom.energy.pw.PotHR;
import atom.shell.Shell;
import atom.wf.WFQuadrR;
import math.vec.Vec;
import math.func.FuncVec;
import math.func.simple.FuncPowInt;
import math.integral.Quadr;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 16:22:19
 */
public class SlaterR extends Slater {
  public static Log log = Log.getLog(SlaterR.class);
  private Vec divR2;
  private Vec divR;
  private PotHR partH;
  public SlaterR(Quadr w) {
    super(w);
    partH = new PotHR((WFQuadrR)w);
  }
//   * THIS FUNCTION IS BASIS DEPENDANT !!!!!!!!!!!
//   * // After r*Rn(r) = Un(r)
//   * //                 1 d^2    L(L+1)   atomZ
//   * // Hamiltonian = - - --   + ------ + - = H(U)
//   * //                 2 dr^2    2r^2    r
//   * // oo
//   * // |  dr Un'(r) H Un(r) = delta_n'n
//   * // o
  //
  public double calcOneKin(Shell sh, Shell SH2) {
    int L = sh.getWfL();
    if (L != SH2.getWfL()) {
      return 0.;
    }
    return calcKin(L, sh.getWf(), SH2.getWf());
  }
  public double calcKin(int L, FuncVec wf, FuncVec wf2) {
    return partH.calcKin(L, wf, wf2);
//    //                 1 d^2    L(L+1)
//    // Hamiltonian = - - --   + ------
//    //                 2 dr^2    2r^2
//    double L2 = 0.5 * (L * (L + 1));
//    double int_L2 = calc(wf, wf2, getDivR2());
//    double int_d = calc(wf.getDrv(), wf2.getDrv());
//    // note 0.5 instead of (-0.5), that is because U' * U' replaced U*U"
//    // x            x               |x
//    // I dr RR" = - I dr R'R' + RR' |  = - drR'R' + RR'(x) - RR'(y)
//    // y            y               |y
//    double firstCorrection = wf.getFirst() * wf2.getDrv().getFirst();
//    double lastCorr = wf.getLast() * wf2.getDrv().getLast();
//    double res = 0.5 * (int_d + firstCorrection - lastCorr) + L2 * int_L2;
//    return res;
  }
//  public double calcKin_L0(FuncVec wf, FuncVec wf2) {
//    //                 1 d^2
//    // Hamiltonian = - - --
//    //                 2 dr^2
//    double int_d = calc(wf.getDrv(), wf2.getDrv());
//    // note 0.5 instead of (-0.5), that is because U' * U' replaced U*U"
//    // x            x               |x
//    // I dr RR" = - I dr R'R' + RR' |  = - drR'R' + RR'(x) - RR'(y)
//    // y            y               |y
//    double firstCorr = wf.getFirst() * wf2.getDrv().getFirst();
//    double lastCorr = wf.getLast() * wf2.getDrv().getLast();
//    double res = 0.5 * (int_d + firstCorr - lastCorr);
//    return res;
//  }
  public double calcOneZPot(double z, Shell sh, Shell sh2) {
    if (sh.getWfL() != sh2.getWfL())
      return 0.;
    return calcZPot(z, sh.getWf(), sh2.getWf());
  }
  public double calcInt(Vec wf, Vec wf2) {
    //  oo                            oo
    //  | r^2 dr Rn(r) Rn'(r) Fn(r) = I dr Rn(r) Rn'(r) r^2;
    //  o                             o
    double res = calc(wf, wf2);
    return res;
  }
  public double calcZPot(double z, Vec wf, Vec wf2) {
    //  oo                                    oo
    //  | r^2 dr Rn(r) Rn'(r) atomZ * 1/r Fn(r) = I dr Rn(r) Rn'(r) atomZ * r;
    //  o                                     o
    double int_z = calc(wf, wf2, getDivR());
    double res = z * int_z;
    return res;
  }

  public Vec getDivR2() {
    if (divR2 == null) {
      divR2 = new FuncVec(getX(), new FuncPowInt(1., -2));
    }
    return divR2;
  }
  public Vec getDivR() {
    if (divR == null) {
      divR = new FuncVec(getX(), new FuncPowInt(1., -1));
    }
    return divR;
  }

}
