package qm_station.ucm.plot.lcr;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.arr.FuncArr;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import scatt.jm_2008.e1.CalcOptE1;
import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.ucm.plot.UCPlotFuncArr;
import qm_station.ucm.plot.UCPlotJmLagrrR;
import scatt.jm_2008.jm.laguerre.lcr.LagrrLcr;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 29/09/2008, Time: 15:57:18
 */
public class UCPlotJmLagrrLCR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotJmLagrrR.class);
  public UCPlotJmLagrrLCR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  public FuncArr makeFuncArr() {
    QMS project = QMSProject.getInstance();
    CalcOptE1 model = project.getJmPotOptLcr();    // LCR
    StepGridModel sg = model.getGrid();
    StepGrid x = new StepGrid(sg);    log.dbg("LCR grid = x =", x);
    WFQuadrLcr w = new WFQuadrLcr(x);     log.dbg("integration weights=", w);
    return new LagrrLcr(w, model.getLgrrModel() );
  }
}