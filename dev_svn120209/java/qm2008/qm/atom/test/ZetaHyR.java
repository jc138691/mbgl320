package atom.test;
import atom.shell.Ls;
import atom.shell.Shell;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import javax.utilx.log.Log;

import math.vec.grid.StepGrid;
import math.vec.grid.test.StepGridTest;
import math.vec.Vec;
import math.func.FuncVec;
import atom.wf.coulomb.WfFactory;
import atom.wf.WFQuadrR;
import atom.angular.Spin;
import atom.energy.slater.SlaterR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 23/07/2008, Time: 10:29:06
 */
public class ZetaHyR extends TestCase {
  public static Log log = Log.getLog(ZetaHyR.class);
  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(StepGridTest.suite());
    suite.addTest(new TestSuite(ZetaHyR.class));
    return suite;
  }

  public static StepGrid makeStepGrid() {
    double FIRST = 0;
    double LAST = 20;
    int GRID_SIZE = 201;
    StepGrid r = new StepGrid(FIRST, LAST, GRID_SIZE);  log.dbg("r=", r);
    return r;
  }

  // 23Jul08:
  public void test_Hy_P_1s() {  // P - means P(r), F is for F(x={logCR,logR})
    log.setDbg();
    StepGrid r = makeStepGrid();
    WFQuadrR w = new WFQuadrR(r);

    // WF
    double Z = 1;
    int L = 0;
    FuncVec f = WfFactory.makeP1s(r, Z); log.dbg("P_1s=", f);
    double res = w.calc(f, f);

    log.assertZero("Hy norm(R)=", res - 1, 2e-6);
    double test0 = -0.5;
    double testKin = -test0;
    double testPot = 2 * test0;
    Ls LS = new Ls(L, Spin.ELECTRON);
    Shell sh = new Shell(0, f, L);

    SlaterR slater = new SlaterR(w);

    double kin = slater.calcOneKin(sh, sh);
    log.assertZero("Hy kin(R)=", testKin - kin, 2e-6);
    double pot = slater.calcOneZPot(-Z, sh, sh);
    log.assertZero("Hy pt(R)=", testPot - pot, 7e-7);
    log.assertZero("Hy pt/kin(R)=", -2. - pot / kin, 8e-6);
    res = kin + pot;
    log.assertZero("Hy tot(R)=", test0 - res, 3e-6);
  }

  // 30Jul08:
  public void testSlaterR() {  // P - means P(r), F is for F(x={logCR,logR})
    log.setDbg();
    StepGrid r = makeStepGrid();
    WFQuadrR w = new WFQuadrR(r);

    // WF
    double Z = 1;
    int L = 0;
    FuncVec f = WfFactory.makeP1s(r, Z); log.dbg("P_1s=", f);
    double res = w.calc(f, f);

    log.assertZero("Hy norm(R)=", res - 1, 2e-6);
    double test0 = -0.5;
    double testKin = -test0;
    double testPot = 2 * test0;
    SlaterR slater = new SlaterR(w);

    double kin = slater.calcKin(L, f, f);
    log.assertZero("Hy kin(R)=", testKin - kin, 2e-6);
    double pot = slater.calcZPot(-Z, f, f);
    log.assertZero("Hy pt(R)=", testPot - pot, 7e-7);

    Vec zPot = slater.getDivR();
    zPot.mult(-Z);                        log.dbg("zPot=", zPot);
    pot = slater.calcPot(zPot, f, f);
    log.assertZero("Hy pt(R)=", testPot - pot, 7e-7);

//    log.assertZero("Hy pt/kin(R)=", -2. - pt / kin, 8e-6);
//    res = kin + pt;
//    log.assertZero("Hy tot(R)=", test0 - res, 3e-6);
  }
}
