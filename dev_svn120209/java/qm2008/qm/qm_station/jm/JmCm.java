package qm_station.jm;
import flanagan.complex.Cmplx;
import scatt.jm_2008.jm.laguerre.JmLgrrR;
import scatt.jm_2008.jm.theory.JmTheory;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 17/12/2008, Time: 14:53:02
 */
public class JmCm extends JmSm {
  public JmCm(JmLgrrR bi, double E) {
    super(bi, E);
  }
  protected void calc(JmLgrrR arr) {
    int L = arr.getModel().getL();
    double lambda = arr.getModel().getLambda();
    for (int n = 0; n < size(); n++) {
      Cmplx z2 = JmTheory.calcSCnL0byE(n, E, lambda);
      set(n, z2.getReal());
    } log.dbg("res=\n", this);
  }
}
