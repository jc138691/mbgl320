package qm_station.ucm.project;
import project.ucm.UCController;

import javax.swingx.OpenFileUI;
import javax.iox.FileX;
import java.io.File;

import qm_station.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 15:35:49
 */
public class UCOpenProject extends OpenFileUI
  implements UCController {
  public boolean run() {
    QMS model = QMSProject.getInstance();
    String name = model.getProjectFileName();
    File file = model.makeFile(name);

//    if (!file.exists()) { // TRY train file
//      name = model.getTrainTableName();
//      file = model.makeFile(name);
//    }

    file = selectFile(QMSFrame.getInstance(), file, new QMSFileFilter());
    if (file == null)
      return false;
    model.setProjectFileName(FileX.getFileName(file));
    model.saveProjectToDefaultLocation();
    model.loadFromDisk(FileX.getFileName(file));
    model.setProjectFileName(FileX.getFileName(file));
    model.saveProjectToDefaultLocation();

    QMSMainUI.getInstance().resetAll();
//    new UCShowSettingsUI().run();
    return true;
  }
}

