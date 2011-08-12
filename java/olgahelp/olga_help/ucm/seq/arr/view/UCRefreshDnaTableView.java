package ucm.seq.arr.view;

import olga.SnpStation;
import olga.SnpStationProject;
import olga.OlgaMainUI;
import olga.OlgaFrame;
import project.workflow.task.TaskOptView;
import project.workflow.task.UCRunTask;

import javax.utilx.log.Log;

import dna.table.view.table.SeqArrView;
import dna.table.view.SeqArrUI;

import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 12/03/2009, 9:11:27 AM
 */
public class UCRefreshDnaTableView extends UCRunTask<SnpStation> {
  public static Log log = Log.getLog(UCRefreshDnaTableView.class);
  public UCRefreshDnaTableView() {
    super(OlgaMainUI.getInstance().getSeqArrUI());
  }

  public boolean run() {
    log.setDbg(); log.info("UCRefreshDnaTableView.run(){");
    SeqArrUI ui = OlgaMainUI.getInstance().getSeqArrUI();
    if (ui == null)
      return true; // nothing to update
    TaskOptView<SnpStation> optView = ui.getOptView();
    SnpStation project = SnpStationProject.getInstance();
    optView.loadTo(project);
    project.saveProjectToDefaultLocation();

    SeqArrView view = ui.getDnaTableView();
    if (view == null)
      return true; // does not exist yet

    Point currPos = view.getViewPos();
    view.loadView();
    view.setViewPos(currPos);
    OlgaFrame.getInstance().repaint();
    return true;
  }
}
