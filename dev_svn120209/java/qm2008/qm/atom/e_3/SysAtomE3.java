package atom.e_3;

import atom.energy.Energy;
import atom.energy.slater.SlaterLcr;
import atom.fano.Atom2011;
import atom.shell.Conf;
import atom.shell.DiEx;
import atom.shell.Ls;
import atom.shell.ShInfo;
import math.func.FuncVec;

import javax.utilx.log.Log;

import static math.Mathx.dlt;


/**
 * Created by Dmitry.A.Konovalov@gmail.com, 02/06/2010, 2:57:06 PM
 */
public class SysAtomE3 extends AtomE3 {   //
  public static Log log = Log.getLog(SysAtomE3.class);

  // the following vars are here for them to be highlighted nicely in the editor
  private Conf conf;
  private Conf conf2;
  private FanoTermE3 t; // a single combination of lambda, rho, sigma
  private FanoTermE3 t2;
  private Ls ls;

  public SysAtomE3(double z, SlaterLcr si) {
    super(z, si);
  }

  @Override
  public Energy calcH(Conf cf, Conf cf2) {
    ls = cf.getTotLS();
    this.conf = cf;
    this.conf2 = cf2;
    // ONE selection of rho,sigma,bar (including their primes) at a time
    assertLS(conf, conf2);

    // with 3 electrons it is easier/easy just to scan with the single spectator electron
    Energy sum = new Energy();
    for (int ib = 0; ib < conf.size(); ib++) { // possible spectator electron; ib- from \bar{a}
      ShInfo b = new ShInfo(ib, conf.getSh(ib));
      for (int ir = 0; ir < conf.size(); ir++) { // rho    (14)
        ShInfo r = new ShInfo(ir, conf.getSh(ir));
        if (!Atom2011.isPossible(b, r))
          continue; // not possible
        for (int is = ir; is < conf.size(); is++) { // sigma >= rho (14)
          ShInfo s = new ShInfo(is, conf.getSh(is));
          if (!Atom2011.isPossible(b, r, s))
            continue; // not possible
          t = new FanoTermE3(b, r, s, conf);

          for (int ib2 = 0; ib2 < conf2.size(); ib2++) {
            ShInfo b2 = new ShInfo(ib2, conf2.getSh(ib2));
            if (!b.id.equals(b2.id))
              continue;  // spectator electrons must be the same
            for (int ir2 = 0; ir2 < conf2.size(); ir2++) { // rho    (14')
              ShInfo r2 = new ShInfo(ir2, conf2.getSh(ir2));
              if (!Atom2011.isPossible(b2, r2))
                continue; // not possible
              for (int is2 = ir2; is2 < conf2.size(); is2++) { // sigma >= rho (14')
                ShInfo s2 = new ShInfo(is2, conf2.getSh(is2));
                if (!Atom2011.isPossible(b2, r2, s2))
                  continue; // not possible
                t2 = new FanoTermE3(b2, r2, s2, conf2);

                // all of the above is just to go over all possible terms
                Energy res = calcH();
                if (res != null)
                  sum.add(res);
              }
            }
          }
        }
      }
    }
    return sum;
  }

// moved to calcFanoE2
//  private Energy calcH() {
//    DiEx spinTerm = SpinTermE3.calc(t, t2);
//    if (spinTerm.isZero())  {
//      return new Energy(0, 0);
//    }
//    ShInfo r = t.r;
//    ShInfo s = t.s;
//    ShInfo r2 = t2.r;
//    ShInfo s2 = t2.s;
//    Energy dir = calcShPairEng(ls.getL(), r.sh, s.sh, r2.sh, s2.sh);
//    Energy exc = new Energy();
//    if (t.hasExc(t2)) {
//      exc = calcShPairEng(ls.getL(), r.sh, s.sh, s2.sh, r2.sh);
//      log.dbg("exc=", exc);
//    }
//
//    log.dbg("spinTerm=", spinTerm);
//    dir.times(spinTerm.di);
//    exc.times(spinTerm.ex);
//    double norm2 = 1. + dlt(r.idx, s.idx) * dlt(r2.idx, s2.idx);
//    log.dbg("norm2=", norm2);
//    double norm = t.norm * t2.norm;
//    log.dbg("norm=", norm);
//    int sign = t.sign * t2.sign;
//    log.dbg("sign=", sign);
//
//    int fano = t.signFano * t2.signFano;
//    log.dbg("fano=", fano);
//    if (sign != fano) {
////      FanoTermE3 tmp = new FanoTermE3(b, r, s, conf);
////      FanoTermE3 tmp2 = new FanoTermE3(b2, r2, s2, conf2);
//
//      log.dbg("t=", t);
//      log.dbg("t2=", t2);
//      log.dbg("sign=", sign);
//      log.dbg("fano=", fano);
//      throw new IllegalArgumentException(log.error("Fano's sign is different"));
//    }
//    sign = fano;
//
//    double normTot = norm * sign / norm2;
//    log.dbg("normTot=", normTot);
//
//    Energy res = new Energy(dir);
//    exc.times(-1); // since it is (dir - exc)
//    res.add(exc);
//    res.times(normTot);
//    log.dbg("res=", res);
////    if (res.kin + res.pot < -7.4) {
////      int dbg = 1;
////    }
//    return res;
//  }

