package scatt.partial.wf;
import scatt.jm_2008.e1.JmCalcOptE1;

import javax.swingx.listx.ListWithToolTip;
import javax.utilx.log.Log;

import scatt.jm_2008.jm.laguerre.SavePlotPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import project.workflow.task.UCRunTask;
import project.ucm.UCController;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 14/11/2008, Time: 12:18:49
 */
public class JmPotPlotListView_DEL extends SavePlotPanel  {
//
// dk120223 DELETE THIS CLASS
//
  public static Log log = Log.getLog(JmPotPlotListView_DEL.class);
  private UCController[] saveArr;
  private UCController[] plotArr;
  private ListWithToolTip list;

  public JmPotPlotListView_DEL(JmCalcOptE1 model) {
    super("Save/Plot");
    init();
    loadFrom(model);
    assemble();
  }
  private void assemble() {
    endRow(list);
    startRow(saveBttn);  endRow(plotBttn);
  }
  private void init() {
//    saveArr = new UCRunTask[JmCalcOptE1.N_OPTS];
//    plotArr = new UCRunTask[JmCalcOptE1.N_OPTS];
    saveArr = new UCRunTask[0];
    plotArr = new UCRunTask[0];
//    list = new ListWithToolTip(JmCalcOptE1.OPT_NAMES, JmCalcOptE1.OPT_HELP_TIPS);
    list = new ListWithToolTip(null, null);

    plotBttn.addActionListener(new ThisActionListener(plotArr));
    saveBttn.addActionListener(new ThisActionListener(saveArr));
  }
  private class ThisActionListener implements ActionListener {
    private UCController[] runArr;
    public ThisActionListener(UCController[] runArr) {
      this.runArr = runArr;
    }
    public void actionPerformed(ActionEvent e) {
      int idx = list.getSelectedIndex();
      if (idx != -1) {
        UCController uc = runArr[idx];
        if (uc != null)
          uc.run();
      }
    }
  }
  public void loadFrom(JmCalcOptE1 from) {
    list.setSelectedIndex(from.getOptIdx());
  }
  public void loadTo(JmCalcOptE1 to) {
    to.setOptIdx(list.getSelectedIndex());
  }
  public void runPlot(UCController uc, int idx) {
    plotArr[idx] = uc;
  }
  public void runSave(UCController uc, int idx) {
    saveArr[idx] = uc;
  }
  public void runSave(UCController uc) {
    throw new IllegalArgumentException(log.error("use runSave(uc, idx)"));
  }
  public void runPlot(UCController uc) {
    throw new IllegalArgumentException(log.error("use runPlot(uc, idx)"));
  }

}
