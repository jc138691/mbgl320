package calc.interpol;

import math.func.Func;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 27/04/2010, 11:57:47 AM
 */
public class InterpCube implements Func {
  private double a = 0;
  private double b = 0;
  private double c = 0;

  public InterpCube(double x, double x2, double x3
    , double y, double y2, double y3) {
    if (x == x2)
      return;
    double y21 = y2 - y;
    double y32 = y3 - y2;
    double x21 = x2 - x;
    double x32 = x3 - x2;
    double x21_2 = x2 * x2 - x * x;
    double x32_2 = x3 * x3 - x2 * x2;
    double det = x21_2 * x32 - x32_2 * x21;

    a = (y21 * x32 - y32 * x21) / det;
    b = -(y21 * x32_2 - y32 * x21_2) / det;
    c = y - a * x * x - b * x;
  }
  public double calc(double x) {
    return (a * x * x + b * x + c);
  }
  public double getA() {return a;}
  public double getB() {return b;}
  public double getC() {return c;}
}
