package atom.e_3;

import atom.energy.Energy;
import atom.energy.slater.SlaterLcr;
import atom.fano.Atom2011;
import atom.shell.LsConf;
import atom.shell.DiEx;
import atom.shell.Ls;
import atom.shell.ShInfo;
import math.func.FuncVec;

import javax.utilx.log.Log;
/**
* Created by Dmitry.A.Konovalov@gmail.com, 02/06/2010, 2:57:06 PM
*/
public class SysE3 extends AtomE3 {   //
public static Log log = Log.getLog(SysE3.class);

// the following vars are here for them to be highlighted nicely in the editor
private LsConf conf;
private LsConf conf2;
private FanoTermE3 t; // a single combination of lambda, rho, sigma
private FanoTermE3 t2;
private Ls ls;

public SysE3(double atomZ, SlaterLcr si) {
  super(atomZ, si);
}

public Energy calcLsH(LsConf cf, LsConf cf2) {
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

private Energy calcH() {
  DiEx spinTerm = SpinTermE3.calc(t, t2);
  if (spinTerm.isZero())  {
    return null;
  }
  return calcFanoE2(spinTerm, ls, t, t2);
}

@Override
public double calcLsOver(LsConf fc, LsConf fc2) {
  throw new IllegalArgumentException(log.error(" todo"));
}

@Override
public FuncVec calcLsDens(LsConf cf, LsConf cf2) {    // based on calcH(LsConf cf, LsConf cf2)
  // DO NOT NOT EDIT!!!  This code copied from calcH(LsConf cf, LsConf cf2)
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