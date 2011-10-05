package lapack4j;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/10/11,1:35 PM
 */
public class Fast2D extends BaseTest {
  private final static int DUFF_SIZE = 8; // see p141 of C++, 3rd ed.

  public static void copy(double[][] from, double[][] to, int len) {
    if (from == to) {
      return;
    }
    int n = (len + DUFF_SIZE - 1) / DUFF_SIZE - 1;
    int i = 0;
    switch (len % DUFF_SIZE) {
      case 0:  System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      case 7:  System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      case 6:  System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      case 5:  System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      case 4:  System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      case 3:  System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      case 2:  System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      case 1:  System.arraycopy(from[i], 0, to[i], 0, len);   i++;
    }
    for (; n > 0; n--) {
      System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      System.arraycopy(from[i], 0, to[i], 0, len);   i++;
      System.arraycopy(from[i], 0, to[i], 0, len);   i++;
    }
  }

}
