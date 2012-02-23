package qm_station.ucm;
import javax.utilx.log.Log;

import qm_station.ui.scatt.CalcOptR;
import qm_station.QMS;
import qm_station.QMSProject;
import math.vec.grid.StepGridModel;
import math.vec.grid.StepGrid;
import math.vec.Vec;
import atom.wf.WFQuadrR;
import atom.wf.coulomb.CoulombWFFactory;
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

    StepGridModel sg = model.getGrid();
    StepGrid r = new StepGrid(sg);    log.dbg("r grid=", r);
    WFQuadrR w = new WFQuadrR(r);     log.dbg("integration weights=", w);
    Vec pot = CoulombWFFactory.makePotHy_1s(r);  log.dbg("V_1s(r)=", pot);


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