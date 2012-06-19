package math.mtrx.api.jama;
import Jama.EigenvalueDecomposition;
import math.mtrx.api.Mtrx;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 18/06/12, 9:10 AM
 */
public class EigenSymmJama extends EigenvalueDecomposition {
public EigenSymmJama(Mtrx mtrx) {
  super(mtrx, true);
}
public EigenSymmJama(Mtrx mtrx, boolean overwrite) {
  super(mtrx, true, overwrite);
}
public double[] getRealEVals () {
  return getRealEigenvalues();
}
public Mtrx getV () {
  return new Mtrx(super.getV().getArray());
}
public Mtrx getD () {
  return new Mtrx(super.getD().getArray());
}
}