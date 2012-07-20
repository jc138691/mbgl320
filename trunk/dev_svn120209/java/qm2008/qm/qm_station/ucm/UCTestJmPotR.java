package qm_station.ucm;

import atom.wf.WFQuadrR;
import atom.wf.coulomb.WfFactory;
import math.func.arr.FuncArrDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;
import papers.project_setup.ProjTestOpt;
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
import scatt.eng.EngOpt;
import scatt.jm_2008.e1.JmCalcOptE1;
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

    JmCalcOptE1 potOpt = project.getJmPotOptR();    // R
    StepGridOpt sg = potOpt.getGridOpt();
    log.dbg("StepGridOpt = ", sg);
    StepGrid r = new StepGrid(sg);
    log.dbg("StepGrid r = ", new VecDbgView(r));
    log.dbg("LgrrOpt = ", potOpt.getBasisOpt());
    WFQuadrR w = new WFQuadrR(r);
    log.dbg("integration weights=", w);
    LgrrR jm = new LgrrR(w, potOpt.getBasisOpt());
    log.dbg("LgrrR = ", new FuncArrDbgView(jm));
    Vec pot = WfFactory.makePotHy_1s_e(r);
    log.dbg("V_1s(r)=", pot);

    // RUN ALL TESTS
    ProjTestOpt testOpt = potOpt.getTestOpt();
    FlowTest.setLog(log);

    FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());     // LOCK MAX ERR
    {
      if (!new H_Hy_P1s_RTest(w).ok())
        return false;

      if (!new JmLagrrRTest(jm).ok())
        return false;

      LagrrBiR bi = new LagrrBiR(w, potOpt.getBasisOpt());
      log.dbg("LagrrBiR = ", bi);
      if (!new JmLagrrBiRTest(jm, bi).ok())
        return false;

      LgrrOrthR orth = new LgrrOrthR(w, potOpt.getBasisOpt());
      log.dbg("LgrrOrthR = ", orth);
      if (!new JmLagrrOrthRTest(orth).ok())
        return false;
      if (!new JmPotEigVecRTest(orth).ok())
        return false;

      EngOpt eng = potOpt.getGridEng();
      if (!new JmJnnRTest(jm, eng).ok())
        return false;
    }
    FlowTest.unlockMaxErr();      

    log.info("finished UCTestJmPotR");
    return true;
  }
}