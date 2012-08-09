package atom.wf.lcr.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 14:29:19 */
import atom.wf.coulomb.WfFactory;
import atom.wf.lcr.*;
import atom.wf.lcr.yk.YkLcr;
import atom.wf.lcr.yk.YkLcrV1;
import atom.wf.lcr.yk.YkLcrV2;
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
private FuncVec tZk, cZk, nZk, tYk, cYk, nYk;
public YkLcrTest() {
  super(YkLcrTest.class);  // NOTE!!! this is needed for FlowTest
}
public void testZ_1() throws Exception { // atomZ = 1
  log.setDbg();
  double FIRST = -4;
  int NUM_STEPS = 220;
  double STEP = 1. / 16.;
  StepGrid x = new StepGrid(FIRST, NUM_STEPS, STEP);
  TransLcrToR xToR = new TransLcrToR(x);
  Vec r = xToR;
  QuadrPts5 w = new QuadrPts5(x);
  WFQuadrLcr wCR = new WFQuadrLcr(x);
  FuncVec f = WfFactory.makeP1s(r, 1.);
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(f), ".", "wf", "P1s_test.csv");
  double res = w.calc(f, f, xToR.getCR());
  assertEquals(0, Math.abs(res - 1), 6e-13);
  f.multSelf(xToR.getDivSqrtCR());
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(f), ".", "wf", "P1s_sqrtCR.csv");
  res = w.calc(f, f, xToR.getCR2());
  assertEquals(0, Math.abs(res - 1), 5.5e-13);

  res = wCR.getWithCR2().calc(f, f);
  assertEquals(0, Math.abs(res - 1), 5.5e-13);
  tZk = WfFactory.makeZ_1_1s(r); // valid
  cZk = new YkLcrV1(wCR, f, f, 1).calcZk();
  nZk = new YkLcrV2(wCR, f, f, 1).calcZk();
//    FuncVec newZk = new YkLcr(wCR, f, f, 1).calcZk_DBG();
//  FuncVec oZk = new YkLcr(wCR, f, f, 1).calcZk_v1();
  log.info("newZk=", new VecDbgView(nZk));
//  log.info("oldZ=", new VecDbgView(oZk));
//    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(newZk, oldZ)), 1e-10);  // with new calcZk() via
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(trueZk), ".", "wf", "Z_1_1s_test.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(atomZ), ".", "wf", "Z_1_1s.csv");

  log.info("f_1s=", new VecDbgView(f));
  log.info("r=", new VecDbgView(r));
  log.info("x=", new VecDbgView(xToR.getX()));
  log.info("trueZk=", new VecDbgView(tZk));
  log.info("calcZk=", new VecDbgView(cZk));
  assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(tZk, cZk)), 6e-9);  // with calcZk_v1
  log.info("trueZk=", new VecDbgView(tZk));
  log.info("newZk=", new VecDbgView(nZk));
  assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(tZk, nZk)), 6e-9);  // with calcZk_v1

  int dbg = 1;
}
public void testZkLcr() {
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
  FuncVec f = WfFactory.makeP1s(r, 1.);
  double res = w.calc(f, f, xToR.getCR());
  assertEquals(0, Math.abs(res - 1), 6e-13);
  f.multSelf(xToR.getDivSqrtCR());
  tZk = WfFactory.makeZ_0_1s(r); // valid
  cZk = new YkLcrV1(quadr, f, f, 0).calcZk();
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(r), ".", "wf", "logCR.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(T), ".", "wf", "Z_0_1s_test.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(atomZ), ".", "wf", "Z_0_1s.csv");
  log.info("trueZk=", new VecDbgView(tZk));
  log.info("calcZk=", new VecDbgView(cZk));
  assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(tZk, cZk)), 6e-9); // was 6e-9 with old

  nZk = new YkLcrV2(quadr, f, f, 0).calcZk();
  log.info("trueZk=", new VecDbgView(tZk));
  log.info("calcZk=", new VecDbgView(cZk));
  log.info("newZk=", new VecDbgView(nZk));
  assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(tZk, nZk)), 6e-9); // was 6e-9 with old

  int dbgStop = 1;
}
public void testYkLcr() {
  log.setDbg();
  double FIRST = -4;
  int NUM_STEPS = 220;
//    int NUM_STEPS = 440;
  double STEP = 1. / 16.;
//    double STEP = 1. / 32.;
  StepGrid x = new StepGrid(FIRST, NUM_STEPS, STEP);
  WFQuadrLcr quadr = new WFQuadrLcr(x);                  log.dbg("x weights =", quadr);
  TransLcrToR xToR = new TransLcrToR(x);
  Vec r = xToR;
  QuadrPts5 w = new QuadrPts5(x);

  // 1s
  FuncVec f = WfFactory.makeP1s(r, 1.);
  f.multSelf(xToR.getDivSqrtCR());
  double res = w.calc(f, f, xToR.getCR2());
  assertEquals(0, Math.abs(res - 1), 2e-10);
  tYk = WfFactory.makeY_0_1s(r); // valid
  cYk = new YkLcrV1(quadr, f, f, 0).calcYk();
  nYk = new YkLcrV2(quadr, f, f, 0).calcYk();
  log.info("tYk = WfFactory.makeY_0_1s(r)=");
  log.info("tYk=", new VecDbgView(tYk));
  log.info("cYk=", new VecDbgView(cYk));
  log.info("nYk=", new VecDbgView(nYk));
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(r), ".", "wf", "logCR.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(T), ".", "wf", "Y_0_1s_test.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(Y), ".", "wf", "Y_0_1s.csv");
  assertEquals(0, DistMaxAbsErr.distSLOW(tYk, cYk), 2e-9);
  assertEquals(0, DistMaxAbsErr.distSLOW(tYk, nYk), 2e-9);
