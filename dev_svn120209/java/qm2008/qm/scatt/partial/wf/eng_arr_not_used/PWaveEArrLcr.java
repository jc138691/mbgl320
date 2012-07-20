package scatt.partial.wf.eng_arr_not_used;
import atom.wf.lcr.WFQuadrLcr;
import atom.wf.lcr.FuncRToDivSqrtCR;
import scatt.eng.EngOpt;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 14:07:09
 */
public class PWaveEArrLcr extends PWaveEArrR {
  private WFQuadrLcr quadr;
  public PWaveEArrLcr(WFQuadrLcr w, EngOpt model, int L) {
    super(w.getR(), model, L);     // NOTE!!! calculated on r
    mult(new FuncRToDivSqrtCR(w.getLcrToRFunc())); // NOTE!!!  /qsrt(c+r)
    setX(w.getX());             // NOTE!!! but stores LCR as x
    quadr = w;
  }

  public WFQuadrLcr getQuadr() {
    return quadr;
  }
}
