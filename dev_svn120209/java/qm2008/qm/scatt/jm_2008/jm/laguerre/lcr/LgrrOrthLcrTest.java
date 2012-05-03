package scatt.jm_2008.jm.laguerre.lcr;
import atom.wf.lcr.WFQuadrLcr;
import math.func.FuncVec;
import project.workflow.task.test.FlowTest;
import math.Mathx;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 13/11/2008, Time: 14:51:59
 */
public class LgrrOrthLcrTest extends FlowTest {
  private static LgrrOrthLcr arr;
  public LgrrOrthLcrTest(LgrrOrthLcr basis) {
    super(LgrrOrthLcrTest.class);    // <------ CHECK!!!!! Must be the same name. [is there a better way??? ;o( ]
    arr = basis;
  }
  public LgrrOrthLcrTest() {
    super(LgrrOrthLcrTest.class);
  }
  public void testNorm() {
//    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();

    log.dbg("testing " + LgrrOrthLcr.HELP);
    log.dbg("TEST_{n,m} = Intergal[0,rMax] R_n(r) * R_m(r) = delta(n, m)");
    log.dbg("Max relative intergation error =", getMaxErr());
    WFQuadrLcr w = arr.getQuadr();
    for (int n = 0; n < arr.size(); n++) {
//      if (monitor != null && monitor.isCanceled(n, 0, arr.size())) {
//        TestCase.fail();
//        return;
//      }
//      try {
//        Thread.sleep(10);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//        TestCase.fail();
//      }
      FuncVec f = arr.getFunc(n);
      for (int m = 0; m < arr.size(); m++) {
        FuncVec f2 = arr.getFunc(m);
//        double norm = w.getWithCR2().calc(f, f2);  // NOTE!! with getWithCR2
        double norm = w.calcInt(f, f2);  // NOTE!! with getWithCR2
        double corr = Mathx.dlt(n, m);
        assertEquals("TEST[" + n + ", " + m +"]", corr, norm, true);
      }
    }
  }
}
