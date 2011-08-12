package papers.hy_swave;

import atom.data.AtomHy;
import atom.wf.coulomb.CoulombWFFactory;
import atom.wf.log_cr.LcrFactory;
import atom.wf.log_cr.WFQuadrLcr;
import atom.wf.log_cr.YkLcrFlowTest;
import calc.interpol.InterpolCubeTest;
import math.complex.Cmplx1F1;
import math.complex.Cmplx2F1;
import math.complex.CmplxGamma;
import math.func.Func;
import math.func.arr.FuncArr;
import math.integral.test.BooleQuadrTest;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import math.vec.test.FastLoopTest;
import project.workflow.task.test.FlowTest;
import qm_station.jm.H_Hy_P1s_LcrTest;
import qm_station.jm.JmPotEigVecLcrTest;
import qm_station.ui.scatt.JmOptLcr;
import scatt.jm_2008.jm.laguerre.lcr.*;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 15/02/2010, 1:20:21 PM
 */
public class Jm2010CommonLcr extends Jm2010Common {
  public static Log log = Log.getLog(Jm2010CommonLcr.class);
  protected static int LCR_N = 301;
  protected static int LCR_FIRST = -4;// -1 for Hydrogen
//  protected static final int HY_Z = 1;

  protected static FuncArr trgtBasisN;
  protected static JmLagrrLcr jmBasisN;
  protected static JmLgrrOrthLcr orthonN;
  protected static JmLagrrBiLcr biorthN;
  protected static WFQuadrLcr quadrLcr;
  protected static Func potFunc;

  public StepGridModel makeStepGridModel() {
    StepGridModel modelR = new StepGridModel(R_FIRST, R_LAST, R_N); // R_N not used!!!
    StepGridModel res = LcrFactory.makeLcrFromR(LCR_FIRST, LCR_N, modelR);
    return res;
  }

  public JmOptLcr makeJmPotOptLcr() {
    JmOptLcr res = new JmOptLcr();
    res.setGrid(makeStepGridModel());
    res.setJmModel(makeJmLagrr());
    res.setJmTest(makeJmTest());
    res.setGridEng(makeGridEng());
    return res;
  }

  protected void initProject() {
    project.setJmPotOptLcr(makeJmPotOptLcr());
    project.setJmPotOptR(makeJmPotOptR());

    jmOpt = project.getJmPotOptLcr();
    testOpt = jmOpt.getJmTest();
  }

  protected void initPotJm() {
    FlowTest.setLog(log);
    FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());      // LOCK MAX ERR
    {
      if (!new CoulombWFFactory().ok()) return;
      if (!new CmplxGamma().ok()) return;
      if (!new Cmplx1F1().ok()) return;
      if (!new Cmplx2F1().ok()) return;
      if (!new FastLoopTest().ok()) return;
      if (!new InterpolCubeTest().ok()) return;

      StepGridModel sg = jmOpt.getGrid();           log.dbg("x step grid model =", sg);
      StepGrid x = new StepGrid(sg);                 log.dbg("x grid =", x);
      quadrLcr = new WFQuadrLcr(x);                  log.dbg("x weights =", quadrLcr);
      rVec = quadrLcr.getR();                        log.dbg("r grid =", rVec);
      if (!new BooleQuadrTest().ok()) return;
    }
    FlowTest.unlockMaxErr();                             // FREE MAX ERR

    if (!new YkLcrFlowTest(quadrLcr).ok()) return;

    basisOptN = jmOpt.getJmModel();                 log.dbg("Laguerr model =", basisOptN);
    jmBasisN = new JmLagrrLcr(quadrLcr, basisOptN);    log.dbg("JmLagrrLcr =\n", jmBasisN);

    FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());       // LOCK MAX ERR
    {
      // JM-jmBasisN
      if (!new JmLagrrLcrTest(jmBasisN).ok()) return;
      biorthN = new JmLagrrBiLcr(quadrLcr, basisOptN);           log.dbg("JmLagrrBiLcr =\n", biorthN);
      if (!new JmLagrrBiLcrTest(jmBasisN, biorthN).ok()) return;
      orthonN = new JmLgrrOrthLcr(quadrLcr, basisOptN);         log.dbg("JmLgrrOrthLcr = ", orthonN);
      if (!new JmLgrrOrthLcrTest(orthonN).ok()) return;

//    // H-integration
      if (!new H_Hy_P1s_LcrTest(quadrLcr).ok()) return;
      // Making inner-jmBasisN

      if (!new JmPotEigVecLcrTest(AtomHy.Z, orthonN).ok()) return;
    }
    FlowTest.unlockMaxErr();         // FREE MAX ERR
  }
}
