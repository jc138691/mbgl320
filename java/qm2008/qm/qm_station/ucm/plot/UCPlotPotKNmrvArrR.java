package qm_station.ucm.plot;
import qm_station.QMS;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.vec.grid.StepGrid;
import math.func.arr.FuncArr;
import scatt.partial.wf.numerov.NmrvPotR;
import scatt.partial.wf.EngFuncArr;
import scatt.partial.wf.KMtrxFuncArrR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 19/11/2008, Time: 12:32:34
 */
public class UCPlotPotKNmrvArrR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotPotNmrvR.class);
  public UCPlotPotKNmrvArrR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  protected void setupLogs() {
    add(log);
    add(StepGrid.log);
    add(NmrvPotR.log);
    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }
  public FuncArr makeFuncArr() {
    UCPlotPotNmrvR uc = new UCPlotPotNmrvR(getDefaultUi());
    EngFuncArr arr = (EngFuncArr)uc.makeFuncArr();
    return new KMtrxFuncArrR(arr);
  }
}
