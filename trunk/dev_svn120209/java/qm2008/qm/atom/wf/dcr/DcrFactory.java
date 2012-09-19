package atom.wf.dcr;
import atom.wf.dcr.func.FuncRToDcr;
import atom.wf.lcr.func.FuncRToLcr;
import math.func.Func;
import math.vec.grid.StepGridOpt;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 17/09/12, 10:13 AM
 */
public class DcrFactory {
public static StepGridOpt makeDcrFromR(double firstX, int nX, StepGridOpt fromR) {
  Func rToX = new FuncRToDcr(firstX, fromR.getFirst());
  StepGridOpt res = new StepGridOpt();
  res.setFirst(rToX.calc(fromR.getFirst()));
  res.setLast(rToX.calc(fromR.getLast()));
  res.setNumPoints(nX);
  return res;
}
}
