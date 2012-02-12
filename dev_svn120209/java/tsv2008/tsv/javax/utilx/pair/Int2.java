package javax.utilx.pair;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,08/06/2010,10:19:19 AM
 */
public class Int2 {
  public int a;
  public int b;
  public Int2(int a, int b) {
    this.a = a;
    this.b = b;
  }
  public Int2(int a) {
    this.a = a;
  }
  public Int2(Int2 from) {
    this.a = from.a;
    this.b = from.b;
  }
  final public int pair(int res) {
    return (res == b) ? a : b;
  }
  public String toString() {
    return "(" + a + ", " + b + ")";
  }
}
