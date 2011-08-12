package ucm.io;

import project.ucm.UCController;
import project.ModelDAO;

import javax.swingx.OpenFileUI;
import javax.iox.FileX;

import olga.SnpStation;
import olga.SnpStationProject;
import olga.OlgaFrame;

import java.io.File;

import io.fasta.FastaQltyFileFilter;
import io.fasta.FastaOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 16/02/2009 at 13:23:43.
 */
public class UCSelectQltyFile extends OpenFileUI
  implements UCController {
  private ModelDAO<SnpStation> view;
  public UCSelectQltyFile(ModelDAO<SnpStation> view) {
    this.view = view;
  }
  public boolean run() {
    SnpStation model = SnpStationProject.getInstance();
    view.loadTo(model);
    FastaOpt opt = model.getFastaOpt();
    String name = opt.getQltyFileName();
    File file = model.makeFile(name);

    if (!file.exists()) { // TRY qlty file
      name = opt.getFileName();
      file = model.makeFile(name);
    }

    file = selectFile(OlgaFrame.getInstance(), file, new FastaQltyFileFilter());
    if (file == null)
      return false;
    opt.setQltyFileName(FileX.getFileName(file));
    opt.setHasQltyFile(true);
    view.loadFrom(model);
    model.saveProjectToDefaultLocation();
    return true;
  }
}
