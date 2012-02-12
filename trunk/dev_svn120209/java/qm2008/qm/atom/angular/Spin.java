package atom.angular;

import javax.utilx.log.Log;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 16:02:06
 */
public class Spin
  implements Comparable<Spin>
{
  public static Log log = Log.getLog(Spin.class);
  final public static Spin SINGLET = new Spin(1);
  final public static Spin ELECTRON = new Spin(2);
  final public static Spin TRIPLET = new Spin(3);
  final public int s2;  // 2s

//  public Spin(int numer, int denom) {// The top number is called the numerator. The bottom number is called the denominator
//    s21 = (2 * numer + denom) / denom;
//    this.denom = denom;
//    this.numer = numer;
//  }
  public Spin(int s21) {//2s+1
    this.s2 = s21 - 1;
  }

  final public boolean equals(Spin obj) {
    if (obj == this)
      return true;
    return (s2 == obj.s2);
  }

  final public int getS21() { // 2s+1
    return s2 + 1;
  }
  final public int getS2() { // 2s
    return s2;
  }
  final public double hat() { // sqrt(2s+1)
    return Math.sqrt(s2+1);
  }
  final public int getS() { // s
    if (s2 % 2 != 0) {
      throw new IllegalArgumentException(log.error("Spin.getS(2*s=" + s2));
    }
    return s2 / 2;
  }

  public static String toString(Spin s) {
    return Integer.toString(s.s2+1);
  }

//  final public int getInt() {
//    if (denom != 1) {
//      throw new IllegalArgumentException(log.error("\"int value is not available for \" + numer + \"/\" + denom"));
//    }
//    return numer;
//  }

  public int compareTo(Spin obj) {
    return s2 - obj.s2;
  }
}
