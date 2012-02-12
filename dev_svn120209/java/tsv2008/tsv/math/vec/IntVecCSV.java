package math.vec;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 13:26:35
 */
public class IntVecCSV {
  static public String toString(int[] ints) {
    return toString(ints, ints.length);
  }
  static public String toString(int[] ints, int size) {
    int L = Math.min(size, ints.length);
    StringBuffer buff = new StringBuffer();
    for (int i = 0; i < L; i++) {
      int anInt = ints[i];
      buff.append(anInt);
      if (i != L - 1)
        buff.append(", ");
    }
    return buff.toString();
  }
}
