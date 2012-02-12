package scatt.jm_2008.e1;
import flanagan.complex.Cmplx;
import math.func.FuncVec;
import math.vec.Vec;
import scatt.Scatt;
import scatt.jm_2008.jm.JmRes;
import scatt.jm_2008.jm.laguerre.JmLgrrModel;
import scatt.jm_2008.jm.theory.JmTheory;

import javax.utilx.log.Log;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 28/08/2008, Time: 16:59:55
 */
public class JmMethodE1 extends JmMethodBaseE1 {   // E1 - one electron
  public static Log log = Log.getLog(JmMethodE1.class);
  public JmMethodE1(JmOptE1 jmOpt) {
    super(jmOpt);
  }

  public JmRes calc(Vec engs) {
    JmLgrrModel model = jmOpt.getJmModel();
    int N = model.getN();
    double lambda = model.getLambda();
    JmRes res = new JmRes();
//    Vec arrR = new Vec(engs.size());
    FuncVec arrCross = new FuncVec(engs);
    FuncVec arrShift = new FuncVec(engs);
    for (int i = 0; i < engs.size(); i++) {              log.dbg("i = ", i);
      double E = engs.get(i);                            log.dbg("E = ", E);
      double Jnn = JmTheory.calcJnnL0byE(N, E, lambda);    log.dbg("Jnn = ", Jnn);
      Cmplx sc = JmTheory.calcSCnL0byE(N, E, lambda);      log.dbg("sc_N = ", sc);
      Cmplx sc1 = JmTheory.calcSCnL0byE(N - 1, E, lambda); log.dbg("sc_{N-1} = ", sc1);
      double G = calcG(E);                                 log.dbg("G = ", G);
      double g = Jnn * G;                                  log.dbg("g = ", g);
      double sN1 = sc1.getIm();
      double cN1 = sc1.getRe();
      double sN = sc.getIm();
      double cN = sc.getRe();                              
      double R = -(sN1 + g * sN) / (cN1 + g * cN);         log.dbg("R = ", R);
      double shift = Math.atan(R);
      Cmplx S = new Cmplx(1., R).div(new Cmplx(1., -R));   log.dbg("S = ", S);
      S = S.add(-1);
      double k = Scatt.calcMomFromE(E);                    log.dbg("k = ", k);
      double k2 = k * k;
//      double sigma = Math.PI * S.abs2();              log.dbg("sigma = ", sigma).eol();
      double sigma = Math.PI * S.abs2() / k2;              log.dbg("sigma = ", sigma).eol();
      arrCross.set(i, sigma);
      arrShift.set(i, shift);
    }
    res.setCross(arrCross);
    res.setShift(arrShift);
    return res;
  }

  private double calcG(double E) {
    double res = 0;
    Vec d = getOverD();
    for (int i = 0; i < sysEngs.size(); i++) {
      double ei = sysEngs.get(i);
      double d2 = d.get(i) * d.get(i);
      if (Float.compare((float)ei, (float)E) == 0)
        throw new IllegalArgumentException(log.error("E=e_i=" + (float)E));
      res += (d2 / (ei - E));
    }
    return res;
  }


}
