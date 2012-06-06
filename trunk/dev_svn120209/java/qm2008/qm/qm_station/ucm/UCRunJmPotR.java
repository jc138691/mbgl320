package qm_station.ucm;
import javax.utilx.log.Log;

import atom.wf.coulomb.WfFactory;
import qm_station.ui.scatt.CalcOptR;
import qm_station.QMS;
import qm_station.QMSProject;
import math.vec.grid.StepGridOpt;
import math.vec.grid.StepGrid;
import math.vec.Vec;
import atom.wf.WFQuadrR;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.UCRunDefaultTask;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 15:47:07
 */
public class UCRunJmPotR extends UCRunDefaultTask<QMS> {
  public static Log log = Log.getLog(UCRunJmPotR.class);
  public UCRunJmPotR(DefaultTaskUI<QMS> ui) {
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
    CalcOptR model = project.getJmPotOptR();

    StepGridOpt sg = model.getGridOpt();
    StepGrid r = new StepGrid(sg);    log.dbg("r grid=", r);
    WFQuadrR w = new WFQuadrR(r);     log.dbg("integration weights=", w);
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