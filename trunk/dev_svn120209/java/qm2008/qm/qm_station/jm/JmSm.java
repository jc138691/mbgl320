package qm_station.jm;
import math.vec.Vec;
import flanagan.complex.Cmplx;

import javax.utilx.log.Log;

import scatt.jm_2008.jm.laguerre.LgrrR;
import scatt.jm_2008.jm.theory.JmTheory;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 17/12/2008, Time: 14:08:35
 */
public class JmSm extends Vec {
//  public static String HELP = "S_n; calculated by numerical integration";
  public static Log log = Log.getLog(JmJmR.class);
  protected double E;

  public JmSm(LgrrR bi, double E) {
    super(bi.size());
    this.E = E;                      log.dbg("E=", E);
    calc(bi);
  }
  protected void calc(LgrrR arr) {
    int L = arr.getModel().getL();
    double lambda = arr.getModel().getLambda();
    for (int n = 0; n < size(); n++) {
      Cmplx z2 = JmTheory.calcSCnL0byE(n, E, lambda);
      set(n, z2.getIm());
    } log.dbg("res=\n", this);
  }
}