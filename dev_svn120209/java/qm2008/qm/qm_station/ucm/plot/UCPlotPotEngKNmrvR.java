package qm_station.ucm.plot;
import qm_station.QMS;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.vec.grid.StepGrid;
import math.func.FuncVec;
import scatt.partial.wf.eng_arr_not_used.EngFuncArr;
import scatt.partial.wf.k_matrix_idea.KMtrxEngR;
import scatt.partial.wf.numerov.NmrvPotR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 19/11/2008, Time: 10:03:31
 */
public class UCPlotPotEngKNmrvR extends UCPlotFuncVec {
  public static Log log = Log.getLog(UCPlotPotEngKNmrvR.class);
  public UCPlotPotEngKNmrvR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  protected void setupLogs() {
    add(log);
    add(StepGrid.log);
    add(NmrvPotR.log);
    add(KMtrxEngR.log);
//    add(KMtrxR.log);

    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }
  public FuncVec makeFuncVec() {
    UCPlotPotNmrvR uc = new UCPlotPotNmrvR(getDefaultUi());
    EngFuncArr arr = (EngFuncArr)uc.makeFuncArr();
    return new KMtrxEngR(arr); //
  }
}