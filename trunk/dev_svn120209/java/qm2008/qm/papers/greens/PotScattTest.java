package papers.greens;
import atom.wf.bspline.BSplBoundBasis;
import atom.wf.WFArrL;
import atom.wf.log_cr.TransLcrToR;
import atom.wf.log_cr.WFQuadrLcr;
import atom.wf.coulomb.CoulombWFFactory;
import atom.energy.slater.SlaterR;
import atom.energy.HMtrxL;
import atom.test.ZetaHyR;

import javax.utilx.log.Log;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import math.mtrx.jamax.EigenSymm;
import math.vec.Vec;
import math.vec.grid.StepGrid;
import math.integral.OrthonFactory;
import math.integral.Quadr;
import math.mtrx.test.MtrxTest;
import math.func.FuncVec;
import func.bspline.BSplOrthonBasis;
import func.bspline.BSplArr;
import func.bspline.BSplBasisFactory;
import scatt.partial.wf.SinPWaveR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 23/07/2008, Time: 10:10:32
 */
public class PotScattTest extends TestCase {
  public static Log log = Log.getLog(PotScattTest.class);
  private static final double H_GROUND_E = -0.5;
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(ZetaHyR.suite()); //MtrxTest
    suite.addTest(MtrxTest.suite());
    suite.addTest(new TestSuite(PotScattTest.class));
    return suite;
  }

  public void testFirstBornInLogCR() {  //    log.setDebug();
    double FIRST = -2;
    double LAST = Math.log(15);
    int BASIS_SIZE = 20;//20
    int N_BLOCKS_PER_KNOT = 2;
    // Do you really need to remove 2 last B-splines?
    BSplBoundBasis basis = BSplBasisFactory.makeDefaultLogCR(FIRST, LAST, BASIS_SIZE, N_BLOCKS_PER_KNOT);
    WFQuadrLcr w = (WFQuadrLcr)basis.getRefQuadr();
    WFQuadrLcr wCR2 = (WFQuadrLcr)basis.getNormQuadr();
    TransLcrToR logCRToR = w.getLcrToR();
    Vec r = logCRToR;
    Vec x = basis.getX();
    Vec V_1s = CoulombWFFactory.makePotHy_1s(r);  log.dbg("V_1s=", V_1s);

    double INC_P_FIRST = 0.1; // incident momentum
    double INC_P_LAST = 10;
    int N_INC_P = 11;
    StepGrid incP = new StepGrid(INC_P_FIRST, INC_P_LAST, N_INC_P);  log.dbg("incP=", incP);

    int L = 0;
    FuncVec arrB = CoulombWFFactory.calcBornDirHy_1s(incP);  log.dbg("arrB=", arrB);
    for (int i = 0; i < incP.size(); i++) {
      double testB = arrB.get(i);                log.dbg("testB=", testB);
      double p = arrB.getX().get(i);             log.dbg("p=", p);
      FuncVec upl = new SinPWaveR(r, p, L);   log.dbg("upl=", upl);
      upl.mult(logCRToR.getDivSqrtCR());  log.dbg("upl/sqrt(y)=", upl);// convert to F(x)=P(r)/sqrt(c+r)
      double calcB = wCR2.calc(V_1s, upl, upl);     log.info("calcB=", calcB);  log.info("calcB-testB=", calcB-testB);
      log.assertZero("calcB-testB=", calcB - testB, 8e-7);
    }
  }
  public void testFirstBornInR() {   //log.setDebug();
    double FIRST = 0;
    double LAST = 15;
    int BASIS_SIZE = 20;
    int N_BLOCKS_PER_KNOT = 2;
    // Do you really need to remove 2 last B-splines?
    BSplBoundBasis basis = BSplBasisFactory.makeDefaultR(FIRST, LAST, BASIS_SIZE, N_BLOCKS_PER_KNOT);
    Vec r = basis.getX();
    Vec V_1s = CoulombWFFactory.makePotHy_1s(r);  log.dbg("V_1s=", V_1s);
    Quadr w = basis.getNormQuadr();

    double INC_P_FIRST = 0.1; // incident momentum
    double INC_P_LAST = 10;
    int N_INC_P = 11;
    StepGrid incP = new StepGrid(INC_P_FIRST, INC_P_LAST, N_INC_P);  log.dbg("incP=", incP);

    int L = 0;
    FuncVec arrB = CoulombWFFactory.calcBornDirHy_1s(incP);  log.dbg("arrB=", arrB);
    for (int i = 0; i < incP.size(); i++) {
      double testB = arrB.get(i);                log.dbg("testB=", testB);
      double p = arrB.getX().get(i);             log.info("p=", p);
      FuncVec upl = new SinPWaveR(r, p, L);   log.dbg("upl=", upl);
      double calcB = w.calc(V_1s, upl, upl);     log.info("calcB=", calcB); log.info("calcB-testB=", calcB-testB);
      log.assertZero("calcB-testB", calcB - testB, 3e-4);
    }
  }
  public void testInR() {
    log.setDbg();
    Log.getLog(BSplBoundBasis.class).setDebugOff();
    Log.getLog(BSplOrthonBasis.class).setDebugOff();
    Log.getLog(StepGrid.class).setDbg();
//    Log.getLog(ConfArrFactoryE2.class).setDebug();
    Log.getLog(BSplArr.class).setDebugOff();
    Log.getLog(OrthonFactory.class).setDebugOff();

    // TODO: 23Jul08 check missing_tail=1,2 ; compare to LogCR
    double FIRST = 0;
    double LAST = 10;
    int BASIS_SIZE = 20;
//    int BASIS_SIZE = 15;
//    int BASIS_SIZE = 10;
    int N_BLOCKS_PER_KNOT = 2;
    // Do you really need to remove 2 last B-splines?
    BSplBoundBasis basis = BSplBasisFactory.makeDefaultR(FIRST, LAST, BASIS_SIZE, N_BLOCKS_PER_KNOT);
    Quadr w = basis.getNormQuadr();
    int L = 0;
    SlaterR slater = new SlaterR(w);
    Vec zPot = slater.getDivR();
    double Z_EFF = 1.;
    zPot.mult(-Z_EFF);                        log.dbg("zPot=", zPot);
    WFArrL wfBasis = new WFArrL(L, slater, basis);

    Log.getLog(HMtrxL.class).setDbg();
    HMtrxL H = new HMtrxL(wfBasis, slater, zPot);
    EigenSymm eig = H.eig();      log.dbg("H=", new Vec(eig.getRealEVals()));
    WFArrL eigVec = H.getEigVec();
    FuncVec grWF = eigVec.get(0);   // ground wf
    double kin = slater.calcKin(L, grWF, grWF);      log.info("kin=", kin);
    double pot = slater.calcPot(zPot, grWF, grWF);   log.info("pot=", pot);
    double E0 = kin + pot;                           log.info("E=kin+pot=", E0);
    log.assertZero("E0=", E0 - H_GROUND_E, 9e-7);
    double maxNormErr = eigVec.calcMaxOrthonErr();  log.info("maxNormErr=", maxNormErr);
    log.assertZero("maxNormErr", maxNormErr, 6e-15);


//    GMtrx G = new GMtrx(H);
////    maxNormErr = G.calcMaxOrthonErr();    log.info("maxNormErr=", maxNormErr);
////    log.assertZero("maxNormErr", maxNormErr, 3e-15);
//
//
//    double INC_E_FIRST = 0; // incident energy
//    double INC_E_LAST = 10;
//    int N_INC_E = 11;
//    StepGrid incE = new StepGrid(INC_E_FIRST, INC_E_LAST, N_INC_E);  log.dbg("incE=", incE);
//
//    KMtrx_todo kMtrx = new KMtrx_todo(H);
//    for (int i = 0; i < incE.size(); i++) {
//        double Ep = incE.get(i);     log.info("Ep=", Ep);
//        double KL = kMtrx.calcOneOver(Ep);  log.info("K_L amplitudes=", KL);
//    }
  }
}
