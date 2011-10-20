package lapack4j.utils;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,19/10/11,4:59 PM
 */
public class DblRef {
  public double getVal() {
    return val;
  }
  public void setVal(double val) {
    this.val = val;
  }
  private double val;
  public static void setVal(DblRef p, double v, DblRef p2, double v2) {
    p.setVal(v);
    p2.setVal(v2);
  }
  public static void setVal(DblRef p, double v, DblRef p2, double v2, DblRef p3, double v3) {
    setVal(p, v, p2, v2);
    p3.setVal(v3);
  }
  public static void setVal(DblRef p, double v, DblRef p2, double v2, DblRef p3, double v3, DblRef p4, double v4) {
    setVal(p, v, p2, v2, p3, v3);
    p4.setVal(v4);
  }
}
