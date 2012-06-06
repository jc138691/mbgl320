package qm_station.ui.scatt;
import math.vec.grid.StepGridOpt;
import qm_station.QMS;
import qm_station.QMSProject;
import math.vec.grid.StepGrid;
import scatt.jm_2008.e1.JmCalcOptE1;

import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 29/10/2008, Time: 09:45:13
 */
public class JmPotFactoryR {
  public static Log log = Log.getLog(JmPotFactoryR.class);

  public static StepGrid makeGrid() {
    QMS project = QMSProject.getInstance();
    JmCalcOptE1 model = project.getJmPotOptR();    // R
    StepGridOpt sg = model.getGridOpt();
    StepGrid r = new StepGrid(sg);    log.dbg("r grid=", r);
    return r;
  }
}
