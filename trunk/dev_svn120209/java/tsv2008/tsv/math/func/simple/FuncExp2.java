package math.func.simple;
import math.func.Func;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 28/02/12, 12:11 PM
 */
public class FuncExp2 implements Func {
  private final double c;
  public FuncExp2(final double c) {
    this.c = c;
  }
  public double calc(double x) {
    return Math.exp(c * x * x);
  }
}