package math.mtrx;

import Jama.Matrix;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.IntArrayData;
import math.vec.IntVec;
import math.vec.Vec;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 16:33:44
 */
public class Mtrx extends Matrix {
//  public class Mtrx extends TMtrx<double> {

  public Mtrx(int m, int n) {
    super(m, n);
  }

  public Mtrx(int m, int n, double s) {
    super(m, n, s);
  }

  public Mtrx(double[][] A) {
    super(A);
  }

  public Mtrx(double[][] A, int m, int n) {
    super(A, m, n);
  }

  public Mtrx(double[] vals, int m) {
    super(vals, m);
  }

  public String toString() {
    return MtrxToString.toCsv(getArray());
  }

  public String toTab(int digs) {
    return MtrxToString.toTab(getArray(), digs);
  }

  public String toTab() {
    return MtrxToString.toTab(getArray());
  }
  public String toGnuplot() {
    return MtrxToString.toTab(getArray());
  }

  public Mtrx inverse() {
    Matrix res = super.inverse();
    return new Mtrx(res.getArray());
  }

  /*
http://java.sun.com/javase/technologies/desktop/java3d/forDevelopers/j3dapi/javax/vecmath/GMatrix.html#GMatrix(int,%20int,%20double[])
public GMatrix(int nRow,
               int nCol,
               double[] matrix)
    Constructs an nRow by nCol matrix initialized to the values in the matrix array. The array values are copied in one row at a time in row major fashion. The array should be at least nRow*nCol in length. Note that even though row and column numbering begins with zero, nRow and nCol will be one larger than the maximum possible matrix index values.
    Parameters:
        nRow - number of rows in this matrix.
        nCol - number of columns in this matrix.
        matrix - a 1D array that specifies a matrix in row major fashion  
   */

  public Mtrx(int nrow, int ncol, double[] vals) {
    super(nrow, ncol);
    Matrix m = new Matrix(vals, ncol);
    m = m.transpose();
    copyEach(m.getArray(), this.getArray());
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
//  public double[] getRowCopy(int r) {
//    double[] res = new double[getNumCols()];
//    for (int c = 0; c < res.length; c++) {
//      res[c] = get(r, c);
//    }
//    return res;
//  }

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
