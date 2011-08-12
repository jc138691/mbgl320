package qm_station.ucm.plot.lcr;
import atom.wf.log_cr.WFQuadrLcr;
import qm_station.ucm.plot.UCPlotFuncArr;
import qm_station.QMS;
import qm_station.ui.scatt.JmPotFactoryLCR;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.arr.FuncArr;
import math.func.FuncVec;
import math.vec.Vec;
import atom.wf.coulomb.CoulombWFFactory;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 18/11/2008, Time: 13:49:06
 */
public class UCPlotPotLCR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotPotLCR.class);
  public UCPlotPotLCR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  public FuncArr makeFuncArr() {
    WFQuadrLcr w = JmPotFactoryLCR.makeQuadr();
    Vec x = w.getX();
    Vec r = w.getR();
    FuncArr res = new FuncArr(x, 1);  // just ONE function
    FuncVec pot = CoulombWFFactory.makePotHy_1s_e(r);  log.dbg("V_1s(r)=", pot);
    pot.setX(x);
    pot.mult(r);  log.dbg("r * V_1s(r)=", pot);
    pot.mult(r);  log.dbg("r^2 * V_1s(r)=", pot);
    res.set(0, pot);
    return res;
  }
}
