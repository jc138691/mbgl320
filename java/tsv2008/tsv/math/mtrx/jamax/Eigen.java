package math.mtrx.jamax;
import Jama.EigenvalueDecomposition;
import math.mtrx.Mtrx;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 12:40:23
 */
public class Eigen extends EigenvalueDecomposition {
  public Eigen(Mtrx mtrx, boolean isSymm) {
    super(mtrx, isSymm);
  }
  public double[] getRealEVals () {
    return getRealEigenvalues();
  }
  public Mtrx getV () {
    return new Mtrx(super.getV().getArray());
  }
}
