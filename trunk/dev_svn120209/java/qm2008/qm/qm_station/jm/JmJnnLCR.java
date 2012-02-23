package qm_station.jm;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;

import javax.utilx.log.Log;

import scatt.eng.EngGrid;
import scatt.eng.EngModel;
import scatt.jm_2008.jm.laguerre.lcr.LagrrLcr;
import atom.energy.part_wave.PartHLcr;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 20/11/2008, Time: 13:45:18
 */
public class JmJnnLCR extends FuncVec {
  public static String HELP = "The JM's J_{N,N-1}; calculated by numerical integration in LCR";
  public static Log log = Log.getLog(JmJnnLCR.class);

  public JmJnnLCR(LagrrLcr arr, EngModel eng) {
    super(new EngGrid(eng));   // energy grid storred in x
    calc(arr);
  }
  private void calc(LagrrLcr arr) {
    WFQuadrLcr w = arr.getQuadrLCR();
    PartHLcr partH = new PartHLcr(w);
    FuncVec f = arr.getFunc(arr.size()-1);
    FuncVec f2 = arr.getFunc(arr.size()-2);
    int L = 0;
    double kin = partH.calcKin(L, f, f2);
    double overlap = w.getWithCR2().calc(f, f2);

    for (int i = 0; i < size(); i++) {
      double E = getX().get(i);       log.dbg("E=", E);
      double res = kin - E * overlap; log.dbg("Jnn=", res);
      set(i, res);
    }
  }
}
