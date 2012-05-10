package math.vec;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 27/10/2008, Time: 11:10:09
 */
public class VecDbgView extends Vec {
public VecDbgView(Vec from) {
  super(from);
}
public VecDbgView(double[] from) {
  super(from);
}
public String toString() {
  if (DbgView.getNumShow() >= size()) {
    return toString(getArr(), size());
  }
  int n = DbgView.getNumShow() / 2;
  String start = toString(arr, 0, n);
  String tail = toString(arr, arr.length - n, n);
  String head = "Vec["+size()+"] = {";
  return head + start + ", ..., " + tail + "}";
}
public static String toString(double[] a, int firstIdx, int size) {
  int len = Math.min(a.length, firstIdx + size);
  StringBuffer buff = new StringBuffer();
  for (int i = firstIdx; i < len; i++) {
    DbgView.append(buff, a[i]);
    if (i != len - 1)
      buff.append(", ");
  }
  return buff.toString();
}

}
