package atom.wf.coulomb;

import math.func.Func;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 14/05/2010, 11:15:37 AM
 */
public class CoulombRegAsymptFunc implements Func {
  private final int L;
  private final double Z;
  private final double k;

  // atomZ=1 for e-H
  public CoulombRegAsymptFunc(int L, double Z, double k) {
    this.L = L;
    this.Z = Z;
    this.k = k;
  }
  public double calc(double r) {
    return CoulombRegAsympt.calc(L, Z, k, r);
  }
}
