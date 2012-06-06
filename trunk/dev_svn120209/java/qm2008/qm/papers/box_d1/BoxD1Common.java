package papers.box_d1;
import atom.wf.WFQuadrR;
import func.d1.BoxTrigOpt;
import func.d1.BoxTrigOrth;
import func.d1.BoxCalcOptD1;
import func.d1.BoxTrigOrthTest;
import math.func.deriv.test.DerivPts5Test;
import math.func.deriv.test.DerivPts9Test;
import math.integral.test.QuadrPts5Test;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;
import math.vec.test.FastLoopTest;
import papers.project_setup.ProjCommon;
import papers.project_setup.ProjTestOpt;
import project.workflow.task.test.FlowTest;
import scatt.jm_2008.jm.laguerre.lcr.AnyOrthTest;

import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 6/06/12, 9:13 AM
 */
public class BoxD1Common extends ProjCommon {
public static Log log = Log.getLog(BoxD1Common.class);
protected static WFQuadrR quadr;
protected static BoxCalcOptD1 calcOpt;
protected static BoxTrigOrth basis;
protected static BoxTrigOpt basisOpt;
protected static int PART_N = 2; // number of particles
protected static int BASIS_MOM_N = 2;// number of momenta
protected static int BOX_LEN = 2;
protected static int GRID_SIZE = 201;
protected static double MAX_INTGRL_ERR = 1e-10;

protected void initProjOpts() {
  log.info("-->initProjOpts()");
  calcOpt = makeCalcOpt();
//  calcOpt.setHomeDir(HOME_DIR);
  testOpt = calcOpt.getTestOpt();
  log.info("<--initProjOpts()");
}
public BoxCalcOptD1 makeCalcOpt() {
  BoxCalcOptD1 res = new BoxCalcOptD1();
  res.setGridOpt(makeGridOpt());
  res.setBasisOpt(makeBasisOpt());
  res.setTestOpt(makeTestOpt());
//  res.setGridEng(makeGridEng());
  return res;
}
public ProjTestOpt makeTestOpt() {
  ProjTestOpt res = new ProjTestOpt();
  res.setMaxIntgrlErr(MAX_INTGRL_ERR);
  return res;
}
public BoxTrigOpt makeBasisOpt() {
  BoxTrigOpt res = new BoxTrigOpt();
  res.setBoxLen(BOX_LEN);
  res.setMomN(BASIS_MOM_N);
  return res;
}
public StepGridOpt makeGridOpt() {
  StepGridOpt res = new StepGridOpt(-BOX_LEN/2, BOX_LEN/2, GRID_SIZE);
  return res;
}

protected void libTestsOk() {
  log.info("-->libTestsOk()");
  FlowTest.setLog(log);
  FlowTest.unlockMaxErr();
  FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());      // LOCK MAX ERR
  {
    if (!new FastLoopTest().ok()) return;
    if (!new DerivPts5Test().ok()) return;
    if (!new DerivPts9Test().ok()) return;
    if (!new QuadrPts5Test().ok()) return;
  }
  FlowTest.unlockMaxErr();                             // FREE MAX ERR
  if (!new BoxTrigOrthTest().ok()) return;
  log.info("<--libTestsOk()");
}
protected void setupProjOk() {
  log.info("-->setupProjOk()");
  FlowTest.setLog(log);
  FlowTest.unlockMaxErr();

  StepGridOpt sg = calcOpt.getGridOpt();           log.dbg("x step grid model =", sg);
  StepGrid x = new StepGrid(sg);                 log.dbg("x grid =", x);
  quadr = new WFQuadrR(x);                  log.dbg("x weights =", quadr);
  basisOpt = calcOpt.getBasisOpt();

  FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());       // LOCK MAX ERR
  {
    basis = new BoxTrigOrth(quadr, basisOpt); log.dbg("\n BoxTrigOrth=\n", basis);
    if (!new AnyOrthTest(basis).ok()) return;
  }
  FlowTest.unlockMaxErr();         // FREE MAX ERR
  log.info("<--setupProjOk()");
}
}
