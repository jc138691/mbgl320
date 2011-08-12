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
import io.pheno.PhenoFileFilter;

import java.io.File;

import dna.snp.pheno.SnpPhenoOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 04/08/2009, 1:30:07 PM
 */
public class UCSelectPhenoFile extends OpenFileUI
  implements UCController {
  private ModelDAO<SnpStation> view;
  public UCSelectPhenoFile(ModelDAO<SnpStation> view) {
    this.view = view;
  }
  public boolean run() {
    SnpStation model = SnpStationProject.getInstance();
    view.loadTo(model);
    SnpPhenoOpt opt = model.getSnpOpt().getPhenoOpt();
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

    file = selectFile(OlgaFrame.getInstance(), file, new PhenoFileFilter());
    if (file == null)
      return false;

    opt.setFileName(FileX.getFileName(file));
    view.loadFrom(model);
    model.saveProjectToDefaultLocation();

    OlgaMainUI ui = OlgaMainUI.getInstance();
    UCRunTask uc = new UCMonitorTaskProgress<SnpStation>(new UCImportPhenoFile(ui.getImportUI()));
    return uc.run();
  }
}