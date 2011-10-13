package lapack4j;
import lapack4j.utils.tests.TestUtils;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/10/11,1:35 PM
 */
public class Dbl2D extends Lapack4J {
  public static String toString(double[][] arr, int nRows, int nCols) {
    StringBuffer buff = new StringBuffer();
    buff.append(arr.toString() + " = " + TestUtils.EOL);
    for (int i = 0; i < nRows; i++) {
      buff.append(DblArr.toString(arr[i], nCols));
      if (i != nRows - 1)
        buff.append(TestUtils.EOL);
    }
    return buff.toString();
  }

  public static void copyFast(double[][] src, double[][] dest, int nRows, int nCols) {
    if (src == dest) {
      return;
    }
    int n = (nRows + DUFF_SIZE - 1) / DUFF_SIZE - 1;
    int i = 0;
    switch (nRows % DUFF_SIZE) {
      case 0:  System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      case 7:  System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      case 6:  System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      case 5:  System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      case 4:  System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      case 3:  System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      case 2:  System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      case 1:  System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
    }
    for (; n > 0; n--) {
      System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
      System.arraycopy(src[i], 0, dest[i], 0, nCols);   i++;
    }
  }

  public static void setCol(double[][] dest, int colIdx, double val, int len) {
    int n = (len + DUFF_SIZE - 1) / DUFF_SIZE - 1;
    int i = 0;
    switch (len % DUFF_SIZE) {
      case 0:  dest[i++][colIdx] = val;
      case 7:  dest[i++][colIdx] = val;
      case 6:  dest[i++][colIdx] = val;
      case 5:  dest[i++][colIdx] = val;
      case 4:  dest[i++][colIdx] = val;
      case 3:  dest[i++][colIdx] = val;
      case 2:  dest[i++][colIdx] = val;
      case 1:  dest[i++][colIdx] = val;
    }
    for (; n > 0; n--) {
      dest[i++][colIdx] = val;
      dest[i++][colIdx] = val;
      dest[i++][colIdx] = val;
      dest[i++][colIdx] = val;
      dest[i++][colIdx] = val;
      dest[i++][colIdx] = val;
      dest[i++][colIdx] = val;
      dest[i++][colIdx] = val;
    }
  }
}
