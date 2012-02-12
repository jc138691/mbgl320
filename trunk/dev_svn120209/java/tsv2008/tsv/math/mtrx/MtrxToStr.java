package math.mtrx;
import math.vec.VecToString;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 16:34:51
 */
public class MtrxToStr {
  public static String toCsv(double[][] a) {
    return toString(a, ", ");
  }
  public static String toTab(double[][] a, int digs) {
    return toString(a, " \t", digs);
  }
  public static String toTab(double[][] a) {
    return toString(a, " \t");
  }
  public static String toString(double[][] a, String delim) {
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < a.length; i++) {
      buff.append(VecToString.toString(a[i], delim));
      if (i != a.length - 1)
        buff.append("\n");
    }
    return buff.toString();
  }

  public static String toString(double[][] a, String delim, int digs) {
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < a.length; i++) {
      buff.append(VecToString.toString(a[i], a.length, delim, digs));
      if (i != a.length - 1)
        buff.append("\n");
    }
    return buff.toString();
  }

}
