package lapack4j;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/10/11,1:35 PM
 */
public class Dbl2D {
  private final static int DUFF_SIZE = 8; // see p141 of C++, 3rd ed.

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

  public static void copyFast(double[][] from, double[][] to, int nRows, int nCols) {
    if (from == to) {
      return;
    }
    int n = (nRows + DUFF_SIZE - 1) / DUFF_SIZE - 1;
    int i = 0;
    switch (nRows % DUFF_SIZE) {
      case 0:  System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      case 7:  System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      case 6:  System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      case 5:  System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      case 4:  System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      case 3:  System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      case 2:  System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      case 1:  System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
    }
    for (; n > 0; n--) {
      System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
      System.arraycopy(from[i], 0, to[i], 0, nCols);   i++;
    }
  }

}
