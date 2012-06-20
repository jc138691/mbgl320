package math.complex;
import flanagan.complex.Cmplx;
import math.Calc;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 1/09/2008, Time: 12:52:33
 */
public class Cmplx2F1 extends FlowTest {
  public static Log log = Log.getLog(Cmplx2F1.class);
  public Cmplx2F1() {
    super(Cmplx2F1.class);
  }
  public static Cmplx calc(Cmplx a, Cmplx b, Cmplx c, Cmplx z, double eps) {  log.dbg("a=", a); log.dbg("b=", b); log.dbg("c=", c); log.dbg("z=", z);
    if (z.equals(Cmplx.ONE)) {
      return calcOneZ(a, b, c, eps);       //z==1
    }

//    double abs2 = z.abs2();  log.dbg("z.abs2()=", abs2);
//    boolean aNegZ = a.isNegOrZeroInt();
//    boolean bNegZ = b.isNegOrZeroInt();
//    boolean cNegZ = c.isNegOrZeroInt();

    return calcSeries(a, b, c, z, eps);
  }
  public static Cmplx calc2(Cmplx a, Cmplx b, Cmplx c, Cmplx z, double eps) {  log.dbg("a=", a); log.dbg("b=", b); log.dbg("c=", c); log.dbg("z=", z);
    double abs2 = z.abs2();  log.dbg("z.abs2()=", abs2);
    if (z.equals(Cmplx.ONE)) {      //test
      return calcOneZ(a, b, c, eps);       //z==1
    }

    if (abs2 < 1) {
      return calcSeries(a, b, c, z, eps);
    }

//    if (abs2 > 1) {
//      return calcLargeZ(a, b, c, z, eps);
//    }

    if (abs2 == 1) {
      Cmplx zzOne = z.div(z.minus(Cmplx.ONE));       // z/(z-1)
//      if (zzOne.abs2() < 1) {
//        return calcZZOne_15_3_4(a, b, c, zzOne, eps);
//      }

//      } else if (zzOne.abs2() > 1) {
//        return calcZOneZ_15_3_4_large(a, b, c, z, eps);
//      }

      Cmplx oneZ = Cmplx.ONE.minus(z);     // 1-z
      if (oneZ.abs2() < 1) {
        return calcOneZ_15_3_6(a, b, c, z, eps);
      }

//      Cmplx oneOneZ = Cmplx.ONE.div(Cmplx.ONE.minus(z));    //1/(1-z)
//      if (oneZ.abs2() < 1) {
//        return calcOneOneZ_15_3_8(a, b, c, z, eps);
//      }



      throw new IllegalArgumentException(log.error("abs2 == 1"));
    }

    throw new IllegalArgumentException(log.error("z.equals(-Cmplx.ONE))"));
  }

  //  subroutine CmplxF1 (a, b, c, z, res, eps)
  // z.abs <= 1
  private static Cmplx calcSeries(Cmplx a, Cmplx b, Cmplx c, Cmplx z, double eps) {  log.dbg("a=", a); log.dbg("b=", b); log.dbg("c=", c); log.dbg("z=", z);
    Cmplx one = Cmplx.ONE;           //  one = dcmplx(1d0, 0d0)
    Cmplx res = new Cmplx(one);            //  res = one
    if (a.equals(Cmplx.ZERO)  ||  b.equals(Cmplx.ZERO) || z.equals(Cmplx.ZERO)) {
      return res;
    }

    boolean aNegZ = a.isNegOrZeroInt();
    boolean bNegZ = b.isNegOrZeroInt();
    boolean cNegZ = c.isNegOrZeroInt();
    if ((!aNegZ && !bNegZ) && z.abs2() > 1) {
      throw new IllegalArgumentException(log.error("calcSeries was called with ((!aNegZ && !bNegZ) && z.abs2() > 1)"));
    }
    if ((!aNegZ && !bNegZ) && cNegZ) {
      throw new IllegalArgumentException(log.error("calcSeries was called with ((!aNegZ && !bNegZ) && cNegZ)"));
    }
    if ((aNegZ || bNegZ) && cNegZ) {
      int ai = (int)a.getRe();
      int bi = (int)b.getRe();
      int ci = (int)c.getRe();
      if (ai <= ci  &&  bi <= ci) {
        throw new IllegalArgumentException(log.error("calcSeries was called with (ai <= ci  &&  bi <= ci)"));
      }
    }

    Cmplx f = new Cmplx(one);              //  f = one
    Cmplx t1 = a.times(b).times(z).div(c);   //  t1 = a * b * z / c
    Cmplx a1 = new Cmplx(a);               //  a1 = a
    Cmplx b1 = new Cmplx(b);               //  b1 = b
    Cmplx c1 = new Cmplx(c);               //  c1 = c
    double eps2 = eps * eps;
    while (t1.abs2() > eps2) {//  do while (real(abs(t1)) .gt. eps)
      res.addToSelf(t1); //     res = res + t1
      a1.addToSelf(one); //     a1 = a1 + one
      b1.addToSelf(one); //     b1 = b1 + one
      c1.addToSelf(one); //     c1 = c1 + one
      f.addToSelf(one);  //     f  = f  + one
      t1 = t1.mult(a1).mult(b1).times(z).div(c1.mult(f));   //log.dbg("t1=", t1); //     t1 = t1 * a1 * b1 * z / (c1 * f)
    }
    return res;
  }

