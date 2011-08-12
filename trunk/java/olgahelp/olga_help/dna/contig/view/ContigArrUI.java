package dna.contig.view;

import olga.SnpStation;
import project.workflow.task.TaskUI;
import project.ucm.UCController;

import javax.swing.*;

import dna.contig.view.opt.ContigArrViewOptUI;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 21/04/2009, 12:28:34 PM
 */
public class ContigArrUI extends TaskUI<SnpStation> {
  public ContigArrUI(SnpStation project, JFrame frame, JPanel outView) {
    super(new ContigArrViewOptUI(project), frame, outView, JSplitPane.VERTICAL_SPLIT);
    setOneTouchExpandable(true);
  }

  public ContigArrView getContigTableView() {
    return (ContigArrView)getResView();
  }

  public ContigArrViewOptUI getContigOptView() {
    return (ContigArrViewOptUI)getOptView();
  }

  public void setActionOnOptChange(UCController uc) {
    getContigOptView().setActionOnOptChange(uc);
  }
  public void loadFrom(SnpStation model) {
    ContigArrViewOptUI optView = getContigOptView();
    if (optView != null)
      optView.loadFrom(model);
  }
}
