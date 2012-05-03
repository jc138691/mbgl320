package scatt.jm_2008.jm.laguerre.lcr;
import atom.wf.lcr.WFQuadrLcr;
import scatt.jm_2008.jm.laguerre.IWFuncArr;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.laguerre.LgrrOrthR;

import javax.utilx.log.Log;

import atom.wf.lcr.FuncRToDivSqrtCR;
import math.func.polynom.laguerre.LgrrOrth;
import math.vec.Vec;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 16/09/2008, Time: 16:42:53
 */
public class LgrrOrthLcr extends LgrrOrth implements IWFuncArr {
  public static Log log = Log.getLog(LgrrOrthLcr.class);
  public static String HELP = "The LCT transform of\n" + LgrrOrthR.HELP;
  private WFQuadrLcr quadr;

  public LgrrOrthLcr(WFQuadrLcr w, LgrrModel model) {
    super(w.getR(), model.getN(), 2 * model.getL() + 2, model.getLambda());  // NOTE!!! calculated on r
    mult(new FuncRToDivSqrtCR(w.getLcrToRFunc()));    // NOTE!!!  /qsrt(c+r)
    setX(w.getX());             // NOTE!!! but stores LCR as x
    quadr = w;
  }

  public WFQuadrLcr getQuadr() {
    return quadr;
  }
  public Vec getR() {
    return quadr.getR();
  }
}