package math.mtrx;

import math.mtrx.api.Mtrx;
import math.vec.DbgView;
import math.vec.Vec;
import math.vec.VecDbgView;

import javax.langx.SysProp;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 05/03/2010, 10:47:03 AM
 */
public class MtrxDbgView extends Mtrx {
public MtrxDbgView(Mtrx from) {
  super(from);
}
public String toString() {
  double[][] a = getArr2D();
  String head = "Mtrx["+getNumRows()+"]["+getNumCols()+"] = {" + SysProp.EOL;
  if (DbgView.getNumShow() >= a.length) {
    return head + toString(a, 0, a.length);
  }
  int n = DbgView.getNumShow() / 2;
  String start = toString(a, 0, n);
  String tail = toString(a, a.length - n, n);
  return head + start + SysProp.EOL + ", ..., " + SysProp.EOL + tail + SysProp.EOL + "}";
}

public static String toString(double[][] a, int firstIdx, int size) {
  int len = Math.min(a.length, firstIdx + size);
  StringBuffer buff = new StringBuffer();
  for (int i = firstIdx; i < len; i++) {
    buff.append(new VecDbgView(new Vec(a[i])));
    if (i != len - 1)
      buff.append(SysProp.EOL);
  }
  return buff.toString();
}
}
