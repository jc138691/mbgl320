package atom.e_2;

import atom.AtomLcr;
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
// babb - bound-any-bound-bound
// NOTE!!!!! bX must be
//   < bX | bX2 > = delta_{bX.id, bX2.id}
//   < bX | bY2 > = delta_{bX.id, bY2.id}
public double calcVbabb(
  Shell bX  // bound shell
  , Shell aY  // any (free-moving-wf or bound)
  , Conf bbXY2  // bound-bound-(shell-pair)    // NOTE! bound wf and conf are built from the same radial basis
) {
  Ls ls = bbXY2.getTotLS();
  ShPair sp = (ShPair) bbXY2;
  Shell bX2 = sp.a;
  Shell bY2 = sp.b;
  double dir = calcShPairVbaba(ls.getL(), bX, aY, bX2, bY2);
  if (bX2.sameId(bY2)) {
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

  // NOTE: < bX | bY2 > = delta_{bX.id, bY2.id}  is used here
  double exc = calcShPairVbaba(ls.getL(), bX, aY, bY2, bX2);
  double res = norm * ( dir + Mathx.pow(-1., ls.getS()) * exc );
  return res;
}

private double calcShPairVbaba(int LL
  , Shell bX, Shell aY
  , Shell bX2, Shell aY2) { // modelled on calcShPairEng()
  double oneV = calcOneVbaba(bX, aY, bX2, aY2);
  double pot = calcTwoPot(LL, bX, aY, bX2, aY2);    // 1/r_max
  return oneV + pot;
}

// < bound(x)  free(y)  |  Z/y |  bound(x)  bound(y)  >
// NOTE: 1/y   - second variable
// baba --- bound-any-bound-any
// "bound" means that < bX | bX2 > = delta_{bX.id, bX2.id}
private double calcOneVbaba(   // modelled on calcOneH()
                               Shell bX  // bound shell
  , Shell aY // any (bound or free)
  , Shell bX2 // bound shell X2
  , Shell aY2 // any (bound or free) shell Y2
) {
  double res = 0;
  if (bX.getWfL() != bX2.getWfL() || aY.getWfL() != aY2.getWfL())
    return res;
  if (bX.sameId(bX2))  // <ab||ab'>    // NOTE: THIS IS WHERE the same basis is used
    res = si.calcOneZPot(Z, aY, aY2);
  return res;
}
}
