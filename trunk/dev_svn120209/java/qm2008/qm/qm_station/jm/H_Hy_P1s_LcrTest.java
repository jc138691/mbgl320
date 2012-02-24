package qm_station.jm;

import atom.energy.part_wave.PartHLcr;
import atom.wf.coulomb.CoulombWFFactory;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.simple.FuncPowInt;
import math.vec.Vec;
import math.vec.VecDbgView;
import project.workflow.task.test.FlowTest;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 15/02/2010, 1:08:26 PM
 */
public class H_Hy_P1s_LcrTest extends FlowTest {
  private static WFQuadrLcr w;
  public H_Hy_P1s_LcrTest(WFQuadrLcr w) {
    super(H_Hy_P1s_LcrTest.class);
    this.w = w;
  }
  public H_Hy_P1s_LcrTest() {
  }
  public void test_Hy_P_1s() {
    log.dbg("Testing numerics on the Hydrogen-like Hamiltonian");

    Vec r = w.getR();   log.dbg("r-grid = ", new VecDbgView(r));
    double Z = 1;       log.dbg("nuclear charge Z = ", Z);
    int L = 0;
    FuncVec f = CoulombWFFactory.makeP1s(r, Z); log.dbg("ground state P(r) = ", new VecDbgView(f));
    f.setX(w.getX()); // MUST change grid for derivatives
    f.mult(w.getDivSqrtCR());
    double normF = w.calcInt(f, f);
    assertEqualsRel("<P|P> = ", 1., normF, true);

    PartHLcr partH = new PartHLcr(w);
    double kinF = partH.calcKin(L, f, f);
    double totEng = -0.5;
    double kinEng = -totEng;
    double potEng = 2 * totEng;
    assertEqualsRel("kinetic eng <P|K|P> = ", kinEng, kinF, true);

    FuncVec pot = new FuncVec(r, new FuncPowInt(1., -1));
    pot.mult(-Z);
    double potF = partH.calcPot(f, f, pot);
    assertEqualsRel("Potential eng <P|-Z/r|P> = ", potEng, potF, true);

  }
}