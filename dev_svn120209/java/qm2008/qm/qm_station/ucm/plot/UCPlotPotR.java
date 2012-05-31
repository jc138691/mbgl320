package qm_station.ucm.plot;
import atom.wf.coulomb.WfFactory;
import qm_station.QMS;
import qm_station.ui.scatt.JmPotFactoryR;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.FuncVec;
import math.vec.grid.StepGrid;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 18/11/2008, Time: 13:40:29
 */
public class UCPlotPotR extends UCPlotFuncVec {
  public static Log log = Log.getLog(UCPlotPotR.class);
  public UCPlotPotR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  public FuncVec makeFuncVec() {
    StepGrid r = JmPotFactoryR.makeGrid();
    FuncVec pot = WfFactory.makePotHy_1s_e(r);  log.dbg("V_1s(r)=", pot);
    pot.multSelf(r);  log.dbg("r * V_1s(r)=", pot);
    pot.multSelf(r);  log.dbg("r^2 * V_1s(r)=", pot);
    return pot;
  }
}