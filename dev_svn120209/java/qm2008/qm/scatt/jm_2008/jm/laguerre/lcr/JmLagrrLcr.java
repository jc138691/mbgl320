package scatt.jm_2008.jm.laguerre.lcr;
import javax.utilx.log.Log;

import atom.wf.log_cr.WFQuadrLcr;
import atom.wf.log_cr.FuncRToDivSqrtCR;
import scatt.jm_2008.jm.laguerre.JmLgrrR;
import scatt.jm_2008.jm.laguerre.LgrrModel;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 16:14:06
 */
public class JmLagrrLcr extends JmLgrrR {
  public static Log log = Log.getLog(JmLgrrR.class);
  public static String HELP = "The LCR transform of\n" + JmLgrrR.HELP;
  private WFQuadrLcr quadr;
  public JmLagrrLcr(WFQuadrLcr w, LgrrModel model) {
    super(w.getR(), model);     // NOTE!!! calculated on r
    mult(new FuncRToDivSqrtCR(w.getLcrToRFunc())); // NOTE!!!  /qsrt(c+r)
    setX(w.getX());             // NOTE!!! but stores LCR as x
    quadr = w;
  }

  public WFQuadrLcr getQuadrLCR() {
    return quadr;
  }
}