package atom.wf.lcr;

import math.func.Func;
import math.func.arr.FuncArr;
import math.vec.grid.StepGridModel;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 14/05/2010, 4:23:19 PM
 */
public class LcrFactory {

  public static FuncArr wfLcrToR(FuncArr lcrArr, WFQuadrLcr w) {
    FuncArr res = lcrArr.copyDeepY();
    res.mult(new FuncLcrToSqrtCr());    // NOTE!!!  *qsrt(c+r)
    res.setX(w.getR());
    return res;
  }
  public static FuncArr densLcrToR(FuncArr lcrArr, WFQuadrLcr w) {
    FuncArr res = lcrArr.copyDeepY();
    res.mult(new FuncLcrToCr());    // NOTE!!!  *(c+r)
    res.setX(w.getR());
    return res;
  }
  public static StepGridModel makeLcrFromR(double firstLcr, int nLcr, StepGridModel fromR) {
    Func rToLcr = new FuncRToLcr(firstLcr, fromR.getFirst());
    StepGridModel res = new StepGridModel();
    res.setFirst(rToLcr.calc(fromR.getFirst()));
    res.setLast(rToLcr.calc(fromR.getLast()));
    res.setNumPoints(nLcr);
    return res;
  }
}
