package scatt.partial.wf;
import atom.wf.log_cr.WFQuadrLcr;
import atom.wf.log_cr.FuncRToDivSqrtCR;
import scatt.eng.EngModel;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 14:07:09
 */
public class PWaveLcr extends PartWaveR {
  private WFQuadrLcr quadr;
  public PWaveLcr(WFQuadrLcr w, EngModel model, int L) {
    super(w.getR(), model, L);     // NOTE!!! calculated on r
    mult(new FuncRToDivSqrtCR(w.getLcrToRFunc())); // NOTE!!!  /qsrt(c+r)
    setX(w.getX());             // NOTE!!! but stores LCR as x
    quadr = w;
  }

  public WFQuadrLcr getQuadr() {
    return quadr;
  }
}
