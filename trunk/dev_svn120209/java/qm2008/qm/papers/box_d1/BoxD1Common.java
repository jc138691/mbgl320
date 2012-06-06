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
import project.workflow.task.test.FlowTest;

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

protected void initProject() {
  log.info("-->initProject()");
  testOpt = calcOpt.getTestOpt();

//  project.setJmPotOptLcr(makeJmPotOptLcr());
//  project.setJmPotOptR(makeJmPotOptR());
//
//  calcOpt = project.getJmPotOptLcr();
//  calcOpt.setHomeDir(HOME_DIR);
//  testOpt = calcOpt.getTestOpt();
//
//  calcOpt.setUseClosed(USE_CLOSED_CHANNELS);
//  calcOpt.setUseClosedNum(KEEP_CLOSED_N);
//  calcOpt.setCalcSdcs(CALC_SDCS);
//  calcOpt.setJmTailN(JM_TAIL_N);
//  calcOpt.setSdcsEngN(SDCS_ENG_N);

  log.info("<--initProject()");
}
protected void allTestsOk() {
  log.info("-->allTestsOk()");
  FlowTest.setLog(log);
  FlowTest.unlockMaxErr();
  FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());      // LOCK MAX ERR
  {
    if (!new FastLoopTest().ok()) return;
    if (!new DerivPts5Test().ok()) return;
    if (!new DerivPts9Test().ok()) return;
    if (!new QuadrPts5Test().ok()) return;

    StepGridOpt sg = calcOpt.getGridOpt();           log.dbg("x step grid model =", sg);
    StepGrid x = new StepGrid(sg);                 log.dbg("x grid =", x);
    quadr = new WFQuadrR(x);                  log.dbg("x weights =", quadr);
  }
  FlowTest.unlockMaxErr();                             // FREE MAX ERR

  if (!new BoxTrigOrthTest().ok()) return;

  basisOpt = calcOpt.getBoxTrigOpt();
  BoxTrigOrth orth = new BoxTrigOrth(quadr, basisOpt); log.dbg("\n BoxTrigOrth=\n", orth);

  lgrrN = new LagrrLcr(quadr, lgrrOptN);    log.dbg("LagrrLcr =\n", lgrrN);

  FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());       // LOCK MAX ERR
  {
    // JM-lgrrN
    if (!new LagrrLcrTest(lgrrN).ok()) return;
    lgrrBiN = new LagrrBiLcr(quadr, lgrrOptN);           log.dbg("LagrrBiLcr =\n", lgrrBiN);
    if (!new LagrrBiLcrTest(lgrrN, lgrrBiN).ok()) return;
    orthN = new LgrrOrthLcr(quadr, lgrrOptN);         log.dbg("LgrrOrthLcr = ", orthN);
    if (!new AnyOrthTest(orthN).ok()) return;

//    // H-integration
    if (!new H_Hy_P1s_LcrTest(quadr).ok()) return;
    // Making inner-lgrrN

    if (!new PotEigVecLcrTest(AtomHy.Z, orthN).ok()) return;
    if (!new PBornDirScattTest(quadr, calcOpt.getGridEng()).ok()) return;
  }
  FlowTest.unlockMaxErr();         // FREE MAX ERR
  log.info("<--allTestsOk()");
}
}
