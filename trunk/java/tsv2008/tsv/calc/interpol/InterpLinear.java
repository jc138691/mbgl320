package calc.interpol;

import math.func.Func;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 13/03/2009, 1:27:42 PM
 */
public class InterpLinear implements Func {
  private double a = 0;
  private double b = 1;
  private double x;
  private double x2;
  private double y;
  private double y2;

  public InterpLinear(double x, double x2, double y, double y2) {
    this.x = x;
    this.y = y;
    this.x2 = x2;
    this.y2 = y2;
    if (x == x2)
      return;
    b = (y2 - y) / (x2 - x);
    a = y2 - b * x2;
  }
  public double calc(double x) {
    return (a + b * x);
  }

//  public double calc(double x) {
//    return (a + b * x);
//  }

//  public double getX() {
//    return x;
//  }

}
