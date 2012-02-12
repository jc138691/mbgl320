package dna.snp.view;

import project.ucm.UCController;

import javax.utilx.log.Log;
import javax.swingx.JTabbedPaneX;
import javax.swingx.panelx.GridBagView;
import javax.swing.*;

import dna.snp.*;

import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 15/05/2009, 2:29:53 PM
 */
public class SnpOptUI extends GridBagView {
  public static Log log = Log.getLog(SnpOptUI.class);
  private JTabbedPaneX ui;

  private SnpFilterOptView filterView;
  private int filterIdx = -1;
  private JRadioButton filterFocus;

  private SnpFlankOptView flankView;
  private int flankIdx = -1;
  private JRadioButton flankFocus;

  private SnpNghbrOptView nbrView;
  private int nbrIdx = -1;
  private JRadioButton nbrFocus;

  private SnpExportOptView exportView;
  private int exportIdx = -1;
  private JRadioButton exportFocus;

  private SnpPhenoOptView phenoView;
  private int phenoIdx = -1;
  private JRadioButton phenoFocus;

  public SnpOptUI(SnpOpt model) {
    init();
    loadFrom(model);
    assemble();       // called from loadFrom(project);
  }

  public void loadTo(SnpOpt model) {  // <---- REMEMBER TO UPDATE when adding new option views
    filterView.loadTo(model);
    flankView.loadTo(model.getFlankFilter());
    nbrView.loadTo(model.getNghbrFilter());
    phenoView.loadTo(model.getPhenoOpt());
    exportView.loadTo(model);
  }
  public void loadFrom(SnpOpt model) {
    filterView = new SnpFilterOptView(model);
    flankView = new SnpFlankOptView(model.getFlankFilter());
    nbrView = new SnpNghbrOptView(model.getNghbrFilter());
    phenoView = new SnpPhenoOptView(model.getPhenoOpt());
    exportView = new SnpExportOptView(model);
  }
  private void assemble() {
    setExportView(exportView);
    setPhenoView(phenoView);
    setNghbrView(nbrView);
    setFlankView(flankView);
    setFilterView(filterView);
  }


  public void reset() {
    ui.removeAll();
    filterView = null;       filterIdx = -1;
    nbrView = null;       nbrIdx = -1;
    flankView = null;       flankIdx = -1;
    phenoView = null;       phenoIdx = -1;
    exportView = null;       exportIdx = -1;
  }
  private void init() {
    setLayout(new GridLayout(1, 1));
    filterFocus = new JRadioButton();
    nbrFocus = new JRadioButton();
    flankFocus = new JRadioButton();
    phenoFocus = new JRadioButton();
    exportFocus = new JRadioButton();
    ButtonGroup group = new ButtonGroup();
    group.add(filterFocus);
    group.add(nbrFocus);
    group.add(flankFocus);
    group.add(phenoFocus);
    group.add(exportFocus);
//    ui = new JTabbedPaneX(JTabbedPane.TOP);
    ui = new JTabbedPaneX(JTabbedPane.LEFT);
    add(ui);
  }
  private void rebuild() {  log.dbg("rebuild()");
    filterIdx = ui.processView(filterView, filterIdx, filterFocus.isSelected(), "Filter", "SNP filtering options");
    flankIdx = ui.processView(flankView, flankIdx, flankFocus.isSelected(), "Flanks", "Filter SNPs by flanks");
    nbrIdx = ui.processView(nbrView, nbrIdx, nbrFocus.isSelected(), "Nghbrhood", "Remove SNPs with bad neighborhood flanks");
    phenoIdx = ui.processView(phenoView, phenoIdx, phenoFocus.isSelected(), "Phenotype", "Filter SNPs by phenotypes");
    exportIdx = ui.processView(exportView, exportIdx, exportFocus.isSelected(), "Export", "Export SNPs from all contigs");
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
  public void setExportView(SnpExportOptView view) {
    this.exportView = view;
    exportFocus.setSelected(true);
    rebuild();
  }
  public void setPhenoView(SnpPhenoOptView view) {
    this.phenoView = view;
    phenoFocus.setSelected(true);
    rebuild();
  }
  public void setFilterView(SnpFilterOptView view) {
    this.filterView = view;
    filterFocus.setSelected(true);
    rebuild();
  }
  public void setFlankView(SnpFlankOptView view) {
    this.flankView = view;
    flankFocus.setSelected(true);
    rebuild();
  }

  public void setNghbrView(SnpNghbrOptView view) {
    this.nbrView = view;
    nbrFocus.setSelected(true);
    rebuild();
  }

  public void setActionOnChange(UCController uc) {
    flankView.setActionOnChange(uc);
    nbrView.setActionOnChange(uc);
    filterView.setActionOnChange(uc);
    phenoView.setActionOnChange(uc);
  }
//  public void setActionExportSnps(UCController uc) {
//    exportView.setActionExportSnps(uc);
//  }

  public SnpExportOptView getExportView() {
    return exportView;
  }
}
