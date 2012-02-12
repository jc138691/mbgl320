package ucm.io;

import project.workflow.task.DefaultTaskUI;
import project.workflow.task.UCRunTask;
import project.workflow.task.DefaultResView;
import project.workflow.task.UCRunDefaultTask;

import javax.utilx.log.Log;
import javax.swingx.textx.TextView;
import javax.swingx.progress.UCMonitorTaskProgress;

import olga.SnpStation;
import olga.SnpStationProject;
import io.fasta.FastaOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 16/02/2009 at 14:47:01.
 */
public class UCImportFastaFiles extends UCRunDefaultTask<SnpStation> {
  public static Log log = Log.getLog(UCImportFastaFiles.class);

  public UCImportFastaFiles(DefaultTaskUI<SnpStation> ui) {
    super(ui);
  }

  public boolean run() {log.dbg("UCImportFastaFiles.run()");
    SnpStation project = SnpStationProject.getInstance();
    getOptView().loadTo(project);       
    project.saveProjectToDefaultLocation();
    FastaOpt opt = project.getFastaOpt();

    DefaultResView out = getOutView();
    TextView usrView = out.getUsrView();
    out.setUsrView(usrView);  // set focus

//    usrView.println("Loading summary info from Fasta File ...");
//    usrView.println("File name = [" + opt.getFileName() + "]");
//    UCRunTask uc = new UCMonitorTaskProgress<SnpStation>(new UCLoadDnaTableInfoFromFasta(getDefaultUi()));
//    if (!uc.run()) {
//      String error = "Unable to proceed: Error occured in the UCLoadDnaTableInfoFromFasta task.";
//      log.error(error);
//      JOptionPane.showMessageDialog(usrView, error);
//      return false;
//    }

    usrView.println("Importing Fasta File ...");
    usrView.println("File name = [" + opt.getFileName() + "]");
    UCRunTask uc = new UCMonitorTaskProgress<SnpStation>(new UCImportFastaFile(getDefaultUi()));
    boolean ok = uc.run();

    if (ok  &&  opt.getHasQltyFile()) {
//      usrView.println("Loading summary info from QUALITY File ...");
//      usrView.println("File name = [" + opt.getQltyFileName() + "]");
//      uc = new UCMonitorTaskProgress<SnpStation>(new UCLoadDnaTableInfoFromQlty(getDefaultUi()));
//      if (!uc.run()) {
//        String error = "Unable to proceed: Error occured in the LoadSeqArrInfoFromQlty task.";
//        log.error(error);
//        JOptionPane.showMessageDialog(usrView, error);
//        return false;
//      }

      usrView.println("Importing Quality File ...");
      usrView.println("File name = [" + opt.getQltyFileName() + "]");
      uc = new UCMonitorTaskProgress<SnpStation>(new UCImportQltyFile(getDefaultUi()));
      ok = uc.run();
    }
    return ok;
  }
}
