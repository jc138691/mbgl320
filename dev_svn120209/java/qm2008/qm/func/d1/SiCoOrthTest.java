package func.d1;
import atom.wf.WFQuadr;
import atom.wf.WFQuadrR;
import math.Mathx;
import math.func.FuncVec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import math.vec.test.FastLoopTest;
import project.workflow.task.test.FlowTest;
import scatt.jm_2008.jm.laguerre.IWFuncArr;
import scatt.jm_2008.jm.laguerre.lcr.AnyOrthTest;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 5/06/12, 10:41 AM
 */
public class SiCoOrthTest extends FlowTest {
public static Log log = Log.getLog(SiCoOrthTest.class);
private static IWFuncArr arr;
private static int N_GRID = 101;
private static int MAX_N = 10;
private static double LEN = 2;
public SiCoOrthTest() {
  super(SiCoOrthTest.class);
}
public void testSiCo() throws Exception {   log.setDbg();
  log.info("-->testSiCo()");
  FlowTest.setLog(log);
  if (!new FastLoopTest().ok()) return;
  SiCoOpt opt = new SiCoOpt();
  opt.setMaxN(MAX_N);
  opt.setLen(LEN);

  StepGridModel sg = new StepGridModel(-LEN/2, LEN/2, N_GRID); log.dbg("StepGridModel = ", sg);
  StepGrid x = new StepGrid(sg);                log.dbg("StepGrid=", new VecDbgView(x));
  WFQuadr quadr = new WFQuadrR(x);  log.dbg("quadr=", new VecDbgView(quadr));
  SiCoOrth orth = new SiCoOrth(quadr, opt); log.dbg("\n SiCoOrth=\n", orth);

  double saveMaxErr = getMaxErr();
  lockMaxErr(2e-15);
  if (!new AnyOrthTest(orth).ok())
    return;
  unlockMaxErr();
  setMaxErr(saveMaxErr);
}
}