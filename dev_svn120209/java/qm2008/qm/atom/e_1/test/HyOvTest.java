package atom.e_1.test;
import atom.energy.pw.lcr.PotHOvMLcr;
import atom.wf.lcr.LcrFactory;
import atom.wf.lcr.WFQuadrLcr;
import atom.wf.slater.SlaterWFFactory;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.deriv.test.DerivPts9Test;
import math.func.simple.FuncPowInt;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;
import math.vec.test.FastLoopTest;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 16/05/12, 12:02 PM
 */
public class HyOvTest extends FlowTest {
public static Log log = Log.getLog(HyOvTest.class);
private static WFQuadrLcr quadr;
private int N = 5;
private int K = 0;
private int L = 0;
private static final double HY_1S = 1;
private static final double HY_2S = 0.5;
public HyOvTest() {
  super(HyOvTest.class);
}
public void testHOvMtrx() throws Exception {  log.setDbg();
  if (!new FastLoopTest().ok()) return;
  if (!new DerivPts9Test().ok()) return;

  int LCR_FIRST = -5;
  int LCR_N = 1001;
  int R_FIRST = 0;
  int R_LAST = 100;
  double LAMBDA = 2;

  StepGridOpt gridR = new StepGridOpt(R_FIRST, R_LAST, LCR_N); // R_N not used!!!
  StepGridOpt gridLcr = LcrFactory.makeLcrFromR(LCR_FIRST, LCR_N, gridR);   log.dbg("gridLcr =\n", gridLcr);
  StepGrid x = new StepGrid(gridLcr);                 log.dbg("StepGrid x = new StepGrid(gridLcr) =\n", x);
  quadr = new WFQuadrLcr(x);                          log.dbg("quadr = new WFQuadrLcr(x)=\n", quadr);

  Vec r = quadr.getR();
  FuncVec pot = new FuncVec(r, new FuncPowInt(-1, -1)); // -1/r
  FuncArr basis = new FuncArr(x);

  FuncVec wf = SlaterWFFactory.makeLcrP1s(quadr, HY_1S);
  basis.add(wf);  log.dbg("makeLcrP1s(quadr, HY_1S)=\n", wf);
  double norm =  quadr.calcInt(wf, wf);   log.info("norm=" + norm);
  assertEquals(0, 1 - norm, 1e-32);

  wf = SlaterWFFactory.makeLcrP1s(quadr, HY_2S);
  basis.add(wf);  log.dbg("makeLcrP1s(quadr, HY_2S)=\n", wf);
  norm = quadr.calcInt(wf, wf);           log.info("norm=" + norm);
  assertEquals(0, 1 - norm, 5e-16);

  wf = SlaterWFFactory.makeLcrP2s(quadr, HY_2S);
  basis.add(wf);  log.dbg("makeLcrP2s(quadr, HY_2S)=\n", wf);
  norm = quadr.calcInt(wf, wf);        log.info("norm=" + norm);
  assertEquals(0, 1 - norm, 1e-32);

  log.dbg("TESTING NON-ORTHOG BASIS");
  PotHOvMLcr potH = new PotHOvMLcr(L, basis, pot, quadr);
  Vec engs = potH.getEigEngs();                log.dbg("engs=", new VecDbgView(engs));
  assertEquals("Hy(1s) =", 0, -0.5 - engs.get(0)); // testing against maxErr
//  assertEquals("Hy(1s) =", 0, -0.5 - engs.get(0), 2e-15); // JAMA
  assertEquals("Hy(1s) =", 0, -0.5 - engs.get(0), 3e-15); //  EJML

  assertEquals("Hy(2s) =", 0, -0.5 / 4. - engs.get(1)); // testing against maxErr
//  assertEquals("Hy(2s) =", 0, -0.5 / 4. - engs.get(1), 2e-15); // JAMA
  assertEquals("Hy(2s) =", 0, -0.5 / 4. - engs.get(1), 3e-15); //  EJML
}
}
