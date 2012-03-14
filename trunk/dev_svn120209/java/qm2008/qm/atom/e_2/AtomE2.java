package atom.e_2;

import atom.AtomLcr;
import atom.energy.Energy;
import atom.energy.slater.SlaterLcr;
import atom.shell.Conf;
import atom.shell.Ls;
import atom.shell.ShPair;
import atom.shell.Shell;
import math.Mathx;
/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,29/11/2010,10:31:35 AM
 */
public abstract class AtomE2 extends AtomLcr {
  private final static int TWO_ELEC = 2;
private static final double OVER_SQRT_TWO = Math.sqrt(0.5);

  public AtomE2(double z, SlaterLcr si) {
    super(z, si);
  }

  @Override
  final public int getNumElec() {
    return TWO_ELEC;
  }


// wfB(r_1) bound
// wfF(r_2) free
// v = -1/r_2 + 1/r_max
//
public double calcVbf(Shell bndSh  // bound shell
  , Shell freeSh  // free moving wf
  , Conf cnf      // NOTE! bound wf and conf are built from the same radial basis
) {
  Ls ls = cnf.getTotLS();
  ShPair sp = (ShPair) cnf;
  Shell a2 = sp.a;
  Shell b2 = sp.b;

  double dir = calcShPairVbf(ls.getL(), bndSh, freeSh, a2, b2);
  if (a2.getId().equals(b2.getId())) {
    if (ls.getS() != 0) {
      throw new IllegalArgumentException(log.error("NOT A VALID config: ls.getS() != 0): ERROR when building configs"));
    }
    return dir;
  }
  // DIFFERENT a2 and b2
  //
  // when f(r) and g(r)  are different
  // psi(r1, r2) = 1/sqrt(2) [ f(r1) g(r2) + (-1)^S f(r2) g(r1) ]
  //
  double norm = OVER_SQRT_TWO;
  double exc = calcShPairVbf(ls.getL(), bndSh, freeSh, b2, a2);
  double res = norm * ( dir + Mathx.pow(-1., ls.getS()) * exc );
  return res;
}
private double calcShPairVbf(int LL, Shell a, Shell freeSh, Shell a2, Shell b2) { // modelled on calcShPairEng()
  double oneV = calcOneVbf(a, freeSh, a2, b2);
  double pot = calcTwoPot(LL, a, freeSh, a2, b2);    // 1/r_max
  return oneV + pot;
}
// < a(r_1)  b(r_2)  |  Z/r_2 |  a2(r_1)  b2(r_2)  >
private double calcOneVbf(Shell a                    // modelled on calcOneH()
  , Shell freeSh // this is the free moving wf!!!
  , Shell a2, Shell b2) {
  double res = 0;
  if (a.getWfL() != a2.getWfL() || freeSh.getWfL() != b2.getWfL())
    return res;
  if (a.getId().equals(a2.getId()))  // <ab||ab'>    // NOTE: THIS IS WHERE the same basis is used
    res = si.calcOneZPot(Z, freeSh, b2);
  return res;
}
}