      // Eq. 15.3.6      p373 of Russian A&S
  private static Cmplx calcZZOne_15_3_4(Cmplx a, Cmplx b, Cmplx c, Cmplx z, double eps) {
    Cmplx zOneZ = z.div(Cmplx.ONE.minus(z));

    Cmplx f  = calcSeries(a, c.minus(b), c, zOneZ, eps);   log.dbg("f=", f);
    Cmplx t = Cmplx.ONE.minus(z).pow(a.times(-1)); log.dbg("t=", t);

    return t.times(f);
  }
//      // Eq. 15.3.4      p373 of Russian A&S
//  private static Cmplx calcZOneZ_15_3_4_large(Cmplx a, Cmplx b, Cmplx c, Cmplx z, double eps) {
//    Cmplx zOneZ = z.div(Cmplx.ONE.minus(z));
//
//    Cmplx f  = calcLargeZ(a, c.minus(b), c, zOneZ, eps);   log.dbg("f=", f);
//    Cmplx t = Cmplx.ONE.minus(z).pow(a.mult(-1)); log.dbg("t=", t);
//
//    return t.mult(f);
//  }

    // Eq. 15.3.6      p373 of Russian A&S
  private static Cmplx calcOneZ_15_3_6(Cmplx a, Cmplx b, Cmplx c, Cmplx z, double eps) {
    Cmplx oneZ = Cmplx.ONE.minus(z);
    Cmplx gA = CmplxGamma.calc(a).log();
    Cmplx gB = CmplxGamma.calc(b).log();
    Cmplx gC = CmplxGamma.calc(c).log();
    Cmplx gCAB = CmplxGamma.calc(c.minus(a.plus(b))).log();
    Cmplx gCA = CmplxGamma.calc(c.minus(a)).log();
    Cmplx gABC = CmplxGamma.calc(a.plus(b).minus(c)).log();
    Cmplx gCB = CmplxGamma.calc(c.minus(b)).log();

    Cmplx f  = calcSeries(a, b, a.plus(b).minus(c).plus(Cmplx.ONE), oneZ, eps);   log.dbg("f=", f);
    Cmplx f2 = calcSeries(c.minus(a), c.minus(b), c.minus(a).minus(b).plus(Cmplx.ONE), oneZ, eps);   log.dbg("f2=", f2);

    Cmplx gCCAB = (gC.plus(gCAB).minus(gCA.plus(gCB))).exp();
    Cmplx gCABC = (gC.plus(gABC).minus(gA.plus(gB))).exp();

    Cmplx t  = gCCAB.times(f);  log.dbg("t=", t);
    Cmplx t2 = (oneZ.pow(c.minus(a.plus(b)))).times(gCABC.times(f2)); log.dbg("t2=", t2);

    return t.plus(t2);
  }
  // Eq. 15.3.7      p373 of Russian A&S
  private static Cmplx calcLargeZ(Cmplx a, Cmplx b, Cmplx c, Cmplx z, double eps) {
    Cmplx overZ = Cmplx.ONE.div(z);
    Cmplx minisZ = z.times(-1);
    Cmplx gA = CmplxGamma.calc(a).log();
    Cmplx gB = CmplxGamma.calc(b).log();
    Cmplx gC = CmplxGamma.calc(c).log();
    Cmplx gBA = CmplxGamma.calc(b.minus(a)).log();
    Cmplx gCA = CmplxGamma.calc(c.minus(a)).log();
    Cmplx gAB = CmplxGamma.calc(a.minus(b)).log();
    Cmplx gCB = CmplxGamma.calc(c.minus(b)).log();

    Cmplx f  = calcSeries(a, a.minus(c).plus(Cmplx.ONE), a.minus(b).plus(Cmplx.ONE), overZ, eps);   log.dbg("f=", f);
    Cmplx f2 = calcSeries(b, b.minus(c).plus(Cmplx.ONE), b.minus(a).plus(Cmplx.ONE), overZ, eps);   log.dbg("f2=", f2);

    Cmplx gCBA = (gC.plus(gBA).minus(gB.plus(gCA))).exp();
    Cmplx gCAB = (gC.plus(gAB).minus(gA.plus(gCB))).exp();

    Cmplx t  = (minisZ.pow(a.times(-1))).times(gCBA.times(f));  log.dbg("t=", t);
    Cmplx t2 = (minisZ.pow(b.times(-1))).times(gCAB.times(f2)); log.dbg("t2=", t2);

    return t.plus(t2);
  }
  private static Cmplx calcOneZ(Cmplx a, Cmplx b, Cmplx c, double eps) {
    Cmplx gC = CmplxGamma.calc(c).log();
    Cmplx gCAB = CmplxGamma.calc(c.minus(a.plus(b))).log();
    Cmplx gCA = CmplxGamma.calc(c.minus(a)).log();
    Cmplx gCB = CmplxGamma.calc(c.minus(b)).log();
    Cmplx res = (gC.plus(gCAB).minus(gCA.plus(gCB))).exp();    log.dbg("res=", res);
    return res;
  }

