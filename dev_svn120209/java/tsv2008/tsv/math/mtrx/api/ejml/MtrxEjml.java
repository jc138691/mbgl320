package math.mtrx.api.ejml;
import Jama.Matrix;
import math.mtrx.MtrxToStr;
import math.vec.Vec;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.simple.SimpleBase;
import org.ejml.simple.SimpleMatrix;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 19/06/12, 10:17 AM
 */
//public class MtrxEjml extends DenseMatrix64F {    //SimpleBase()
public class MtrxEjml extends SimpleMatrix {
public static Log log = Log.getLog(MtrxEjml.class);

public MtrxEjml(MtrxEjml from) {
  super(from.getMatrix(), true);
}
public MtrxEjml(SimpleMatrix from) {
  super(from.getMatrix(), true);// shallow copy
}
public MtrxEjml(int m, int n) {
  super(m, n);
}
public MtrxEjml(double[][] A) {
  super(A);
  log.error("TODO: STOP USING raw double[][], since Ejml uses double[], JAMA uses double[][]");
}

public double[] getArr1D() {
  log.error("TODO: STOP USING double[] getArr1D()");//since Ejml uses double[], JAMA uses double[][]
  return super.getMatrix().getData();
}

public String toString() {
  return MtrxToStr.toCsv(getArr2D());
}
public String toTab(int digs) {
  return MtrxToStr.toTab(getArr2D(), digs);
}
public String toTab() {
  return MtrxToStr.toTab(getArr2D());
}
public String toGnuplot() {
  return MtrxToStr.toTab(getArr2D());
}


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


public int getNumRows() {
  return super.getMatrix().getNumRows();
}

public int getNumCols() {
  return super.getMatrix().getNumCols();
}
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
public double[][] getArr2D() {  log.setDbg();
  log.error("TODO: STOP USING double[][] getArr2D()");//since Ejml uses double[], JAMA uses double[][]
  int nr = getNumRows();
  int nc = getNumCols();
  double[][] res = new double[nr][nc];
  for (int r = 0; r < nr; r++) {
    for (int c = 0; c < nc; c++) {
      res[r][c] = get(r, c);
    }
  }
  return res;
}
}
