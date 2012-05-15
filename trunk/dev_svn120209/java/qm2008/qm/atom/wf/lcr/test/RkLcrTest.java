package atom.wf.lcr.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 09:31:25 */
import atom.wf.lcr.RkLcr;
import atom.wf.lcr.TransLcrToR;
import atom.wf.lcr.WFQuadrLcr;
import atom.wf.lcr.YkLcr;
import atom.wf.coulomb.CoulombWFFactory;
import atom.wf.mm.HkMm;
import math.vec.grid.StepGrid;
import math.vec.Vec;
import math.vec.metric.DistMaxAbsErr;
import math.func.FuncVec;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

public class RkLcrTest extends FlowTest {
  public static Log log = Log.getLog(RkLcrTest.class);
  public RkLcrTest() {
    super(RkLcrTest.class);  // NOTE!!! this is needed for FlowTest
  }
  public void testRkLcr() throws Exception {
    double FIRST = -4;
    int NUM_STEPS = 220;
    double STEP = 1. / 16.;
    StepGrid x = new StepGrid(FIRST, NUM_STEPS, STEP);
    TransLcrToR xToR = new TransLcrToR(x);
    Vec r = xToR;
//      QuadrPts5 w = new QuadrPts5(x);
    WFQuadrLcr wCR = new WFQuadrLcr(x);

    FuncVec f = CoulombWFFactory.makeP1s(r, 1.);
    f.multSelf(xToR.getDivSqrtCR());
    double res = 0, rMm = 0;
    res = wCR.getWithCR2().calc(f, f);
    assertEquals(0, Math.abs(res - 1), 6e-13);

    FuncVec f2 = CoulombWFFactory.makeP1s(r, 1.);
    f2.multSelf(xToR.getDivSqrtCR());
    f2.mult(2.); // NOTE! Checking diff norm
    res = wCR.getWithCR2().calc(f2, f2);
    assertEquals(0, Math.abs(res - 4), 3e-12);

    FuncVec T = CoulombWFFactory.makeY_0_1s(r); // valid
    FuncVec Y = new YkLcr(wCR, f, f, 0).calcYk();
//    assertEquals(0, Math.abs(T.distSLOW(Y)), 2e-9);
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)), 2e-9);
    res = RkLcr.calc(wCR, f, f, f, f, 0);
    // from p236 of C.F.Fischer
    assertEquals(0, Math.abs(res - 5. / 8.), 5e-11);

    res = RkLcr.calc(wCR, f2, f, f, f, 0);  // note f2
    assertEquals(0, Math.abs(res - 2. * 5. / 8.), 9e-11);

    res = RkLcr.calc(wCR, f, f2, f, f, 0);  // note f2
    assertEquals(0, Math.abs(res - 2. * 5. / 8.), 9e-11);

    res = RkLcr.calc(wCR, f, f, f2, f, 0);  // note f2
    assertEquals(0, Math.abs(res - 2. * 5. / 8.), 9e-11);

    res = RkLcr.calc(wCR, f, f, f, f2, 0);  // note f2
    assertEquals(0, Math.abs(res - 2. * 5. / 8.), 9e-11);

    HkMm hkMm  = new HkMm(wCR, f, f, f, f, 0);
    rMm = hkMm.calcPot2();
    assertEquals(0, Math.abs(rMm - 5. / 8.), 2e-10); // todo: was 5e-11???????

    FuncVec f2s = CoulombWFFactory.makeP2s(r, 1.);
    f2s.multSelf(xToR.getDivSqrtCR());
    res = wCR.getWithCR2().calc(f2s, f2s);
    assertEquals(0, Math.abs(res - 1), 2e-13);

    res = RkLcr.calc(wCR, f, f2s, f, f2s, 0);
    assertEquals(0, Math.abs(res - 17. / 81), 4e-11);

    res = RkLcr.calc(wCR, f2s, f, f2s, f, 0);
    assertEquals(0, Math.abs(res - 17. / 81), 4e-11);
    res = RkLcr.calc(wCR, f, f2s, f2s, f, 0);
    assertEquals(0, Math.abs(res - 16. / 729), 5e-11);
    res = RkLcr.calc(wCR, f2s, f, f, f2s, 0);
    assertEquals(0, Math.abs(res - 16. / 729), 5e-11);

    res = RkLcr.calc(wCR, f2s, f2s, f2s, f2s, 0);
    hkMm  = new HkMm(wCR, f2s, f2s, f2s, f2s, 0);
    rMm = hkMm.calcPot2();
    assertEquals(0, Math.abs(res - 77. / 512), 4e-10);
    assertEquals(0, Math.abs(rMm - 77. / 512), 3e-10);

    FuncVec f2p = CoulombWFFactory.makeP2p(r, 1.);
    f2p.multSelf(xToR.getDivSqrtCR());
    res = wCR.getWithCR2().calc(f2p, f2p);
    assertEquals(0, Math.abs(res - 1), 7e-14);

    res = RkLcr.calc(wCR, f2p, f, f2p, f, 0);
    assertEquals(0, Math.abs(res - 59. / 243), 3e-11);
    res = RkLcr.calc(wCR, f, f2p, f, f2p, 0);
    assertEquals(0, Math.abs(res - 59. / 243), 3e-11);
    res = RkLcr.calc(wCR, f2p, f, f, f2p, 1);
    assertEquals(0, Math.abs(res - 112. / 2187), 2e-10);
    res = RkLcr.calc(wCR, f2s, f2p, f2s, f2p, 0);
    assertEquals(0, Math.abs(res - 83. / 512), 2e-10);
    res = RkLcr.calc(wCR, f2p, f2s, f2p, f2s, 0);
    assertEquals(0, Math.abs(res - 83. / 512), 2e-10);
    res = RkLcr.calc(wCR, f2s, f2p, f2p, f2s, 1);
    assertEquals(0, Math.abs(res - 45. / 512), 9e-10);
    res = RkLcr.calc(wCR, f2p, f2p, f2p, f2p, 0);
    assertEquals(0, Math.abs(res - 93. / 512), 1e-10);
    res = RkLcr.calc(wCR, f2p, f2p, f2p, f2p, 1);
    assertEquals(0, Math.abs(res - 0.12044270784428178), 2e-20); // at x=-4, 1/16, 220
    res = RkLcr.calc(wCR, f2p, f2p, f2p, f2p, 2);
    assertEquals(0, Math.abs(res - 0.08789062488574255), 2e-20); // at x=-4, 1/16, 220
  }
}