package ucm.seq.arr.view;

import olga.SnpStation;
import olga.OlgaMainUI;
import olga.SnpStationProject;
import olga.OlgaFrame;
import project.workflow.task.UCRunTask;
import project.workflow.task.TaskOptView;

import javax.utilx.log.Log;

import dna.contig.view.ContigArrUI;
import dna.contig.view.ContigArrView;

import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 22/04/2009, 2:20:56 PM
 */
public class UCRefreshContigTableView extends UCRunTask<SnpStation> {
  public static Log log = Log.getLog(UCRefreshDnaTableView.class);
  public UCRefreshContigTableView() {
    super(OlgaMainUI.getInstance().getContigArrUI());
  }

  public boolean run() {
//    log.setDbg(); log.info("UCRefreshContigTableView.run(){");
    ContigArrUI ui = OlgaMainUI.getInstance().getContigArrUI();
    if (ui == null)
      return true; // nothing to update
    TaskOptView<SnpStation> optView = ui.getOptView();
    SnpStation project = SnpStationProject.getInstance();
    optView.loadTo(project);
    project.saveProjectToDefaultLocation();

    ContigArrView view = ui.getContigTableView();
    if (view == null)
      return true; // does not exist yet

    Point currPos = view.getViewPos();
    int colIdx = view.getSelectedColIdx();
    view.loadView();
    view.setViewPos(currPos);
    view.showSnpAt(colIdx);
//    view.repaint();
    
    ui.getContigOptView().getSnpExportOptView().loadFrom(project.getSnpOpt());

    OlgaFrame.getInstance().repaint();
    return true;
  }
}
