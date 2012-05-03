package scatt.jm_2008.jm.laguerre.lcr;
import atom.wf.WFQuadr;
import atom.wf.lcr.WFQuadrLcr;
import atom.wf.lcr.FuncRToDivSqrtCR;

import javax.utilx.log.Log;

import scatt.jm_2008.jm.laguerre.LagrrBiR;
import scatt.jm_2008.jm.laguerre.LgrrModel;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 10:15:16
 */
// this is done the same way as for LagrrLcr
public class LagrrBiLcr extends LagrrBiR {
  public static Log log = Log.getLog(LagrrBiLcr.class);
  public static String HELP = "The LCR transform of\n" + LagrrBiR.HELP;

  private WFQuadrLcr quadr;
  public LagrrBiLcr(WFQuadrLcr w, LgrrModel model) {
    super(w.getR(), model);     // NOTE!!! calculated on r
    mult(new FuncRToDivSqrtCR(w.getLcrToRFunc()));    // NOTE!!!  /qsrt(1+r)
    setX(w.getX());             // NOTE!!! but stores LCR as x
    quadr = w;
  }
  public WFQuadr getQuadr() {
    return quadr;
  }
}
