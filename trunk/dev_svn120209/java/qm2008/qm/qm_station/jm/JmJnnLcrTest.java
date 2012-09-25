package qm_station.jm;
import project.workflow.task.test.FlowTest;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;
import scatt.jm_2008.jm.laguerre.lcr.LagrrLcr;
import scatt.jm_2008.jm.theory.JmTheory;
import scatt.eng.EngOpt;
import math.vec.Vec;
import junit.framework.TestCase;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 20/11/2008, Time: 13:43:34
 */
public class JmJnnLcrTest extends FlowTest {
  private static LagrrLcr funcArr;
  private static EngOpt eng;
  public JmJnnLcrTest(LagrrLcr arr, EngOpt eng) {
    super(JmJnnLcrTest.class);
    this.funcArr = arr;
    this.eng = eng;
  }
  public JmJnnLcrTest() {
  }
  public void testNorm() {
    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();

    log.dbg("testing " + JmJnnLcr2.HELP);
//    log.dbg("running JmSCnRTest: numerical values are compared against analytical expressions from JmTheory");
//    log.dbg("NOTE: All requested energies are tested");
//    log.dbg("NOTE: Max relative intergation error =" + getMaxErr());

    double lambda = funcArr.getModel().getLambda();
    int N = funcArr.size() - 1;  log.dbg("N = " + N);
    JmJnnLcr2 jnn = new JmJnnLcr2(funcArr, eng);
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

