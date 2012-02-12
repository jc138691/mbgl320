package ucm.project;

import project.ucm.UCController;

import javax.swingx.SaveFileUI;
import javax.utilx.log.Log;
import javax.iox.FileX;
import java.io.File;

import olga.SnpStation;
import olga.SnpStationProject;
import olga.OlgaFrame;
import olga.OlgaFileFilter;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 13/02/2009 at 13:38:38.
 */
public class UCSaveProject extends SaveFileUI
  implements UCController {
  public static Log log = Log.getLog(UCSaveProject.class);

  public boolean run() {log.dbg("UCSaveProject.run()");
    SnpStation model = SnpStationProject.getInstance();
    String name = model.getProjectFileName();
    File file = model.makeFile(name);

//    if (!file.exists()) { // TRY train file
//      log.debug("try trainning table file name");
//      name = model.getTrainTableName();
//      file = model.makeFile(name);
//    }

    file = selectFile(OlgaFrame.getInstance(), file, new OlgaFileFilter());
    if (file == null)
      return false;
    model.setProjectFileName(FileX.getFileName(file));
    model.saveProjectToDefaultLocation();
    model.writeProject(FileX.getFileName(file));
    return true;
  }
}
