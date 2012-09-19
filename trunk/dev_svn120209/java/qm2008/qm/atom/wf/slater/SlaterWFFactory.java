package atom.wf.slater;

import atom.wf.coulomb.WfFactory;
import atom.wf.dcr.DcrFactory;
import atom.wf.dcr.TransDcrToR;
import atom.wf.lcr.LcrFactory;
import atom.wf.lcr.TransLcrToR;
import atom.wf.lcr.WFQuadrLcr;
import atom.wf.lr.TransLrToR;
import math.func.Func;
import math.func.FuncVec;
import math.integral.QuadrPts5;
import math.vec.Vec;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;
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
    return WfFactory.makeP1s(r, zeta);
  }

  public static FuncVec makeLcrP1s(WFQuadrLcr lcr, double zeta) {
    Vec r = lcr.getR();
    FuncVec res = SlaterWFFactory.makeP1s(r, zeta);
    res.multSelf(lcr.getDivSqrtCR());
    res.setX(lcr.getX()); // MUST change grid for derivatives
    return res;
  }

  public static FuncVec makeLcrP2s(WFQuadrLcr lcr, double zeta) {
    Vec r = lcr.getR();
    FuncVec res = SlaterWFFactory.makeP2s(r, zeta);
    res.multSelf(lcr.getDivSqrtCR());
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
    f.multSelf(quadr.getDivSqrtCR());
    double res = quadr.calcInt(f, f);
    assertEqualsRel("SlaterWFFactory.makeRawP2s(zeta=1) norm=", 1, res, true);

    f = SlaterWFFactory.makeP2s(r, 2.);
    f.multSelf(quadr.getDivSqrtCR());
    res = quadr.calcInt(f, f);
    assertEqualsRel("SlaterWFFactory.makeRawP2s(zeta=2) norm=", 1, res, true);

    f = SlaterWFFactory.makeP2s(r, 3.);
    f.multSelf(quadr.getDivSqrtCR());
    res = quadr.calcInt(f, f);
    assertEqualsRel("SlaterWFFactory.makeRawP2s(zeta=3) norm=", 1, res, true);
  }

  public void testMakeP2s() throws Exception {    //log.setDbg();
    int nX = 220;
    double firstX = -4.;
    double STEP = 1. / 16.;
    StepGrid x = new StepGrid(firstX, nX, STEP);  log.dbg("x=", x);
    TransLcrToR logCR = new TransLcrToR(x);    log.dbg("logCR=", logCR);
    TransLrToR logR = new TransLrToR(x);       log.dbg("logR=", logR);
    Vec r = logR;
    QuadrPts5 w = new QuadrPts5(x);

    double maxR = 50.;
    double firstX2 = -1;
    int nX2 = 421;
    StepGridOpt gridR = new StepGridOpt(0, maxR, -1);
    StepGridOpt optX = DcrFactory.makeDcrFromR(firstX2, nX2, gridR);
    StepGrid x2 = new StepGrid(optX);                 log.dbg("x2 =", x2);
    QuadrPts5 w2 = new QuadrPts5(x2);
    TransDcrToR dcr = new TransDcrToR(x2);      log.dbg("dcr=", dcr);
    Vec r2 = dcr;                              log.dbg("r2=", r2);

    FuncVec f = SlaterWFFactory.makeP2s(r, 1.);
    double res = w.calc(f, f, r);
    assertEquals(0, Math.abs(res - 1), 6e-10);

    f = SlaterWFFactory.makeP2s(logCR, 1.); // by log(c+r)
    res = w.calc(f, f, logCR.getCR());
    assertEquals(0, Math.abs(res - 1), 3e-14);

    // TODO: Do not use; Not as good as LCR!!!!!!!!!!!!!!!!!!!
    FuncVec f2 = SlaterWFFactory.makeP2s(r2, 1.);
    res = w2.calc(f2, f2, dcr.getCR2());
    assertEquals(0, Math.abs(res - 1), 5e-16);

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