//    assertEquals(0, DistMaxAbsErr.distSLOW(T, Y), 4e-8); // TODO: was 2e-9

  // 1s-2s
  FuncVec f2 = WfFactory.makeP2s(r, 1.);
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(f2), ".", "wf", "P2s_test.csv");
  f2.multSelf(xToR.getDivSqrtCR());
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(f2), ".", "wf", "P2s_sqrtCR.csv");
  res = w.calc(f, f2, xToR.getCR2());
  assertEquals(0, Math.abs(res), 2e-13);
  cYk = new YkLcrV1(quadr, f, f2, 0).calcYk();
  nYk = new YkLcrV2(quadr, f, f2, 0).calcYk();
  assertEquals(0, DistMaxAbsErr.distSLOW(cYk, nYk), 2e-9);
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(Y), ".", "wf", "Y_0_1s2s.csv");

  // 2s
  f = WfFactory.makeP2s(r, 1.);
//    res = FastLoop.dot(f, f, w, xToR.getCR());
  res = w.calc(f, f, xToR.getCR());
  assertEquals(0, Math.abs(res - 1), 2e-13);
  f.multSelf(xToR.getDivSqrtCR());
  tYk = WfFactory.makeY_0_2s(r); // valid
  cYk = new YkLcr(quadr, f, f, 0).calcYk();
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(T), ".", "wf", "Y_0_2s_test.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(Y), ".", "wf", "Y_0_2s.csv");
  assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(tYk, cYk)), 2e-8);

  // 2p
  f = WfFactory.makeP2p(r, 1.);
  res = w.calc(f, f, xToR.getCR());
  assertEquals(0, Math.abs(res - 1), 7e-14);
  f.multSelf(xToR.getDivSqrtCR());
  tYk = WfFactory.makeY_0_2p(r); // valid
  cYk = new YkLcr(quadr, f, f, 0).calcYk();
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(T), ".", "wf", "Y_0_2p_test.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(Y), ".", "wf", "Y_0_2p.csv");
  assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(tYk, cYk)), 6e-9);
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
  FuncVec f = WfFactory.makeP2p(r, 1.);
  double res = w.calc(f, f, xToR.getCR());
  assertEquals(0, Math.abs(res - 1), 7e-14);
  f.multSelf(xToR.getDivSqrtCR());
  FuncVec T = WfFactory.makeY_2_2p(r); // valid
  FuncVec Y = new YkLcr(quadr, f, f, 2).calcYk();
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(T), ".", "wf", "Y_2_2p_test.csv");
//    log.saveToFile(VecToString.toCsv(x) + "\n" + VecToString.toCsv(Y), ".", "wf", "Y_2_2p.csv");
  assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(T, Y)), 5e-8);
}
}