package scatt.partial.wf;

import math.func.FuncVec;
import math.vec.Vec;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 14/05/2010, 9:06:26 AM
 */
public class CosPWaveR extends FuncVec {
  public static Log log = Log.getLog(CosPWaveR.class);
  public CosPWaveR(final Vec x, final double p, final int L) {
    super(x, new CosLFunc(p, L));
    if (L > 0) {
      throw new IllegalArgumentException(log.error("todo L>0"));
    }
  }
}
