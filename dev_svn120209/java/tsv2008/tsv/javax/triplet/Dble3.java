package javax.triplet;
import javax.utilx.pair.Dble2;
import javax.utilx.pair.Int2;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 30/03/12, 10:28 AM
 */
public class Dble3 extends Dble2
{
  public double c;
  public Dble3(int a, int b, int c) {
    super(a, b);
    this.c = c;
  }
public Dble3() {
}

public String toString() {
  if (isShortStr())
    return "(" + (float)a + ", " + (float)b + ", " + (float)c + ")";
  else
    return "(" + a + ", " + b + ", " + c + ")";
}
}