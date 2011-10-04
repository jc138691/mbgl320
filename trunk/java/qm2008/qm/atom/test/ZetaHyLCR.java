package atom.test;
import atom.e_1.SysE1;
import atom.energy.slater.SlaterLcr;
import atom.energy.slater.SlaterLr;
import atom.shell.Conf;
import atom.shell.Ls;
import atom.shell.Shell;
import atom.wf.log_cr.WFQuadrLcr;
import atom.wf.log_cr.TransLcrToR;
import atom.wf.log_r.TransLrToR;
import atom.wf.log_r.WFQuadrLr;
import atom.wf.coulomb.CoulombWFFactory;
import atom.wf.WFQuadrR;
import atom.angular.Spin;
import atom.energy.slater.SlaterR;
import atom.energy.ConfHMtrx;
import atom.energy.HMtrxFactory;
import math.func.FuncVec;
import math.mtrx.jamax.EigenSymm;
import math.vec.Vec;
import math.vec.grid.StepGrid;

import javax.utilx.log.Log;

import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/07/2008, Time: 14:01:09
 */
public class ZetaHyLCR extends LCRTestCase {
  public static Log log = Log.getLog(ZetaHyLCR.class);
  public static Test suite() {
    TestSuite suite = new TestSuite(ZetaHyLCR.class);
    return suite;
  }

