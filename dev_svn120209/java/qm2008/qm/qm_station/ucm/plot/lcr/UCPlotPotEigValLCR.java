package qm_station.ucm.plot.lcr;
import qm_station.ucm.plot.UCPlotFuncVec;
import qm_station.QMS;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.FuncVec;
import math.vec.Vec;
import atom.energy.part_wave.PartHMtrx;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 20/11/2008, Time: 16:11:40
 */
public class UCPlotPotEigValLCR extends UCPlotFuncVec {
  public static Log log = Log.getLog(UCPlotPotEigValLCR.class);
  public UCPlotPotEigValLCR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  protected void setupLogs() {
//    add(log);
//    add(StepGrid.log);
//    add(NumerovPotR.log);
//    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }
  public FuncVec makeFuncVec() {
    UCPlotPotEigFuncLCR uc = new UCPlotPotEigFuncLCR(getDefaultUi());

    PartHMtrx H = uc.makeH();
    Vec val = H.getEigVal();                     log.dbg("eigVal=", val);
    Vec x = new Vec(val.size());
    for (int i = 0; i < x.size(); i++) {
      x.set(i, i);
    }
    FuncVec res = new FuncVec(x, val);           log.dbg("res=", res);
    return res;
  }
}

