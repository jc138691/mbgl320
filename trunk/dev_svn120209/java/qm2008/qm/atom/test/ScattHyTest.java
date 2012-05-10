package atom.test;
import atom.e_2.SysE2OldOk;
import atom.energy.ConfHMtrx;
import atom.energy.slater.SlaterLcr;
import atom.shell.ConfArr;
import atom.shell.Ls;
import atom.wf.lcr.WFQuadrLcr;
import junit.framework.Test;
import junit.framework.TestSuite;
import math.vec.grid.StepGrid;
import math.vec.Vec;
import math.func.arr.FuncArr;
import math.integral.OrthonFactory;
import math.mtrx.jamax.EigenSymm;
import atom.angular.Spin;
import atom.shell.ConfArrFactoryE2;
import func.bspline.BSplBasisFactory;
import func.bspline.BSplOrthonBasis;
import atom.wf.lcr.test.BSplLogCRBasisTest;
import atom.wf.bspline.BSplBoundBasis;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/07/2008, Time: 10:51:53
 */
public class ScattHyTest extends BSplLogCRBasisTest {
  public static Log log = Log.getLog(ScattHyTest.class);
  public static Test suite() {
    return new TestSuite(ScattHyTest.class);
  }
  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
  public void testLimitNS() {
    log.setDbg();
    Log.getLog(BSplBoundBasis.class).setDbg();
    Log.getLog(BSplOrthonBasis.class).setDbg();
//    Log.getLog(BSplArr.class).setDebug();

    double FIRST = -2;
    int NUM_STEPS = 441;
    double LAST = 2; //
    StepGrid logCR = new StepGrid(FIRST, LAST, NUM_STEPS);
    WFQuadrLcr w = new WFQuadrLcr(logCR);
    int k = 5;
    int N = 20;
    FuncArr arr = BSplBasisFactory.makeFromBasisSize(w, N, k);
    double normErr = OrthonFactory.calcMaxOrthonErr(arr, w);
    assertEquals(0, normErr, 3.e-15);
    SlaterLcr slater = new SlaterLcr(w);
    Ls S1 = new Ls(0, Spin.SINGLET);
    SysE2OldOk sys = new SysE2OldOk(-2., slater);

    // Multi-Config Hartree-Fock with 1s2+...+4s2
    double tot = -2.878990; // from p.164 of Froese-Fischer
    int L = 0;
    ConfArr basis = ConfArrFactoryE2.makeTwoElec(S1, N, L, arr);
    ConfHMtrx H = new ConfHMtrx(basis, sys);
    EigenSymm eig = H.eig();
//    LOG.report(this, "H=" + Vec.toCsv(eig.getRealEigenvalues()));
    log.dbg("H=", new Vec(eig.getRealEVals()));
    double e0 = eig.getRealEVals()[0];
//    LOG.report(this, "Multi-Config Hartree-Fock with 1s2+...+4s2"
//      + "\ntot =" + tot
//      + "\ne[0]=" + e0);
    log.dbg("Multi-Config Hartree-Fock with 1s2+...+4s2"
      + "\ntot =" + tot
      + "\ne[0]=" + e0);
    assertEquals(0, Math.abs(e0 - tot), 6e-4);
  }
}

