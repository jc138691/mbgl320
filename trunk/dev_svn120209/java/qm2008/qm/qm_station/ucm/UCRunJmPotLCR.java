package qm_station.ucm;
import atom.wf.lcr.WFQuadrLcr;
import qm_station.ui.scatt.CalcOptLcr;
import qm_station.QMS;
import qm_station.QMSProject;

import javax.utilx.log.Log;

import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;
import math.vec.Vec;
import atom.wf.coulomb.WfFactory;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.UCRunDefaultTask;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 24/09/2008, Time: 11:32:18
 */
public class UCRunJmPotLCR extends UCRunDefaultTask<QMS> {
  public static Log log = Log.getLog(UCRunJmPotR.class);
  public UCRunJmPotLCR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
//  protected void setupLogs() {
//    logs.add(log);
//    logs.add(StepGrid.log);
//    logs.add(StepGridView.log);
//  }

  public boolean run() {
//    setupLogs();
//    logs.setDebug(true);
//    logs.addView(getResView().getDebugView());

    log.dbg("run()");
    QMS project = QMSProject.getInstance();
    CalcOptLcr model = project.getJmPotOptLcr();
    StepGridOpt sg = model.getGridOpt();
    StepGrid x = new StepGrid(sg);    log.dbg("LogCR grid = x =", x);
    WFQuadrLcr w = new WFQuadrLcr(x);     log.dbg("integration weights=", w);
    Vec r = w.getR();
    Vec pot = WfFactory.makePotHy_1s(r);  log.dbg("V_1s(r)=", pot);


//    QMSMainUI ui = QMSMainUI.getInstance();
//
//    MenuScattUI scattUI = ui.getScattUI(); // NOTE: it makes it as well
//    JmPotOptView optView = new JmPotOptView();
//    DefaultTaskUI unit = new DefaultTaskUI(optView);

    // todo: attach UCC
//    scattUI.setPotUI(unit);

    getOutView().getSysView().println("running UCRunJmPotR");
//    logs.setDebug(false);
    return true;
  }
}