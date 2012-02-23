package papers.hy_swave;

import atom.wf.WFQuadrR;
import math.integral.test.BooleQuadrTest;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import project.workflow.task.test.FlowTest;
import qm_station.jm.H_Hy_P1s_RTest;
import qm_station.jm.JmPotEigVecRTest;
import scatt.jm_2008.jm.TestModel;
import scatt.jm_2008.jm.laguerre.*;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 15/02/2010, 1:20:05 PM
 */
public class Jm2010CommonR extends Jm2010Common {
  public static Log log = Log.getLog(Jm2010CommonR.class);
//  protected static int R_N = 1301;

  protected static JmLgrrR basis;
  protected static JmLgrrOrthR orth;
  protected static JmLagrrBiR bi;
  protected static WFQuadrR quadr;

  protected void initJm() {
    project.setJmPotOptR(makeJmPotOptR());

    calcOpt = project.getJmPotOptR();
    TestModel testOpt = calcOpt.getTestModel();
    FlowTest.setMaxErr(testOpt.getMaxIntgrlErr());
    FlowTest.setLog(log);

    StepGridModel sg = calcOpt.getGrid();    log.dbg("r step grid model =", sg);
    quadr = new WFQuadrR(new StepGrid(sg));            log.dbg("r weights =", quadr);
    rVec = quadr.getR();                   log.dbg("r grid =", rVec);
    if (!new BooleQuadrTest().ok())         return;

    basisOptN = calcOpt.getLgrrModel();          log.dbg("Laguerr model =", basisOptN);
    basis = new JmLgrrR(quadr, basisOptN);   log.dbg("JmLgrrR =\n", basis);

    // JM-jmBasisN
    if (!new JmLagrrRTest(basis).ok())         return;
    bi = new JmLagrrBiR(quadr, basisOptN);      log.dbg("JmLagrrBiR =\n", bi);
    if (!new JmLagrrBiRTest(basis, bi).ok())   return;
    orth = new JmLgrrOrthR(quadr, basisOptN);  log.dbg("JmLgrrOrthR = ", orth);
    if (!new JmLagrrOrthRTest(orth).ok())      return;

    // H-integration
    if (!new H_Hy_P1s_RTest(quadr).ok())           return;

    // Making inner-jmBasisN
    if (!new JmPotEigVecRTest(orth).ok())      return;    
  }

}
