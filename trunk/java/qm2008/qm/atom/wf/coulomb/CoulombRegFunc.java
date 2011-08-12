package atom.wf.coulomb;

import math.func.Func;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 14/05/2010, 9:37:36 AM
 */
public class CoulombRegFunc implements Func {
  private final int L;
  private final double Z;
  private final double k;
  public CoulombRegFunc(int L, double Z, double k) {
    this.L = L;
    this.Z = Z;
    this.k = k;
  }
  public double calc(double r) {
    return CoulombReg.calc(L, Z, k, r).getRe();
  }
}
