package qm_station.ucm.plot.lcr;
import atom.wf.log_cr.WFQuadrLcr;
import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.ucm.plot.UCPlotFuncArr;
import scatt.jm_2008.e1.CalcOptE1;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.arr.FuncArr;
import math.vec.grid.StepGridModel;
import math.vec.grid.StepGrid;
import scatt.jm_2008.jm.laguerre.lcr.LagrrBiLcr;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 15:15:16
 */
public class UCPlotJmLagrrBiLCR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotJmLagrrBiLCR.class);
  public UCPlotJmLagrrBiLCR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  public FuncArr makeFuncArr() {
    QMS project = QMSProject.getInstance();
    CalcOptE1 model = project.getJmPotOptLcr();    // LCR
    StepGridModel sg = model.getGrid();
    StepGrid x = new StepGrid(sg);    log.dbg("LCR grid = x =", x);
    WFQuadrLcr w = new WFQuadrLcr(x);     log.dbg("integration weights=", w);
    return new LagrrBiLcr(w, model.getLgrrModel() );
  }
}
