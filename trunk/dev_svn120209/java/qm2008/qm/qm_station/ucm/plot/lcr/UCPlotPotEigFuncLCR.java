package qm_station.ucm.plot.lcr;
import atom.energy.part_wave.PartHMtrxLcr;
import atom.wf.WFQuadr;
import qm_station.ucm.plot.UCPlotFuncArr;
import qm_station.QMS;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.arr.FuncArr;
import math.func.FuncVec;
import math.vec.Vec;
import atom.energy.part_wave.PartHMtrx;
import atom.wf.coulomb.CoulombWFFactory;
import scatt.jm_2008.jm.laguerre.IWFuncArr;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 20/11/2008, Time: 16:11:29
 */
public class UCPlotPotEigFuncLCR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotPotEigFuncLCR.class);
  public UCPlotPotEigFuncLCR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  protected void setupLogs() {
//    add(log);
//    add(StepGrid.log);
//    add(NumerovPotR.log);
//    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }
  public FuncArr makeFuncArr() {
    PartHMtrx H = makeH();
    FuncArr eigVec = H.getEigFuncArr();                     log.dbg("eigVec=", eigVec);
    return eigVec;
  }

  protected PartHMtrx makeH() {
    UCPlotJmLagrrOrthLCR uc = new UCPlotJmLagrrOrthLCR(getDefaultUi());
    IWFuncArr arr = (LgrrOrthLcr)uc.makeFuncArr();
    WFQuadr w = arr.getQuadr();
    Vec x = w.getX();
    Vec r = w.getR();
    FuncVec pot = CoulombWFFactory.makePotHy_1s_e(r);  log.dbg("V_1s(r)=", pot);
    pot.setX(x);

    int L = 0;
    return new PartHMtrxLcr(L, arr, pot);
  }
}