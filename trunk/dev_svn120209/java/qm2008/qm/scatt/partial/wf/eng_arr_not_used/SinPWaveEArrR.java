package scatt.partial.wf.eng_arr_not_used;
import math.func.Func;
import math.vec.Vec;
import scatt.eng.EngModel;
import scatt.partial.wf.SinPWaveFunc;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 12:52:31
 */
public class SinPWaveEArrR extends PWaveEArrR {
  public static String HELP = "Sin-like partial plain wave.";
//  public static Log log = Log.getLog(SinPWaveEArrR.class);
  public SinPWaveEArrR(Vec r, EngModel model, int L) {
    super(r, model, L);
  }
  protected Func makeFunc(double p, int L) { return new SinPWaveFunc(p, L); }
}
