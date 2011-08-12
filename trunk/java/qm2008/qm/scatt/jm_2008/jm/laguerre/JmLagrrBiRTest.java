package scatt.jm_2008.jm.laguerre;
import project.workflow.task.test.FlowTest;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;
import atom.wf.WFQuadrR;
import math.vec.grid.StepGrid;
import math.vec.Vec;
import math.Mathx;
import junit.framework.TestCase;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 22/10/2008, Time: 16:41:18
 */
public class JmLagrrBiRTest extends FlowTest {
  private static JmLgrrR arr;
  private static JmLagrrBiR bio;
  public JmLagrrBiRTest(JmLgrrR basis, JmLagrrBiR bio) {
    super(JmLagrrBiRTest.class);
    arr = basis;
    this.bio = bio;
  }
  public JmLagrrBiRTest() {
  }
  public void testNorm() {
    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();

    log.dbg("testing " + JmLagrrBiR.HELP);
    log.dbg("JmLgrrR.HELP: " + JmLgrrR.HELP);
    log.dbg("running JmLagrrBiRTest: JM-Laguerre and Bi-diagonal Laguerre function orthonormality");
    log.dbg("TEST_{n,m} = Intergal[0,rMax] R_n(r) * BiR_m(r) = delta(n, m)");
    log.dbg("Max relative intergation error =" + getMaxErr());
    WFQuadrR w = new WFQuadrR((StepGrid)arr.getX());
    for (int n = 0; n < arr.size(); n++) {
      if (monitor != null && monitor.isCanceled(n, 0, arr.size())) {
        TestCase.fail();
        return;
      }
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
        TestCase.fail();
      }
      Vec f = arr.getFunc(n);
      for (int m = 0; m < bio.size(); m++) {
        Vec f2 = bio.getFunc(m);
        double norm = w.calc(f, f2);
        double corr = Mathx.dlt(n, m);
        assertEquals("TEST[" + n + ", " + m +"]", corr, norm, true);
      }
    }
  }
}
