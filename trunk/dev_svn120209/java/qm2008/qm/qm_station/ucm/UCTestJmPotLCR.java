package qm_station.ucm;
import atom.wf.lcr.WFQuadrLcr;
import math.vec.grid.StepGridOpt;
import papers.project_setup.ProjTestOpt;
import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.jm.*;
import scatt.eng.EngOpt;
import scatt.jm_2008.e1.JmCalcOptE1;
import qm_station.ui.StepGridView;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.DefaultResView;
import project.workflow.task.UCRunDefaultTask;
import project.workflow.task.test.FlowTest;

import javax.utilx.log.Log;
import javax.swingx.textx.TextView;

import math.vec.grid.StepGrid;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.func.arr.FuncArrDbgView;
import scatt.jm_2008.jm.laguerre.lcr.*;
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

    JmCalcOptE1 potOpt = project.getJmPotOptLcr();    // LCR
    StepGridOpt sg = potOpt.getGridOpt();           log.dbg("StepGridOpt = ", sg);
    StepGrid x = new StepGrid(sg);                log.dbg("LogCR grid, StepGrid=", new VecDbgView(x));
    WFQuadrLcr w = new WFQuadrLcr(x);             log.dbg("integration weights, WFQuadrLcr=", new VecDbgView(w));
    Vec r = w.getR();                             log.dbg("StepGrid r = ", new VecDbgView(r));
                                                  log.dbg("LgrrOpt = ", potOpt.getBasisOpt());
    LagrrLcr basis = new LagrrLcr(w, potOpt.getBasisOpt() );   log.dbg("LagrrLcr = ", new FuncArrDbgView(basis));

    // RUN ALL TESTS
    ProjTestOpt testOpt = potOpt.getTestOpt();
    FlowTest.setMaxErr(testOpt.getMaxIntgrlErr());
    FlowTest.setLog(log);
//    if (!new PartHMtrxLCRTest(w).ok())
//      return false;

    if (!new LagrrLcrTest(basis).ok())
        return false;

    LagrrBiLcr bi = new LagrrBiLcr(w, potOpt.getBasisOpt() );  log.dbg("LagrrBiLcr = ", new FuncArrDbgView(bi));
    if (!new LagrrBiLcrTest(basis, bi).ok())
      return false;

    LgrrOrthLcr orth = new LgrrOrthLcr(w, potOpt.getBasisOpt() );  log.dbg("LgrrOrthLcr = ", new FuncArrDbgView(bi));
    if (!new AnyOrthTest(orth).ok())
      return false;

    double Z = 1; // for hydrogen 
    if (!new PotEigVecLcrTest(Z, orth).ok())
      return false;

    EngOpt eng = potOpt.getGridEng();
    if (!new JmJnnLCRTest(basis, eng).ok())
      return false;

    log.info("finished UCTestJmPotLCR");
    return true;
  }
}
