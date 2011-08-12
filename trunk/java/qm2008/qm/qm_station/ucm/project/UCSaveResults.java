package qm_station.ucm.project;
import project.ucm.UCController;

import javax.swingx.SaveFileUI;
import javax.swing.*;
import javax.iox.FileX;
import javax.utilx.log.Log;
import java.io.File;

import qm_station.QMS;
import qm_station.QMSProject;
import qm_station.QMSFrame;
import qm_station.QMSResults;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 16:35:47
 */
public class UCSaveResults
  implements UCController {
  public static Log log = Log.getLog(UCSaveResults.class);
  private static SaveFileUI ui;

  public boolean run() {
    QMS model = QMSProject.getInstance();
    String name = model.getResultsFileName();   log.dbg("getResultsFileName()=", name);
    File file = model.makeFile(name);
//    if (!file.exists())  {
//      name = model.getTrainTableName();         log.debug("getTrainTableName()=", name);
//      file = model.makeFile(name);
//    }
    if (ui == null) {
      ui = new SaveFileUI();
    }
    file = ui.selectFile(QMSFrame.getInstance(), file);
    if (file == null)
      return false;
    if (file.exists()) {
      if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(QMSFrame.getInstance()
        , "Replace existing \"" + file.getName() + "\" ?"))
        return false;
    }
    model.setResultsFileName(FileX.getFileName(file));
    model.saveProjectToDefaultLocation();
    QMSResults.save();
    file = null;  System.gc(); //TODO: is this how one suppose to release a file?
    return true;
  }
}