  private Energy calcH() {
    DiEx spinTerm = SpinTermE3.calc(t, t2);
    if (spinTerm.isZero())  {
      return null;
    }
    return calcFanoE2(spinTerm, ls, t, t2);
  }

  @Override
  public double calcOverlap(Conf fc, Conf fc2) {
    throw new IllegalArgumentException(log.error(" todo"));
  }

  @Override
  public FuncVec calcDensity(Conf cf, Conf cf2) {    // based on calcH(Conf cf, Conf cf2)
    // DO NOT NOT EDIT!!!  This code copied from calcH(Conf cf, Conf cf2)
    ls = cf.getTotLS();
    this.conf = cf;
    this.conf2 = cf2;
    // ONE selection of rho,sigma,bar (including their primes) at a time
    assertLS(conf, conf2);

    // with 3 electrons it is easier/easy just to scan with the single spectator electron
    FuncVec sum = null;
    for (int ib = 0; ib < conf.size(); ib++) { // possible spectator electron; ib- from \bar{a}
      ShInfo b = new ShInfo(ib, conf.getSh(ib));
      for (int ir = 0; ir < conf.size(); ir++) { // rho    (14)
        ShInfo r = new ShInfo(ir, conf.getSh(ir));
        if (!Atom2011.isPossible(b, r))
          continue; // not possible
        for (int is = ir; is < conf.size(); is++) { // sigma >= rho (14)
          ShInfo s = new ShInfo(is, conf.getSh(is));
          if (!Atom2011.isPossible(b, r, s))
            continue; // not possible
          t = new FanoTermE3(b, r, s, conf);

          for (int ib2 = 0; ib2 < conf2.size(); ib2++) {
            ShInfo b2 = new ShInfo(ib2, conf2.getSh(ib2));
            if (!b.id.equals(b2.id))
              continue;  // spectator electrons must be the same
            for (int ir2 = 0; ir2 < conf2.size(); ir2++) { // rho    (14')
              ShInfo r2 = new ShInfo(ir2, conf2.getSh(ir2));
              if (!Atom2011.isPossible(b2, r2))
                continue; // not possible
              for (int is2 = ir2; is2 < conf2.size(); is2++) { // sigma >= rho (14')
                ShInfo s2 = new ShInfo(is2, conf2.getSh(is2));
                if (!Atom2011.isPossible(b2, r2, s2))
                  continue; // not possible
                t2 = new FanoTermE3(b2, r2, s2, conf2);

                // all of the above is just to go over all possible terms
                FuncVec res = calcDensity();
                if (res != null) {
                  if (sum == null)
                    sum = res;
                  else
                    sum.addSafe(res);
                }
              }
            }
          }
        }
      }
    }
    return sum;
 }
  private FuncVec calcDensity() {
    DiEx spinTerm = SpinTermE3.calc(t, t2);
    if (spinTerm.isZero())  {
      return null;
    }
    return calcDensity(spinTerm, ls, t, t2);
  }

}