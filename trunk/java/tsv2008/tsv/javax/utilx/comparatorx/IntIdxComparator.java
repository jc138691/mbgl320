package javax.utilx.comparatorx;

import java.util.Comparator;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 27/05/2009, 10:01:34 AM
 */
public class IntIdxComparator implements Comparator<Integer> {
  private final int[] src;
  private int retLess = -1;
  private int retGreater = 1;
  public IntIdxComparator(int[] from, boolean ascending) {
    src = from;
    if (!ascending) {
      retLess *= -1;
      retGreater *= -1;
    }
  }
  public int compare(Integer idx, Integer idx2) {
    int d = src[idx];
    int d2 = src[idx2];
    if (d == d2)
      return 0;
    if (d < d2)
      return retLess;
    return retGreater;
  }

}
