package scatt.jm_2008.jm.laguerre.lcr;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import project.workflow.task.test.FlowTest;
import math.Mathx;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 10:28:06
 */
public class JmLagrrBiLcrTest extends FlowTest {
  private static JmLagrrLcr arr;
  private static JmLagrrBiLcr bio;
  public JmLagrrBiLcrTest(JmLagrrLcr basis, JmLagrrBiLcr bio) {
    super(JmLagrrBiLcrTest.class);    // <------ CHECK!!!!! Must be the same name. [is there a better way??? ;o( ]
    arr = basis;
    this.bio = bio;
  }
  public JmLagrrBiLcrTest() {
  }
  public void testNorm() {
//    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();

    log.dbg("testing " + JmLagrrBiLcr.HELP);
    log.dbg("running JmLagrrBiLCRRTest: JM-Laguerre and Bi-diagonal Laguerre function orthonormality");
    log.dbg("TEST_{n,m} = Intergal[0,rMax] R_n(r) * BiR_m(r) = delta(n, m)");
    log.dbg("Max relative intergation error =" + getMaxErr());
    WFQuadrLcr w = arr.getQuadrLCR();
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
      for (int m = 0; m < bio.size(); m++) {
        FuncVec f2 = bio.getFunc(m);
        double norm = w.calcOverlap(f, f2);  // NOTE!! with getWithCR2
        double corr = Mathx.dlt(n, m);
        assertEquals("TEST[" + n + ", " + m +"]", corr, norm, true);
      }
    }
  }
}
