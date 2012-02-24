package atom.wf.coulomb;
import atom.wf.log_cr.TransLcrToR;
import atom.wf.log_r.TransLrToR;
import math.func.FuncVec;
import math.func.Func;
import math.integral.BooleQuadr;
import math.vec.Vec;
import math.Mathx;
import math.vec.grid.StepGrid;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 11/07/2008, Time: 14:43:54
 */
public class CoulombWFFactory extends FlowTest {
  public static Log log = Log.getLog(CoulombWFFactory.class);
  public CoulombWFFactory() {
    super(CoulombWFFactory.class);
  }

  // from Landay QM p.127:
  // The mean potential of the field created by the nucleus
  // and the electron in the ground state of the hydrogen atom.
  // V_1s(r) = (1/r+1) exp(-2r)
  // also see Y_0_1s(r)
  public static FuncVec makePotHy_1s(Vec r) {
    return new FuncVec(r, new PotFuncHy_1s());
  }

  // V_1s as felt by an electron
  public static FuncVec makePotHy_1s_e(Vec r) {
    FuncVec res = makePotHy_1s(r);
    res.mult(-1);
    return res;
  }

  // V_1s(r) = (1/r+1) exp(-2r)
  //  oo
  // INT  dr  V_1s sin^2(pr) = 1/4 * [ln(1+p^2) + p^2/(1+p^2)]
  // 0
  public static FuncVec calcBornDirHy_1s(Vec pArr) {
    // NOTE: This is NOT the scattering amplitude, just the integral
    return new FuncVec(pArr, new FuncBornDirHy_1s());
  }

  // from p.190 of Condon & Odabasi "Atomic Structure"
  // all INTL P^2 dr = 1;  INTL PP' dr = 0.
  public static FuncVec makeP1s(Vec r, double z) {
    return new FuncVec(r, new WFuncP1s(z));
  }
  public static FuncVec makeP2s(Vec r, double z) {
    return new FuncVec(r, new WFuncP2s(z));
  }
  public static FuncVec makeP2p(Vec r, double z) {
    return new FuncVec(r, new WFuncP2p(z));
  }
  public static FuncVec makeP3s(Vec r, double z) {
    return new FuncVec(r, new WFuncP3s(z));
  }
  public static FuncVec makeP3p(Vec r, double z) {
    return new FuncVec(r, new WFuncP3p(z));
  }
  public static FuncVec makeP3d(Vec r, double z) {
    return new FuncVec(r, new WFuncP3d(z));
  }
  public static FuncVec makeY_0_1s(Vec r) {
    return new FuncVec(r, new Y_0_1s());
  }
  public static FuncVec makeY_0_2s(Vec r) {
    return new FuncVec(r, new functorY_0_2s());
  }
  public static FuncVec makeY_0_2p(Vec r) {
    return new FuncVec(r, new FuncY_0_2p());
  }
  public static FuncVec makeY_2_2p(Vec r) {
    return new FuncVec(r, new FuncY_2_2p());
  }
  // Z(r, K) = INTL_0^r (s/r)^K * f(s)*f(s) ds
  public static FuncVec makeZ_0_1s(Vec r) {
    return new FuncVec(r, new Z_0_1s());
  }
  public static FuncVec makeZ_1_1s(Vec r) {
    return new FuncVec(r, new functorZ_1_1s());
  }

////////////////////////// TESTS    ///////////////////////////////////////

