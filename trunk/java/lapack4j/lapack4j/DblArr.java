package lapack4j;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/10/11,4:00 PM
 */
public class DblArr {
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
}
