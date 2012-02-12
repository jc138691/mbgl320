package scatt.partial.wf.numerov;
import math.func.FuncVec;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 19/12/2008, Time: 10:45:18
 */
public class PotScattNmrvAlg {
  public static String HELP = "Partial scattering wave (via Numerov algorithm)";
  public static Log log = Log.getLog(PotScattNmrvAlg.class);
  protected int L;
  private FuncVec pot;
  public PotScattNmrvAlg(FuncVec pot, double E, int L) {
    this.pot = pot;
    this.L = L;
    if (L > 0) {
      throw new IllegalArgumentException(log.error("todo L>0"));
    }
//    load();
  }
}
