package math.mtrx.api.ejml;
import Jama.Matrix;
import math.mtrx.MtrxToStr;
import math.vec.Vec;
import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleBase;
import org.ejml.simple.SimpleMatrix;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 19/06/12, 10:17 AM
 */
//public class MtrxEjml extends DenseMatrix64F {    //SimpleBase()
public class MtrxEjml extends SimpleMatrix {

public MtrxEjml(MtrxEjml from) {
  super(from.getMatrix(), true);
}
public MtrxEjml(SimpleMatrix from) {
  super(from.getMatrix(), true);// shallow copy
}
//public MtrxEjml(DenseMatrix64F from) {
//  super(from, true);
//}
public MtrxEjml(int m, int n) {
  super(m, n);
}
public MtrxEjml(double[][] A) {
  super(A);
}

//public String toString() {
//  return MtrxToStr.toCsv(getArray());
//}
//
//public String toTab(int digs) {
//  return MtrxToStr.toTab(getArray(), digs);
//}
//
//public String toTab() {
//  return MtrxToStr.toTab(getArray());
//}
//public String toGnuplot() {
//  return MtrxToStr.toTab(getArray());
//}
//
public MtrxEjml inverse() {
  return new MtrxEjml(super.invert());
}
public MtrxEjml copy() {
  return new MtrxEjml(super.copy());
}
public MtrxEjml plusEquals(MtrxEjml B) {
  super.plusEquals(B);
  return this;
}
//public MtrxEjml minusEquals(MtrxEjml B) {
//  super.minusEquals(B);
//  return this;
//}
//public MtrxEjml timesEquals(double d) {
//  super.timesEquals(d);
//  return this;
//}
//
//
//public int getNumRows() {
//  return getRowDimension();
//}
//
//public int getNumCols() {
//  return getColumnDimension();
//}
//
//public static void copyEach(double[][] from, double[][] to) {
//  for (int r = 0; r < from.length; r++) {
//    Vec.copyEach(from[r], to[r]);
//  }
//}
//
//public double[] getColCopy(int c) {
//  double[] res = new double[getNumRows()];
//  for (int r = 0; r < res.length; r++) {
//    res[r] = get(r, c);
//  }
//  return res;
//}
//
//public MtrxEjml times(MtrxEjml B) {
//  return new MtrxEjml(super.times(B).getArray());
//}
//public MtrxEjml transpose () {
//  return new MtrxJama(super.transpose().getArray());
//}
//
//public Vec times(Vec vec) {     // v = M * w
//  Vec res = new Vec(getNumRows());
//  double[][] arr = getArray();
//  for (int i = 0; i < getNumRows(); i++) {
//    double d = vec.dot(arr[i]);
//    res.set(i, d);
//  }
//  return res;
//}
}
