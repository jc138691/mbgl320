package scatt.jm_2008.jm.laguerre;
import math.vec.Vec;
import math.func.Func;
import atom.wf.WFQuadrR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 22/10/2008, Time: 16:41:56
 */
public class LagrrBiR extends LgrrR {
  public static String HELP = "Bio-diagonal JM-matrix Laguerre lgrrN:\n"
    + "BioR(n, a, lambda, r) = [n! / (n + a)!] * R(n, a, lambda, r) / r,\n"
    + "see LgrrR.HELP for R";
  public LagrrBiR(WFQuadrR w, LgrrModel model) {
    super(w, model);
//    multFirst(new ThisNormFunc()); // THIS IS called in super
    calcBioNorm();
  }
  public LagrrBiR(Vec r, LgrrModel model) {
    super(r, model);
//    multFirst(new ThisNormFunc()); // THIS IS called in super
    calcBioNorm();
  }

  protected Func makeNormFunc() {
    return new ThisNormFunc();
  }
  private class ThisNormFunc implements Func {
    public double calc(double r) {
      double x = lambda * r;
      double res = Math.exp(-0.5 * x);
      double a = (alpha - 1.) / 2.;
      res *= Math.pow(x, a);
      return res * lambda;
    }
  }

  private void calcBioNorm() {
    int a = 2 * model.getL() + 1;
    for (int n = 0; n < size(); n++) {
      double bioNorm = fLn.calcFactDiv(n, n + a);
      get(n).mult(bioNorm);
    }
  }
}
