package qm_station.ui.scatt;
import qm_station.QMS;
import qm_station.ui.StepGridView;
import project.workflow.task.TaskOptView;
import project.workflow.task.UCRunTask;
import scatt.jm_2008.e1.JmOptE1;
import scatt.jm_2008.jm.laguerre.JmLagrrView;
import scatt.jm_2008.jm.JmTestView;
import scatt.partial.wf.JmPotPlotListView;

import javax.swingx.panelx.GridBagView;
import javax.swing.*;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 14/11/2008, Time: 12:11:47
 */
abstract public class JmPotOptView extends TaskOptView<QMS> {

  protected StepGridView gridView;
  protected StepGridView engView;
  protected JmLagrrView lagrrView;
  protected JmTestView testView;
  protected JmPotPlotListView listOptView;

  protected JmPotOptView(QMS model) {
    init();
    loadFrom(model);
    assemble();
  }
  private void init() {
  }
  private void assemble() {
    GridBagView c;
    c = new GridBagView(); // column #1
    c.endRow(lagrrView);
    c.endRow(gridView);
    c.endRow(engView);
    c.endRow(testView);
    startRow(c);

    JPanel panel = new JPanel(new BorderLayout());
    c = new GridBagView(); // column #2
    c.endRow(listOptView);
    panel.add(c, BorderLayout.NORTH);
    endRow(panel);

  }
  public void loadTo(JmOptE1 model) {
    gridView.loadTo(model.getGrid());
    lagrrView.loadTo(model.getJmModel());
    engView.loadTo(model.getGridEng());
    testView.loadTo(model.getJmTest());
    listOptView.loadTo(model);
  }
  public void loadFrom(JmOptE1 model) {
    gridView = new StepGridView(model.getGrid(), model.getGridName());
    lagrrView = new JmLagrrView(model.getJmModel());
    testView = new JmTestView(model.getJmTest());
    engView = new StepGridView(model.getGridEng(), "Energy grid");

    listOptView = new JmPotPlotListView(model);
  }
  public void setRunTest(UCRunTask uc) {
    testView.runTest(uc);
  }
  public void setRunPlot(UCRunTask uc, int idxType) {
    listOptView.runPlot(uc, idxType);
  }
  public void setRunSave(UCRunTask uc, int idxType) {
    listOptView.runSave(uc, idxType);
  }
}
