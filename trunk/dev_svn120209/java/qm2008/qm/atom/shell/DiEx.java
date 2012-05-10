package atom.shell;
import math.Calc;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 13:37:34
 */
public class DiEx {
  public final double di;
  public final double ex;
  public DiEx(double d, double e) {
    di = d;
    ex = e;
  }
  public DiEx(double d) {
    di = d;
    ex = 0;
  }
  public String toString() {
    return "di="+ (float)di + ", ex=" + (float)ex;
  }
  public boolean isZero() {
    return Calc.isZero(di)  &&  Calc.isZero(ex);
  }
}
