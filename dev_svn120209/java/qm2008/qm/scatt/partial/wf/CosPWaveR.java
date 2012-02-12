package scatt.partial.wf;
import math.func.Func;
import math.vec.Vec;
import scatt.eng.EngModel;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 13:11:24
 */
public class CosPWaveR extends PartWaveR {
  public static String HELP = "Cos-like partial plain wave.";
//  public static Log log = Log.getLog(SinPWaveR.class);
  public CosPWaveR(Vec r, EngModel model, int L) {
    super(r, model, L);
  }
  protected Func makeFunc(double p, int L) { return new CosPartPWaveFunc(p, L); }
}
