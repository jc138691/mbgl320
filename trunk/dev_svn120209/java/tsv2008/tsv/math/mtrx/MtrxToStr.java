package math.mtrx;
import math.mtrx.api.Mtrx;
import math.vec.VecToString;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 16:34:51
 */
public class MtrxToStr {
  public static String toCsv(Object a) {
    if (a instanceof Mtrx) {
      return toString((Mtrx)a, ", ");
    }
    return "";
  }
  public static String toTab(Object a, int digs) {
    if (a instanceof Mtrx) {
      return toString((Mtrx)a, " \t", digs);
    }
    return "";
  }
  public static String toTab(Object a) {
    if (a instanceof Mtrx) {
      return toString((Mtrx)a, " \t");
    }
    return "";
  }
  public static String toString(Mtrx a, String delim) {
    StringBuffer buff = new StringBuffer();
    for (int r = 0; r < a.getNumRows(); r++) {
      buff.append(VecToString.toString(a, r, delim));
      if (r != a.getNumRows() - 1)
        buff.append("\n");
    }
    return buff.toString();
  }

  public static String toString(Mtrx a, String delim, int digs) {
    StringBuffer buff = new StringBuffer();
    for (int r = 0; r < a.getNumRows(); r++) {
      buff.append(VecToString.toString(a, r, a.getNumRows(), delim, digs));
      if (r != a.getNumRows() - 1)
        buff.append("\n");
    }
    return buff.toString();
  }

}
