package qm_station.ucm.plot.lcr;
import atom.wf.log_cr.WFQuadrLcr;
import qm_station.ucm.plot.UCPlotFuncArr;
import qm_station.QMS;
import qm_station.QMSProject;
import scatt.jm_2008.e1.JmOptE1;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.arr.FuncArr;
import math.vec.grid.StepGrid;
import scatt.jm_2008.jm.laguerre.lcr.JmLgrrOrthLcr;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 13/11/2008, Time: 14:20:48
 */
public class UCPlotJmLagrrOrthLCR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotJmLagrrOrthLCR.class);
  public UCPlotJmLagrrOrthLCR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  public FuncArr makeFuncArr() {
    QMS project = QMSProject.getInstance();
    JmOptE1 model = project.getJmPotOptLcr();    // LCR
    StepGrid x = new StepGrid(model.getGrid());    log.dbg("LCR grid = x =", x);
    WFQuadrLcr w = new WFQuadrLcr(x);     log.dbg("integration weights=", w);
    return new JmLgrrOrthLcr(w, model.getJmModel() );
  }
}
