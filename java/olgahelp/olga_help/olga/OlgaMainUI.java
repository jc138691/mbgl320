package olga;

import project.workflow.task.DefaultTaskUI;

import javax.swing.*;
import javax.swingx.JTabbedPaneX;
import javax.swingx.textx.TextView;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import dna.table.view.SeqArrUI;
import dna.contig.view.ContigArrUI;
import dna.pheno.PhenoMap;
import io.input.OlgaImportOptUI;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 13/02/2009 at 13:25:41.
 */
public class OlgaMainUI extends JPanel {
  private static OlgaMainUI instance;
  private JTabbedPaneX tabbedPane;
  private JLabel status;

  private TextView consoleView;
  private int consoleIdx = -1;
  private JRadioButton consoleFocus;

  private DefaultTaskUI importUI;
  private int importIdx = -1;
  private JRadioButton importFocus;

  private SeqArrUI seqArrUI;
  private int dnaIdx = -1;
  private JRadioButton seqArrFocus;

  private ContigArrUI contigArrUI;
  private int contigIdx = -1;
  private JRadioButton contigFocus;

//  private PhenoContigArrUI phenoTableUI;
//  private int phenoIdx = -1;
//  private JRadioButton phenoFocus;

  private PhenoMap phenoMap;

  private OlgaMainUI() {
    init();
  }
  public static OlgaMainUI getInstance() {
    if (instance == null) {
      instance = new OlgaMainUI();
    }
    return instance;
  }

  synchronized public void repaint()
  {
    super.repaint();
  }

  synchronized public void validate()
  {
    super.validate();
  }

  public void rebuildAll() {
//    tabbedPane.removeAll();
//    importFileIdx = -1;
    rebuild();
  }
  private void init() {
    seqArrFocus = new JRadioButton();
    contigFocus = new JRadioButton();
//    phenoFocus = new JRadioButton();
    importFocus = new JRadioButton();
    consoleFocus = new JRadioButton();
    ButtonGroup group = new ButtonGroup();
    group.add(seqArrFocus);
    group.add(contigFocus);
//    group.add(phenoFocus);
    group.add(importFocus);
    group.add(consoleFocus);
    consoleFocus.setSelected(true);

    setLayout(new BorderLayout());

    tabbedPane = new JTabbedPaneX(JTabbedPane.TOP);
    add(tabbedPane, BorderLayout.CENTER);

    status = new JLabel(" ", SwingConstants.LEFT);
    add(status, BorderLayout.SOUTH);
  }
  public void setStatus(String msg) {
    status.setText(msg);
  }
  public String getStatus() {
    return status.getText();
  }
  public void setStatusWithTime(String msg) {
    SimpleDateFormat date = new SimpleDateFormat();
    try {
      date.applyPattern("HH:mm:ss");
    }
    catch (IllegalArgumentException e) {
    }
    status.setText(date.format(new Date()) + " : " + msg);
  }
  public void loadFrom(SnpStation model) {
    if (importUI != null) {
      OlgaImportOptUI importOptView = (OlgaImportOptUI)importUI.getOptView();
      importOptView.loadFrom(model);
    }
    if (contigArrUI != null)
      contigArrUI.loadFrom(model);
//    if (phenoTableUI != null)
//      phenoTableUI.loadFrom(model);
    if (seqArrUI != null)
      seqArrUI.loadFrom(model);
    rebuild();
  }

  private void rebuild() {
    SnpStation project = SnpStationProject.getInstance();
//    int maxCols = project.getTableDisplayOpt().getMaxLen();
    String maxNColsMssg = "";
//    if (maxCols > 0)
//      maxNColsMssg = " [first " + maxCols + " columns]";

    consoleIdx = tabbedPane.processView(consoleView, consoleIdx, consoleFocus.isSelected()
      , "Console", "Console output for system-wide messages.");
    if (consoleIdx != -1) {
      tabbedPane.setBackgroundAt(consoleIdx, project.getColorConsole());
    }

    importIdx = tabbedPane.processView(importUI, importIdx, importFocus.isSelected()
      , "Import", "Import data files");
    if (importIdx != -1) {
      tabbedPane.setBackgroundAt(importIdx, project.getColorImportOpt());
    }

    dnaIdx = tabbedPane.processView(seqArrUI, dnaIdx, seqArrFocus.isSelected()
      , "Reads", "View loaded DNA sequences/Reads");
    if (dnaIdx != -1) {
      tabbedPane.setBackgroundAt(dnaIdx, project.getColorSeqView());
    }

    contigIdx = tabbedPane.processView(contigArrUI, contigIdx, contigFocus.isSelected()
      , "Contigs", "Explore SNPs in the imported contigs");
    if (contigIdx != -1) {
      tabbedPane.setBackgroundAt(contigIdx, project.getColorSeqView());
    }

//    phenoIdx = tabbedPane.processView(phenoTableUI, phenoIdx, phenoFocus.isSelected()
//      , "Phenotype SNPs", "Explore phenotype SNPs in the imported contigs");
//    if (phenoIdx != -1) {
//      tabbedPane.setBackgroundAt(phenoIdx, project.getColorSeqView());
//    }

    JFrame frame = OlgaFrame.getInstance();
    if (frame != null) {
      frame.validate();
      frame.repaint();
    }
    validate();
    repaint();
  }

  public void showNotImplemented() {
    JOptionPane.showMessageDialog(this, "This option has not been implemented yet.");
  }
  public void setConsoleView(TextView view) {
    this.consoleView = view;
    consoleFocus.setSelected(true);
    rebuild();
  }
  public TextView getConsoleView() {
    return consoleView;
  }

  public JTable getSelectedResultsTable()
  {
//    int idx = tabbedPane.getSelectedIndex();
//    if (idx == -1)
//      return null;

//    if (zIdx == idx) { // NOTE!!!  Table's view may be truncated
//      QBench project = QBenchProject.getInstance();
//      TableDisplayOpt model = project.getTableDisplayOpt();
//      TableDisplayOpt tmp = new TableDisplayOpt();
//      tmp.setDigitsModel(model.getDigitsModel());
//      tmp.setMaxLen(-1); //turn off
//      tmp.setMaxNumRows(-1); //turn off
//      tmp.setCountOn(false);
//      return zTableView.makeTableView(tmp, this);
//    }
    return null;
  }

  public void resetAll()
  {
//    tabbedPane.removeAll();
//    importFileView = null;     importFileIdx = -1;
    rebuild();
  }
  public void setImportUI(DefaultTaskUI view) {
    this.importUI = view;
    importFocus.setSelected(true);
    rebuild();
  }

  public DefaultTaskUI getImportUI()  {
    return importUI;
  }

  public void setSeqArrUI(SeqArrUI view) {
    this.seqArrUI = view;
    seqArrFocus.setSelected(true);
    rebuild();
  }

  public SeqArrUI getSeqArrUI()  {
    return seqArrUI;
  }

  public void setContigArrUI(ContigArrUI view) {
    this.contigArrUI = view;
    contigFocus.setSelected(true);
    rebuild();
  }
//  public void setPhenoTableUI(PhenoContigArrUI view) {
//    this.phenoTableUI = view;
//    phenoFocus.setSelected(true);
//    rebuild();
//  }

  public ContigArrUI getContigArrUI()  {
    return contigArrUI;
  }

//  public PhenoContigArrUI getPhenoTableUI()  {
//    return phenoTableUI;
//  }

  public void setPhenoMap(PhenoMap phenoMap) {
    this.phenoMap = phenoMap;
  }

  public PhenoMap getPhenoMap() {
    return phenoMap;
  }
}