  public void testMakeP1s() throws Exception {
    int NUM_STEPS = 220;
    double FIRST = -4;
    double STEP = 1. / 16.;
    StepGrid x = new StepGrid(FIRST, NUM_STEPS, STEP);
    TransLcrToR logCR = new TransLcrToR(x);
    TransLrToR logR = new TransLrToR(x);
    Vec r = logR;
    BooleQuadr w = new BooleQuadr(x);
    FuncVec f = CoulombWFFactory.makeP1s(r, 1.);  // by log(r)
    double res = w.calc(f, f, r);
//    double res = FastLoop.dot(f, f, w, r);
    assertEquals(0, Math.abs(res - 1), 1e-5);
    f = CoulombWFFactory.makeP1s(logCR, 1.); // by log(c+r)
//      LOG.saveToFile(valarray.asArray(x), valarray.asArray(f), "wf", "coulP1s.csv");
//      LOG.saveToFile(valarray.asArray(x), valarray.asArray(logCR), "wf", "logCR.csv");
//      LOG.saveToFile(valarray.asArray(x), valarray.asArray(logCR.getCR()), "wf", "y.csv");
//    res = FastLoop.dot(f, f, w, logCR.getCR());
    res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 6e-13);
    f = CoulombWFFactory.makeP1s(logCR, 2.);
//    res = FastLoop.dot(f, f, w, logCR.getCR());
    res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 4e-12);
    f = CoulombWFFactory.makeP1s(logR, 2.);
//    res = FastLoop.dot(f, f, w, r);
    res = w.calc(f, f, r);
    assertEquals(0, Math.abs(res - 1), 1e-4);
  }
  public void testMakeP2sp() {
    int NUM_STEPS = 220;
    double FIRST = -4;
    double STEP = 1. / 16.;
    StepGrid x = new StepGrid(FIRST, NUM_STEPS, STEP);
//    LogCRToR logCR = new LogCRToR(x);
    TransLcrToR logCR = new TransLcrToR(x);
    Vec r = logCR;
    BooleQuadr w = new BooleQuadr(x);
    FuncVec f = CoulombWFFactory.makeP2s(r, 1.); // by log(c+r)
//    double res = FastLoop.dot(f, f, w, logCR.getCR());
    double res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 2e-13);
    FuncVec f2 = CoulombWFFactory.makeP1s(r, 1.);
//    res = FastLoop.dot(f, f2, w, logCR.getCR());
    res = w.calc(f, f2, logCR.getCR());
    assertEquals(0, Math.abs(res), 2e-13);
    f = CoulombWFFactory.makeP2s(r, 2.);
//    res = FastLoop.dot(f, f, w, logCR.getCR());
    res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 5e-13);
    f2 = CoulombWFFactory.makeP1s(r, 2.);
//    res = FastLoop.dot(f, f2, w, logCR.getCR());
    res = w.calc(f, f2, logCR.getCR());
    assertEquals(0, Math.abs(res), 2e-12);
    f = CoulombWFFactory.makeP2p(r, 1.);      // 2p
//    res = FastLoop.dot(f, f, w, logCR.getCR());
    res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 7e-14);
    f = CoulombWFFactory.makeP2p(r, 2.);    // 2p
//    res = FastLoop.dot(f, f, w, logCR.getCR());
    res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 3e-14);
  }
  public void testMakeP3spd() {
    int NUM_STEPS = 220;
    double FIRST = -4;
    double STEP = 1. / 16.;
    StepGrid x = new StepGrid(FIRST, NUM_STEPS, STEP);
    TransLcrToR logCR = new TransLcrToR(x);
    Vec r = logCR;
    BooleQuadr w = new BooleQuadr(x);
    FuncVec f = CoulombWFFactory.makeP3s(r, 1.); // by log(c+r)
//    double res = FastLoop.dot(f, f, w, logCR.getCR());
    double res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 2e-11);
    FuncVec f2 = CoulombWFFactory.makeP1s(r, 1.);
//    res = FastLoop.dot(f, f2, w, logCR.getCR());
    res = w.calc(f, f2, logCR.getCR());
    assertEquals(0, Math.abs(res), 1e-13);
    f2 = CoulombWFFactory.makeP2s(r, 1.);
//    res = FastLoop.dot(f, f2, w, logCR.getCR());
    res = w.calc(f, f2, logCR.getCR());
    assertEquals(0, Math.abs(res), 2e-12);
    f = CoulombWFFactory.makeP3s(r, 2.);
//    res = FastLoop.dot(f, f, w, logCR.getCR());
    res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 6e-12);
    f = CoulombWFFactory.makeP3p(r, 2.);
//    res = FastLoop.dot(f, f, w, logCR.getCR());
    res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 2e-12);
    f2 = CoulombWFFactory.makeP2p(r, 2.);
