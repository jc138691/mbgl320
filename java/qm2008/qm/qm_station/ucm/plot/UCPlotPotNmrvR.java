package qm_station.ucm.plot;
import qm_station.QMS;
import qm_station.QMSProject;
import scatt.jm_2008.e1.JmOptE1;

import javax.utilx.log.Log;

import project.workflow.task.DefaultTaskUI;
import math.func.arr.FuncArr;
import math.func.FuncVec;
import math.vec.grid.StepGridModel;
import math.vec.grid.StepGrid;
import scatt.partial.wf.numerov.NmrvPotR;
import atom.wf.coulomb.CoulombWFFactory;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 18/11/2008, Time: 13:09:16
 */
public class UCPlotPotNmrvR extends UCPlotFuncArr {
  public static Log log = Log.getLog(UCPlotPotNmrvR.class);
  public UCPlotPotNmrvR(DefaultTaskUI<QMS> ui) {
    super(ui);
  }
  protected void setupLogs() {
    add(log);
    add(StepGrid.log);
    add(NmrvPotR.log);
    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }
  public FuncArr makeFuncArr() {
    QMS project = QMSProject.getInstance();
    JmOptE1 model = project.getJmPotOptR();    // R
    StepGridModel sg = model.getGrid();
    StepGrid r = new StepGrid(sg);    log.dbg("r grid=", r);
    FuncVec pot = CoulombWFFactory.makePotHy_1s_e(r);  log.dbg("V_1s(r)=", pot);
    return new NmrvPotR(pot, model.getGridEng(), model.getJmModel().getL() ); //
  }
}