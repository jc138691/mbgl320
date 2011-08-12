package dna.table.view.opt;

import olga.SnpStation;

import javax.swing.*;
import javax.utilx.log.Log;
import javax.swingx.JTabbedPaneX;
import java.awt.*;

import project.workflow.task.TaskOptView;
import project.ucm.UCController;
import dna.contig.view.opt.BaseViewOptUI;
import dna.seq.arr.view.opt.SeqArrFilterOptUI;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 3/03/2009 at 09:52:11.
 */
public class SeqArrViewOptUI extends TaskOptView<SnpStation> {
  public static Log log = Log.getLog(SeqArrViewOptUI.class);
  private JTabbedPaneX ui;

  private BaseViewOptUI dispView;
  private int dispIdx = -1;
  private JRadioButton dispFocus;

  private SeqArrFilterOptUI filterView;
  private int filterIdx = -1;
  private JRadioButton filterFocus;

  public SeqArrViewOptUI(SnpStation project) {
    init();
    loadFrom(project);
//    assemble();       // called from loadFrom(project); 
  }

  public void loadTo(SnpStation model) {  // <---- REMEMBER TO UPDATE when adding new option views
    if (dispView != null)
      dispView.loadTo(model);
    if (filterView != null)
      filterView.loadTo(model.getFastaOpt());
  }
  public void loadFrom(SnpStation model) {
    reset();
    dispView = new BaseViewOptUI(model);
    filterView = new SeqArrFilterOptUI(model.getFastaOpt());
    assemble();
  }
  private void assemble() {
    setDispView(dispView);
    setFilterView(filterView);
  }


  public void reset() {
    ui.removeAll();
    dispView = null;       dispIdx = -1;
    filterView = null;       filterIdx = -1;
  }
  private void init() {
    setLayout(new GridLayout(1, 1));
    dispFocus = new JRadioButton();
    filterFocus = new JRadioButton();
    ButtonGroup group = new ButtonGroup();
    group.add(dispFocus);
    group.add(filterFocus);
//    ui = new JTabbedPaneX(JTabbedPane.TOP);
    ui = new JTabbedPaneX(JTabbedPane.LEFT);
    add(ui);
  }
  private void rebuild() {  log.dbg("rebuild()");
    dispIdx = ui.processView(dispView, dispIdx, dispFocus.isSelected(), "View", "Viewing options");
    filterIdx = ui.processView(filterView, filterIdx, filterFocus.isSelected(), "Filter", "Filtering options");
    validate();
    repaint();
  }

  public JTable getSelectedResultsTable()
  {
    int idx = ui.getSelectedIndex();
    if (idx == -1)
      return null;
//    if (potIdx == idx) {
//      return potView.getTableView();
//    }
    return null;
  }
  public void setDispView(BaseViewOptUI view) {
    this.dispView = view;
    dispFocus.setSelected(true);
    rebuild();
  }
  public void setFilterView(SeqArrFilterOptUI view) {
    this.filterView = view;
    filterFocus.setSelected(true);
    rebuild();
  }
  public void setActionOnOptChange(UCController uc) {
    dispView.setActionOnChange(uc);
    filterView.setActionOnChange(uc);
  }
}
