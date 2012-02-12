package javax.utilx.intx;

import javax.triplet.Int3;
import javax.utilx.pair.Int2;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,08/06/2010,1:54:49 PM
 */
public class Int4 extends Int3
{
  public int d;
  public Int4(int a, int b, int c, int d) {
    super(a, b, c);
    this.d = d;
  }
  public Int4(int a, int b, int c) {
    super(a, b, c);
  }
  public Int4(int a, int b) {
    super(a, b);
  }
  public Int4(int a) {
    super(a);
  }
  public Int4(Int4 from) {
    super(from);
    this.d = from.d;
  }

  public String toString() {
    return "(" + a + ", " + b + ", " + c + ", " + d + ")";
  }
}