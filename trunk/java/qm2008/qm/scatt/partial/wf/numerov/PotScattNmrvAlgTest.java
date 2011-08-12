package scatt.partial.wf.numerov;
import project.workflow.task.test.FlowTest;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;
import atom.wf.WFQuadrR;
import atom.wf.coulomb.CoulombWFFactory;
import atom.energy.part_wave.PartHR;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.func.FuncVec;
import math.func.simple.FuncPowInt;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 19/12/2008, Time: 10:46:50
 */
public class PotScattNmrvAlgTest extends FlowTest {
  private static WFQuadrR w;
  public PotScattNmrvAlgTest(WFQuadrR w) {
    super(PotScattNmrvAlgTest.class);
    this.w = w;
  }
  public PotScattNmrvAlgTest() {
  }
  public void testThis() {
  }
}