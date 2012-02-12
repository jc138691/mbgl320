package math.vec;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 17/03/2009, 2:11:18 PM
 */
public class ByteVec {
  static public String toString(byte[] arr) {
    StringBuffer buff = new StringBuffer();
    buff.append("{");
    buff.append(toCSV(arr));
    buff.append("}");
    return buff.toString();
  }
  static public String toCSV(byte[] arr) {
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < arr.length; i++) {
      buff.append(arr[i]);
      if (i != arr.length - 1)
        buff.append(", ");
    }
    return buff.toString();
  }
  public static int min(byte[] ia) {
    if (ia == null) {
      throw new RuntimeException("ia == null");
    }
    if (ia.length == 0) {
      throw new RuntimeException("ia.length == 0");
    }
    int res = ia[0];
    for (int i = 1; i < ia.length; i++) {
      if (res <= ia[i])
        continue;
      res = ia[i];
    }
    return res;
  }
  public static int max(byte[] ia) {
    if (ia == null) {
      throw new RuntimeException("ia == null");
    }
    if (ia.length == 0) {
      throw new RuntimeException("ia.length == 0");
    }
    int res = ia[0];
    for (int i = 1; i < ia.length; i++) {
      if (res >= ia[i])
        continue;
      res = ia[i];
    }
    return res;
  }

  public static byte[] toArray(Object[] intObjs) {
    byte[] res = new byte[intObjs.length];
    for (int i = 0; i < intObjs.length; i++) {
      res[i] = (byte)((Integer) intObjs[i]).intValue();
    }
    return res;
  }
  public static byte[] make(int size, byte initVal) {
    byte[] res = new byte[size];
    for (int i = 0; i < size; i++) {
      res[i] = initVal;
    }
    return res;
  }

}