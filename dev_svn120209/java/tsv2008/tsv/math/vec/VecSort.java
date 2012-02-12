package math.vec;
import javax.utilx.IdxComparator;
import javax.utilx.IdxAbsComparator;
import java.util.List;
import java.util.Comparator;
import java.util.Arrays;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 13:22:02
 */
public class VecSort {
  public static int[] sortIdx(double[] v) {
    return sortIdx(v, true);
  }
  public static int[] sortIdx(double[] v, boolean ascending) {
    List list = IntVec.makeIdxList(v.length);
    Object[] res = list.toArray();
    Comparator comp = new IdxComparator(v, ascending);
    Arrays.sort(res, comp);
    return IntVec.toArray(res);
  }
  public static int[] sortIdxByAbs(double[] v, boolean ascending) {
    List list = IntVec.makeIdxList(v.length);
    Object[] res = list.toArray();
    Comparator comp = new IdxAbsComparator(v, ascending);
    Arrays.sort(res, comp);
    return IntVec.toArray(res);
  }
}


