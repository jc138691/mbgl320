package d1;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 6/06/12, 10:54 AM
 */
public class ConfD1 implements IConf {
protected int[] arrQ;  // occupation numbers
protected int[] arrId; //
public ConfD1(int size) {
  arrQ = new int[size];
  arrId = new int[size];
}
public int[] getArrQ() {
  return arrQ;
}
public int[] getArrId() {
  return arrId;
}
public String toString() {
  StringBuffer buff = new StringBuffer();
  for (int i = 0; i < arrQ.length; i++) {
    int q = arrQ[i];
    int id = arrId[i];
    buff.append(id);
    if (q > 1)
      buff.append("^" + q);
    if (i != arrQ.length-1)
      buff.append(", ");
  }
  return buff.toString();
}
}
