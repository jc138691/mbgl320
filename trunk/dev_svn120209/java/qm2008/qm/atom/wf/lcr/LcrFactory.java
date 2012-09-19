package atom.wf.lcr;

import atom.wf.lcr.func.FuncLcrToCr;
import atom.wf.lcr.func.FuncLcrToSqrtCr;
import atom.wf.lcr.func.FuncRToLcr;
import math.func.Func;
import math.func.arr.FuncArr;
import math.vec.grid.StepGridOpt;
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
  public static StepGridOpt makeLcrFromR(double firstX, int nLcr, StepGridOpt fromR) {
    Func rToX = new FuncRToLcr(firstX, fromR.getFirst());
    StepGridOpt res = new StepGridOpt();
    res.setFirst(rToX.calc(fromR.getFirst()));
    res.setLast(rToX.calc(fromR.getLast()));
    res.setNumPoints(nLcr);
    return res;
  }
}
