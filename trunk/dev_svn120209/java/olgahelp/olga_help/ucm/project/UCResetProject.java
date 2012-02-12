package ucm.project;

import project.ucm.UCController;

import javax.utilx.log.Log;

import olga.SnpStation;
import olga.SnpStationProject;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 13/02/2009 at 13:36:04.
 */
public class UCResetProject implements UCController {
  public static Log log = Log.getLog(UCResetProject.class);

  public boolean run() {log.dbg("UCResetProject.run()");
    SnpStation model = SnpStationProject.getInstance();
    model.loadDefault();
    model.saveProjectToDefaultLocation();
    return true;
  }
}