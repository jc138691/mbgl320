package scatt.jm_2008.jm.laguerre;
import math.func.polynom.laguerre.LgrrOrth;
import atom.wf.WFQuadrR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 14:12:21
 */
public class JmLgrrOrthR extends LgrrOrth implements IWFuncArr {
  private WFQuadrR quadr;
  public static String HELP = "Orthonormal Laguerre jmBasisN:\n"
  + "R(n, a, lambda, r) = C_n * exp(-x/2) x^(a/2) L^a_n(x),\n"
  + "where x = lambda * r,  a = alpha = 2*l+2, l - angular momentum, L^a_n - the associated Laguerre polynomials.";

  public JmLgrrOrthR(WFQuadrR w, JmLgrrModel model) {
    super(w.getX(), model.getN(), 2 * model.getL() + 2, model.getLambda());
    quadr = w;
  }
//  public JmLgrrOrthR(Vec r, JmLgrrModel model) {
//    super(r, model.getMaxN(), 2 * model.getTotL() + 2, model.getLambda());
//  }
  public WFQuadrR getQuadr() {
    return quadr;
  }
}
