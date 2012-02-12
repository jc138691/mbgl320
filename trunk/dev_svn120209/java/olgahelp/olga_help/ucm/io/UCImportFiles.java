package ucm.io;

import olga.SnpStation;
import olga.SnpStationProject;
import project.workflow.task.UCRunDefaultTask;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.DefaultResView;

import javax.utilx.log.Log;
import javax.swingx.textx.TextView;
import javax.swingx.progress.UCMonitorTaskProgress;
import javax.swing.*;

import io.input.OlgaImportOptUI;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 27/03/2009, 3:20:12 PM
 */
public class UCImportFiles extends UCRunDefaultTask<SnpStation> {
  public static Log log = Log.getLog(UCImportFiles.class);

  public UCImportFiles(DefaultTaskUI<SnpStation> ui) {
    super(ui);
  }

  public boolean run() {log.dbg("UCImportFiles.run()");
    SnpStation project = SnpStationProject.getInstance();
    getOptView().loadTo(project);
    project.saveProjectToDefaultLocation();

    DefaultResView out = getOutView();
    TextView usrView = out.getUsrView();
    out.setUsrView(usrView);  // set focus

    switch (project.getImportFileType()) {
      case OlgaImportOptUI.IMPORT_CAF:
        return new UCMonitorTaskProgress<SnpStation>(new UCImportCafFile(getDefaultUi())).run();
      case OlgaImportOptUI.IMPORT_ACE:
        return new UCMonitorTaskProgress<SnpStation>(new UCImportAceFile(getDefaultUi())).run();
      case OlgaImportOptUI.IMPORT_PHENOTYPES:
        return new UCImportPhenoFile(getDefaultUi()).run();
      case OlgaImportOptUI.IMPORT_FASTA:
        return new UCImportFastaFiles(getDefaultUi()).run();
      default:
        String error = "This option is not ready.";
        log.error(error);
        JOptionPane.showMessageDialog(usrView, error);
        return false;
    }
  }
}
