package ucm.io;

import olga.SnpStation;
import olga.SnpStationProject;
import olga.OlgaMainUI;
import project.workflow.task.UCRunDefaultTask;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.DefaultResView;

import javax.utilx.log.Log;
import javax.swingx.textx.TextView;

import dna.pheno.PhenoMap;
import io.pheno.PhenoReader;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 04/08/2009, 1:49:48 PM
 */
public class UCImportPhenoFile extends UCRunDefaultTask<SnpStation> {
  public static Log log = Log.getLog(UCImportCafFile.class);

  public UCImportPhenoFile(DefaultTaskUI<SnpStation> ui) {
    super(ui);
  }

  public boolean run() {log.dbg("UCImportPhenoFile.run()");
    SnpStation project = SnpStationProject.getInstance();
    getOptView().loadTo(project);
    project.saveProjectToDefaultLocation();

    DefaultResView out = getOutView();
    TextView usrView = out.getUsrView();
    out.setUsrView(usrView);  // set focus

    PhenoMap arr = PhenoReader.read(project, getOutView().getUsrView());
    if (arr == null)
      return false;
    project.getSnpOpt().getPhenoOpt().setPhenoList(arr.getPhenoSet().toStrArr());
    OlgaMainUI ui = OlgaMainUI.getInstance();
    ui.setPhenoMap(arr);
    return true;
  }
}