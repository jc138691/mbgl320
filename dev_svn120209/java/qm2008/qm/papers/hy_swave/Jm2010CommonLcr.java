package papers.hy_swave;

import atom.data.AtomHy;
import atom.wf.coulomb.CoulombNmrvLcr;
import atom.wf.coulomb.CoulombNmrvR;
import atom.wf.coulomb.CoulombWFFactory;
import atom.wf.log_cr.LcrFactory;
import atom.wf.log_cr.RkLcrFlowTest;
import atom.wf.log_cr.WFQuadrLcr;
import atom.wf.log_cr.YkLcrFlowTest;
import atom.wf.log_cr.test.RkLcrTest;
import atom.wf.log_cr.test.YkLcrTest;
import calc.interpol.InterpolCubeTest;
import math.complex.Cmplx1F1;
import math.complex.Cmplx2F1;
import math.complex.CmplxGamma;
import math.func.Func;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.simple.FuncPowInt;
import math.integral.test.BooleQuadrTest;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import math.vec.test.FastLoopTest;
import project.workflow.task.test.FlowTest;
import qm_station.jm.H_Hy_P1s_LcrTest;
import qm_station.jm.PotEigVecLcrTest;
import qm_station.ui.scatt.CalcOptLcr;
import scatt.jm_2008.jm.laguerre.JmLgrrLabelMaker;
import scatt.jm_2008.jm.laguerre.LgrrModel;
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
protected static LgrrOrthLcr orthonNt;  // for N_t
protected static LagrrBiLcr biorthN;
protected static WFQuadrLcr quadrLcr;
protected static Func potFunc;
protected static int Nt = 20;
protected static boolean USE_CLOSED_CHANNELS = true;

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
  calcOpt.setHomeDir(HOME_DIR);
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
protected void hydrScattTestOk(int trgtZ) {
  FlowTest.setLog(log);

  calcOpt.setUseClosed(USE_CLOSED_CHANNELS);

  basisOptN = new JmLgrrLabelMaker(basisOptN, Nt);    log.dbg("basisOptN =", basisOptN); // this is just for the file name label
  LgrrModel lgrrOptNt = new LgrrModel(basisOptN); // for the target N, i.e. N_t
  lgrrOptNt.setN(Nt);                             log.dbg("Laguerr model (N_t)=", lgrrOptNt);

  orthonNt = new LgrrOrthLcr(quadrLcr, lgrrOptNt); log.dbg("LgrrOrthLcr(N_t) = ", orthonNt);
  FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());      // LOCK MAX ERR
  {
    if (!new LgrrOrthLcrTest(orthonNt).ok()) return;
//      if (!new PotEigVecLcrTest(AtomHy.Z, orthonNt).ok()) return;
    if (!new PotEigVecLcrTest(trgtZ, orthonNt).ok()) return;
  }
  FlowTest.unlockMaxErr();                             // FREE MAX ERR

//    potFunc = new FuncPowInt(-AtomHy.Z, -1);  // f(r)=-1./r
  potFunc = new FuncPowInt(-trgtZ, -1);  // f(r)=-1./r
  pot = new FuncVec(rVec, potFunc);                       log.dbg("-1/r=", new VecDbgView(pot));

  if (!new YkLcrTest().ok()) return;
  if (!new YkLcrFlowTest(quadrLcr).ok()) return;

  if (!new RkLcrTest().ok()) return;
  if (!new RkLcrFlowTest(quadrLcr).ok()) return;

  if (!new CoulombNmrvR().ok()) return;
  if (!new CoulombNmrvLcr().ok()) return;

}
}