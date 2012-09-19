package scatt.jm_2008.jm.laguerre.lcr;
import javax.utilx.log.Log;

import atom.wf.lcr.WFQuadrLcr;
import atom.wf.lcr.func.FuncRToDivSqrtCR;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.jm_2008.jm.laguerre.LgrrR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 16:14:06
 */
public class LagrrLcr extends LgrrR {
  public static Log log = Log.getLog(LgrrR.class);
  public static String HELP = "The LCR transform of\n" + LgrrR.HELP;
  private WFQuadrLcr quadr;
  public LagrrLcr(WFQuadrLcr w, LgrrOpt model) {
    super(w.getR(), model);     // NOTE!!! calculated on r
    mult(new FuncRToDivSqrtCR(w.getLcrToRFunc())); // NOTE!!!  /qsrt(c+r)
    setX(w.getX());             // NOTE!!! but stores LCR as x
    quadr = w;
  }

  public WFQuadrLcr getQuadrLCR() {
    return quadr;
  }
}