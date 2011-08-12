package ucm.io;

import project.ucm.UCController;
import dna.snp.SnpOpt;

import javax.iox.FileX;

import olga.SnpStation;
import io.DnaFileOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 08/10/2009, 10:35:58 AM
 */
public class UCSetupSnpExportFileName implements UCController {
  private SnpStation model;
  private DnaFileOpt opt;
  public UCSetupSnpExportFileName(SnpStation model, DnaFileOpt opt) {
    this.model = model;
    this.opt = opt;
  }
  public boolean run() {
    // setup default SNPs exports
    SnpOpt snpOpt = model.getSnpOpt();
    String snpFile = snpOpt.getExportFileName();
    if (snpFile == null  ||  snpFile.length() == 0) {
      String name = opt.getFileName();
      snpFile = FileX.replaceExtension(name, snpOpt.getExportFileExt());
      snpOpt.setExportFileName(snpFile);
//      view.loadFrom(model);
      model.saveProjectToDefaultLocation();
    }
    return false;
  }
}
