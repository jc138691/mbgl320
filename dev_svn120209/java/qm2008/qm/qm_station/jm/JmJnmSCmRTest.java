package qm_station.jm;
import project.workflow.task.test.FlowTest;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;
import scatt.eng.EngGrid;
import scatt.eng.EngOpt;
import scatt.jm_2008.jm.laguerre.LgrrR;
import math.vec.Vec;
import junit.framework.TestCase;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 17/12/2008, Time: 14:42:43
 */
public class JmJnmSCmRTest extends FlowTest {
  private static LgrrR basis;
  private static EngOpt eng;
  public JmJnmSCmRTest(LgrrR basis, EngOpt engModel) {
    super(JmJnmSCmRTest.class);
    JmJnmSCmRTest.basis = basis;
    JmJnmSCmRTest.eng = engModel;
  }
  public JmJnmSCmRTest() {
  }
  public void testNorm() {
    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();

    log.dbg("testing Sum_m Jnm*Sm = 0, n < N-1");
    log.dbg("testing Sum_m Jnm*Cm = 0, 0 < n < N-1");

    EngGrid engGrid = new EngGrid(eng);
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
      JmJMtrxR jnm = new JmJMtrxR(basis, E);      log.dbg("J-matrix, Jnm=", jnm);
      JmSm sn = new JmSm(basis, E);      log.dbg("S_n =", sn);
      Vec js = jnm.mult(sn);               log.info("Jnm * Sm =", js);

      JmCm cn = new JmCm(basis, E);      log.dbg("C_n =", cn);
      Vec jc = jnm.mult(cn);               log.info("Jnm * Cm =", jc);

      for (int n = 0; n < js.size()-1; n++) { // NOTE!! all but last, "size-1"
        assertEquals("Jnm*Sm=V[" + n + "] =", 0, js.get(n), true);
        if (n > 0)
          assertEquals("Jnm*Cm=V[" + n + "] =", 0, jc.get(n), true);
      }
    }

  }
}
