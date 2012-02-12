package ucm.io;

import olga.SnpStation;
import olga.SnpStationProject;
import olga.OlgaMainUI;
import olga.OlgaFrame;
import project.workflow.task.DefaultTaskUI;
import project.workflow.task.UCRunDefaultTask;

import javax.utilx.log.Log;
import javax.swingx.progress.UCMonitorTaskProgress;

import dna.seq.arr.SeqArr;
import dna.table.view.SeqArrUI;
import dna.table.view.table.SeqArrView;
import io.fasta.FastaReader;
import ucm.seq.arr.view.UCRefreshViews;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 16/02/2009 at 13:46:06.
 */
public class UCImportFastaFile extends UCRunDefaultTask<SnpStation> {
  public static Log log = Log.getLog(UCImportFastaFile.class);

  public UCImportFastaFile(DefaultTaskUI<SnpStation> ui) {
    super(ui);
  }
  public boolean run() {log.dbg("UCImportFastaFile.run()");
    SnpStation project = SnpStationProject.getInstance();
    getOptView().loadTo(project);
    project.saveProjectToDefaultLocation();
    SeqArr inputArr = new FastaReader().read(project, getOutView().getUsrView());
    if (inputArr == null)
      return false;

    SeqArrView view = new SeqArrView(inputArr);
    SeqArrUI ui = new SeqArrUI(project, OlgaFrame.getInstance(), view);
    OlgaMainUI.getInstance().setSeqArrUI(ui);
    ui.setActionOnOptChange(new UCMonitorTaskProgress<SnpStation>(new UCRefreshViews(ui)));
    
//    ui.getOptView().loadFrom(project); // font does not refresh if this is not called????
    return true;
  }
}