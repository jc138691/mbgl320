package qm_station.ucm.plot;
import math.func.arr.FuncArr;
import math.func.FuncVec;
import qm_station.QMS;
import project.workflow.task.DefaultTaskUI;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 19/11/2008, Time: 10:39:50
 */
public abstract class UCPlotFuncVec extends UCPlotFuncArr {
  public UCPlotFuncVec(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  final public FuncArr makeFuncArr() {
    FuncVec f = makeFuncVec();
    FuncArr res = new FuncArr(f.getX(), 1);  // just ONE function
    res.set(0, f);
    return res;
  }
  public abstract FuncVec makeFuncVec();
}