//    res = FastLoop.dot(f, f2, w, logCR.getCR());
    res = w.calc(f, f2, logCR.getCR());
    assertEquals(0, Math.abs(res), 3e-13);
    f = CoulombWFFactory.makeP3d(r, 2.);
//    res = FastLoop.dot(f, f, w, logCR.getCR());
    res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 1e-13);
  }
}

// V_1s(r) = (1/r+1) exp(-2r)
//       oo
//f(p)= INT dr  V_1s sin^2(pr) = 1/4 * [ln(1+p^2) + p^2/(1+p^2)]
//      0
class FuncBornDirHy_1s implements Func {
  public double calc(double p) {
    double p2 = p * p;
    double oneP2 = 1 + p2;
    return 0.25 * (Math.log(oneP2) + p2 / oneP2);
  }
}
// Y^0(1s, 1s; r) = 1-exp(-2r)*(1+r)
class Y_0_1s implements Func {
  public double calc(double r) {
    return 1. - Math.exp(-2. * r) * (1. + r);
  }
}
class Z_0_1s implements Func {
  // = INTL_0^r 4 s^2 exp(-2s) ds
  // INTL r^2 exp(ax) dx = exp(ax) (r^2/a - 2x/a^2 + 2/a^3)
  // a = -2
  public double calc(double r) {
    double a = -2;
    double ex = Math.exp(a * r);
    return 4. * ex * (r * r / a - 2. * r / (a * a)) + (ex - 1) * 8. / (a * a * a);
  }
}
class functorZ_1_1s implements Func {
  // = INTL_0^r 4 s/r * s^2 exp(-2s) ds
  // INTL x^3 exp(ax) dx = exp(ax)/a (x^3 - 3x^2/a + 6x/a^2 - 6/a^3)
  // a = -2
  public double calc(double r) {
    if (r == 0)
      return 0;
    double a = -2;
    double ex = Math.exp(a * r);
    double d2 = 24. / (r * Mathx.pow(a, 4));
    double d = 4. * ex / a * (r * r - 3. * r / a + 6. / (a * a));
    return d + (1. - ex) * d2;
  }
}
class functorY_0_2s implements Func {
  public double calc(double r) {
    //return 1. - exp(-r) * (1. + 3./4 * r + 1./4 * r * r + 1./8 * pow(r, 3));
    return 1. - Math.exp(-r) * (1. + 3. / 4 * r + 1. / 4 * r * r + 1. / 8 * Mathx.pow(r, 3));
  }
}
class FuncY_0_2p implements Func {
  public double calc(double r) {
    //return 1. - exp(-r) * (1. + 3./4 * r + 1./4 * r * r + 1./24 * pow(r, 3));
    return 1. - Math.exp(-r) * (1. + 3. / 4 * r + 1. / 4 * r * r + 1. / 24 * Mathx.pow(r, 3));
  }
}
class FuncY_2_2p implements Func {
  private static double LOW_R = 0.0001;
  public double calc(double r) {
    if (r == 0)
      return 0;
    if (r > LOW_R) {
      return 30. / r * ((1. - Math.exp(-r)) / r - Math.exp(-r) * (1. + 1. / 2. * r
        + 1. / 6. * Mathx.pow(r, 2) + 1. / 24. * Mathx.pow(r, 3) + 1. / 144. * Mathx.pow(r, 4)));
    }
    double  x = Math.exp(-r);
    double r2 = r * r;
    double r3 = r2 * r;
    double r4 = r3 * r;
    double r5 = r4 * r;
////    return 30. / r * (
////      (1. - x) / r
////        - x * (1. + 0.5 * r + 1. / 6. * r2 + 1. / 24. * r3 + 1. / 144. * r4) );
////    return 30. / r * x * (
////      (1./x - 1.) / r
////        - (1. + 0.5 * r + 1. / 6. * r2 + 1. / 24. * r3 + 1. / 144. * r4) );

    // exp(r) = 1 + r + r^2 / 2 + r^3/6 + r^4/24 + r^5/120 + r^6/720
    // (exp(r)-1)/r = 1 + r / 2 + r^2/6 + r^3/24 + r^4/120 + r^5/720

    return 30. / r * x * (r4 / 120. - r4 / 144. + r5 / 720.);

  }
}
class WFuncP1s implements Func {
  public static Log log = Log.getLog(WFuncP1s.class);
  protected final double z;
  public WFuncP1s(final double z) {
    this.z = z;
    if (z <= 0) {
      throw new IllegalArgumentException(log.error("invalid z=" + z));
    }
  }
  // INTL P^2 dr = 1.
  public double calc(double r) {
    if (r <= 0) {
      return 0;
    }
    double d = r * z;
    return 2. * Math.sqrt(z) * Math.exp(-d) * d;
  }
}
class PotFuncHy_1s implements Func {
  public double calc(double r) {
    if (r <= 0) {
      return 0;
    }
    return (1. / r + 1.) * Math.exp(-2. * r);
  }
}
class WFuncP2s extends WFuncP1s {
  public WFuncP2s(final double z) {
    super(z);
  }
  // INTL P^2 dr = 1.
  public double calc(double r) {
    if (r <= 0)
      return 0;
    double d = r * z * 0.5;
    return Math.sqrt(2 * z) * Math.exp(-d) * d * (1.0 - d);
  }
}
class WFuncP3s extends WFuncP1s {
  public WFuncP3s(final double z) {
    super(z);
  }
  public double calc(double r) {
    if (r <= 0)
      return 0;
    double d = r * z / 3.;
    return Math.sqrt(4. * z / 3.) * Math.exp(-d) * d * (1.0 - 2 * d + 2. * d * d / 3.);
  }
}
class WFuncP2p extends WFuncP1s {
  public WFuncP2p(final double z) {
    super(z);
  }
  public double calc(double r) {
    if (r <= 0)
      return 0;
    double d = r * z * 0.5;
    return Math.sqrt(2 * z / 3.) * Math.exp(-d) * d * d;
  }
}
class WFuncP3p extends WFuncP1s {
  public WFuncP3p(final double z) {
    super(z);
  }
  public double calc(double r) {
    if (r <= 0)
      return 0;
    double d = r * z / 3.;
    return 8. / 3. * Math.sqrt(z / 6.) * Math.exp(-d) * d * d * (1.0 - 0.5 * d);
  }
}
class WFuncP3d extends WFuncP1s {
  public WFuncP3d(final double z) {
    super(z);
  }
  public double calc(double r) {
    if (r <= 0)
      return 0;
    double d = r * z / 3.;
    return 4. / 3. * Math.sqrt(z / 30.) * Math.exp(-d) * d * d * d;
  }

}

