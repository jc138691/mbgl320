package scatt.jm_2008.jm.coulomb;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import project.workflow.task.test.FlowTest;
import scatt.partial.wf.JmClmbLcr;

import javax.utilx.log.Log;
/**
* Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 24/04/12, 11:57 AM
*/
public class ClmbHyBoundTest extends FlowTest {
public static Log log = Log.getLog(ClmbHyBoundTest.class);
private static final double MAX_INTGRL_ERR = 1e-8;
private static JmClmbLcr cont;
private static FuncArr target;
private static final int BOUND_TEST_N = 3;
public ClmbHyBoundTest(JmClmbLcr cont, FuncArr target) {
  super(ClmbHyBoundTest.class);    // <------ CHECK!!!!! Must be the same name. [is there a better way??? ;o( ]
  this.cont = cont;
  this.target = target;
//    setMaxErr(MAX_INTGRL_ERR);
}
public ClmbHyBoundTest() {
}
public void testNorm() {  //log.setDbg();
  double currMaxErr = getMaxErr();
  unlockMaxErr();
  setMaxErr(Math.min(currMaxErr, MAX_INTGRL_ERR));
  log.dbg("Max relative intergation error =" + getMaxErr());
  WFQuadrLcr w = cont.getQuadrLcr();
  for (int n = 0; n < cont.size(); n++) { log.dbg("n=", n);
    FuncVec f = cont.getFunc(n);
    for (int m = 0; m < BOUND_TEST_N; m++) { log.dbg("m=", m);
      FuncVec f2 = target.getFunc(m);
      double norm = w.calcInt(f, f2);  log.dbg("w.calcInt(f, f2)=", norm);
//          double corr = Mathx.delta(n, m);
      assertEquals("TEST[" + n + ", " + m +"]", 0, norm, true);
    }
  }
  lockMaxErr(currMaxErr);
}
}