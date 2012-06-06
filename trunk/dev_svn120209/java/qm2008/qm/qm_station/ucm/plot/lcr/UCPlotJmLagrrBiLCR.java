package qm_station.ucm.plot.lcr;
import atom.wf.lcr.WFQuadrLcr;
import math.vec.grid.StepGridOpt;
import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.ucm.plot.UCPlotFuncArr;
import scatt.jm_2008.e1.JmCalcOptE1;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.arr.FuncArr;
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
    JmCalcOptE1 model = project.getJmPotOptLcr();    // LCR
    StepGridOpt sg = model.getGridOpt();
    StepGrid x = new StepGrid(sg);    log.dbg("LCR grid = x =", x);
    WFQuadrLcr w = new WFQuadrLcr(x);     log.dbg("integration weights=", w);
    return new LagrrBiLcr(w, model.getLgrrModel() );
  }
}
