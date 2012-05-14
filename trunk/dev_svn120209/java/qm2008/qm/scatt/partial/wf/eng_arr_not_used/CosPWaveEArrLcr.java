package scatt.partial.wf.eng_arr_not_used;
import atom.wf.lcr.WFQuadrLcr;
import scatt.eng.EngModel;
import math.func.Func;
import scatt.partial.wf.CosPWaveFunc;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 14:11:32
 */
public class CosPWaveEArrLcr extends PWaveEArrLcr {
  public static String HELP = "Cos-like partial plain wave after the LCR transform.";
  public CosPWaveEArrLcr(WFQuadrLcr w, EngModel model, int L) {
    super(w, model, L);
  }
  protected Func makeFunc(double p, int L) { return new CosPWaveFunc(p, L); }
}