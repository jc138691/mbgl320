package ucm.io;

import olga.SnpStation;
import olga.SnpStationProject;
import project.workflow.task.UCRunDefaultTask;
import project.workflow.task.DefaultTaskUI;

import javax.utilx.log.Log;

import io.fasta.FastaQltyReader;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 23/03/2009, 2:53:39 PM
 */
public class UCLoadDnaTableInfoFromQlty extends UCRunDefaultTask<SnpStation> {
  public static Log log = Log.getLog(UCLoadDnaTableInfoFromQlty.class);
  public UCLoadDnaTableInfoFromQlty(DefaultTaskUI<SnpStation> ui) {
    super(ui);
  }

  public boolean run() {log.dbg("UCLoadDnaTableInfoFromQlty.run()");
    SnpStation project = SnpStationProject.getInstance();
    getOptView().loadTo(project);
    project.saveProjectToDefaultLocation();
    boolean ok = FastaQltyReader.loadInfo(project, getOutView().getUsrView());
    if (!ok)
      return false;
    project.saveProjectToDefaultLocation();
    return true;
  }
}