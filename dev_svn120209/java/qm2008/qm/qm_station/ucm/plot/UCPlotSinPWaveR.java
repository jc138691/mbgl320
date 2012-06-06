package qm_station.ucm.plot;
import math.vec.grid.StepGridOpt;
import qm_station.QMS;
import qm_station.QMSProject;
import scatt.jm_2008.e1.JmCalcOptE1;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.arr.FuncArr;
import math.vec.grid.StepGrid;
import scatt.partial.wf.eng_arr_not_used.SinPWaveEArrR;
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
    JmCalcOptE1 model = project.getJmPotOptR();    // R
    StepGridOpt sg = model.getGridOpt();
    StepGrid r = new StepGrid(sg);    log.dbg("r grid=", r);
    return new SinPWaveEArrR(r, model.getGridEng(), model.getLgrrModel().getL() ); //
  }
}