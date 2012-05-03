package scatt.jm_2008.jm.laguerre.lcr;
import atom.energy.part_wave.PotHLcr;
import atom.wf.lcr.WFQuadrLcr;
import project.workflow.task.test.FlowTest;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;
import atom.wf.coulomb.CoulombWFFactory;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.func.FuncVec;
import math.func.simple.FuncPowInt;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 21/11/2008, Time: 16:55:24
 */
public class PartHMtrxLCRTest extends FlowTest {
  private static WFQuadrLcr w;
  public PartHMtrxLCRTest(WFQuadrLcr w) {
    super(PartHMtrxLCRTest.class);
    this.w = w;
  }
  public PartHMtrxLCRTest() {
  }
  public void test_Hy_P_1s() {
    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();

    log.dbg("Testing LCR numerics on the Hydrogen-like Hamiltonian");

    Vec r = w.getR();   log.dbg("r-grid = ", new VecDbgView(r));
    double Z = 1;       log.dbg("nuclear charge Z = ", Z);
    int L = 0;
    FuncVec f = CoulombWFFactory.makeP1s(r, Z); log.dbg("ground state P(r) = ", new VecDbgView(f));
    w.transRToLCR(f);                           log.dbg("in LCR P(x(r)) = ", new VecDbgView(f));
    double normF = w.getWithCR2().calc(f, f);
    assertEqualsRel("<P|P> = ", 1., normF, true);

    PotHLcr partH = new PotHLcr(w);
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

