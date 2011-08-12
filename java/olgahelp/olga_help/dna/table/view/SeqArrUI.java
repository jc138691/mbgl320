package dna.table.view;

import olga.SnpStation;
import project.workflow.task.TaskUI;
import project.ucm.UCController;

import javax.swing.*;

import dna.table.view.opt.SeqArrViewOptUI;
import dna.table.view.table.SeqArrView;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 3/03/2009 at 09:51:04.
 */
public class SeqArrUI extends TaskUI<SnpStation> {
  public SeqArrUI(SnpStation project, JFrame frame, JPanel outView) {
    super(new SeqArrViewOptUI(project), frame, outView, JSplitPane.VERTICAL_SPLIT);
    setOneTouchExpandable(true);
  }

  public SeqArrView getDnaTableView() {
    return (SeqArrView)getResView();
  }
  public void loadFrom(SnpStation model) {
    SeqArrViewOptUI optView = (SeqArrViewOptUI)getOptView();
    if (optView != null)
      optView.loadFrom(model);
  }

  public void setActionOnOptChange(UCController uc) {
    ((SeqArrViewOptUI)getOptView()).setActionOnOptChange(uc);
  }
}
