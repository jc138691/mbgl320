package atom.e_2;

import atom.energy.Energy;
import atom.energy.slater.SlaterLcr;
import atom.fano.Atom2011;
import atom.fano.FanoTermE2;
import atom.shell.LsConf;
import atom.shell.DiEx;
import atom.shell.Ls;
import atom.shell.ShInfo;
import d1.IConf;
import math.func.FuncVec;
/**
* dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/11/2010,10:25:12 AM

THIS uses equations from the 2011 e-He paper
*/
public class SysE2 extends AtomE2 {
private LsConf conf;
private LsConf conf2;
private FanoTermE2 t; // a single combination of lambda, rho, sigma
private FanoTermE2 t2;
private Ls ls;

public SysE2(double z, SlaterLcr si) {
  super(z, si);
}

public Energy calcLsH(LsConf cf, LsConf cf2) {
  this.conf = cf;
  this.conf2 = cf2;
  ls = conf.getTotLS();
  // ONE selection of rho,sigma,bar (including their primes) at a time
  assertLS(conf, conf2);
  t = loadTerm(conf);
  t2 = loadTerm(conf2);
  Energy res = calcH();
  log.dbg("res=", res);
  return res;
}

public FuncVec calcDens(IConf cf, IConf cf2) {
  this.conf = (LsConf)cf;
  this.conf2 = (LsConf)cf2;
  ls = conf.getTotLS();
  // ONE selection of rho,sigma,bar (including their primes) at a time
  assertLS(conf, conf2);
  t = loadTerm(conf);
  t2 = loadTerm(conf2);
  FuncVec res = calcDensity();
  log.dbg("res=", res);
  return res;
}

private FanoTermE2 loadTerm(LsConf from) {
  FanoTermE2 res = null;
  for (int ir = 0; ir < from.size(); ir++) { // rho    (14)
    ShInfo r = new ShInfo(ir, from.getSh(ir));
    for (int is = ir; is < from.size(); is++) { // sigma >= rho (14)
      ShInfo s = new ShInfo(is, from.getSh(is));
      if (!Atom2011.isPossible(r, s))
        continue; // not possible
      if (res != null) {
        throw new IllegalArgumentException(log.error("t != null; only one (rho,sigma) for E2"));
      }
      res = new FanoTermE2(r, s, from);
    }
  }
  return res;
}

private Energy calcH() {
  DiEx spinTerm = SpinTermE2.calc(t, t2);                                 log.dbg("spinTerm=", spinTerm);
  if (spinTerm.isZero())  {
    return null;
  }
  return calcFanoE2(spinTerm, ls, t, t2);
}

// Based on  calcH, see also   SysE2OldOk.calcConfigDensity
private FuncVec calcDensity() {
  DiEx spinTerm = SpinTermE2.calc(t, t2);                           log.dbg("spinTerm=", spinTerm);
  if (spinTerm.isZero())  {
    return null;
  }
  return calcDensity(spinTerm, ls, t, t2);
}

public double calcOver(IConf fc, IConf fc2) {
  throw new IllegalArgumentException(log.error("use calcInt(LsConf sa, LsConf sa2)"));
}
}
