package javax.utilx;
import java.util.Comparator;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 13:38:07
 */
public class IdxComparator implements Comparator<Integer> {
  protected final double[] src;
  protected int LESS = -1;
  protected int GREATER = 1;
  public IdxComparator(double[] from, boolean ascending) {
    src = from;
    if (!ascending) {
      LESS *= -1;
      GREATER *= -1;
    }
  }
  public int compare(Integer intObj, Integer intObj2) {
    double d = src[intObj.intValue()];
    double d2 = src[intObj2.intValue()];
    if (d == d2)
      return 0;
    if (d < d2)
      return LESS;
    return GREATER;
  }
}
