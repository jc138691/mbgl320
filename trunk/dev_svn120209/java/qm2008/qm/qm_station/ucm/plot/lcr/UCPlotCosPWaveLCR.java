package qm_station.ucm.plot.lcr;
import atom.wf.lcr.WFQuadrLcr;
import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.ucm.plot.UCPlotFuncArr;
import qm_station.ucm.plot.UCPlotJmLagrrR;
import scatt.jm_2008.e1.CalcOptE1;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.arr.FuncArr;
import math.vec.grid.StepGridModel;
import math.vec.grid.StepGrid;
import math.vec.VecDbgView;
import scatt.partial.wf.eng_arr_not_used.CosPWaveEArrLcr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 13:57:38
 */
public class UCPlotCosPWaveLCR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotJmLagrrR.class);
  public UCPlotCosPWaveLCR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  public FuncArr makeFuncArr() {
    QMS project = QMSProject.getInstance();
    CalcOptE1 model = project.getJmPotOptLcr();    // LCR
    StepGridModel sg = model.getGrid();
    StepGrid x = new StepGrid(sg);    log.dbg("LCR grid=", x);
    WFQuadrLcr w = new WFQuadrLcr(x);             log.dbg("LCR integration weights, WFQuadrLcr=", new VecDbgView(w));
    return new CosPWaveEArrLcr(w, model.getGridEng(), model.getLgrrModel().getL() ); //
  }
}
