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
 * Copyright dmitry.konovalov@jcu.edu.au Date: 13/11/2008, Time: 14:31:44
 */
public class JmLagrrOrthRTest extends FlowTest {
  private static JmLgrrOrthR arr;
  public JmLagrrOrthRTest(JmLgrrOrthR basis) {
    super(JmLagrrOrthRTest.class);
    arr = basis;
  }
  public JmLagrrOrthRTest() {
  }
  public void testNorm() {
    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();

    log.dbg("testing " + JmLgrrOrthR.HELP);
    log.dbg("TEST_{n,m} = Intergal[0,rMax] R_n(r) * R_m(r) = delta(n, m)");
    log.dbg("Max relative intergation error =", getMaxErr());
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
      for (int m = 0; m < arr.size(); m++) {
        Vec f2 = arr.getFunc(m);
        double norm = w.calc(f, f2);
        double corr = Mathx.dlt(n, m);
        assertEquals("TEST[" + n + ", " + m +"]", corr, norm, true);
      }
    }
  }
}
