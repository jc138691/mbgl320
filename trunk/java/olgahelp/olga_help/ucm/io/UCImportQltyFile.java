package ucm.io;

import olga.SnpStation;
import olga.SnpStationProject;
import olga.OlgaMainUI;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.UCRunDefaultTask;

import javax.utilx.log.Log;
import javax.swing.*;
import javax.swingx.progress.UCMonitorTaskProgress;

import dna.table.view.table.SeqArrView;
import dna.table.view.SeqArrUI;
import dna.seq.arr.SeqArr;
import io.fasta.FastaQltyReader;
import ucm.seq.arr.view.UCRefreshViews;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 20/02/2009 at 12:19:42.
 */
public class UCImportQltyFile extends UCRunDefaultTask<SnpStation> {
  public static Log log = Log.getLog(UCImportQltyFile.class);

  protected void setupLogs() {
    add(log);
//    addContig(FastaQltyReader.log);
//    addContig(FuncPlotView.log);
//    setDbg(true);                      // THIS IS where to switch on/off the debugging
  }

  public UCImportQltyFile(DefaultTaskUI<SnpStation> ui) {
    super(ui);
  }
  public boolean run() {log.dbg("UCImportQltyFile.run()");
    SnpStation project = SnpStationProject.getInstance();
    getOptView().loadTo(project);
    project.saveProjectToDefaultLocation();

    SeqArrUI ui = OlgaMainUI.getInstance().getSeqArrUI();
    SeqArrView view = ui.getDnaTableView();
    if (view == null) {
      String error = "Unable to import FASTA Quality file without the corresponding FASTA file.";
      log.error(error);
      JOptionPane.showMessageDialog(view, error);
    }
    SeqArr seqArr = view.getSeqArr();
    seqArr = FastaQltyReader.read(seqArr, project, getOutView().getUsrView());
    if (seqArr == null)
      return false;

    ui.getOptView().loadFrom(project); // could be changed
    view.loadView();
    OlgaMainUI.getInstance().setSeqArrUI(ui);

    ui.setActionOnOptChange(new UCMonitorTaskProgress<SnpStation>(new UCRefreshViews(ui)));

    return true;
  }
}