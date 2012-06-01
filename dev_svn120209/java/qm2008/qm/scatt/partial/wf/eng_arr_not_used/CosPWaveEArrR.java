package scatt.partial.wf.eng_arr_not_used;
import math.func.Func;
import math.vec.Vec;
import scatt.eng.EngModel;
import scatt.partial.wf.CosLFunc;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/10/2008, Time: 13:11:24
 */
public class CosPWaveEArrR extends PWaveEArrR {
  public static String HELP = "Cos-like partial plain wave.";
//  public static Log log = Log.getLog(SinPWaveEArrR.class);
  public CosPWaveEArrR(Vec r, EngModel model, int L) {
    super(r, model, L);
  }
  protected Func makeFunc(double p, int L) { return new CosLFunc(p, L); }
}
