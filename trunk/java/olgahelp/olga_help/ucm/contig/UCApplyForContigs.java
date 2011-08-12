package ucm.contig;

import olga.SnpStation;
import project.workflow.task.UCRunDefaultTask;
import project.workflow.task.DefaultTaskUI;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 21/05/2009, 4:22:52 PM
 */
public class UCApplyForContigs extends UCRunDefaultTask<SnpStation> {
  public static Log log = Log.getLog(UCApplyForContigs.class);

  public UCApplyForContigs(DefaultTaskUI<SnpStation> ui) {
    super(ui);
  }

  public boolean run() {log.dbg("UCApplyForContigs.run()");
    // TODO?
    return true;
//    SnpStation project = SnpStationProject.getInstance();
//    getOptView().loadTo(project);
//    project.saveProjectToDefaultLocation();
//
//    DefaultResView out = getOutView();
//    TextView usrView = out.getUsrView();
//    out.setUsrView(usrView);  // set focus
//
//    if (project.getImportFileType() == OlgaImportOptUI.IMPORT_FASTA) {
//      return new UCImportFastaFiles(getDefaultUi()).run();
//    }
//    else {
//      UCRunTask uc = new UCMonitorTaskProgress<SnpStation>(new UCImportCafFile(getDefaultUi()));
//      return uc.run();
//    }
  }
}
