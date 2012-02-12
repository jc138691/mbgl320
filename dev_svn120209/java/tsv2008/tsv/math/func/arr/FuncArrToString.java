package math.func.arr;

import math.func.FuncVec;
import math.vec.VecArr;

import javax.langx.SysProp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 17/05/2010, 10:07:45 AM
 */
public class FuncArrToString extends FuncArr {
  public FuncArrToString(final FuncArr from) {
    super(from);
  }
  public String toTab() {
    return toTab(getX().getArr(), this);
  }
  public static String toTab(double[] x, VecArr arr) {
    return toString(x, arr, " \t");
  }

  public static String toString(double[] x, VecArr arr, String delim) {
    StringBuffer buff = new StringBuffer();
    for (int ix = 0; ix < x.length; ix++) {
      buff.append(" " + x[ix]);
      for (int func = 0; func < arr.size(); func++) {
        buff.append(delim + arr.get(func).get(ix));
        if (func == arr.size()-1)
          buff.append(SysProp.EOL);
      }
    }
    return buff.toString();
  }
}
