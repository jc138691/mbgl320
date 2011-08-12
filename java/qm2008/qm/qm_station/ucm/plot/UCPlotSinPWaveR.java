package qm_station.ucm.plot;
import qm_station.QMS;
import qm_station.QMSProject;
import scatt.jm_2008.e1.JmOptE1;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.arr.FuncArr;
import math.vec.grid.StepGridModel;
import math.vec.grid.StepGrid;
import scatt.partial.wf.SinPWaveR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 11:59:26
 */
public class UCPlotSinPWaveR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotJmLagrrR.class);
  public UCPlotSinPWaveR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  public FuncArr makeFuncArr() {
    QMS project = QMSProject.getInstance();
    JmOptE1 model = project.getJmPotOptR();    // R
    StepGridModel sg = model.getGrid();
    StepGrid r = new StepGrid(sg);    log.dbg("r grid=", r);
    return new SinPWaveR(r, model.getGridEng(), model.getJmModel().getL() ); //
  }
}