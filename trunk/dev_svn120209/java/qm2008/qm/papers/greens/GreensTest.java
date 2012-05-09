package papers.greens;
import atom.energy.slater.SlaterLcr;
import atom.shell.Conf;
import atom.shell.Ls;
import atom.shell.Shell;
import atom.wf.lcr.TransLcrToR;
import atom.wf.lcr.WFQuadrLcr;
import atom.wf.coulomb.CoulombWFFactory;
import atom.wf.bspline.BSplBoundBasis;
import func.bspline.BSplBasisFactory;
import func.bspline.BSplOrthonBasis;
import func.bspline.BSplArr;
import func.bspline.test.BSplBasisFactoryTest;
import atom.angular.Spin;
import atom.test.ZetaHyLCR;
import atom.e_1.SysE1;

import javax.utilx.log.Log;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import math.vec.VecToString;
import math.vec.grid.test.StepGridTest;
import math.vec.Vec;
import math.vec.metric.DistMaxAbsErr;
import math.func.FuncVec;
import math.integral.OrthonFactory;
import math.integral.Quadr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 17/07/2008, Time: 12:45:02
 */
//public class GreensTest extends BSplLogCRBasisTest { //TestCase
public class GreensTest extends TestCase { //
  public static Log log = Log.getLog(GreensTest.class);
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(StepGridTest.suite());
    suite.addTest(BSplBasisFactoryTest.suite());
    suite.addTest(ZetaHyLCR.suite());
    suite.addTest(new TestSuite(GreensTest.class));
    return suite;
  }

  public void testLogCRBasis_completness() {
    log.setDbg();
    log.setDbg();
    Log.getLog(BSplBoundBasis.class).setDebugOff();
    Log.getLog(BSplOrthonBasis.class).setDebugOff();
//    Log.getLog(StepGrid.class).setDebug();
//    Log.getLog(WFQuadrLcr.class).setDebug();
//    Log.getLog(SlaterLcr.class).setDebug();
//    Log.getLog(TransLcrToR.class).setDebug();
//    Log.getLog(ConfArrFactoryE2.class).setDebug();
    Log.getLog(BSplArr.class).setDebugOff();
    Log.getLog(OrthonFactory.class).setDebugOff();

    double FIRST = -2;
    double LAST = 4;
    int BASIS_SIZE = 20;
//    int BASIS_SIZE = 15;
//    int BASIS_SIZE = 10;
    int N_BLOCKS_PER_KNOT = 2;
    // Do you really need to remove 2 last B-splines?
    BSplBoundBasis basis = BSplBasisFactory.makeDefaultLogCR(FIRST, LAST, BASIS_SIZE, N_BLOCKS_PER_KNOT);
    Quadr wCR2 = basis.getNormQuadr();
    WFQuadrLcr w = (WFQuadrLcr)basis.getRefQuadr();
    Vec logCR = w.getX();                                log.info("logCR grid=", logCR);
//    log.info("logCR.getFirst()=", logCR.getFirst());       log.info("logCR.getLast()=", logCR.getLast());

    double Z_EFF = 1.;

    // 22Jul08:  check <P_1s|P_1s>=1
    int L = 0;
    TransLcrToR logCRToR = w.getLcrToR();
    Vec r = logCRToR;
    log.assertZero("r.getFirst()=", r.getFirst(), 6e-27);    // FIRST=0
    log.info("r.getLast()=", r.getLast());
    FuncVec f = CoulombWFFactory.makeP1s(r, Z_EFF);   log.dbg("P_1s(r)=", f);
    log.info("P_1s[0]=", f.getFirst());       log.info("P_1s.last=", f.getLast());
    f.setX(logCR); log.dbg("P_1s(x)=", f); // MUST change grid for derivatives
    f.mult(logCRToR.getDivSqrtCR());  log.dbg("F_1s=", f);// convert to F(x)=P(r)/sqrt(c+r)
    log.info("P_1s[0]/sqrt(c+r)=F_1s=", f.getFirst());      log.info("P_1s.last/sqrt(c+r)=", f.getLast());
    double res = wCR2.calc(f, f);
    log.assertZero("Hy norm(LogCR)=", res - 1, 2e-11);    // FIRST=-2, LAST=10

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
    log.assertZero("Hy kin(LogCR)=", testKin - kin, 1e-11);
    double pot = sys.calcPot(fc, fc);
    log.assertZero("Hy pot(LogCR)=", testPot - pot, 8e-12);
    log.assertZero("Hy kin/pot(LogCR)=", -2. - pot / kin, 6e-11);
    res = sys.calcTot(fc, fc);
    log.assertZero("Hy tot(LogCR)=", test0 - res, 2e-11);

    // 22Jul08:  check P_1s(x)=SUM <fn|P_1s> fn(x)
    Log.getLog(BSplOrthonBasis.class).setDbg();
    FuncVec f2 = basis.expand(f);
    Vec diff = f2.copy();
    diff.addMultSafe(-1, f);
    log.saveToFile(VecToString.toString(logCR) + "\n" + VecToString.toString(f), ".", "wf", "F_1s.csv");
    log.saveToFile(VecToString.toString(logCR) + "\n" + VecToString.toString(f2), ".", "wf", "F_1s_from_bspline.csv");
    log.saveToFile(VecToString.toString(logCR) + "\n" + VecToString.toString(diff), ".", "wf", "F_1s_diff.csv");
    log.saveToFile(basis.toCSV(), ".", "wf", "BSplineBasis.csv");
    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(f, f2)), 2e-5);

  }
}