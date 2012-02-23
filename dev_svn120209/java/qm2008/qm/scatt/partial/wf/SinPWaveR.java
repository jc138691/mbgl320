package scatt.partial.wf;
import math.func.FuncVec;
import math.vec.Vec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 23/07/2008, Time: 16:32:32
 */
public class SinPWaveR extends FuncVec {
  public static Log log = Log.getLog(SinPWaveR.class);
  public SinPWaveR(final Vec x, final double p, final int L) {
    super(x, new SinPWaveFunc(p, L));
    if (L > 0) {
      throw new IllegalArgumentException(log.error("todo L>0"));
    }
  }
}
