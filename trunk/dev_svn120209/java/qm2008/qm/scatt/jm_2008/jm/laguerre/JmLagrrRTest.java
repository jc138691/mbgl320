package scatt.jm_2008.jm.laguerre;
import project.workflow.task.test.FlowTest;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;
import atom.wf.WFQuadrR;
import math.vec.grid.StepGrid;
import math.vec.Vec;
import junit.framework.TestCase;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 3/10/2008, Time: 14:58:40
 */
public class JmLagrrRTest extends FlowTest {
  private static LgrrR arr;
  public JmLagrrRTest(LgrrR basis) {
    super(JmLagrrRTest.class);
    arr = basis;
  }
  public JmLagrrRTest() {
  }
  public void testNorm() {
    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();

    log.dbg("testing " + LgrrR.HELP);
    log.dbg("JmLagrrRTest: JM-Laguerre function normalization");
    log.dbg("TEST_n = Integral[0,rMax] R_n^2(r) = C_n + C_(n-1), where C_n=(n+v+1)!/(lambda * n!)");
    log.dbg("Max relative integration error =" + getMaxErr());
//    log.dbg("testing " + LgrrR.HELP);
//    log.dbg("JmLagrrRTest: JM-Laguerre function normalization");
//    log.dbg("TEST_n = Integral[0,rMax] R_n^2(r) = C_n + C_(n-1), where C_n=(n+v+1)!/(lambda * n!)");
//    log.dbg("Max relative integration error =" + getMaxErr());
    WFQuadrR w = new WFQuadrR((StepGrid)arr.getX());
    for (int n = 0; n < arr.size(); n++) {
      if (monitor != null && monitor.isCanceled(n, 0, arr.size())) {
        TestCase.fail();
        return;
      }
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();  
        TestCase.fail();
      }
      Vec f = arr.getFunc(n);
      double norm = w.calc(f, f);
      double corr = arr.calcNorm(n);
      assertEqualsRel("TEST_" + n, corr, norm, true);
//      assertEquals("R_" + n, corr, norm, true);
    }
  }
}
