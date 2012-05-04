package atom.wf.lcr.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 14:29:19 */
import atom.wf.lcr.TransLcrToR;
import atom.wf.lcr.WFQuadrLcr;
import atom.wf.lcr.YkLcr;
import atom.wf.coulomb.CoulombWFFactory;
import math.integral.QuadrPts5;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.metric.DistMaxAbsErr;
import math.vec.grid.StepGrid;
import math.func.FuncVec;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;
public class YkLcrTest extends FlowTest {
  public static Log log = Log.getLog(YkLcrTest.class);
  public YkLcrTest() {
    super(YkLcrTest.class);  // NOTE!!! this is needed for FlowTest
  }
  public void testZ_1() throws Exception { // Z = 1
    log.setDbg();
    double FIRST = -4;
    int NUM_STEPS = 220;
    double STEP = 1. / 16.;
    StepGrid x = new StepGrid(FIRST, NUM_STEPS, STEP);
    TransLcrToR xToR = new TransLcrToR(x);
    Vec r = xToR;
    QuadrPts5 w = new QuadrPts5(x);
    WFQuadrLcr wCR = new WFQuadrLcr(x);
    FuncVec f = CoulombWFFactory.makeP1s(r, 1.);
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(f), ".", "wf", "P1s_test.csv");
    double res = w.calc(f, f, xToR.getCR());
    assertEquals(0, Math.abs(res - 1), 6e-13);
    f.mult(xToR.getDivSqrtCR());
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(f), ".", "wf", "P1s_sqrtCR.csv");
    res = w.calc(f, f, xToR.getCR2());
    assertEquals(0, Math.abs(res - 1), 5.5e-13);

    res = wCR.getWithCR2().calc(f, f);
    assertEquals(0, Math.abs(res - 1), 5.5e-13);
    FuncVec T = CoulombWFFactory.makeZ_1_1s(r); // valid
    FuncVec Z = new YkLcr(wCR, f, f, 1).calcZk();
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(T), ".", "wf", "Z_1_1s_test.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(Z), ".", "wf", "Z_1_1s.csv");

    log.info("f_1s=", new VecDbgView(f));
    log.info("r=", new VecDbgView(r));
    log.info("x=", new VecDbgView(xToR.getX()));
    log.info("T=", new VecDbgView(T));
    log.info("Z=", new VecDbgView(Z));
//    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Z)), 6e-9);  // with calcZk_OLD
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Z)), 2e-9);  // with new calcZk() via

    int dbg = 1;
  }
  public void testZkLogCR() {
    log.setDbg();
    double FIRST = -4;
//      int NUM_STEPS = 440;
//      double STEP = 1./32.;
    int NUM_STEPS = 220;
    double STEP = 1. / 16.;
    StepGrid x = new StepGrid(FIRST, NUM_STEPS, STEP);
    WFQuadrLcr quadr = new WFQuadrLcr(x);                  log.dbg("x weights =", quadr);
    TransLcrToR xToR = new TransLcrToR(x);
    Vec r = xToR;
    QuadrPts5 w = new QuadrPts5(x);
    FuncVec f = CoulombWFFactory.makeP1s(r, 1.);
    double res = w.calc(f, f, xToR.getCR());
    assertEquals(0, Math.abs(res - 1), 6e-13);
    f.mult(xToR.getDivSqrtCR());
    FuncVec T = CoulombWFFactory.makeZ_0_1s(r); // valid
    FuncVec Z = new YkLcr(quadr, f, f, 0).calcZk();
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(r), ".", "wf", "logCR.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(T), ".", "wf", "Z_0_1s_test.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(Z), ".", "wf", "Z_0_1s.csv");
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Z)), 6e-9);
  }
  public void testYkLogCR() {
    log.setDbg();
    double FIRST = -4;
    int NUM_STEPS = 220;
    double STEP = 1. / 16.;
    StepGrid x = new StepGrid(FIRST, NUM_STEPS, STEP);
    WFQuadrLcr quadr = new WFQuadrLcr(x);                  log.dbg("x weights =", quadr);
    TransLcrToR xToR = new TransLcrToR(x);
    Vec r = xToR;
    QuadrPts5 w = new QuadrPts5(x);

    // 1s
    FuncVec f = CoulombWFFactory.makeP1s(r, 1.);
    f.mult(xToR.getDivSqrtCR());
    double res = w.calc(f, f, xToR.getCR2());
    assertEquals(0, Math.abs(res - 1), 2e-10);
    FuncVec T = CoulombWFFactory.makeY_0_1s(r); // valid
    FuncVec Y = new YkLcr(quadr, f, f, 0).calcYk();
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(r), ".", "wf", "logCR.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(T), ".", "wf", "Y_0_1s_test.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(Y), ".", "wf", "Y_0_1s.csv");
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)), 2e-9);

    // 1s-2s
    FuncVec f2 = CoulombWFFactory.makeP2s(r, 1.);
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(f2), ".", "wf", "P2s_test.csv");
    f2.mult(xToR.getDivSqrtCR());
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(f2), ".", "wf", "P2s_sqrtCR.csv");
    res = w.calc(f, f2, xToR.getCR2());
    assertEquals(0, Math.abs(res), 2e-13);
    Y = new YkLcr(quadr, f, f2, 0).calcYk();
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(Y), ".", "wf", "Y_0_1s2s.csv");

    // 2s
    f = CoulombWFFactory.makeP2s(r, 1.);
//    res = FastLoop.dot(f, f, w, xToR.getCR());
    res = w.calc(f, f, xToR.getCR());
    assertEquals(0, Math.abs(res - 1), 2e-13);
    f.mult(xToR.getDivSqrtCR());
    T = CoulombWFFactory.makeY_0_2s(r); // valid
    Y = new YkLcr(quadr, f, f, 0).calcYk();
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(T), ".", "wf", "Y_0_2s_test.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(Y), ".", "wf", "Y_0_2s.csv");
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)), 2e-8);

    // 2p
    f = CoulombWFFactory.makeP2p(r, 1.);
    res = w.calc(f, f, xToR.getCR());
    assertEquals(0, Math.abs(res - 1), 7e-14);
    f.mult(xToR.getDivSqrtCR());
    T = CoulombWFFactory.makeY_0_2p(r); // valid
    Y = new YkLcr(quadr, f, f, 0).calcYk();
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(T), ".", "wf", "Y_0_2p_test.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(Y), ".", "wf", "Y_0_2p.csv");
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)), 6e-9);
  }
  public void testY_2() {    log.setDbg();
    double FIRST = -4;
    int NUM_STEPS = 220;
    double STEP = 1. / 16.;
    StepGrid x = new StepGrid(FIRST, NUM_STEPS, STEP);
    WFQuadrLcr quadr = new WFQuadrLcr(x);                  log.dbg("x weights =", quadr);
    TransLcrToR xToR = new TransLcrToR(x);
    Vec r = xToR;
    QuadrPts5 w = new QuadrPts5(x);

    // 2p
    FuncVec f = CoulombWFFactory.makeP2p(r, 1.);
    double res = w.calc(f, f, xToR.getCR());
    assertEquals(0, Math.abs(res - 1), 7e-14);
    f.mult(xToR.getDivSqrtCR());
    FuncVec T = CoulombWFFactory.makeY_2_2p(r); // valid
    FuncVec Y = new YkLcr(quadr, f, f, 2).calcYk();
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(T), ".", "wf", "Y_2_2p_test.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(Y), ".", "wf", "Y_2_2p.csv");
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)), 5e-8);
  }
}