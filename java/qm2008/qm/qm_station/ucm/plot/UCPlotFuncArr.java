package qm_station.ucm.plot;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.DefaultResView;
import project.workflow.task.UCRunDefaultTask;
import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.ui.StepGridView;

import javax.utilx.log.Log;

import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.func.view.FuncArrPlot;
import math.func.view.FuncPlotView;
import math.vec.grid.StepGrid;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 29/09/2008, Time: 15:57:51
 */
public abstract class UCPlotFuncArr extends UCRunDefaultTask<QMS> {
  public static Log log = Log.getLog(UCPlotFuncArr.class);

  public UCPlotFuncArr(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  protected void setupLogs() {
    add(log);
    add(StepGrid.log);
    add(StepGridView.log);
//    add(FuncArrPlot.log);
//    add(FuncPlotView.log);
    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }

  public boolean run() {
    DefaultResView out = getOutView();
    addView(out.getSysView());    log.info("run UCPlotFuncArr");

    QMS project = QMSProject.getInstance();
    getOptView().loadTo(project);
    project.saveProjectToDefaultLocation();    
    FuncArr funcArr = makeFuncArr();    log.dbg("FuncArr = ", new FuncArrDbgView(funcArr));

//    FuncVec test =  new FuncVec(r, new FuncLinear(0, 1));
    FuncArrPlot arr = new FuncArrPlot(funcArr);
    FuncPlotView view = new FuncPlotView(arr);
    view.setShowX(true);
    out.setPlotView(view);  // set focus

    log.dbg("end UCPlotFuncArr");
    return true;
  }
  public FuncArr makeFuncArr() {return null;}
}