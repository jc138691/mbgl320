package atom.e_2.test;

import atom.angular.Spin;
import atom.data.clementi.AtomHeClementi;
import atom.e_2.SysAtomE2;
import atom.e_2.SysHe;
import atom.energy.slater.SlaterLcr;
import atom.shell.*;
import atom.wf.coulomb.CoulombWFFactory;
import atom.wf.lcr.WFQuadrLcr;
import math.func.FuncVec;
import math.vec.Vec;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;

/**
 * dmitry.d.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,07/12/2010,11:16:37 AM
 */
public class HeClementiZetaTest extends FlowTest {
  public static Log log = Log.getLog(HeClementiZetaTest.class);
  private static WFQuadrLcr quadr;
  public HeClementiZetaTest(WFQuadrLcr w) {
    super(HeClementiZetaTest.class);
    quadr = w;
  }
  public HeClementiZetaTest() {      // needed by FlowTest
    super(HeClementiZetaTest.class);
  }
  public void testClementiSingleZeta() throws Exception  {
    Vec r = quadr.getR();
    double effZ = AtomHeClementi.ZETA;// from p445 of Clementi Roetti, Atomic Data 14, 177 (1974)
    FuncVec f = CoulombWFFactory.makeP1s(r, effZ);
    f.mult(quadr.getDivSqrtCR());
    f.setX(quadr.getX()); // MUST change grid for derivatives
    double res = quadr.calcInt(f, f);
    assertEqualsRel("norm=<1s|1s>=", 1, res, true);

    // Making He(1s^2)
    Ls LS = new Ls(0, Spin.SINGLET);
    Shell sh = new Shell(1, f, 2, LS.getL(), LS);
    ShPair fc = new ShPair(sh);
    SlaterLcr slater = new SlaterLcr(quadr);
    SysAtomE2 sys = new SysHe(slater);  // NOTE: Dec2010, switching from old SysE2_OLD to SysAtomE2
    double kin = sys.calcKin(fc, fc);
//    assertEquals(0, Math.abs(AtomHeClementi.E_ZETA_KIN - kin), 2e-11);
    assertEqualsRel("AtomHeClementi.E_ZETA_KIN", AtomHeClementi.E_ZETA_KIN, kin, true);

    double pot = sys.calcPot(fc, fc);
//    assertEquals(0, Math.abs(AtomHeClementi.E_ZETA_POT - pot), 2e-11);
    assertEqualsRel("AtomHeClementi.E_ZETA_POT", AtomHeClementi.E_ZETA_POT, pot, true);
//    assertEquals(0, Math.abs(-2. - pot / kin), 2e-11);
    assertEqualsRel("pot / kin=2=", -2, pot / kin, true);

    res = sys.calcTot(fc, fc);
//    assertEquals(0, Math.abs(AtomHeClementi.E_ZETA_TOT - res), 3e-11);
    assertEqualsRel("AtomHeClementi.E_ZETA_TOT", AtomHeClementi.E_ZETA_TOT, res, true);
  }
}