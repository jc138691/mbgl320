package lapack4j;
import java.util.Arrays;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/10/11,4:00 PM
 */
public class DblArr extends Lapack4J {
  public static String toString(double[] arr, int len) {
    StringBuffer buff = new StringBuffer();
    buff.append(arr.toString() + " = ");
    for (int i = 0; i < len; i++) {
      double v = arr[i];
      buff.append(Double.toString(v));
      if (i != len - 1)
        buff.append(", ");
    }
    return buff.toString();
  }
  public static void copy(double[] src, double[] dest, int len) {
    if (src == dest) {
      return;
    }
    System.arraycopy(src, 0, dest, 0, len);
  }
  public static void set(double[] dest, double val, int len) {
    Arrays.fill(dest, 0, len, val);
  }
  public static double sumAbs(double initVal, double[] src, int len) {
    int n = (len + DUFF_SIZE - 1) / DUFF_SIZE - 1;
    int i = 0;
    double res = initVal;
    switch (len % DUFF_SIZE) {
      case 0:  res += Math.abs(src[i++]);
      case 7:  res += Math.abs(src[i++]);
      case 6:  res += Math.abs(src[i++]);
      case 5:  res += Math.abs(src[i++]);
      case 4:  res += Math.abs(src[i++]);
      case 3:  res += Math.abs(src[i++]);
      case 2:  res += Math.abs(src[i++]);
      case 1:  res += Math.abs(src[i++]);
    }
    for (; n > 0; n--) {
      res += Math.abs(src[i++]);
      res += Math.abs(src[i++]);
      res += Math.abs(src[i++]);
      res += Math.abs(src[i++]);
      res += Math.abs(src[i++]);
      res += Math.abs(src[i++]);
      res += Math.abs(src[i++]);
      res += Math.abs(src[i++]);
    }
    return res;
  }
}
