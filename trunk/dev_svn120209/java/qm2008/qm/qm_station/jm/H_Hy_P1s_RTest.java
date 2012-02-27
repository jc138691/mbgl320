package qm_station.jm;
import atom.energy.part_wave.PotHR;
import project.workflow.task.test.FlowTest;
import math.vec.VecDbgView;
import math.vec.Vec;
import math.func.FuncVec;
import math.func.simple.FuncPowInt;
import atom.wf.coulomb.CoulombWFFactory;
import atom.wf.WFQuadrR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 16:31:01
 */
public class H_Hy_P1s_RTest extends FlowTest {
  private static WFQuadrR w;
  public H_Hy_P1s_RTest(WFQuadrR w) {
    super(H_Hy_P1s_RTest.class);
    this.w = w;
  }
  public H_Hy_P1s_RTest() {
    super(H_Hy_P1s_RTest.class);
  }
  public void test_Hy_P_1s() {
    log.dbg("Testing numerics on the Hydrogen-like Hamiltonian");

    Vec r = w.getX();   log.dbg("r-grid = ", new VecDbgView(r));
    double Z = 1;       log.dbg("nuclear charge Z = ", Z);
    int L = 0;
    FuncVec f = CoulombWFFactory.makeP1s(r, Z); log.dbg("ground state P(r) = ", new VecDbgView(f));
    double normF = w.calc(f, f);
    assertEqualsRel("<P|P> = ", 1., normF, true);

    PotHR partH = new PotHR(w);
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

