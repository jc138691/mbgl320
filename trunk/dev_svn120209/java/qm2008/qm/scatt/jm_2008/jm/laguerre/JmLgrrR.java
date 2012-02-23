package scatt.jm_2008.jm.laguerre;
import atom.wf.WFQuadr;
import math.func.polynom.laguerre.LgrrArr;
import math.func.Func;
import math.vec.Vec;

import javax.utilx.log.Log;
import static java.lang.Math.*;

import atom.wf.WFQuadrR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 10:31:43
 */
public class JmLgrrR extends LgrrArr implements IWFuncArr {
  public static String HELP = "JM-matrix Laguerre jmBasisN:\n"
  + "R(n, a, lambda, r) = exp(-x/2) x^((a+1)/2) L^a_n(x),\n"
  + "where x = lambda * r;  a = alpha=2*L+1, L - angular momentum; L^a_n - the associated Laguerre polynomials.";
  public static Log log = Log.getLog(JmLgrrR.class);
  protected LgrrModel model;
  private WFQuadrR quadr;

  public LgrrModel getModel() {
    return model;
  }
  public JmLgrrR(WFQuadrR w, LgrrModel model) {
    super(w.getX(), model.getN(), 2 * model.getL() + 1, model.getLambda());
    this.model = model;
    quadr = w;
    mult(makeNormFunc());
  }
  public JmLgrrR(Vec r, LgrrModel model) {
    super(r, model.getN(), 2 * model.getL() + 1, model.getLambda());
    this.model = model;
    mult(makeNormFunc());
  }
  // C(n) = (n+alpha+1)! / (lambda * n!)
  // norm = C(n) + C(n-1)
  public double calcNorm(int i) {
    int v = 2 * model.getL() + 1;
    double lnLambda = log(lambda);
    double res = exp(fLn.calc(i + v + 1) - lnLambda - fLn.calc(i));
    if (i > 0)
      res += exp(fLn.calc(i + v) - lnLambda - fLn.calc(i-1));
    return res;
  }
  protected Func makeNormFunc() {
    return new ThisNormFunc();
  }
  public WFQuadr getQuadr() {
    return quadr;
  }

  private class ThisNormFunc implements Func {
    public double calc(double r) {
      double x = lambda * r;
      if (x == 0)
        return 0;
      return Math.exp(-0.5 * x + 0.5 * (alpha + 1.) * Math.log(x));
    }
  }

}
