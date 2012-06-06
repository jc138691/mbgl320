package qm_station.ui.scatt;
import atom.wf.lcr.WFQuadrLcr;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;

import javax.utilx.log.Log;

import qm_station.QMS;
import qm_station.QMSProject;
import scatt.jm_2008.e1.JmCalcOptE1;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 18/11/2008, Time: 13:50:24
 */
public class JmPotFactoryLCR {
  public static Log log = Log.getLog(JmPotFactoryLCR.class);

  public static WFQuadrLcr makeQuadr() {
    QMS project = QMSProject.getInstance();
    JmCalcOptE1 model = project.getJmPotOptLcr();    // LCR
    StepGridOpt sg = model.getGridOpt();
    StepGrid x = new StepGrid(sg);    log.dbg("LCR grid = x =", x);
    WFQuadrLcr w = new WFQuadrLcr(x);     log.dbg("LCR integration weights=", w);
    return w;
  }
}
