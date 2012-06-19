package math.mtrx.api.jama;
import Jama.Matrix;
import math.mtrx.MtrxToStr;
import math.vec.Vec;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 19/06/12, 10:14 AM
 */
// Adaptor to Jama
public class MtrxJama extends Matrix {

public MtrxJama(int m, int n) {
  super(m, n);
}
public MtrxJama(double[][] A) {
  super(A);
}

public MtrxJama(MtrxJama m) {
  super(m);
}
private MtrxJama(Matrix m) {
  super(m);
}

public String toString() {
  return MtrxToStr.toCsv(getArray());
}

public String toTab(int digs) {
  return MtrxToStr.toTab(getArray(), digs);
}

public String toTab() {
  return MtrxToStr.toTab(getArray());
}
public String toGnuplot() {
  return MtrxToStr.toTab(getArray());
}

public MtrxJama inverse() {
  Matrix res = super.inverse();
  return new MtrxJama(res);
}
public MtrxJama copy() {
  Matrix res = super.copy();
  return new MtrxJama(res);
}
public MtrxJama plusEquals(MtrxJama B) {
  super.plusEquals(B);
  return this;
}
public MtrxJama minusEquals(MtrxJama B) {
  super.minusEquals(B);
  return this;
}
public MtrxJama timesEquals(double d) {
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

public double[] getColCopy(int c) {
  double[] res = new double[getNumRows()];
  for (int r = 0; r < res.length; r++) {
    res[r] = get(r, c);
  }
  return res;
}

public MtrxJama times(MtrxJama B) {
  return new MtrxJama(super.times(B));
}
public MtrxJama transpose () {
  return new MtrxJama(super.transpose());
}

public Vec times(Vec vec) {     // v = M * w
  Vec res = new Vec(getNumRows());
  double[][] arr = getArray();
  for (int i = 0; i < getNumRows(); i++) {
    double d = vec.dot(arr[i]);
    res.set(i, d);
  }
  return res;
}
}
