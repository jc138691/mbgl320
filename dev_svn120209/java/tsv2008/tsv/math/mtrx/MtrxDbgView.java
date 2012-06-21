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
  int nr = getNumRows();
  String head = "Mtrx["+getNumRows()+"]["+getNumCols()+"] = {" + SysProp.EOL;
  if (DbgView.getNumShow() >= nr) {
    return head + toString(this, 0, nr);
  }
  int n = DbgView.getNumShow() / 2;
  String start = toString(this, 0, n);
  String tail = toString(this, nr - n, n);
  return head + start + SysProp.EOL + ", ..., " + SysProp.EOL + tail + SysProp.EOL + "}";
}

public static String toString(Mtrx m, int firstIdx, int size) {
  int nr = m.getNumRows();
  int len = Math.min(nr, firstIdx + size);
  StringBuffer buff = new StringBuffer();
  for (int i = firstIdx; i < len; i++) {
    buff.append(new VecDbgView(new Vec(m.getRowCopy(i))));
    if (i != len - 1)
      buff.append(SysProp.EOL);
  }
  return buff.toString();
}
}
