package qm_station.jm;
import math.vec.Vec;
import flanagan.complex.Cmplx;
import scatt.eng.EngGrid;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.jm_2008.jm.theory.JmTheory;

import javax.utilx.log.Log;

import scatt.eng.EngModel;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 12/11/2008, Time: 16:37:07
 */
public class JmEngSC_NN1 extends EngGrid {
  public static Log log = Log.getLog(JmEngSC_NN1.class);
  private Vec sn;
  private Vec sn1;
  private Vec cn;
  private Vec cn1;
  public JmEngSC_NN1(LgrrOpt jmLagrr, EngModel engModel) {
    super(engModel);
    calc(jmLagrr);
  }
  private void calc(LgrrOpt jmLagrr) {
    int L = jmLagrr.getL();           log.dbg("L=", L);
    double lambda = jmLagrr.getLambda(); log.dbg("lambda=", lambda);
    int N = jmLagrr.getN();           log.dbg("N=", N);
    sn = new Vec(size());
    sn1 = new Vec(size());
    cn = new Vec(size());
    cn1 = new Vec(size());
    log.dbg("NOTE!!! assuming L=0");
    for (int i = 0; i < size(); i++) {
      double E = get(i);        log.dbg("E = ", E);
      Cmplx z = JmTheory.calcSCnL0byE(N, E, lambda); log.dbg("CS(E) = ", z);
      cn.set(i, z.getRe());
      sn.set(i, z.getIm());

      z = JmTheory.calcSCnL0byE(N - 1, E, lambda);
      cn1.set(i, z.getRe());
      sn1.set(i, z.getIm());
    }
  }
  public Vec getSn() {
    return sn;
  }
  public Vec getSn1() {
    return sn1;
  }
  public Vec getCn() {
    return cn;
  }
  public Vec getCn1() {
    return cn1;
  }
}
