package ucm.io;

import olga.SnpStation;
import olga.SnpStationProject;
import project.workflow.task.UCRunDefaultTask;
import project.workflow.task.DefaultTaskUI;

import javax.utilx.log.Log;

import io.fasta.FastaReader;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 20/03/2009, 2:18:45 PM
 */
public class UCLoadDnaTableInfoFromFasta extends UCRunDefaultTask<SnpStation> {
  public static Log log = Log.getLog(UCLoadDnaTableInfoFromFasta.class);

  public UCLoadDnaTableInfoFromFasta(DefaultTaskUI<SnpStation> ui) {
    super(ui);
  }

  public boolean run() {log.dbg("UCLoadDnaTableInfoFromFasta.run()");
    SnpStation project = SnpStationProject.getInstance();
    getOptView().loadTo(project);
    project.saveProjectToDefaultLocation();
    boolean ok = FastaReader.loadInfo(project, getOutView().getUsrView());
    if (!ok)
      return false;
    project.saveProjectToDefaultLocation();
    return true;
  }
}