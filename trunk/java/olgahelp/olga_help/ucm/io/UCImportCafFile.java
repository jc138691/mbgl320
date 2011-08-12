package ucm.io;

import olga.SnpStation;
import project.workflow.task.DefaultTaskUI;

import javax.swingx.textx.TextView;

import io.caf.CafReader;
import dna.contig.ContigArr;
import dna.table.PageOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 27/03/2009, 3:22:02 PM
 */
public class UCImportCafFile extends UCImportContigFile {
  public UCImportCafFile(DefaultTaskUI<SnpStation> ui) {
    super(ui);
  }

  public PageOpt loadPageOpt(SnpStation project) {
    return project.getCafOpt();
  }

  public ContigArr readContigs(SnpStation project, TextView usrView) {
    return new CafReader().read(project, usrView);
  }
  public void setupExportSnps() {
    UCExportSnps.setInstance(new UCExportSnpsFromCaf());
  }
}