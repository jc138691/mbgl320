package papers.jm2008;
import qm_station.ui.scatt.JmOptR;
import scatt.jm_2008.e1.JmOptE1;
import scatt.eng.EngModel;
import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.jm.*;

import javax.utilx.log.Log;

import math.vec.grid.StepGridModel;
import math.vec.grid.StepGrid;
import math.vec.Vec;
import math.func.FuncVec;
import atom.wf.WFQuadrR;
import atom.wf.coulomb.CoulombWFFactory;
import scatt.jm_2008.jm.laguerre.*;
import scatt.jm_2008.jm.JmTestModel;
import project.workflow.task.test.FlowTest;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/12/2008, Time: 13:05:09
 */
public class Jm2008PotR extends Jm2008Common {
  public static Log log = Log.getLog(Jm2008PotR.class);
  public void setUp() {
    Log.addGlobal(System.out);
//    log = Log.getRootLog();
    log.info("log.info(Jm2008PotR)");
    log.setDbg();
  }

  public static JmLgrrModel makeJmLagrr() {
    JmLgrrModel res = new JmLgrrModel();
    res.setL(0);
    res.setLambda(1);
    res.setN(10);
    return res;
  }
  public static StepGridModel makeStepGridModel() {
    StepGridModel res = new StepGridModel();
    res.setFirst(0);
    res.setLast(100);
    res.setNumPoints(1301);
    return res;
  }
  public static JmTestModel makeJmTest() {
    JmTestModel res = new JmTestModel();
    res.setMaxIntgrlErr(0.001f);
    return res;
  }
  public static EngModel makeGridEng() {
    EngModel res = new EngModel();
    res.setFirst(0.01f);
    res.setLast(1);
    res.setNumPoints(3);
    return res;
  }
  public static JmOptR makeJmPotOpt() {
    JmOptR res = new JmOptR();
    res.setGrid(makeStepGridModel());
    res.setJmModel(makeJmLagrr());
    res.setJmTest(makeJmTest());
    res.setGridEng(makeGridEng());
    return res;
  }
  public static QMS makeProject() {
    QMS res = QMSProject.makeInstance("Jm2008PotR", "081218");
    res.setJmPotOptR(makeJmPotOpt());
    return res;
  }
  public FuncVec makePot(Vec r) {
    FuncVec pot = CoulombWFFactory.makePotHy_1s_e(r);  log.dbg("V_1s(r)=", pot);
    return pot;
  }
  public void tearDown() {
//    QMSFrame.getInstance().dispose();
  }
  public void testHyPot() {
    QMS project = makeProject();

    JmOptE1 potOpt = project.getJmPotOptR();
    StepGridModel sg = potOpt.getGrid();     log.dbg("r step grid model =", sg);
    StepGrid r = new StepGrid(sg);           log.dbg("r grid =", r);
    WFQuadrR w = new WFQuadrR(r);            log.dbg("r weights =", w);

    JmLgrrModel lgrrOpt = potOpt.getJmModel(); log.dbg("Laguerr model =", lgrrOpt);
    JmLgrrR basis = new JmLgrrR(w, lgrrOpt);  log.dbg("JmLgrrR =\n", basis);
    JmTestModel testOpt = potOpt.getJmTest();
    FlowTest.setMaxErr(testOpt.getMaxIntgrlErr());
    FlowTest.setLog(log);

    // H-integration
    if (!new H_Hy_P1s_RTest(w).ok())           return;

    // JM-jmBasisN
    if (!new JmLagrrRTest(basis).ok())         return;
    JmLagrrBiR bi = new JmLagrrBiR(w, lgrrOpt );  log.dbg("JmLagrrBiR =\n", bi);
    if (!new JmLagrrBiRTest(basis, bi).ok())   return;
    JmLgrrOrthR orth = new JmLgrrOrthR(w, lgrrOpt );  log.dbg("JmLgrrOrthR = ", orth);
    if (!new JmLagrrOrthRTest(orth).ok())      return;
    if (!new JmPotEigVecRTest(orth).ok())      return;

    // JM-properties
    EngModel eng = potOpt.getGridEng();    log.dbg("Incident Energies =", eng);
    if (!new JmJnnRTest(basis, eng).ok())      return;
    if (!new JmJnmSCmRTest(basis, eng).ok())      return;

  }
}
