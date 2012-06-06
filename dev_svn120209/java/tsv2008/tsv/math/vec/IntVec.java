package math.vec;
import javax.utilx.RandomSeed;
import javax.utilx.IdxComparator;
import javax.utilx.comparatorx.IntIdxComparator;
import java.util.List;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Arrays;

/**
* Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 13:24:32
*/
public class IntVec {
private int[] arr;
public IntVec(int size) {
  this.arr = new int[size];
}
public IntVec(int[] from) {
  this.arr = from;
}
public String toString() {
  return IntVec.toString(arr);
}

public static int min(int[] ia) {
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
public static int max(int[] ia) {
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
//  public static int maxIdx(int [] dx, int size) {
//    if (dx == null)
//      return -1;
//    int n = Math.min(dx.length, size);
//    if (n == 0)
//      return -1;
//    int idx = 0;
//    int v = dx[idx];
//    for (int i = 1; i < n; i++) {
//      if (v >= dx[i])
//        continue;
//      idx = i;
//      v = dx[i];
//    }
//    return idx;
//  }
//  public static int minIdx(int [] dx, int size) {
//    if (dx == null)
//      return -1;
//    int n = Math.min(dx.length, size);
//    if (n == 0)
//      return -1;
//    int idx = 0;
//    int v = dx[idx];
//    for (int i = 1; i < n; i++) {
//      if (v <= dx[i])
//        continue;
//      idx = i;
//      v = dx[i];
//    }
//    return idx;
//  }
//  public static boolean has(int[] ia, int size, int val) {
//    int L = Math.min(ia.length, size);
//    for (int i = 0; i < L; i++) {
//      if (ia[i] == val)
//        return true;
//    }
//    return false;
//  }
public static int[] make(int size, int val) {
  int[] res = new int[size];
  Arrays.fill(res, val);
  return res;
}
//  public static int minAbove(int excl, int[] ia) {
//    if (ia == null || ia.length == 0)
//      return 0;
//    int res = excl;
//    for (int i = 0; i < ia.length; i++) {
//      final int v = ia[i];
//      if (excl >= v)
//        continue; // ignore below excluded boundary
//      if (res == excl || res > v)
//        res = v;
//    }
//    return res;
//  }
static public String toString(int[] ints, int size) {
  StringBuffer buff = new StringBuffer();
  buff.append("{");
  buff.append(IntVecCSV.toString(ints, size));
  buff.append("}");
  return buff.toString();
}
static public String toString(int[] ints) {
  return toString(ints, ints.length);
}
public static int[] makeIdxArray(int size) {
  return makeIdxArray(0, size);
}
public static int[] makeIdxArray(int startIdx, int size) {
  int[] res = new int[size];
  for (int i = 0; i < size; i++) {
    res[i] = (startIdx + i);
  }
  return res;
}
public static int[] makeRandIdxArr(int size) {
  List pool = makeIdxList(size);
  RandomSeed rand = RandomSeed.getInstance();
  int[] res = new int[size];
  for (int i = 0; i < size; i++) {
    int from = rand.nextInt(pool.size());
    int idx = ((Integer)pool.remove(from)).intValue();
    res[i] = idx;
  }
  return res;
}
public static List<Integer> makeIdxList(int size) {
  List<Integer> res = new LinkedList<Integer>();
  for (int i = 0; i < size; i++) {
    res.add(i);
  }
  return res;
}
public static Integer[] makeIdxArr(int size) {
  Integer[] res = new Integer[size];
  for (int i = 0; i < size; i++) {
    res[i] = i;
  }
  return res;
}
public static int[] toArray(Object[] from) {
  int[] res = new int[from.length];
  for (int i = 0; i < from.length; i++) {
    res[i] = ((Integer) from[i]).intValue();
  }
  return res;
}
public static int[] toArray(Integer[] from) {
  int[] res = new int[from.length];
  for (int i = 0; i < from.length; i++) {
    res[i] = from[i];
  }
  return res;
}
//  public static void set(int[] a, int val) {
//    for (int i = 0; i < a.length; i++) {
//      a[i] = val;
//    }
//  }
//  public static void set(byte[] a, byte val) {
//    for (int i = 0; i < a.length; i++) {
//      a[i] = val;
//    }
//  }
//
//  public static void add(int[] to, int[] from)
//  {
//    if (to.length != from.length) {
//      throw new RuntimeException("to.length != from.length");
//    }
//    for (int i = 0; i < to.length; i++) {
//      to[i] += from[i];
//    }
//  }

public static int sum(int[] arr) {
  int res = 0;
  for (int i = 0; i < arr.length; i++) {
    res += arr[i];
  }
  return res;
}

public static int[] sortIdx(int[] v) {
  return sortIdx(v, true);
}
public static int[] sortIdx(int[] v, boolean ascending) {
  Integer[] res = IntVec.makeIdxArr(v.length);
  Comparator comp = new IntIdxComparator(v, ascending);
  Arrays.<Integer>sort(res, comp);
  return IntVec.toArray(res);
}

//  public static LinkedList<Integer> toList(int[] from)
//  {
//    LinkedList<Integer> res = new LinkedList<Integer>();
//    for (int i : from) {
//      res.add(new Integer(i));
//    }
//    return res;
//  }
//
//  public static int[] move(int intVal, int toIdx, int[] res)
//  {
//    if (res[toIdx] == intVal)
//      return res;  // already there
//    LinkedList<Integer> list = IntVec.toList(res);
//    list.remove(new Integer(intVal));
//    list.add(toIdx, new Integer(intVal));
//    return IntVec.toArray(list.toArray());
//  }
}
