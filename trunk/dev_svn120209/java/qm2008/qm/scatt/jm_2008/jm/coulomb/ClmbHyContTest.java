package scatt.jm_2008.jm.coulomb;

import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import project.workflow.task.test.FlowTest;
import scatt.partial.wf.JmClmbLcr;

import javax.utilx.log.Log;

/**
* Created by Dmitry.A.Konovalov@gmail.com, 17/05/2010, 11:43:17 AM
*/
public class ClmbHyContTest extends FlowTest {
public static Log log = Log.getLog(ClmbHyContTest.class);
private static final double MAX_INTGRL_ERR = 1e-5;
private static JmClmbLcr cont;
private static FuncArr target;
public ClmbHyContTest(JmClmbLcr cont, FuncArr target) {
  super(ClmbHyContTest.class);    // <------ CHECK!!!!! Must be the same name. [is there a better way??? ;o( ]
  this.cont = cont;
  this.target = target;
}
public ClmbHyContTest() {
}
public void testNorm() {
  double currMaxErr = getMaxErr();
  unlockMaxErr();
  setMaxErr(Math.min(currMaxErr, MAX_INTGRL_ERR));
  log.dbg("Max relative intergation error =" + getMaxErr());
  WFQuadrLcr w = cont.getQuadrLcr();
  for (int n = 0; n < cont.size(); n++) {
    FuncVec f = cont.getFunc(n);
    for (int m = 0; m < target.size(); m++) {
      FuncVec f2 = target.getFunc(m);
      double norm = w.calcInt(f, f2);
      if (n == m) {
        log.info("w.calcInt(f, f2)=" + norm);
      }
      else {
//          double corr = Mathx.delta(n, m);
        assertEquals("TEST[" + n + ", " + m +"]", 0, norm, true);
      }
    }
  }
  lockMaxErr(currMaxErr);
}
}