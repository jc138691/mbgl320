package math.mtrx.jamax;
import Jama.EigenvalueDecomposition;
import math.mtrx.Mtrx;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 12:40:23
 */
public class EigenSymm extends EigenvalueDecomposition {
  public EigenSymm(Mtrx mtrx) {
    super(mtrx, true);
  }
  public EigenSymm(Mtrx mtrx, boolean overwrite) {
    super(mtrx, true, overwrite);
  }
  public double[] getRealEVals () {
    return getRealEigenvalues();
  }
  public Mtrx getV () {
    return new Mtrx(super.getV().getArray());
  }
}
