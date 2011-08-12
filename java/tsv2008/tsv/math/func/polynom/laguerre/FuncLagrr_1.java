package math.func.polynom.laguerre;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 10:07:51
 */
public class FuncLagrr_1 implements Func {
  final private double alpha;
  final private double lambda;
  public FuncLagrr_1(final double alpha, final double lambda) {
    this.alpha = alpha;
    this.lambda = lambda;
  }
  public double calc(double x) {
    return alpha + 1 - lambda * x;
  }
}