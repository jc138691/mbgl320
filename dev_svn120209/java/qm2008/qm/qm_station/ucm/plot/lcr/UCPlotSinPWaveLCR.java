package qm_station.ucm.plot.lcr;
import atom.wf.lcr.WFQuadrLcr;
import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.ucm.plot.UCPlotFuncArr;
import qm_station.ucm.plot.UCPlotJmLagrrR;
import scatt.jm_2008.e1.JmCalcOptE1;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.arr.FuncArr;
import math.vec.grid.StepGridOpt;
import math.vec.grid.StepGrid;
import math.vec.VecDbgView;
import scatt.partial.wf.eng_arr_not_used.SinPWaveEArrLcr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 13:56:08
 */
public class UCPlotSinPWaveLCR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotJmLagrrR.class);
  public UCPlotSinPWaveLCR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  protected void setupLogs() {
    super.setupLogs();
    add(log);
    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }
  public FuncArr makeFuncArr() {
    QMS project = QMSProject.getInstance();
    JmCalcOptE1 model = project.getJmPotOptLcr();    // LCR
    StepGridOpt sg = model.getGridOpt();
    StepGrid x = new StepGrid(sg);    log.dbg("LCR grid=", x);
    WFQuadrLcr w = new WFQuadrLcr(x); log.dbg("LCR integration weights, WFQuadrLcr=", new VecDbgView(w));
    return new SinPWaveEArrLcr(w, model.getGridEng(), model.getBasisOpt().getL() ); //
  }
}
