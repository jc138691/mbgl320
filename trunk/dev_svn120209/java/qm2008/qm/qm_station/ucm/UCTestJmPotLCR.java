package qm_station.ucm;
import atom.wf.log_cr.WFQuadrLcr;
import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.jm.*;
import scatt.jm_2008.e1.CalcOptE1;
import scatt.eng.EngModel;
import qm_station.ui.StepGridView;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.DefaultResView;
import project.workflow.task.UCRunDefaultTask;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;
import javax.swingx.textx.TextView;

import math.vec.grid.StepGridModel;
import math.vec.grid.StepGrid;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.func.arr.FuncArrDbgView;
import scatt.jm_2008.jm.laguerre.lcr.*;
import scatt.jm_2008.jm.TestModel;
import atom.wf.coulomb.CoulombWFFactory;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 22/10/2008, Time: 13:04:14
 */
public class UCTestJmPotLCR extends UCRunDefaultTask<QMS> {
  public static Log log = Log.getLog(UCRunJmPotR.class);
  public UCTestJmPotLCR(DefaultTaskUI<QMS> ui) {
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
    addView(out.getSysView());    log.info("run UCTestJmPotLCR");

    QMS project = QMSProject.getInstance();
    getOptView().loadTo(project);
    project.saveProjectToDefaultLocation();

    CalcOptE1 potOpt = project.getJmPotOptLcr();    // LCR
    StepGridModel sg = potOpt.getGrid();           log.dbg("StepGridModel = ", sg);
    StepGrid x = new StepGrid(sg);                log.dbg("LogCR grid, StepGrid=", new VecDbgView(x));
    WFQuadrLcr w = new WFQuadrLcr(x);             log.dbg("integration weights, WFQuadrLcr=", new VecDbgView(w));
    Vec r = w.getR();                             log.dbg("StepGrid r = ", new VecDbgView(r));
                                                  log.dbg("LgrrModel = ", potOpt.getLgrrModel());
    LagrrLcr basis = new LagrrLcr(w, potOpt.getLgrrModel() );   log.dbg("LagrrLcr = ", new FuncArrDbgView(basis));

    // RUN ALL TESTS
    TestModel testOpt = potOpt.getTestModel();
    FlowTest.setMaxErr(testOpt.getMaxIntgrlErr());
    FlowTest.setLog(log);
    if (!new PartHMtrxLCRTest(w).ok())
      return false;

    if (!new LagrrLcrTest(basis).ok())
        return false;

    LagrrBiLcr bi = new LagrrBiLcr(w, potOpt.getLgrrModel() );  log.dbg("LagrrBiLcr = ", new FuncArrDbgView(bi));
    if (!new JmLagrrBiLcrTest(basis, bi).ok())
      return false;

    LgrrOrthLcr orth = new LgrrOrthLcr(w, potOpt.getLgrrModel() );  log.dbg("LgrrOrthLcr = ", new FuncArrDbgView(bi));
    if (!new LgrrOrthLcrTest(orth).ok())
      return false;

    double Z = 1; // for hydrogen 
    if (!new JmPotEigVecLcrTest(Z, orth).ok())
      return false;

    EngModel eng = potOpt.getGridEng();
    if (!new JmJnnLCRTest(basis, eng).ok())
      return false;

    Vec pot = CoulombWFFactory.makePotHy_1s(r);  log.dbg("V_1s(r)=", pot);

//    QMSMainUI ui = QMSMainUI.getInstance();
//    MenuScattUI scattUI = ui.getScattUI(); // NOTE: it makes it as well
//    JmPotOptView optView = new JmPotOptView();
//    DefaultTaskUI unit = new DefaultTaskUI(optView);

    log.info("finished UCTestJmPotLCR");
    return true;
  }
}
