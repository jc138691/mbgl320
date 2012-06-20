package math.mtrx.api.jama;
import Jama.EigenvalueDecomposition;
import math.mtrx.api.Mtrx;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 18/06/12, 9:10 AM
 */
public class EigenJama extends EigenvalueDecomposition {
public EigenJama(MtrxJama mtrx) {
  super(mtrx, true);
}
public EigenJama(MtrxJama mtrx, boolean overwrite) {
  super(mtrx, true, overwrite);
}
public double[] getRealEVals () {
  return getRealEigenvalues();
}
public MtrxJama getV () {
  return new MtrxJama(super.getV());
}
public MtrxJama getD () {
  return new MtrxJama(super.getD());
}
}