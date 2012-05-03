package atom.energy.test;
/** Copyright dmitry.konovalov@jcu.edu.au Date: 25/07/2008, Time: 17:19:55 */
import atom.wf.lcr.WFQuadrLcr;
import junit.framework.*;
import atom.wf.bspline.BSplBoundBasis;

import javax.utilx.log.Log;

import func.bspline.BSplOrthonBasis;
import func.bspline.BSplArr;
import func.bspline.BSplBasisFactory;
import math.integral.OrthonFactory;
import math.integral.Quadr;
import math.vec.Vec;
public class HMtrxLTest extends TestCase {
  public static Log log = Log.getLog(HMtrxLTest.class);
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
  public static Test suite() {
    TestSuite suite = new TestSuite();
//    suite.addTest(BSplBasisFactoryTest.suite());
//    suite.addTest(ZetaHyLCR.suite());
    suite.addTest(new TestSuite(HMtrxLTest.class));
    return suite;
  }
  public void testLoadEigVect() throws Exception {
    log.setDbg();
    log.setDbg();
    Log.getLog(BSplBoundBasis.class).setDebugOff();
    Log.getLog(BSplOrthonBasis.class).setDebugOff();
//    Log.getLog(StepGrid.class).setDebug();
//    Log.getLog(ConfArrFactoryE2.class).setDebug();
    Log.getLog(BSplArr.class).setDebugOff();
    Log.getLog(OrthonFactory.class).setDebugOff();

    double FIRST = -2;
    double LAST = 4;
    int BASIS_SIZE = 20;
    int N_BLOCKS_PER_KNOT = 2;
    // Do you really need to remove 2 last B-splines?
    BSplBoundBasis basis = BSplBasisFactory.makeDefaultLogCR(FIRST, LAST, BASIS_SIZE, N_BLOCKS_PER_KNOT);
    Quadr wCR2 = basis.getNormQuadr();
    WFQuadrLcr w = (WFQuadrLcr)basis.getRefQuadr();
    Vec logCR = w.getX();                                log.info("logCR grid=", logCR);


    // 30Jul08:  
//    Log.getLog(HMtrxL.class).setDebug();
//    int L = 0;
//    SlaterR slater = new SlaterR(w);
//    WFArrL wfBasis = new WFArrL(L, slater, basisN);
//    HMtrxL H = new HMtrxL(wfBasis, slater, zPot);
//    EigenSymm eig = H.eig();      log.debug("H=", new Vec(eig.getRealEVals()));

//    double Z_EFF = 1.;
//
//    // 22Jul08:  check <P_1s|P_1s>=1
//    TransLcrToR logCRToR = w.getLcrToR();
//    Vec r = logCRToR;
//    log.assertZero("r.getFirst()=", r.getFirst(), 6e-27);    // FIRST=0
//    log.info("r.getLast()=", r.getLast());
//    FuncVec f = CoulombWFFactory.makeRawP1s(r, Z_EFF);   log.debug("P_1s(r)=", f);
//    log.info("P_1s[0]=", f.getFirst());       log.info("P_1s.last=", f.getLast());
//    f.setX(logCR); log.debug("P_1s(x)=", f); // MUST change grid for derivatives
//    f.multSelf(logCRToR.getDivSqrtCR());  log.debug("F_1s=", f);// convert to F(x)=P(r)/sqrt(c+r)
//    log.info("P_1s[0]/sqrt(c+r)=F_1s=", f.getFirst());      log.info("P_1s.last/sqrt(c+r)=", f.getLast());
//    double res = wCR2.calc(f, f);
//    log.assertZero("Hy norm(LogCR)=", res - 1, 2e-11);    // FIRST=-2, LAST=10
//
//    // 18Jul08:  check <P_1s|H|P_1s>=-0.5
//    double test0 = -0.5;
//    double testKin = -test0;
//    double testPot = 2 * test0;
//    Ls LS = new Ls(L, Spin.ELECTRON);
//    Shell sh = new Shell(1, f, 1, LS.L, LS);
//    Conf fc = new Conf(sh);
//    SlaterLcr slater = new SlaterLcr(w);
//    SysE1 sys = new SysE1(-Z_EFF, slater);
//    double kin = sys.calcKin(fc, fc);
//    log.assertZero("Hy kin(LogCR)=", testKin - kin, 1e-11);
//    double pot = sys.calcPot(fc, fc);
//    log.assertZero("Hy pot(LogCR)=", testPot - pot, 8e-12);
//    log.assertZero("Hy kin/pot(LogCR)=", -2. - pot / kin, 6e-11);
//    res = sys.calcTot(fc, fc);
//    log.assertZero("Hy tot(LogCR)=", test0 - res, 2e-11);
//
//    // 22Jul08:  check P_1s(x)=SUM <fn|P_1s> fn(x)
//    Log.getLog(BSplOrthonBasis.class).setDebug();
//    FuncVec f2 = basisN.expand(f);
//    Vec diff = f2.copy();
//    diff.addMultSafe(-1, f);
//    log.saveToFile(VecToString.toCsv(logCR) + "\n" + VecToString.toCsv(f), ".", "wf", "F_1s.csv");
//    log.saveToFile(VecToString.toCsv(logCR) + "\n" + VecToString.toCsv(f2), ".", "wf", "F_1s_from_bspline.csv");
//    log.saveToFile(VecToString.toCsv(logCR) + "\n" + VecToString.toCsv(diff), ".", "wf", "F_1s_diff.csv");
//    log.saveToFile(basisN.toCSV(), ".", "wf", "BSplineBasis.csv");
//    assertEquals(0, Math.abs(DistMaxAbsErr.distSLOW(f, f2)), 2e-5);
  }
}