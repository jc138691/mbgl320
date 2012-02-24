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
import qm_station.jm.PotEigVecLcrTest;
import qm_station.ui.scatt.CalcOptLcr;
import scatt.jm_2008.jm.laguerre.lcr.*;
import scatt.partial.born.PBornDirScattTest;

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
protected static LagrrLcr basisN;
protected static LgrrOrthLcr orthonN;
protected static LagrrBiLcr biorthN;
protected static WFQuadrLcr quadrLcr;
protected static Func potFunc;

public StepGridModel makeStepGridModel() {
  StepGridModel modelR = new StepGridModel(R_FIRST, R_LAST, R_N); // R_N not used!!!
  StepGridModel res = LcrFactory.makeLcrFromR(LCR_FIRST, LCR_N, modelR);
  return res;
}

public CalcOptLcr makeJmPotOptLcr() {
  CalcOptLcr res = new CalcOptLcr();
  res.setGrid(makeStepGridModel());
  res.setLgrrModel(makeJmLagrr());
  res.setTestModel(makeJmTest());
  res.setGridEng(makeGridEng());
  return res;
}

protected void initProject() {
  log.info("-->initProject()");
  project.setJmPotOptLcr(makeJmPotOptLcr());
  project.setJmPotOptR(makeJmPotOptR());

  calcOpt = project.getJmPotOptLcr();
  testOpt = calcOpt.getTestModel();
  log.info("<--initProject()");
}

protected void potScattTestOk() {
  log.info("-->potScattTestOk()");
  FlowTest.setLog(log);
  FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());      // LOCK MAX ERR
  {
    if (!new CoulombWFFactory().ok()) return;
    if (!new CmplxGamma().ok()) return;
    if (!new Cmplx1F1().ok()) return;
    if (!new Cmplx2F1().ok()) return;
    if (!new FastLoopTest().ok()) return;
    if (!new InterpolCubeTest().ok()) return;

    StepGridModel sg = calcOpt.getGrid();           log.dbg("x step grid model =", sg);
    StepGrid x = new StepGrid(sg);                 log.dbg("x grid =", x);
    quadrLcr = new WFQuadrLcr(x);                  log.dbg("x weights =", quadrLcr);
    rVec = quadrLcr.getR();                        log.dbg("r grid =", rVec);
    if (!new BooleQuadrTest().ok()) return;
  }
  FlowTest.unlockMaxErr();                             // FREE MAX ERR

  if (!new YkLcrFlowTest(quadrLcr).ok()) return;

  basisOptN = calcOpt.getLgrrModel();                 log.dbg("Laguerr model =", basisOptN);
  basisN = new LagrrLcr(quadrLcr, basisOptN);    log.dbg("LagrrLcr =\n", basisN);

  FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());       // LOCK MAX ERR
  {
    // JM-basisN
    if (!new LagrrLcrTest(basisN).ok()) return;
    biorthN = new LagrrBiLcr(quadrLcr, basisOptN);           log.dbg("LagrrBiLcr =\n", biorthN);
    if (!new JmLagrrBiLcrTest(basisN, biorthN).ok()) return;
    orthonN = new LgrrOrthLcr(quadrLcr, basisOptN);         log.dbg("LgrrOrthLcr = ", orthonN);
    if (!new LgrrOrthLcrTest(orthonN).ok()) return;

//    // H-integration
    if (!new H_Hy_P1s_LcrTest(quadrLcr).ok()) return;
    // Making inner-basisN

    if (!new PotEigVecLcrTest(AtomHy.Z, orthonN).ok()) return;
    if (!new PBornDirScattTest(quadrLcr, calcOpt.getGridEng()).ok()) return;
  }
  FlowTest.unlockMaxErr();         // FREE MAX ERR
  log.info("<--potScattTestOk()");
}
}
