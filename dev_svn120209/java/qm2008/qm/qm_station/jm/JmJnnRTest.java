package qm_station.jm;
import project.workflow.task.test.FlowTest;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;
import scatt.eng.EngModel;
import scatt.jm_2008.jm.laguerre.LgrrR;
import scatt.jm_2008.jm.theory.JmTheory;
import math.vec.Vec;
import junit.framework.TestCase;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 19/11/2008, Time: 15:45:52
 */
public class JmJnnRTest extends FlowTest {
  private static LgrrR funcArr;
  private static EngModel eng;
  public JmJnnRTest(LgrrR arr, EngModel eng) {
    super(JmJnnRTest.class);
    this.funcArr = arr;
    this.eng = eng;
  }
  public JmJnnRTest() {
  }
  public void testNorm() {
    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();

    log.dbg("testing " + JmJnnR.HELP);
//    log.dbg("running JmSCnRTest: numerical values are compared against analytical expressions from JmTheory");
//    log.dbg("NOTE: All requested energies are tested");
//    log.dbg("NOTE: Max relative intergation error =" + getMaxErr());

    double lambda = funcArr.getModel().getLambda();
    int N = funcArr.size() - 1;  log.dbg("N = " + N);
    JmJnnR jnn = new JmJnnR(funcArr, eng); // jnn is now J_{N-2, N-1}
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

