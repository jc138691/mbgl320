package scatt.jm_1995.tools_jm_lib.test;
import junit.framework.TestCase;

import javax.utilx.log.Log;

import flanagan.complex.Cmplx;
import scatt.jm_2008.jm.theory.JmTheory;
import scatt.jm_2008.jm.theory.JmTools;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 2/09/2008, Time: 14:33:46
 */
public class SCnTest extends TestCase {
  public static Log log = Log.getLog(SCnTest.class);
  public void testCalc() throws Exception {  log.setDbg();

    Cmplx z;
    Cmplx z2;

    int n, L;
    double th, x, E, k, lambda = 2, eps = 1.e-8;

    // check Cn = -cos((n+1)*theta), Sn = sin((n+1)*theta);
    // cos(theta) = (E-lambda^2/8) / (E+lambda^2/8)
    L = 0;
    n = 10;
    k = 0.1;
    JmTheory.log.setDbg();
    z2 = JmTheory.calcSCnL0(n, k, lambda);
    z = JmTools.sc_n(n, L, new Cmplx(k), lambda, eps);
    log.assertZero("Re(for L=0)=", z.getRe() - z2.getRe(), 4e-16);
    log.assertZero("Im(for L=0)=", z.getIm() - z2.getIm(), 5e-16);

//    z = new Cmplx(0.5, 0.5);
//    z2 = new Cmplx(z).add(1);
//    g = CmplxGamma.calc(z).multSelf(z);
//    g2 = CmplxGamma.calc(z2).minus(g);
//    log.assertZero("Gamma(z+1) - z*Gamma(z)=Re=", g2.getRe(), 4e-17);
//    log.assertZero("Gamma(z+1) - z*Gamma(z)=Im=", g2.getIm(), 2e-16);
//
//    // check Gamma(z+1)=z*Gamma(z)
//    z = new Cmplx(1.5, 0.5);
//    z2 = new Cmplx(z).add(1);
//    g = CmplxGamma.calc(z).multSelf(z);
//    g2 = CmplxGamma.calc(z2).minus(g);
//    log.assertZero("Gamma(z+1) - z*Gamma(z)=Re=", g2.getRe(), 2e-15);
//    log.assertZero("Gamma(z+1) - z*Gamma(z)=Im=", g2.getIm(), 2e-15);


  }
}