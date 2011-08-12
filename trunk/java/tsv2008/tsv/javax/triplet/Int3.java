package javax.triplet;

import javax.utilx.pair.Int2;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,08/06/2010,1:09:15 PM
 */
public class Int3 extends Int2
{
  public int c;
  public Int3(int a, int b, int c) {
    super(a, b);
    this.c = c;
  }
  public Int3(int a, int b) {
    super(a, b);
  }
  public Int3(int a) {
    super(a);
  }
  public Int3(Int3 from) {
    super(from);
    this.c = from.c;
  }

  public String toString() {
    return "(" + a + ", " + b + ", " + c + ")";
  }
}
