package d1;
import atom.wf.WFQuadrD1;
import atom.wf.WFQuadrR;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;
import math.vec.test.FastLoopTest;
import project.workflow.task.test.FlowTest;
import scatt.jm_2008.jm.laguerre.IWFuncArr;
import scatt.jm_2008.jm.laguerre.lcr.AnyOrthTest;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 5/06/12, 10:41 AM
 */
public class BoxTrigOrthTest extends FlowTest {
public static Log log = Log.getLog(BoxTrigOrthTest.class);
private static IWFuncArr arr;
private static int N_GRID = 101;
private static int MAX_N = 10;
private static double LEN = 2;
public BoxTrigOrthTest() {
  super(BoxTrigOrthTest.class);
}
public void testSiCo() throws Exception {   log.setDbg();
  log.info("-->BoxTrigOrthTest()");
  FlowTest.setLog(log);
  if (!new FastLoopTest().ok()) return;
  BoxTrigOpt opt = new BoxTrigOpt();
  opt.setMomN(MAX_N);
  opt.setBoxLen(LEN);

  StepGridOpt sg = new StepGridOpt(-LEN/2, LEN/2, N_GRID); log.dbg("StepGridOpt = ", sg);
  StepGrid x = new StepGrid(sg);                log.dbg("StepGrid=", new VecDbgView(x));
  WFQuadrD1 quadr = new WFQuadrR(x);  log.dbg("quadr=", new VecDbgView(quadr));
  BoxTrigOrth orth = new BoxTrigOrth(quadr, opt); log.dbg("\n BoxTrigOrth=\n", orth);

  double saveMaxErr = getMaxErr();
  lockMaxErr(2e-15);
  if (!new AnyOrthTest(orth).ok())
    return;
  unlockMaxErr();
  setMaxErr(saveMaxErr);
}
}