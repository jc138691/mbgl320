package qm_station.jm;
import atom.energy.pw.PotHR;
import atom.wf.WFQuadrD1;
import scatt.eng.EngGrid;
import scatt.eng.EngOpt;
import scatt.jm_2008.jm.laguerre.LgrrR;

import javax.utilx.log.Log;

import math.func.FuncVec;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 19/11/2008, Time: 15:46:32
 */
public class JmJnnR extends FuncVec {
  public static String HELP = "The JM's J_{N,N-1}; calculated by numerical integration";
  public static Log log = Log.getLog(JmJnnR.class);

  public JmJnnR(LgrrR arr, EngOpt eng) {
    super(new EngGrid(eng));   // energy grid storred in x
    calc(arr);
  }
  private void calc(LgrrR arr) {
    WFQuadrD1 w = arr.getQuadr();
    PotHR partH = new PotHR(w);
    FuncVec f = arr.getFunc(arr.size()-1);
    FuncVec f2 = arr.getFunc(arr.size()-2);
    int L = 0;
    double kin = partH.calcKin(L, f, f2);
    double overlap = w.calc(f, f2);

    for (int i = 0; i < size(); i++) {
      double E = getX().get(i);       log.dbg("E=", E);
      double res = kin - E * overlap; log.dbg("Jnn=", res);
      set(i, res);
    }
  }
}