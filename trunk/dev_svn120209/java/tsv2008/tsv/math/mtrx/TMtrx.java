package math.mtrx;

import math.vec.DbgView;

import javax.langx.SysProp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 26/02/2010, 11:42:35 AM
 */
public class TMtrx<T> {
  private int numRows;
  private int numCols;
  protected T[][] mtrx;

  public TMtrx(T[][] A) {
    numRows = A.length;
    numCols = A[0].length;
    for (int i = 0; i < numRows; i++) {
      if (A[i].length != numCols) {
        throw new IllegalArgumentException("All rows must have the same length.");
      }
    }
    this.mtrx = A;
  }

  public T[][] getByRows() {
    return mtrx;
  }

  public T[] getRow(int r) {
    return mtrx[r];
  }


  public T[][] getArr() {
    return mtrx;
  }

  public void setArr(T[][] arr) {
    mtrx = arr;
  }

  public T get(int r, int c) {
    return mtrx[r][c];
  }

  public void set(int r, int c, T v) {
    mtrx[r][c] = v;
  }

  public int getNumRows() {
    return numRows;
  }

  public int getNumCols() {
    return numCols;
  }

  public String toDbgString() {
    if (DbgView.getNumShow() >= getNumRows()) {
      return toString(0, getNumRows());
    }
    int n = DbgView.getNumShow() / 2;
    String start = toString(0, n);
    String tail = toString(getNumRows() - n, n);
    String head = "Mtrx[" + getNumRows() + "][...] = {";
    return head + SysProp.EOL + start
      + "..." + SysProp.EOL + tail + "}";
  }

  public String toString() {
    return toString(0, getNumRows());
  }

  // overide this if do not want the default toString()

  public String toString(T[] row) {
    return row.toString();
  }

  public String toString(int firstRowIdx, int size) {
    int L = Math.min(getNumRows(), firstRowIdx + size);
    StringBuffer buff = new StringBuffer();
    for (int i = firstRowIdx; i < L; i++) {
      DbgView.append(buff, toString(getRow(i)));
      buff.append(SysProp.EOL);
    }
    return buff.toString();
  }

}
