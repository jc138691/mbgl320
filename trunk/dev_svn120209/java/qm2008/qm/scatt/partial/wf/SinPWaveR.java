package scatt.partial.wf;
import math.func.Func;
import math.vec.Vec;
import scatt.eng.EngModel;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 12:52:31
 */
public class SinPWaveR extends PartWaveR {
  public static String HELP = "Sin-like partial plain wave.";
//  public static Log log = Log.getLog(SinPWaveR.class);
  public SinPWaveR(Vec r, EngModel model, int L) {
    super(r, model, L);
  }
  protected Func makeFunc(double p, int L) { return new SinPartPWaveFunc(p, L); }
}
