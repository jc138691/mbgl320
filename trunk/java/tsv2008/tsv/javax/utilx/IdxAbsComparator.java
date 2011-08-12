package javax.utilx;
import java.util.Comparator;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 13:39:51
 */
public class IdxAbsComparator extends IdxComparator {
  public IdxAbsComparator(double[] from, boolean ascending) {
    super(from, ascending);
  }
  public int compare(Integer intObj, Integer intObj2) {
    double d = src[intObj.intValue()];
    double d2 = src[intObj2.intValue()];
    if (d == d2)
      return 0;
    if (Math.abs(d) < Math.abs(d2))
      return LESS;
    return GREATER;
  }
}
