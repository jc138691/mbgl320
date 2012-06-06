package qm_station.ucm.plot.lcr;
import atom.wf.lcr.WFQuadrLcr;
import math.func.arr.FuncArr;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import scatt.jm_2008.e1.JmCalcOptE1;
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
    JmCalcOptE1 model = project.getJmPotOptLcr();    // LCR
    StepGridOpt sg = model.getGridOpt();
    StepGrid x = new StepGrid(sg);    log.dbg("LCR grid = x =", x);
    WFQuadrLcr w = new WFQuadrLcr(x);     log.dbg("integration weights=", w);
    return new LagrrLcr(w, model.getBasisOpt() );
  }
}