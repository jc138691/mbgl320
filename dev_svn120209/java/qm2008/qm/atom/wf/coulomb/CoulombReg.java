package atom.wf.coulomb;

import flanagan.complex.Cmplx;
import math.Calc;
import math.complex.Cmplx1F1;
import math.complex.CmplxGamma;
import project.workflow.task.test.FlowTest;
import scatt.Scatt;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 13/05/2010, 1:03:03 PM
 */
public class CoulombReg extends FlowTest {
  public static Log log = Log.getLog(CoulombReg.class);
  private static final double VALID_ASYMT = 0.1;

  public CoulombReg() {
    super(CoulombReg.class);
  }
  // atomZ=1 for e-Hydrogen
  public static Cmplx calc(int L, double Z, double k, double r) {
    return calc(L, Z, k, r, Calc.EPS_18);
  }
  public static Cmplx calcEng(int L, double Z, double E, double r) {
    double k = Scatt.calcMomFromE(E);
    return calc(L, Z, k, r);
  }
  public static Cmplx calc(int L, double Z, double k, double r, double eps) {
    double kr = k * r;
    double kr2 = 2. * kr;
    double eta = -Z/k;
    Cmplx a = new Cmplx(L + 1, -eta);
    Cmplx b = new Cmplx(2. * L + 2.);
    Cmplx x = new Cmplx(0, kr2);
    if (Math.abs(eta / kr) < VALID_ASYMT && Math.abs(eta * eta / kr) < VALID_ASYMT )
     return new Cmplx(CoulombRegAsympt.calc(L, Z, k, r)); 

    Cmplx F = Cmplx1F1.calc(a, b, x, eps);

    Cmplx G = CmplxGamma.calc(a.conjugate());
    Cmplx GL = CmplxGamma.calc(b);
    double t = Math.exp(-Math.PI * eta / 2);
    double Ckl = t * Math.pow(2., L) * G.abs() / GL.abs();

    double krL = Math.pow(kr, L+1);
    Cmplx exp_kr = new Cmplx(0, -kr).exp();

    Cmplx res = exp_kr.mult(F);
    res = res.times(Ckl * krL);
    return res;
  }
  public void testCalc() throws Exception {
    log.dbg("testing " + CoulombReg.class);
    double eps = 1e-5;

    double eta = 0;
    int L = 0;
    double k = 1;
    double r = 1;
    double Z = -1;
    Cmplx res = CoulombReg.calc(L, Z, k, r);
    Cmplx answ = new Cmplx(0.22753);//rho=1, eta=1
    assertEquals(0, res.minus(answ).abs(), eps);

    r = 2;
    res = CoulombReg.calc(L, Z, k, r);
    answ = new Cmplx(0.66178); //rho=2, eta=1
    assertEquals(0, res.minus(answ).abs(), eps);

    k = 2;
    res = CoulombReg.calc(L, Z, k, r);
    answ = new Cmplx(0.41924);   //rho=4, eta=0.5
    assertEquals(0, res.minus(answ).abs(), eps);

//    eta = 10; // (-atomZ/k)
//    atomZ = -1;
//    k = -atomZ / eta;
//    res = CoulombReg.calc(L, atomZ, k, r);
//    answ = new Cmplx(0.41924);   //rho=4, eta=0.5
//    assertEquals(0, res.minus(answ).abs(), eps);
  }
}