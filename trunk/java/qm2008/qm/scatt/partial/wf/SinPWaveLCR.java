package scatt.partial.wf;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.Func;
import scatt.eng.EngModel;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 14:09:58
 */
public class SinPWaveLCR extends PWaveLcr {
  public static String HELP = "Sin-like partial plain wave after the LCR transform.";
  public SinPWaveLCR(WFQuadrLcr w, EngModel model, int L) {
    super(w, model, L);
  }
  protected Func makeFunc(double p, int L) { return new SinPartPWaveFunc(p, L); }
}