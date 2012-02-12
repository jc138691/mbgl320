package math.mtrx;

import math.vec.Vec;
import math.vec.VecDbgView;

import javax.langx.SysProp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 05/03/2010, 10:47:03 AM
 */
public class MtrxDbgView extends Mtrx {
  public MtrxDbgView(Mtrx from) {
    super(from.getArray());
  }
  public String toString() {
    double[][] a = getArray();
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < a.length; i++) {
      buff.append(new VecDbgView(new Vec(a[i])));
      if (i != a.length - 1)
        buff.append(SysProp.EOL);
    }
    return buff.toString();
  }
}
