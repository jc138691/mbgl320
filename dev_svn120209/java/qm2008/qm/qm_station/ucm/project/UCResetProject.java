package qm_station.ucm.project;
import project.ucm.UCController;

import javax.utilx.log.Log;

import qm_station.QMS;
import qm_station.QMSProject;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 10:18:30
 */
public class UCResetProject implements UCController {
  public static Log log = Log.getLog(UCResetProject.class);

  public boolean run() {log.dbg("UCResetProject.run()");
    QMS model = QMSProject.getInstance();
    model.loadDefault();
    model.saveProjectToDefaultLocation();
    return true;
  }
}

