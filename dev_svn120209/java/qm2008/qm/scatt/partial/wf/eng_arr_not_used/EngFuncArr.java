package scatt.partial.wf.eng_arr_not_used;
import math.func.arr.FuncArr;
import math.vec.grid.StepGrid;
import math.vec.Vec;

import javax.utilx.log.Log;

import scatt.eng.EngModel;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 19/11/2008, Time: 10:11:44
 */
public class EngFuncArr extends FuncArr {
  public static Log log = Log.getLog(EngFuncArr.class);
  private EngModel engModel;
  private StepGrid eng;

  public EngFuncArr(Vec r, EngModel model) {
    super(r, model.getNumPoints());
    this.engModel = model;                          log.dbg("engModel = ", engModel);
    this.eng = new StepGrid(model);                 log.dbg("engGrid = ", eng);
  }
  public StepGrid getEng() {
    return eng;
  }
}
