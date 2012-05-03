package qm_station.ui.scatt;
import atom.wf.lcr.WFQuadrLcr;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;

import javax.utilx.log.Log;

import qm_station.QMS;
import qm_station.QMSProject;
import scatt.jm_2008.e1.CalcOptE1;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 18/11/2008, Time: 13:50:24
 */
public class JmPotFactoryLCR {
  public static Log log = Log.getLog(JmPotFactoryLCR.class);

  public static WFQuadrLcr makeQuadr() {
    QMS project = QMSProject.getInstance();
    CalcOptE1 model = project.getJmPotOptLcr();    // LCR
    StepGridModel sg = model.getGrid();
    StepGrid x = new StepGrid(sg);    log.dbg("LCR grid = x =", x);
    WFQuadrLcr w = new WFQuadrLcr(x);     log.dbg("LCR integration weights=", w);
    return w;
  }
}
