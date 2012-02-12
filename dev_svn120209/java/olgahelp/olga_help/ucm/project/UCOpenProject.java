package ucm.project;

import project.ucm.UCController;

import javax.swingx.OpenFileUI;
import javax.iox.FileX;
import java.io.File;

import olga.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 13/02/2009 at 13:37:19.
 */
public class UCOpenProject extends OpenFileUI
  implements UCController {
  public boolean run() {
    SnpStation model = SnpStationProject.getInstance();
    String name = model.getProjectFileName();
    File file = model.makeFile(name);

    if (!file.exists()) { // TRY caf file
      name = model.getCafOpt().getFileName();
      file = model.makeFile(name);
    }
    if (!file.exists()) { // TRY fasta file
      name = model.getFastaOpt().getFileName();
      file = model.makeFile(name);
    }

    file = selectFile(OlgaFrame.getInstance(), file, new OlgaFileFilter());
    if (file == null)
      return false;
    model.loadFromDisk(FileX.getFileName(file));
    model.setProjectFileName(FileX.getFileName(file));
    model.saveProjectToDefaultLocation();

//    model.saveProjectToDefaultLocation();
//    model.setProjectFileName(FileX.getFileName(file));
//    model.saveProjectToDefaultLocation();

    OlgaMainUI.getInstance().loadFrom(model);
    return true;
  }
}


