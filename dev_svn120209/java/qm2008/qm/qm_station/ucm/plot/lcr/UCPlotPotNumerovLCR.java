package qm_station.ucm.plot.lcr;
import atom.wf.log_cr.WFQuadrLcr;
import qm_station.ucm.plot.UCPlotFuncArr;
import qm_station.QMS;
import qm_station.QMSProject;
import scatt.jm_2008.e1.JmOptE1;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.arr.FuncArr;
import math.vec.grid.StepGridModel;
import math.vec.grid.StepGrid;
import math.vec.VecDbgView;
import scatt.partial.wf.SinPWaveLCR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 18/11/2008, Time: 13:23:23
 */
public class UCPlotPotNumerovLCR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotPotNumerovLCR.class);
  public UCPlotPotNumerovLCR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  protected void setupLogs() {
    super.setupLogs();
    add(log);
    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }
  public FuncArr makeFuncArr() {
    QMS project = QMSProject.getInstance();
    JmOptE1 model = project.getJmPotOptLcr();    // LCR
    StepGridModel sg = model.getGrid();
    StepGrid x = new StepGrid(sg);    log.dbg("LCR grid=", x);
    WFQuadrLcr w = new WFQuadrLcr(x); log.dbg("LCR integration weights, WFQuadrLcr=", new VecDbgView(w));
    return new SinPWaveLCR(w, model.getGridEng(), model.getJmModel().getL() ); //
  }
}
