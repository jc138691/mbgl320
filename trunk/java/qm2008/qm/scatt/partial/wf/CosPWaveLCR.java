package scatt.partial.wf;
import atom.wf.log_cr.WFQuadrLcr;
import scatt.eng.EngModel;
import math.func.Func;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 14:11:32
 */
public class CosPWaveLCR extends PWaveLcr {
  public static String HELP = "Cos-like partial plain wave after the LCR transform.";
  public CosPWaveLCR(WFQuadrLcr w, EngModel model, int L) {
    super(w, model, L);
  }
  protected Func makeFunc(double p, int L) { return new CosPartPWaveFunc(p, L); }
}