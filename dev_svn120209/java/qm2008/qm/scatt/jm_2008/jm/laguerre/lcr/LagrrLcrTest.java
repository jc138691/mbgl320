package scatt.jm_2008.jm.laguerre.lcr;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import project.workflow.task.test.FlowTest;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 22/10/2008, Time: 15:28:57
 */
public class LagrrLcrTest extends FlowTest {
  private static LagrrLcr arr;
  public LagrrLcrTest(LagrrLcr basis) {
    super(LagrrLcrTest.class);
    arr = basis;
  }
  public LagrrLcrTest() {
  }

  public void testNorm() {
//    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();
//
    log.dbg("testing LagrrLcr: JM-Laguerre function normalization");
    log.dbg("TEST_n = Intergal[0,rMax] R_n^2(r) = C_n + C_(n-1), where C_n=(n+v+1)!/(lambda * n!)");
    log.dbg("Max relative integration error =" + getMaxErr());
    WFQuadrLcr w = arr.getQuadrLCR();
    for (int n = 0; n < arr.size(); n++) {
//      if (monitor != null && monitor.isCanceled(n, 0, arr.size())) {
//        TestCase.fail();
//        return;
//      }
//      try {
//        Thread.sleep(100);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//        TestCase.fail();
//      }
      FuncVec f = arr.getFunc(n);
      double norm = w.calcOverlap(f, f);
      double corr = arr.calcNorm(n);
      assertEqualsRel("TEST_" + n, corr, norm, true);
    }
  }
}
