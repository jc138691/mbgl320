package qm_station.ucm.plot;
import qm_station.QMS;
import qm_station.QMSProject;
import scatt.jm_2008.e1.CalcOptE1;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.arr.FuncArr;
import math.vec.grid.StepGrid;
import scatt.jm_2008.jm.laguerre.LgrrOrthR;
import atom.wf.WFQuadrR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 13/11/2008, Time: 14:23:56
 */
public class UCPlotJmLagrrOrthR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotJmLagrrOrthR.class);
  public UCPlotJmLagrrOrthR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  public FuncArr makeFuncArr() {
    QMS project = QMSProject.getInstance();
    CalcOptE1 model = project.getJmPotOptR();    // R
    StepGrid r = new StepGrid(model.getGrid());    log.dbg("r grid=", r);
    WFQuadrR w = new WFQuadrR(r);     log.dbg("integration weights=", w);
    return new LgrrOrthR(w, model.getLgrrModel() );
  }
}