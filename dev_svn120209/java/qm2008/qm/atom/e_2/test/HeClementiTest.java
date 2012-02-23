package atom.e_2.test;

import Jama.EigenvalueDecomposition;
import atom.AtomUtil;
import atom.angular.Spin;
import atom.data.clementi.AtomHeClementi;
import atom.e_2.SysAtomE2;
import atom.e_2.SysE2_OLD;
import atom.e_2.SysHe;
import atom.energy.ConfHMtrx;
import atom.energy.slater.SlaterLcr;
import atom.shell.*;
import atom.wf.coulomb.CoulombWFFactory;
import atom.wf.log_cr.TransLcrToR;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.integral.OrthonFactory;
import math.vec.Vec;
import math.vec.grid.StepGrid;
import project.workflow.task.test.FlowTest;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.JmLgrrOrthLcr;
/**
 * Created by Dmitry.A.Konovalov@gmail.com, 16/02/2010, 11:12:27 AM
 */
public class HeClementiTest extends FlowTest {
  public HeClementiTest() {      // needed by FlowTest
    super(HeClementiTest.class);
  }
//  public static Test suite() {
//    return new TestSuite(HeClementiTest.class);
//  }
//  public static void main(String[] args) {
//    junit.textui.TestRunner.run(suite());
//  }
//  public void setUp() {
//    Log.addGlobal(System.out);
//  }
  public void testClementiSingleZeta() throws Exception  {
    double FIRST = 0;
    int NUM_STEPS = 361;
    double LAST = 3; // exp(7) = 1096
    StepGrid x = new StepGrid(FIRST, LAST, NUM_STEPS);
    WFQuadrLcr w = new WFQuadrLcr(x);
    TransLcrToR xToR = w.getLcrToR();
    Vec r = xToR;

    // from p445 of Clementi Roetti, Atomic Data 14, 177 (1974)
    double Zeff = AtomHeClementi.ZETA;// from p445 of Clementi Roetti, Atomic Data 14, 177 (1974)
    FuncVec f = CoulombWFFactory.makeP1s(r, Zeff);
    f.setX(w.getX()); // MUST change grid for derivatives
    f.mult(xToR.getDivSqrtCR());
    double res = w.calcOverlap(f, f);
    assertEquals(0, Math.abs(res - 1), 3e-11);
    Ls LS = new Ls(0, Spin.SINGLET);
    Shell sh = new Shell(1, f, 2, LS.getL(), LS);
    ShPair fc = new ShPair(sh);
    SlaterLcr slater = new SlaterLcr(w);

    SysE2_OLD sys = new SysE2_OLD(-2., slater);
    double kin = sys.calcKin(fc, fc);
    assertEquals(0, Math.abs(AtomHeClementi.E_ZETA_KIN  - kin), 2e-11);

    SysAtomE2 sys2 = new SysHe(slater);
    double kin2 = sys2.calcKin(fc, fc);
    assertEquals(kin, kin2, 1e-11);

    double pot = sys.calcPot(fc, fc);
    assertEquals(0, Math.abs(AtomHeClementi.E_ZETA_POT - pot), 2e-11);
    assertEquals(0, Math.abs(-2. - pot / kin), 2e-11);

    double pot2 = sys2.calcPot(fc, fc);
    assertEquals(pot, pot2, 1e-11);

    res = sys.calcTot(fc, fc);
    assertEquals(0, Math.abs(AtomHeClementi.E_ZETA_TOT - res), 3e-11);
  }
  public void testClementiLimitOneConfig() throws Exception  {
//    LOG.setTrace(true);
    double FIRST = 0;
    int NUM_STEPS = 601;
    double LAST = 4; // exp(7) = 1096
    StepGrid x = new StepGrid(FIRST, LAST, NUM_STEPS);
    WFQuadrLcr w = new WFQuadrLcr(x);
//    TransLcrToR xToR = w.getLcrToR();
    int L = 0;
    double Zeff = AtomHeClementi.ZETA;// from p445 of Clementi Roetti, Atomic Data 14, 177 (1974)
    double lambda = 2. * Zeff;
    int N = 10;
    LgrrModel model = new LgrrModel();
    model.setL(L);
    model.setLambda((float)lambda);
    model.setN(N);
    FuncArr arr = new JmLgrrOrthLcr(w, model);
    AtomUtil.trimTailSLOW(arr);
    
    OrthonFactory.log.setDbg();
    double res = OrthonFactory.calcMaxOrthonErr(arr, w.getWithCR2());

    assertEquals(0, res, 2e-8);
    SlaterLcr slater = new SlaterLcr(w);
    Ls LS = new Ls(0, Spin.SINGLET);
    SysE2_OLD sys = new SysE2_OLD(-2., slater);

    // One config hartree-fock limit
    double kin = 2.8617128;// from Clementi, p185
    double pot = -5.7233927;// from Clementi, p185
    double tot = -2.8616799;
    assertEquals(kin + pot, tot, 6e-22);
    ConfArr basis = ConfArrFactoryE2.makeTwoElecSameN(LS, N, arr);
    ConfHMtrx H = new ConfHMtrx(basis, sys);
    EigenvalueDecomposition eig = H.eig();
//    LOG.report(this, "H=" + Vec.toCsv(eig.getRealEigenvalues()));
    double e0 = eig.getRealEigenvalues()[0];
//    LOG.report(this, "\nkin+pot="
//      + (kin + pot) + "\n   e[0]=" + e0);
    assertEquals(0, Math.abs(-2.8615628084911 - e0), 3e-6); //
    assertEquals(0, e0 - tot, 2e-4);
//    FuncVec conf = H.calcDensity(eig, 0);
//      LOG.saveToFile(valarray.asArray(x), valarray.asArray(conf), "wf", "He_ground_density.csv");
//    res = FastLoop.dot(conf, w);
//    assertEquals(2, res, 3e-15);
  }
}