/*
// J-Matrix v5
// by Dmitry Konovalov www.it.jcu.edu.au/~dmitry
//
#if !defined(ATOMIC_YK_H)
#define      ATOMIC_YK_H

#include "../debug/Test.h"
#include "FuncShell.h"
#include "../cpp/Functors.h"

namespace tomsk {

// from F.Fisher "The hartree-fock method for atoms"
// p236
// Y^0(1s, 1s; r) = 1-exp(-2r)*(1+r)
struct FY_0_1s : public CFunctor {
   virtual double operator()(double x) const {
      return 1. - exp(-2. * x) * (1. + x);
   }
};
struct FY_0_2s : public CFunctor {
   virtual double operator()(double x) const {
      return 1. - exp(-x) * (1. + 3./4 * x + 1./4 * x * x + 1./8 * pow(x, 3));
   }
};
struct FY_0_2p : public CFunctor {
   virtual double operator()(double x) const {
      return 1. - exp(-x) * (1. + 3./4 * x + 1./4 * x * x + 1./24 * pow(x, 3));
   }
};
struct FY_2_2p : public CFunctor {
   virtual double operator()(double x) const {
      return 30./(x * x) * (1. - exp(-x) * (1. + x + 1./2 * x * x
         + 1./6 * pow(x, 3) + 1./24 * pow(x, 4) + 1./144 * pow(x, 5)));
   }
};*/

