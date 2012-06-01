package scatt.partial.wf.eng_arr_not_used;
import atom.wf.lcr.WFQuadrLcr;
import math.func.Func;
import scatt.eng.EngModel;
import scatt.partial.wf.SinLFunc;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 14:09:58
 */
public class SinPWaveEArrLcr extends PWaveEArrLcr {
  public static String HELP = "Sin-like partial plain wave after the LCR transform.";
  public SinPWaveEArrLcr(WFQuadrLcr w, EngModel model, int L) {
    super(w, model, L);
  }
  protected Func makeFunc(double p, int L) { return new SinLFunc(p, L); }
}