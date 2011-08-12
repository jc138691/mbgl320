package atom.shell;
import atom.angular.Spin;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 15:58:52
 */
public class Ls implements Comparable<Ls> {
  public static final Ls CLOSED_SHELL = new Ls(0, Spin.SINGLET);
  private static final String strL[] = {"s", "p", "d", "f", "g", "h"};
  private static final String upL[] = {"S", "P", "D", "F", "G", "H"};
  final private int l;
  final private Spin s;
  public static String toString(int L) {
    if (L < strL.length)
      return strL[L];
    else
      return "L" + L;
  }
  public static String toUpper(int L) {
    if (L < upL.length)
      return upL[L];
    else
      return "L" + L;
  }
  public String toString() {
    return Spin.toString(s) + Ls.toUpper(l);
  }
//  public Ls(final Ls from) {
//    this.l = from.l;
//    this.s = from.s;
//  }
  public Ls(final int L, final Spin S) {
    this.l = L;
    this.s = S;
  }
  final public boolean equals(Ls obj) {
    if (obj == this)
      return true;
    return (l == obj.l  &&  s.equals(obj.s));
  }
  public int compareTo(Ls id) {
    if (id == this)
      return 0;
    int res = l - id.l;
    if (res != 0)
      return res;
    return s.compareTo(id.s);
  }

  public int getL() {
    return l;
  }

  public int getS21() {
    return s.getS21();
  }
  public int getS2() {
    return s.getS2();
  }
  public double hatS() {
    return s.hat();
  }
  public int getS() {
    return s.getS();
  }
}
