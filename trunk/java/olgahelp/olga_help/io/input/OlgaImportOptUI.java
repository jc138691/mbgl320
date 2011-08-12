package io.input;

import olga.SnpStation;
import project.workflow.task.TaskOptView;

import javax.utilx.log.Log;
import javax.swingx.JTabbedPaneX;
import javax.swing.*;

import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 27/03/2009, 2:10:42 PM
 */
public class OlgaImportOptUI extends TaskOptView<SnpStation> {
  public static Log log = Log.getLog(OlgaImportOptUI.class);
  private JTabbedPaneX ui;

  private ImportFastaOptView fastaView;
  private int fastaIdx = -1;
  private JRadioButton fastaFocus;

  private ImportPhenoOptView phenoView;
  private int phenoIdx = -1;
  private JRadioButton phenoFocus;

  private ImportCafOptView cafView;
  private int cafIdx = -1;
  private JRadioButton cafFocus;

  private ImportAceOptView aceView;
  private int aceIdx = -1;
  private JRadioButton aceFocus;

  public static final int IMPORT_CAF = 0;
  public static final int IMPORT_ACE = 1;
  public static final int IMPORT_PHENOTYPES = 2;
  public static final int IMPORT_FASTA = 3;

  public OlgaImportOptUI(SnpStation project) {
    init();
    loadFrom(project);
  }

  public void loadTo(SnpStation model) {  // <---- REMEMBER TO UPDATE when adding new option views
    if (fastaView != null)
      fastaView.loadTo(model);
    if (phenoView != null)
      phenoView.loadTo(model);
    if (cafView != null)
      cafView.loadTo(model);
    if (aceView != null)
      aceView.loadTo(model);
    model.setImportFileType(ui.getSelectedIndex());
  }
  public void loadFrom(SnpStation model) {
    reset();
    fastaView = new ImportFastaOptView(model);
    phenoView = new ImportPhenoOptView(model);
    cafView = new ImportCafOptView(model);
    aceView = new ImportAceOptView(model);
    assemble();
    ui.setSelectedIndex(model.getImportFileType());
  }
  private void assemble() {
    setCafView(cafView);
    setAceView(aceView);
    setPhenoView(phenoView);
    setFastaView(fastaView);
  }

  public void reset() {
    ui.removeAll();
    fastaView = null;       fastaIdx = -1;
    phenoView = null;       phenoIdx = -1;
    cafView = null;       cafIdx = -1;
    aceView = null;       aceIdx = -1;
  }
  private void init() {
    setLayout(new GridLayout(1, 1));
    fastaFocus = new JRadioButton();
    phenoFocus = new JRadioButton();
    cafFocus = new JRadioButton();
    aceFocus = new JRadioButton();
    ButtonGroup group = new ButtonGroup();
    group.add(fastaFocus);
    group.add(phenoFocus);
    group.add(cafFocus);
    group.add(aceFocus);
    ui = new JTabbedPaneX(JTabbedPane.TOP);
//    ui = new JTabbedPaneX(JTabbedPane.LEFT);
    add(ui);
  }
  private void rebuild() {  log.dbg("rebuild()");
    cafIdx = ui.processView(cafView, cafIdx, cafFocus.isSelected(), "CAF", "CAF options");
    aceIdx = ui.processView(aceView, aceIdx, aceFocus.isSelected(), "ACE", "ACE options");
    phenoIdx = ui.processView(phenoView, phenoIdx, phenoFocus.isSelected(), "Phenotypes", "Phenotype options");
    fastaIdx = ui.processView(fastaView, fastaIdx, fastaFocus.isSelected(), "FASTA", "FASTA options");
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
  public void setFastaView(ImportFastaOptView view) {
    this.fastaView = view;
    fastaFocus.setSelected(true);
    rebuild();
  }
  public void setPhenoView(ImportPhenoOptView view) {
    this.phenoView = view;
    phenoFocus.setSelected(true);
    rebuild();
  }
  public void setCafView(ImportCafOptView view) {
    this.cafView = view;
    cafFocus.setSelected(true);
    rebuild();
  }
  public ImportCafOptView getCafView() {
    return cafView;
  }
  public void setAceView(ImportAceOptView view) {
    this.aceView = view;
    aceFocus.setSelected(true);
    rebuild();
  }
  public ImportAceOptView getAceView() {
    return aceView;
  }

}