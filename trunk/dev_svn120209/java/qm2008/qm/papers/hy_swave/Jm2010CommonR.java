package papers.hy_swave;

import atom.wf.WFQuadrR;
import math.integral.test.QuadrPts5Test;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;
import papers.project_setup.ProjTestOpt;
import project.workflow.task.test.FlowTest;
import qm_station.jm.H_Hy_P1s_RTest;
import qm_station.jm.JmPotEigVecRTest;
import scatt.jm_2008.jm.laguerre.*;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 15/02/2010, 1:20:05 PM
 */
public class Jm2010CommonR extends Jm2010Common {
  public static Log log = Log.getLog(Jm2010CommonR.class);
//  protected static int R_N = 1301;

  protected static LgrrR basis;
  protected static LgrrOrthR orth;
  protected static LagrrBiR bi;
  protected static WFQuadrR quadr;

  protected void initJm() {
    project.setJmPotOptR(makeJmPotOptR());

    calcOpt = project.getJmPotOptR();
    ProjTestOpt testOpt = calcOpt.getTestOpt();
    FlowTest.setMaxErr(testOpt.getMaxIntgrlErr());
    FlowTest.setLog(log);

    StepGridOpt sg = calcOpt.getGridOpt();    log.dbg("r step grid model =", sg);
    quadr = new WFQuadrR(new StepGrid(sg));            log.dbg("r weights =", quadr);
    vR = quadr.getR();                   log.dbg("r grid =", vR);
    if (!new QuadrPts5Test().ok())         return;

    lgrrOptN = calcOpt.getBasisOpt();          log.dbg("Laguerr model =", lgrrOptN);
    basis = new LgrrR(quadr, lgrrOptN);   log.dbg("LgrrR =\n", basis);

    // JM-lgrrN
    if (!new JmLagrrRTest(basis).ok())         return;
    bi = new LagrrBiR(quadr, lgrrOptN);      log.dbg("LagrrBiR =\n", bi);
    if (!new JmLagrrBiRTest(basis, bi).ok())   return;
    orth = new LgrrOrthR(quadr, lgrrOptN);  log.dbg("LgrrOrthR = ", orth);
    if (!new JmLagrrOrthRTest(orth).ok())      return;

    // H-integration
    if (!new H_Hy_P1s_RTest(quadr).ok())           return;

    // Making inner-lgrrN
    if (!new JmPotEigVecRTest(orth).ok())      return;    
  }

}
