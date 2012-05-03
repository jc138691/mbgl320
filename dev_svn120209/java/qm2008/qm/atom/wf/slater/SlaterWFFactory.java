package atom.wf.slater;

import atom.wf.coulomb.CoulombWFFactory;
import atom.wf.lcr.TransLcrToR;
import atom.wf.lcr.WFQuadrLcr;
import atom.wf.log_r.TransLrToR;
import math.func.Func;
import math.func.FuncVec;
import math.integral.QuadrStep5;
import math.vec.Vec;
import math.vec.grid.StepGrid;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,07/12/2010,1:33:38 PM
 */
public class SlaterWFFactory extends FlowTest {
  public static Log log = Log.getLog(SlaterWFFactory.class);
  private static WFQuadrLcr quadr;

  public SlaterWFFactory(WFQuadrLcr w) {
    super(SlaterWFFactory.class);
    quadr = w;
  }

  public SlaterWFFactory() {
    super(SlaterWFFactory.class);
  }

  public static FuncVec makeP1s(Vec r, double zeta) {
    return CoulombWFFactory.makeP1s(r, zeta);
  }

  public static FuncVec makeLcrP1s(WFQuadrLcr lcr, double zeta) {
    Vec r = lcr.getR();
    FuncVec res = SlaterWFFactory.makeP1s(r, zeta);
    res.mult(lcr.getDivSqrtCR());
    res.setX(lcr.getX()); // MUST change grid for derivatives
    return res;
  }

  public static FuncVec makeLcrP2s(WFQuadrLcr lcr, double zeta) {
    Vec r = lcr.getR();
    FuncVec res = SlaterWFFactory.makeP2s(r, zeta);
    res.mult(lcr.getDivSqrtCR());
    res.setX(lcr.getX()); // MUST change grid for derivatives
    return res;
  }

  public static FuncVec makeP2s(Vec r, double z) {
    return new FuncVec(r, new SlaterWFuncP2s(z));
  }


  //////////////////////////////////  TESTS ///////////////////////////////////

  public void testUsrP2s() throws Exception {
    Vec r = quadr.getR();

    FuncVec f = SlaterWFFactory.makeP2s(r, 1.);
    f.mult(quadr.getDivSqrtCR());
    double res = quadr.calcInt(f, f);
    assertEqualsRel("SlaterWFFactory.makeRawP2s(zeta=1) norm=", 1, res, true);

    f = SlaterWFFactory.makeP2s(r, 2.);
    f.mult(quadr.getDivSqrtCR());
    res = quadr.calcInt(f, f);
    assertEqualsRel("SlaterWFFactory.makeRawP2s(zeta=2) norm=", 1, res, true);

    f = SlaterWFFactory.makeP2s(r, 3.);
    f.mult(quadr.getDivSqrtCR());
    res = quadr.calcInt(f, f);
    assertEqualsRel("SlaterWFFactory.makeRawP2s(zeta=3) norm=", 1, res, true);
  }

  public void testMakeP2s() throws Exception {
    int NUM_STEPS = 220;
    double FIRST = -4;
    double STEP = 1. / 16.;
    StepGrid x = new StepGrid(FIRST, NUM_STEPS, STEP);
    TransLcrToR logCR = new TransLcrToR(x);
    TransLrToR logR = new TransLrToR(x);
    Vec r = logR;
    QuadrStep5 w = new QuadrStep5(x);
    FuncVec f = SlaterWFFactory.makeP2s(r, 1.);
    double res = w.calc(f, f, r);
    assertEquals(0, Math.abs(res - 1), 6e-10);
    f = SlaterWFFactory.makeP2s(logCR, 1.); // by log(c+r)
    res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 3e-14);
    f = SlaterWFFactory.makeP2s(logCR, 2.);
    res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 7e-14);
    f = SlaterWFFactory.makeP2s(logR, 2.);
    res = w.calc(f, f, r);
    assertEquals(0, Math.abs(res - 1), 2e-8);
  }

}

class SlaterWFuncP2s implements Func {
  public static Log log = Log.getLog(SlaterWFuncP2s.class);
  protected final double zet;

  public SlaterWFuncP2s(final double zet) {
    this.zet = zet;
    if (zet <= 0) {
      throw new IllegalArgumentException(log.error("invalid zet=" + zet));
    }
  }
  // INTL P^2 dr = 1.

  public double calc(double r) {
    if (r <= 0) {
      return 0;
    }
    double d = r * zet;
    // see http://en.wikipedia.org/wiki/Slater-type_orbital
    return 4. * Math.sqrt(zet / 12.) * Math.exp(-d) * d * d;
  }
}


