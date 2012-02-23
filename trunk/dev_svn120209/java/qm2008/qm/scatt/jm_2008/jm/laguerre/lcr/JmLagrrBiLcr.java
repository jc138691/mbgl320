package scatt.jm_2008.jm.laguerre.lcr;
import atom.wf.WFQuadr;
import atom.wf.log_cr.WFQuadrLcr;
import atom.wf.log_cr.FuncRToDivSqrtCR;

import javax.utilx.log.Log;

import scatt.jm_2008.jm.laguerre.JmLagrrBiR;
import scatt.jm_2008.jm.laguerre.LgrrModel;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 10:15:16
 */
// this is done the same way as for JmLagrrLcr
public class JmLagrrBiLcr extends JmLagrrBiR {
  public static Log log = Log.getLog(JmLagrrBiLcr.class);
  public static String HELP = "The LCR transform of\n" + JmLagrrBiR.HELP;

  private WFQuadrLcr quadr;
  public JmLagrrBiLcr(WFQuadrLcr w, LgrrModel model) {
    super(w.getR(), model);     // NOTE!!! calculated on r
    mult(new FuncRToDivSqrtCR(w.getLcrToRFunc()));    // NOTE!!!  /qsrt(1+r)
    setX(w.getX());             // NOTE!!! but stores LCR as x
    quadr = w;
  }
  public WFQuadr getQuadr() {
    return quadr;
  }
}
