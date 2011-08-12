package qm_station.ucm.plot;
import project.workflow.task.DefaultTaskUI;
import scatt.jm_2008.e1.JmOptE1;
import qm_station.QMS;
import qm_station.QMSProject;

import javax.utilx.log.Log;

import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import math.func.arr.FuncArr;
import scatt.jm_2008.jm.laguerre.JmLgrrR;
import atom.wf.WFQuadrR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 26/09/2008, Time: 17:10:49
 */
public class UCPlotJmLagrrR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotJmLagrrR.class);
  public UCPlotJmLagrrR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  public FuncArr makeFuncArr() {
    QMS project = QMSProject.getInstance();
    JmOptE1 model = project.getJmPotOptR();    // R
    StepGridModel sg = model.getGrid();
    StepGrid r = new StepGrid(sg);    log.dbg("r grid=", r);
    WFQuadrR w = new WFQuadrR(r);
    return new JmLgrrR(w, model.getJmModel() );
  }
}