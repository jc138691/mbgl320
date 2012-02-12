package dna.contig.view.opt;

import olga.SnpStation;
import project.workflow.task.TaskOptView;
import project.ucm.UCController;

import javax.utilx.log.Log;
import javax.swingx.JTabbedPaneX;
import javax.swing.*;

import dna.snp.view.SnpOptUI;
import dna.snp.view.SnpExportOptView;

import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 21/04/2009, 12:30:02 PM
 */
public class ContigArrViewOptUI extends TaskOptView<SnpStation> {
  public static Log log = Log.getLog(ContigArrViewOptUI.class);
  private JTabbedPaneX ui;

  private BaseViewOptUI dispView;
  private int dispIdx = -1;
  private JRadioButton dispFocus;
//
//  private PageOptView pageView;
//  private int pageIdx = -1;
//  private JRadioButton pageFocus;

  private SnpOptUI snpView;
  private int snpIdx = -1;
  private JRadioButton snpFocus;

  public ContigArrViewOptUI(SnpStation project) {
    init();
    loadFrom(project);
//    assemble();       // called from loadFrom(project);
  }

  public void loadTo(SnpStation model) {  // <---- REMEMBER TO UPDATE when adding new option views
    if (dispView != null)
      dispView.loadTo(model);
    if (snpView != null)
      snpView.loadTo(model.getSnpOpt());
  }
  public void loadFrom(SnpStation model) {
    reset();
//    CafOpt opt = model.getCafOpt();
//    infoView = new DnaTableInfoView(opt.getDnaTableInfo());
//    pageView = new PageOptView(opt);
    dispView = new BaseViewOptUI(model);
    snpView = new SnpOptUI(model.getSnpOpt());
    assemble();
  }
  private void assemble() {
    setDispView(dispView);
    setSnpView(snpView);
  }


  public void reset() {
    ui.removeAll();
    dispView = null;       dispIdx = -1;
    snpView = null;       snpIdx = -1;
  }
  private void init() {
    setLayout(new GridLayout(1, 1));
//    infoFocus = new JRadioButton();
//    pageFocus = new JRadioButton();
    dispFocus = new JRadioButton();
    snpFocus = new JRadioButton();
    ButtonGroup group = new ButtonGroup();
//    group.add(infoFocus);
//    group.add(pageFocus);
    group.add(dispFocus);
    group.add(snpFocus);
//    ui = new JTabbedPaneX(JTabbedPane.TOP);
    ui = new JTabbedPaneX(JTabbedPane.LEFT);
    add(ui);
  }
  private void rebuild() {  log.dbg("rebuild()");
    dispIdx = ui.processView(dispView, dispIdx, dispFocus.isSelected(), "View", "Viewing options");
    snpIdx = ui.processView(snpView, snpIdx, snpFocus.isSelected(), "SNPs", "SNPs selection options");
//    ui.setAlignmentX(Component.LEFT_ALIGNMENT );
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
  public void setSnpView(SnpOptUI view) {
    this.snpView = view;
    snpFocus.setSelected(true);
    rebuild();
  }
  public void setDispView(BaseViewOptUI view) {
    this.dispView = view;
    dispFocus.setSelected(true);
    rebuild();
  }
  public SnpOptUI getSnpView() {
    return snpView;
  }
  public SnpExportOptView getSnpExportOptView() {
    return snpView.getExportView();
  }

  public void setActionOnOptChange(UCController uc) {
    dispView.setActionOnChange(uc);
    snpView.setActionOnChange(uc);
  }
}
