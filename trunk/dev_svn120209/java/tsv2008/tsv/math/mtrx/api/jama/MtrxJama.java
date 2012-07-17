package math.mtrx.api.jama;
import Jama.Matrix;
import math.mtrx.MtrxToStr;
import math.mtrx.api.Mtrx;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 19/06/12, 10:14 AM
 */
// Adaptor to Jama
public class MtrxJama extends Matrix {
public static Log log = Log.getLog(MtrxJama.class);

public MtrxJama(int m, int n) {
  super(m, n);
}
public MtrxJama(double[][] A) {
  super(A);
}

public MtrxJama(MtrxJama m) {
  super(m);
}
public MtrxJama(Matrix m) {
  super(m);
}

public double[][] getArr2D() {
  log.error("TODO: STOP USING double[][] getArr2D()");//since Ejml uses double[], JAMA uses double[][]
  return super.getArray();
}
public double[][] getArray() {
  throw new IllegalArgumentException(log.error("Use Mtrx.getArr2D"));
}
public double[] getArr1D() {
  log.error("TODO: STOP USING double[] getArr1D()");//since Ejml uses double[], JAMA uses double[][]
  throw new IllegalArgumentException(log.error("Jama does not have getArr1D()"));
}

public MtrxJama inverse() {
  return new MtrxJama(super.inverse());
}
public MtrxJama copy() {
  Matrix res = super.copy();
  return new MtrxJama(res);
}
public MtrxJama addEquals(MtrxJama B) {
  super.plusEquals(B);
  return this;
}
public MtrxJama subEquals(MtrxJama B) {
  super.minusEquals(B);
  return this;
}
public MtrxJama multEquals(double d) {
  super.timesEquals(d);
  return this;
}


public int getNumRows() {
  return getRowDimension();
}

public int getNumCols() {
  return getColumnDimension();
}

public static void copyEach(double[][] from, double[][] to) {
  for (int r = 0; r < from.length; r++) {
    Vec.copyEach(from[r], to[r]);
  }
}


public MtrxJama mult(MtrxJama B) {
  return new MtrxJama(super.times(B));
}
public MtrxJama transpose () {
  return new MtrxJama(super.transpose());
}

}
