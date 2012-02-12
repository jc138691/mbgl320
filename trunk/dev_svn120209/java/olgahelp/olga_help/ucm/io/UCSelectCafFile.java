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

import java.io.File;

import io.caf.CafFileFilter;
import io.caf.CafOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 27/03/2009, 2:17:37 PM
 */
public class UCSelectCafFile extends OpenFileUI
  implements UCController {
  private ModelDAO<SnpStation> view;
  public UCSelectCafFile(ModelDAO<SnpStation> view) {
    this.view = view;
  }
  public boolean run() {
    SnpStation model = SnpStationProject.getInstance();
    view.loadTo(model);
    CafOpt opt = model.getCafOpt();
    String name = opt.getFileName();
    File file = model.makeFile(name);

    if (!file.exists()) { // TRY ACE file
      name = model.getAceOpt().getFileName();
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

    file = selectFile(OlgaFrame.getInstance(), file, new CafFileFilter());
    if (file == null)
      return false;

    opt.setFileName(FileX.getFileName(file));
    view.loadFrom(model);
    model.saveProjectToDefaultLocation();

    // setup default SNPs exports
    new UCSetupSnpExportFileName(model, opt).run();
//    SnpOpt snpOpt = model.getSnpOpt();
//    String snpFile = snpOpt.getExportFileName();
//    if (snpFile == null  ||  snpFile.length() == 0) {
//      name = opt.getFileName();
//      snpFile = FileX.replaceExtension(name, snpOpt.getExportFileExt());
//      snpOpt.setExportFileName(snpFile);
//      view.loadFrom(model);
//      model.saveProjectToDefaultLocation();
//    }

    OlgaMainUI ui = OlgaMainUI.getInstance();    
    UCRunTask uc = new UCMonitorTaskProgress<SnpStation>(new UCImportCafFile(ui.getImportUI()));
    return uc.run();
  }
}
