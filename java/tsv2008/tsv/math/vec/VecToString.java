package math.vec;

import java.text.NumberFormat;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/07/2008, Time: 14:33:15
 */
//public class VecToString extends Vec {
public class VecToString {
  private Vec src;
  public VecToString(Vec src) {
//    super(src);
    this.src = src;
  }
  public String toString() {
//    return toCsv(getArr());
    return toCsv(src.getArr());
  }
  public static String toString(Vec v) {
    return toCsv(v.getArr());
  }
  public static String toString(double[] arr, String delim) {
    return toString(arr, arr.length, delim);
  }
  public static String toCsv(double[] arr) {
    return toString(arr, arr.length, ", ");
  }
  public static String toString(double[] a, int size) {
    return toString(a, size, ", ");
  }
  public static String toString(double[] a, int size, String delim) {
    int L = Math.min(a.length, size);
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < L; i++) {
      double v = a[i];
//      buff.append(Float.toString((float) v));
      buff.append(Double.toString(v));
      if (i != L - 1)
        buff.append(delim);
    }
    return buff.toString();
  }
  public static String toString(double[] a, int size, String delim, int digs) {
    NumberFormat format = NumberFormat.getNumberInstance();
    format.setMaximumFractionDigits(digs);
    int L = Math.min(a.length, size);
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < L; i++) {
      double v = a[i];
      buff.append(format.format(v));
      if (i != L - 1)
        buff.append(delim);
    }
    return buff.toString();
  }
  public static String toStringTail(double[] a, int size) {
    int L = Math.min(a.length, size);
    StringBuffer buff = new StringBuffer();
    int first = a.length - size;
    for (int i = first; i < a.length; i++) {
      double v = a[i];
      buff.append(Float.toString((float) v));
      if (i != a.length - 1)
        buff.append(", ");
    }
    return buff.toString();
  }

}