  // 18Jul08:
  public void test_Hy_LogCRBasis() {
    log.setDbg();
//    Log.getLog(BSplBoundBasis.class).setDebug();
//    Log.getLog(BSplOrthonBasis.class).setDebug();
//    Log.getLog(StepGrid.class).setDebug();
//    Log.getLog(WFQuadrLcr.class).setDebug();
//    Log.getLog(SlaterLcr.class).setDebug();
//    Log.getLog(TransLcrToR.class).setDebug();
//    Log.getLog(ConfArrFactoryE2.class).setDebug();
//    Log.getLog(BSplArr.class).setDebug();

    double FIRST = -4;
    int GRID_SIZE = 441;
    int N_STEPS = GRID_SIZE - 1;
    double STEP = 1./50;
    double LAST = 4; //
    StepGrid logCR = new StepGrid(FIRST, LAST, GRID_SIZE);
//    StepGrid logCR = new StepGrid(FIRST, N_STEPS, STEP);

    WFQuadrLcr w = new WFQuadrLcr(logCR);

    double Z_EFF = 1.;

    // 18Jul08:  check <P_1s|P_1s>=1
    int L = 0;
    TransLcrToR logCRToR = w.getLcrToR();
    Vec r = logCRToR;
    FuncVec f = CoulombWFFactory.makeP1s(r, Z_EFF);   log.dbg("P_1s(r)=", f);
    f.setX(logCR); log.dbg("P_1s(x)=", f); // MUST change grid for derivatives
    f.mult(logCRToR.getDivSqrtCR());  log.dbg("F_1s=", f);// convert to F(x)=P(r)/sqrt(c+r)
    double res = w.getWithCR2().calc(f, f);
//    assertAndView("Hy norm(LogCR)=", res - 1, 3e-11);  // FIRST=-2, LAST=3
    log.assertZero("Hy norm(LogCR)=", res - 1, 6e-7);    // FIRST=-4, LAST=10

    // 18Jul08:  check <P_1s|H|P_1s>=-0.5
    double test0 = -0.5;
    double testKin = -test0;
    double testPot = 2 * test0;
    Ls LS = new Ls(L, Spin.ELECTRON);
    Shell sh = new Shell(0, f, L);
    Conf fc = new Conf(sh);
    SlaterLcr slater = new SlaterLcr(w);
    SysE1 sys = new SysE1(-Z_EFF, slater);
    double kin = sys.calcKin(fc, fc);
    log.assertZero("Hy kin(LogCR)=", testKin - kin, 4e-11);
    double pot = sys.calcPot(fc, fc);
    log.assertZero("Hy pot(LogCR)=", testPot - pot, 5e-12);
    log.assertZero("Hy kin/pot(LogCR)=", -2. - pot / kin, 2e-10);
    res = sys.calcTot(fc, fc);
    log.assertZero("Hy tot(LogCR)=", test0 - res, 3e-11);

    // 18Jul08:  check B-Splines
    int N_BASIS = 20;
    ConfHMtrx H = HMtrxFactory.makeFromBsplLogCR(FIRST, LAST, GRID_SIZE, N_BASIS, 0);
    EigenSymm eig = H.eig();
    log.dbg("H.eig()=", new Vec(eig.getRealEVals()));

  }
  public void test_Hy_1S_LogCR() {
    WFQuadrLcr w = new WFQuadrLcr(x);
    TransLcrToR logCRToR = w.getLcrToR();
    Vec r = logCRToR;

    // WF
    double Zeff = 1;
    int L = 0;
    FuncVec f = CoulombWFFactory.makeP1s(r, Zeff);
    f.setX(w.getX()); // MUST change grid for derivatives
    f.mult(logCRToR.getDivSqrtCR());
    double res = w.getWithCR2().calc(f, f);

    log.assertZero("Hy norm(LogCR)=", res - 1, 6e-13);
    double test0 = -0.5;
    double testKin = -test0;
    double testPot = 2 * test0;
    Ls LS = new Ls(L, Spin.ELECTRON);
    Shell sh = new Shell(0, f, L);
    Conf fc = new Conf(sh);
    SlaterLcr slater = new SlaterLcr(w);
    SysE1 sys = new SysE1(-1., slater);
    double kin = sys.calcKin(fc, fc);
    log.assertZero("Hy kin(LogCR)=", testKin - kin, 4e-11);
    double pot = sys.calcPot(fc, fc);
    log.assertZero("Hy pot(LogCR)=", testPot - pot, 5e-12);
    log.assertZero("Hy kin/pot(LogCR)=", -2. - pot / kin, 2e-10);
    res = sys.calcTot(fc, fc);
    log.assertZero("Hy tot(LogCR)=", test0 - res, 3e-11);
  }
  public void test_Hy_1S_LogR() {
    WFQuadrLr w = new WFQuadrLr(x);
    TransLrToR logRToR = w.getLrToR();
    Vec r = logRToR;

    // WF
    double Z = 1;
    int L = 0;
    FuncVec f = CoulombWFFactory.makeP1s(r, Z);
    f.setX(w.getX()); // MUST change grid for derivatives
    f.mult(logRToR.getDivSqrtR());
    double res = w.getWithR2().calc(f, f);

    log.assertZero("Hy norm(LogR)=", res - 1, 8e-6);
    double test0 = -0.5;
    double testKin = -test0;
    double testPot = 2 * test0;
    Ls LS = new Ls(L, Spin.ELECTRON);
    Shell sh = new Shell(0, f, L);
    Conf fc = new Conf(sh);
    SlaterLr slater = new SlaterLr(w);
    SysE1 sys = new SysE1(-Z, slater);
    double kin = sys.calcKin(fc, fc);
    log.assertZero("Hy kin(LogR)=", testKin - kin, 7e-4);
    double pot = sys.calcPot(fc, fc);
    log.assertZero("Hy pot(LogR)=", testPot - pot, 7e-4);
    log.assertZero("Hy pot/kin(LogR)=", -2. - pot / kin, 2e-3);
    res = sys.calcTot(fc, fc);
    log.assertZero("Hy tot(LogR)=", test0 - res, 4e-6);

    // With small R correction
    StepGrid smallR = new StepGrid(0, r.get(0), 9);
    f = CoulombWFFactory.makeP1s(smallR, Z);
    sh = new Shell(0, f, L);
    fc = new Conf(sh);
    WFQuadrR wR = new WFQuadrR(smallR);
    SlaterR slaterR = new SlaterR(wR);
    sys = new SysE1(-Z, slaterR);
    kin += sys.calcKin(fc, fc);
    log.assertZero("Hy kin(LogR+smallR)=", testKin - kin, 4e-11);
    pot += sys.calcPot(fc, fc);
    log.assertZero("Hy pot(LogR+smallR)=", testPot - pot, 5e-12);
    log.assertZero("Hy pot/kin(LogR+smallR)=", -2. - pot / kin, 2e-10);
    res += sys.calcTot(fc, fc);
    log.assertZero("Hy tot(LogR+smallR)=", test0 - res, 4e-11);
  }
}
