package ucm.io;

import project.ucm.UCController;

import javax.swingx.SaveFileUI;
import javax.swingx.filechooserx.SingleFileFilter;
import javax.utilx.log.Log;
import javax.iox.FileX;
import java.io.File;

import olga.SnpStation;
import olga.SnpStationProject;
import olga.OlgaFrame;
import dna.snp.view.SnpExportOptView;
import dna.snp.SnpOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 22/05/2009, 12:13:06 PM
 */
public class UCSelectExportSnpsFile extends SaveFileUI
  implements UCController {
  public static Log log = Log.getLog(UCSelectExportSnpsFile.class);
  private SnpExportOptView view;
  public UCSelectExportSnpsFile(SnpExportOptView view) {
    this.view = view;
  }
  public boolean run() {log.dbg("UCSelectExportSnpsFile.run()");
    SnpStation model = SnpStationProject.getInstance();
    SnpOpt opt = model.getSnpOpt();
    view.loadTo(opt);
    String name = opt.getExportFileName();
    String ext = opt.getExportFileExt();
    File file = model.makeFile(name);

    if (!file.exists()) { // TRY CAF file
      name = model.getCafOpt().getFileName();
      file = model.makeFile(name);
    }

    SingleFileFilter fileFilter = new SingleFileFilter("Exported SNP files (*." + ext + ")", ext);
    file = selectFile(OlgaFrame.getInstance(), file, fileFilter);
    if (file == null)
      return false;
    if (file == null)
      return false;
    opt.setExportFileName(FileX.getFileName(file));
    view.loadFrom(opt);
    model.saveProjectToDefaultLocation();
    return new UCSelectExportSnpsType(view).run();
//    return new UCMonitorUCProgress(new UCSelectExportSnpsType(view), OlgaFrame.getInstance()).run();
  }
}

