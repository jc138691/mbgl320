package math.vec;

import flanagan.complex.Cmplx;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 05/03/2010, 11:57:52 AM
 */
public class DbgView {
  private static int numShow = 10;
  private static double minVal = 0;

  public static int getNumShow() {
    return numShow;
  }
  public static void setNumShow(int numShow) {
    DbgView.numShow = numShow;
  }
  public static void append(StringBuffer buff, double v) {
    if (minVal == 0  ||  Math.abs(v) >= minVal)
      buff.append(Float.toString((float) v));
    else
      buff.append(Float.toString(0));
  }
  public static void append(StringBuffer buff, String str) {
    buff.append(str);
  }
  public static void append(StringBuffer buff, Cmplx v) {
    if (minVal == 0  ||  v.abs() >= minVal)
      buff.append("(" + Float.toString((float) v.getRe())
        + "," + Float.toString((float) v.getIm()) + ")");
    else
      buff.append("(0)");
  }
  public static double getMinVal() {
    return minVal;
  }

  public static void setMinVal(double minVal) {
    DbgView.minVal = Math.abs(minVal);
  }
}
