package ucm.io;

import project.ucm.UCController;
import project.ModelDAO;
import project.workflow.task.UCRunTask;

import javax.swingx.OpenFileUI;
import javax.swingx.progress.UCMonitorTaskProgress;
import javax.iox.FileX;
import javax.swing.*;

import olga.*;

import java.io.File;

import io.fasta.FastaFileFilter;
import io.fasta.FastaOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 16/02/2009 at 13:23:27.
 */
public class UCSelectFastaFiles extends OpenFileUI
  implements UCController {
  private ModelDAO<SnpStation> view;
  public UCSelectFastaFiles(ModelDAO<SnpStation> view) {
    this.view = view;
  }

  public boolean run() {
    SnpStation model = SnpStationProject.getInstance();
    view.loadTo(model);
    FastaOpt opt = model.getFastaOpt();
    String name = opt.getFileName();
    File file = model.makeFile(name);

    if (!file.exists()) { // TRY qlty file
      name = opt.getQltyFileName();
      file = model.makeFile(name);
    }

    if (!file.exists()) { // TRY caf file
      name = model.getCafOpt().getFileName();
      file = model.makeFile(name);
    }

    file = selectFile(OlgaFrame.getInstance(), file, new FastaFileFilter());
    if (file == null)
      return false;
    opt.setFileName(FileX.getFileName(file));
    view.loadFrom(model);
    model.saveProjectToDefaultLocation();

    // CHECK and select quality file
    name = opt.getFileName();
    String qltyName = FileX.replaceExtension(name, opt.getQltyFileExt());
    file = model.makeFile(qltyName);
    if (file.exists()) {
      int ok = JOptionPane.showConfirmDialog(OlgaFrame.getInstance(), "Found the corresponding Quality file \n"
        + FileX.getFileName(file)
        + "\nDo you want to select it as the quality file?", "Select Quality file", JOptionPane.YES_NO_OPTION);
      if (ok != JOptionPane.YES_OPTION)
        return false;
      opt.setQltyFileName(FileX.getFileName(file));
      opt.setHasQltyFile(true);
      view.loadFrom(model);
      model.saveProjectToDefaultLocation();
    }
    OlgaMainUI ui = OlgaMainUI.getInstance();
    UCRunTask uc = new UCMonitorTaskProgress<SnpStation>(new UCImportFastaFiles(ui.getImportUI()));
    return uc.run();
  }
}