  public void testCalc() throws Exception {
    Cmplx res, res2, res3;
    Cmplx one = new Cmplx(1);
    Cmplx two = new Cmplx(2);

    // F_21(a, b; c; z) = F_21(1, 1; 2; z) = -z^(-1) ln(1-z)   // p.370 Abr&Steg
    Cmplx a = new Cmplx(1, 0);
    Cmplx b = new Cmplx(1, 0);
    Cmplx c = new Cmplx(2, 0);
    double zRe = 0.5;
    Cmplx z = new Cmplx(zRe, 0);

    res = Cmplx2F1.calc(a, b, c, z, Calc.EPS_10);
    double corr = -Math.log(1. - zRe) / zRe;
    log.assertZero("F_21(1, 1; 2; z=0.5)=", corr - res.getRe(), 2e-10);

    z = new Cmplx(0.5);
    log.assertZero("F_21(1, 1; 1; z) err=", calcErrTestOne(z), 2e-10);

    z = new Cmplx(-0.5);
    log.assertZero("F_21(1, 1; 1; z) err=", calcErrTestOne(z), 2e-10);
    z = new Cmplx(0, 0.5);
    log.assertZero("F_21(1, 1; 1; z) err=", calcErrTestOne(z), 2e-10);
    z = new Cmplx(0, -0.5);
    log.assertZero("F_21(1, 1; 1; z) err=", calcErrTestOne(z), 2e-10);
    z = new Cmplx(0.5, 0.5);
    log.assertZero("F_21(1, 1; 1; z) err=", calcErrTestOne(z), 2e-10);

    z = new Cmplx(-0.5, 0.8);
    log.assertZero("F_21(1, 1; 1; z) err=", calcErrTestOne(z), 2e-10);
    z = new Cmplx(0.3, -0.2);
    log.assertZero("F_21(1, 1; 1; z) err=", calcErrTestOne(z), 2e-10);


    a = new Cmplx(1.1);
    b = new Cmplx(-0.1);
    c = new Cmplx(10.1);
    z = new Cmplx(0.5);
    res = calcOneZ_15_3_6(a, b, c, z, Calc.EPS_11);
    res2 = calcSeries(a, b, c, z, Calc.EPS_11);
    log.assertZero("calcOneZ_15_3_6 err=", (res.minus(res2)).abs(), Calc.EPS_10);

    z = new Cmplx(0.5, 0.5);
    res = calcOneZ_15_3_6(a, b, c, z, Calc.EPS_11);
    res2 = calcSeries(a, b, c, z, Calc.EPS_11);
    log.assertZero("calcOneZ_15_3_6 err=", (res.minus(res2)).abs(), Calc.EPS_10);

    a = new Cmplx(0.0, 10.217647);
    b = new Cmplx(21.0, 0.0);
    c = new Cmplx(22.0, 10.217647);
    double x = 0.924819;
    double y = Math.sqrt(1 - x*x);
    z = new Cmplx(x, y);

//    res = Cmplx2F1.calc(a, b, c, z, Calc.EPS_10);

//    z = new Cmplx(0, 1.5);
//    log.assertZero("F_21(1, 1; 1; z) err=", calcErrTestOne(z), 2e-10);
//    z = new Cmplx(2, 0);
//    log.assertZero("F_21(1, 1; 1; z) err=", calcErrTestOne(z), 2e-10);

  }

//http://mathworld.wolfram.com/HypergeometricFunction.html
  // 2F1(1,1;1;z) = 1/(1-z)
  private static double calcErrTestOne(Cmplx z) {
    Cmplx one = new Cmplx(1);
    Cmplx res = Cmplx2F1.calc(one, one, one, z, Calc.EPS_10);
    Cmplx corr = Cmplx.ONE.div(Cmplx.ONE.minus(z));
    return Math.abs(res.getRe() - corr.getRe()) + Math.abs(res.getIm() - corr.getIm());
  }
}


// original from tools_jm_lib.f
//c-------------------------------------------------------------------------
//  subroutine CmplxF1 (a, b, c, z, res, eps)
//  real*8  eps
//  complex*16 a, b, c, z, t1, a1, b1, c1, f, one, res
//  one = dcmplx(1d0, 0d0)
//  res = one
//c
//c     If  z.gt.1, there is another formular with (1/z)^n, but it diverges!!
//  f = one
//  t1 = a * b * z / c
//  a1 = a
//  b1 = b
//  c1 = c
//  do while (real(abs(t1)) .gt. eps)
//     res = res + t1
//     a1 = a1 + one
//     b1 = b1 + one
//     c1 = c1 + one
//     f  = f  + one
//     t1 = t1 * a1 * b1 * z / (c1 * f)
//  end do
//  return
//  end
//c------------------------------------------------------------
