package math.func.polynom.laguerre;
import math.func.Func;
import math.func.FuncGamma;
import math.vec.Vec;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 14:01:47
 */
//  Orthonormal version: pp579-580 from Russian copy of Abramowitz
//                               (a)
//   LagrrBasis's polinomials   L  (y), n = 0,..., nmax
//                               n
//   oo   (a)    (a)    -y  a
//   I   L  (y) L  (y) e   y  dy = Gamma(a + n + 1) / n!
//   0     n     n'
//
//     y = lambda * r; dr = dy/lambda
//
//   oo   (a)             (a)             -y  a
//   I   L  (lambda * r) L  (lambda * r) e   y  dr = 1/lambda * Gamma(a + n + 1) / n!
//   0     n               n'
//                                                oo
//Set of orthonormal functions L_n(r), such that I L_n(r) L_n'(r) dr = delta_nn'
//                                                0
// NOTE: integration is dr  not  r**2 dr
public class LgrrOrth extends LgrrArr {
  private double[] fromNonOrth;
  public LgrrOrth(Vec r, int size, int alpha, double lambda) {
    super(r, size, alpha, lambda);
    mult(new ThisNormFunc());
    norm();
  }
  private class ThisNormFunc implements Func {
    public double calc(double r) {
      double x = lambda * r;
      if (x == 0)
        return 0;
      return Math.exp(-0.5 * x + 0.5 * alpha * Math.log(x));
    }
  }
  public double[] getFromNonOrth() {
    return fromNonOrth;
  }
  private void norm() {
    fromNonOrth = new double[size()];
    if (alpha <= 1e-16) {
      normZeroAlpha();
      return;
    }

//[OLD]
//    FuncGamma G = new FuncGamma();
//    for (int n = 0; n < size(); n++) {
//      double normN = 1.0 / Math.sqrt(G.calc(alpha + n + 1) / G.calc(n + 1) / lambda);
//      get(n).multSelf(normN);
//    }

    //new 29Apr2011
    FuncGamma G = new FuncGamma();
    for (int n = 0; n < size(); n++) {
//      double normN = 1.0 / Math.sqrt(G.calc(alpha + n + 1) / G.calc(n + 1) / lambda);
      double normN = Math.sqrt(lambda * fLn.calcFactDiv(n, alpha + n));
      fromNonOrth[n] = normN;
      get(n).mult(normN);
    }
  }
  private void normZeroAlpha() {
    double normN = Math.sqrt(lambda);
    for (int n = 0; n < size(); n++) {
      get(n).mult(normN);
    }
  }
}
