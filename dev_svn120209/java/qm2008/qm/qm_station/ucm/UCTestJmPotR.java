package qm_station.ucm;

import atom.wf.WFQuadrR;
import atom.wf.coulomb.CoulombWFFactory;
import math.func.arr.FuncArrDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import project.workflow.task.DefaultResView;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.UCRunDefaultTask;
import project.workflow.task.test.FlowTest;
import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.jm.H_Hy_P1s_RTest;
import qm_station.jm.JmJnnRTest;
import qm_station.jm.JmPotEigVecRTest;
import qm_station.ui.StepGridView;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.jm_2008.jm.TestModel;
import scatt.jm_2008.jm.laguerre.*;

import javax.swingx.textx.TextView;
import javax.utilx.log.Log;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 3/10/2008, Time: 13:10:53
 */
public class UCTestJmPotR extends UCRunDefaultTask<QMS> {
  public static Log log = Log.getLog(UCRunJmPotR.class);

  public UCTestJmPotR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }

  protected void setupLogs() {
    add(log);
    add(StepGrid.log);
    add(StepGridView.log);
    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }

  public boolean run() {
    DefaultResView out = getOutView();
    TextView usr = out.getUsrView();
    out.setUsrView(usr);  // set focus
    addView(out.getSysView());
    log.info("run UCTestJmPotR");

    QMS project = QMSProject.getInstance();
    getOptView().loadTo(project);
    project.saveProjectToDefaultLocation();

    CalcOptE1 potOpt = project.getJmPotOptR();    // R
    StepGridModel sg = potOpt.getGrid();
    log.dbg("StepGridModel = ", sg);
    StepGrid r = new StepGrid(sg);
    log.dbg("StepGrid r = ", new VecDbgView(r));
    log.dbg("LgrrModel = ", potOpt.getLgrrModel());
    WFQuadrR w = new WFQuadrR(r);
    log.dbg("integration weights=", w);
    LgrrR jm = new LgrrR(w, potOpt.getLgrrModel());
    log.dbg("LgrrR = ", new FuncArrDbgView(jm));
    Vec pot = CoulombWFFactory.makePotHy_1s_e(r);
    log.dbg("V_1s(r)=", pot);

    // RUN ALL TESTS
    TestModel testOpt = potOpt.getTestModel();
    FlowTest.setLog(log);

    FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());     // LOCK MAX ERR
    {
      if (!new H_Hy_P1s_RTest(w).ok())
        return false;

      if (!new JmLagrrRTest(jm).ok())
        return false;

      LagrrBiR bi = new LagrrBiR(w, potOpt.getLgrrModel());
      log.dbg("LagrrBiR = ", bi);
      if (!new JmLagrrBiRTest(jm, bi).ok())
        return false;

      LgrrOrthR orth = new LgrrOrthR(w, potOpt.getLgrrModel());
      log.dbg("LgrrOrthR = ", orth);
      if (!new JmLagrrOrthRTest(orth).ok())
        return false;
      if (!new JmPotEigVecRTest(orth).ok())
        return false;

      EngModel eng = potOpt.getGridEng();
      if (!new JmJnnRTest(jm, eng).ok())
        return false;
    }
    FlowTest.unlockMaxErr();      

    log.info("finished UCTestJmPotR");
    return true;
  }
}