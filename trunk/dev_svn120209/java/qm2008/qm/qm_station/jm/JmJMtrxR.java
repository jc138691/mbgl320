package qm_station.jm;
import atom.energy.pw.PotHR;
import atom.wf.WFQuadrD1;
import math.func.FuncVec;
import math.mtrx.api.Mtrx;

import javax.utilx.log.Log;

import scatt.jm_2008.jm.laguerre.LgrrR;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 17/12/2008, Time: 13:51:25
 */
public class JmJMtrxR extends Mtrx {
  public static String HELP = "J_{n,m}; calculated by numerical integration";
  public static Log log = Log.getLog(JmJMtrxR.class);
  private double E;

  public JmJMtrxR(LgrrR arr, double E) {
    super(arr.size(), arr.size());
    this.E = E;                      log.dbg("E=", E);
    calc(arr);
  }
  private void calc(LgrrR arr) {
    WFQuadrD1 w = arr.getQuadr();
    PotHR partH = new PotHR(w);
    int L = arr.getModel().getL();
    for (int n = 0; n < getNumRows(); n++) {
      FuncVec f = arr.getFunc(n);
      for (int c = 0; c < getNumCols(); c++) {
        FuncVec f2 = arr.getFunc(c);
        double kin = partH.calcKin(L, f, f2);
        double overlap = w.calc(f, f2);
        double res = kin - E * overlap;
        set(n, c, res);
      }
    } log.dbg("JMtrix=\n", this);
  }
}