package atom.wf.coulomb;

import flanagan.complex.Cmplx;
import math.Calc;
import math.complex.CmplxGamma;
import project.workflow.task.test.FlowTest;
import scatt.Scatt;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 14/05/2010, 11:16:29 AM
 */
public class CoulombRegAsympt extends FlowTest {
  private static double EPS = 1e-18;
  public static Log log = Log.getLog(CoulombRegAsympt.class);
  public CoulombRegAsympt() { super(CoulombRegAsympt.class);  }

  // Z=1 for e-Hydrogen
  public static double calcEng(int L, double Z, double E, double r) {
    if (E <= 0   ||  Calc.isZero(E)) {
      throw new IllegalArgumentException("E <= 0   ||  Calc.isZero(E); E=" + E);
    }
    double k = Scatt.calcMomFromE(E);
    return calc(L, Z, k, r);
  }
  // Z=1 for e-Hydrogen
  public static double calc(int L, double Z, double k, double r) {
    if (k <= 0   ||  Calc.isZero(k)) {
      throw new IllegalArgumentException("k <= 0   ||  Calc.isZero(k); k=" + k);
    }
    if (r <= 0   ||  Calc.isZero(r)) {
      return 0;
//      throw new IllegalArgumentException("r <= 0   ||  Calc.isZero(r); r=" + r);
    }
    double kr = k * r;
    double kr2 = 2. * kr;
    double eta = -Z/k;
    Cmplx a = new Cmplx(L + 1, eta);
    Cmplx G = CmplxGamma.calc(a);
    double delta = G.arg();
    double x = kr - eta * Math.log(kr2) - 0.5 * Math.PI * L + delta;

    double fi = 1;
    double gi = 0;

    int MAX_I = 20;
    double f = 0;
    double g = 0;
    // Abramowitz&Stegan
    for (int i = 0; i < MAX_I; i++) {
      if (Math.abs(fi) < EPS  && Math.abs(gi) < EPS)
        break;
      f += fi;
      g += gi;
      double ai = (2. * i + 1) * eta / ((i + 1.) * kr2);
      double bi = (eta * eta + L * (L+1) - i * (i + 1.)) / ((i + 1.) * kr2);
      double f2 = ai * fi - bi * gi;
      double g2 = ai * gi + bi * fi;
      if (Math.abs(f2) > 1.  ||  Math.abs(g2) > 1.) {
        throw new IllegalArgumentException("Math.abs(f2) > 1.  ||  Math.abs(g2) > 1.; k=" + k + ", r=" + r);
      }
      fi = f2;
      gi = g2;
    }

    double res = g * Math.cos(x) + f * Math.sin(x);
    return res;
  }

  public void testCalc() throws Exception {
    log.dbg("testing " + CoulombRegAsympt.class);
    double eps = 1e-5;

    int L = 0;
    double k = 1;
    double r = 50;
    double Z = -9;
    double res = CoulombRegAsympt.calc(L, Z, k, r);
    // Rev. Mod. Phys. 27, 399–411 (1955)
    // Numerical Treatment of Coulomb Wave Functions
    assertEquals(0, Math.abs(res - 0.935709), eps); //rho=50, eta=9

    r = 40;
    k = 0.5;
    Z = -2.5;
    res = CoulombRegAsympt.calc(L, Z, k, r);
    assertEquals(0, Math.abs(res + 0.229352), eps); //rho=20, eta=(-Z)/k=5

    r = 30;
    k = 1./3.;
    Z = -1;
    res = CoulombRegAsympt.calc(L, Z, k, r);
    assertEquals(0, Math.abs(res - 0.660103), 3.e-5); //rho=10, eta=3
  }
}