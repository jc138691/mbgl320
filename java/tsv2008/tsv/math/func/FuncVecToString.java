package math.func;

import javax.langx.SysProp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 12/02/2010, 11:43:44 AM
 */
public class FuncVecToString extends FuncVec {
  public FuncVecToString(final FuncVec from) {
    super(from);
  }
  public String toString() {
    return getX().toCSV() + SysProp.EOL + toCSV();
  }

  public String toTab() {
    return toTab(getX().getArr(), getY().getArr());
  }

  public static String toTab(double[] x, double[] y) {
    return toString(x, y, " \t");
  }
  public static String toString(double[] x, double[] y, String delim) {
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < x.length; i++) {
      buff.append(" " + x[i] + delim + y[i] + SysProp.EOL);
    }
    return buff.toString();
  }
}
