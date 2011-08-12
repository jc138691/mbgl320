package ucm.io;

import project.ucm.UCController;
import project.ModelDAO;
import project.workflow.task.UCRunTask;

import javax.swingx.OpenFileUI;
import javax.swingx.progress.UCMonitorTaskProgress;
import javax.iox.FileX;

import olga.SnpStation;
import olga.SnpStationProject;
import olga.OlgaFrame;
import olga.OlgaMainUI;
import io.ace.AceOpt;
import io.ace.AceFileFilter;

import java.io.File;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 08/10/2009, 10:24:42 AM
 */
public class UCSelectAceFile extends OpenFileUI
  implements UCController {
  private ModelDAO<SnpStation> view;
  public UCSelectAceFile(ModelDAO<SnpStation> view) {
    this.view = view;
  }
  public boolean run() {
    SnpStation model = SnpStationProject.getInstance();
    view.loadTo(model);
    AceOpt opt = model.getAceOpt();
    String name = opt.getFileName();
    File file = model.makeFile(name);

    if (!file.exists()) { // TRY CAF file
      name = model.getCafOpt().getFileName();
      file = model.makeFile(name);
    }
    if (!file.exists()) { // TRY fasta file
      name = model.getFastaOpt().getFileName();
      file = model.makeFile(name);
    }
    if (!file.exists()) { // TRY pheno file
      name = model.getSnpOpt().getPhenoOpt().getFileName();
      file = model.makeFile(name);
    }

    file = selectFile(OlgaFrame.getInstance(), file, new AceFileFilter());
    if (file == null)
      return false;

    opt.setFileName(FileX.getFileName(file));
    view.loadFrom(model);
    model.saveProjectToDefaultLocation();

    new UCSetupSnpExportFileName(model, opt).run();

    OlgaMainUI ui = OlgaMainUI.getInstance();
    UCRunTask uc = new UCMonitorTaskProgress<SnpStation>(new UCImportAceFile(ui.getImportUI()));
    return uc.run();
  }
}
