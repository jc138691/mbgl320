package qm_station.ucm.project;
import project.ucm.UCController;

import javax.swingx.SaveFileUI;
import javax.utilx.log.Log;
import javax.iox.FileX;
import java.io.File;

import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.QMSFrame;
import qm_station.QMSFileFilter;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 15:35:36
 */
public class UCSaveProject extends SaveFileUI
  implements UCController {
  public static Log log = Log.getLog(UCSaveProject.class);

  public boolean run() {log.dbg("UCSaveProject.run()");
    QMS model = QMSProject.getInstance();
    String name = model.getProjectFileName();
    File file = model.makeFile(name);

//    if (!file.exists()) { // TRY train file
//      log.debug("try trainning table file name");
//      name = model.getTrainTableName();
//      file = model.makeFile(name);
//    }

    file = selectFile(QMSFrame.getInstance(), file, new QMSFileFilter());
    if (file == null)
      return false;
    model.setProjectFileName(FileX.getFileName(file));
    model.saveProjectToDefaultLocation();
    model.writeProject(FileX.getFileName(file));
    return true;
  }
}

