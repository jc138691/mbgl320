package math.func.simple;
import math.func.Func;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 5/06/12, 10:28 AM
 */
public class FuncCos2 implements Func {
  private double k;
  public FuncCos2(double p) {
    this.k = p;
  }
  public double calc(double r) {
    return Math.cos(k * r);
  }
}