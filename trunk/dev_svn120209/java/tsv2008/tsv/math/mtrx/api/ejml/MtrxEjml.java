package math.mtrx.api.ejml;
import Jama.Matrix;
import math.mtrx.MtrxToStr;
import math.vec.Vec;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
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
public MtrxEjml addEquals(MtrxEjml B) {
  CommonOps.addEquals(getMatrix(), B.getMatrix());
  return this;
}
public MtrxEjml subEquals(MtrxEjml B) {
  CommonOps.subEquals(getMatrix(), B.getMatrix());
  return this;
}
public MtrxEjml multEquals(double d) {
  CommonOps.scale(d, getMatrix());
  return this;
}
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
public MtrxEjml mult(MtrxEjml B) {
  return new MtrxEjml(super.mult(B));
}
public MtrxEjml transpose () {
  return new MtrxEjml(super.transpose());
}
}
