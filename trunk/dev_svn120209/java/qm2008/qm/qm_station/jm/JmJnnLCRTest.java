package qm_station.jm;
import project.workflow.task.test.FlowTest;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;
import scatt.jm_2008.jm.laguerre.lcr.JmLagrrLcr;
import scatt.jm_2008.jm.theory.JmTheory;
import scatt.eng.EngModel;
import math.vec.Vec;
import junit.framework.TestCase;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 20/11/2008, Time: 13:43:34
 */
public class JmJnnLCRTest extends FlowTest {
  private static JmLagrrLcr funcArr;
  private static EngModel eng;
  public JmJnnLCRTest(JmLagrrLcr arr, EngModel eng) {
    super(JmJnnLCRTest.class);
    this.funcArr = arr;
    this.eng = eng;
  }
  public JmJnnLCRTest() {
  }
  public void testNorm() {
    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();

    log.dbg("testing " + JmJnnLCR.HELP);
//    log.dbg("running JmSCnRTest: numerical values are compared against analytical expressions from JmTheory");
//    log.dbg("NOTE: All requested energies are tested");
//    log.dbg("NOTE: Max relative intergation error =" + getMaxErr());

    double lambda = funcArr.getModel().getLambda();
    int N = funcArr.size() - 1;  log.dbg("N = " + N);
    JmJnnLCR jnn = new JmJnnLCR(funcArr, eng);
    Vec engGrid = jnn.getX();
    for (int i = 0; i < engGrid.size(); i++) {
      if (monitor != null && monitor.isCanceled(i, 0, engGrid.size())) {
        TestCase.fail();
        return;
      }
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
        TestCase.fail();
      }

      double E = engGrid.get(i);  log.dbg("E = " + E);
      double Jnn = JmTheory.calcJnnL0byE(N, E, lambda);
      assertEqualsRel("J_NN[" + i + "] =", Jnn, jnn.get(i), true);
    }
  }
}

