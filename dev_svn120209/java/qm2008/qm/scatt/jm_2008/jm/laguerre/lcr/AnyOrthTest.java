package scatt.jm_2008.jm.laguerre.lcr;
import atom.wf.WFQuadrD1;
import math.func.FuncVec;
import project.workflow.task.test.FlowTest;
import math.Mathx;
import scatt.jm_2008.jm.laguerre.IWFuncArr;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 13/11/2008, Time: 14:51:59
 */
public class AnyOrthTest extends FlowTest {
public static Log log = Log.getLog(AnyOrthTest.class);
private static IWFuncArr arr;
public AnyOrthTest(IWFuncArr basis) {
  super(AnyOrthTest.class);    // <------ CHECK!!!!! Must be the same name. [is there a better way??? ;o( ]
  arr = basis;
}
public AnyOrthTest() {
  super(AnyOrthTest.class);
}
public void testNorm() {
//    TaskProgressMonitor monitor = ProjectProgressMonitor.getInstance();
  log.dbg("TEST_{n,m} = Intergal[0,rMax] R_n(r) * R_m(r) = delta(n, m)");
  log.dbg("Max relative intergation error =", getMaxErr());
  WFQuadrD1 w = arr.getQuadr();
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
      double norm = w.calcInt(f, f2);
      double corr = Mathx.dlt(n, m);
      assertEquals("TEST[" + n + ", " + m +"]", corr, norm, true);
    }
  }
}
}
