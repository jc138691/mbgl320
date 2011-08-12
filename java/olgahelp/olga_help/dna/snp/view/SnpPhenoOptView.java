package dna.snp.view;

import dna.snp.pheno.SnpPhenoOpt;

import javax.swing.*;
import javax.utilx.log.Log;
import javax.swingx.text_fieldx.IntField;
import javax.swingx.text_fieldx.ScientificField;
import javax.swingx.panelx.GridBagView;
import javax.swingx.eventx.ListenerUtil;
import java.awt.*;

import project.ucm.*;
import project.ProjectFrame;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 18/08/2009, 9:49:45 AM
 */
public class SnpPhenoOptView extends JPanel {
  public static Log log = Log.getLog(SnpNghbrOptView.class);

  protected JCheckBox minAssOn;
  protected IntField minAss;

  protected JCheckBox maxPValOn;
  protected ScientificField maxPVal;

  protected JCheckBox minReadsOn;
  protected IntField minReads;

  protected static final int INT_SIZE = 2;
  private static final int FLOAT_SIZE = 8;

  private JLabel phenoHelp;
  private JComboBox phenoA;
  private JComboBox phenoB;
  private static final int MAX_NUM_PHENOTYPES = 10;
  private static final int MIN_NUM_PHENOTYPES = 2;

  public SnpPhenoOptView(SnpPhenoOpt model) {
    init();
    loadFrom(model);
    assemble();
  }

  private void init() {
    setLayout(new FlowLayout(FlowLayout.LEFT));

    String toolTip;
    phenoHelp = new JLabel("Phenotypes: ");

    toolTip = "Minimum accepted phenotype association [%]";
    minAssOn = new JCheckBox("Min % association");   minAssOn.setToolTipText(toolTip);
    minAss = new IntField(INT_SIZE, 0, 100);    minAss.setToolTipText(toolTip);
    minAss.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(minAssOn));

    toolTip = "Maximum accepted exact 2-Tail p-value";
    maxPValOn = new JCheckBox("Max P-value");   maxPValOn.setToolTipText(toolTip);
//    maxPVal = new ScientificField(FLOAT_SIZE, 0.000001f, 0.99f, new DecimalFormat("0.#E0"));   maxPVal.setToolTipText(toolTip);
    maxPVal = new ScientificField(FLOAT_SIZE, 0.000001f, 0.99f);   maxPVal.setToolTipText(toolTip);
    maxPVal.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(maxPValOn));

    toolTip = "Minimum number of reads for each phenotype association";
    minReadsOn = new JCheckBox("Min reads");       minReadsOn.setToolTipText(toolTip);
    minReads = new IntField(INT_SIZE, 0, 100);    minReads.setToolTipText(toolTip);
    minReads.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(minReadsOn));
  }
  protected void assemble() {
    JButton maxPValHelp;
    maxPValHelp = ProjectFrame.makeHelpButton();
    maxPValHelp.addActionListener(new AdapterUCCToALThread(new UCShowHelpMssg(
      "java code adopted from FisherExact.java, http://sourceforge.net/projects/tassel/\n"
        + "see http://www.langsrud.com/fisher.htm\n"
        + "The 2-Tail p-value is calculated as defined in Agresti (1992) Sec. 2.1. (b).\n"
        + "Agresti A, (1992), A Survey of Exact Inference for Contegency Tables, Statitical Science,7,131-153."
      , this)));

    JButton minAssHelp;
    minAssHelp = ProjectFrame.makeHelpButton();
    minAssHelp.addActionListener(new AdapterUCCToALThread(new UCShowHelpMssg(
      "Example:\n"
        + " High/Low-phenotype T/C-SNP passes this test with 80% association level if\n"
        + " T is associated with 'High' in more than 80% cases while at the same time\n"
        + " C is associated with 'Low' in more than 80% occurrences of C.\n\n"
        + " If a SNP has 4xT-High, 1xT-Low, 5xC-Low, 1xG-High,\n"
        + " than Association T-High=80% and C-Low=100%"
      , this)));

    GridBagView bag2 = new GridBagView();
    bag2.setInserts(new Insets(0, 0, 0, 0));
    bag2.startRow(minAssOn);        bag2.startRow(minAss);   bag2.endRow(minAssHelp);
    bag2.startRow(maxPValOn);       bag2.startRow(maxPVal);  bag2.endRow(maxPValHelp);
    bag2.startRow(minReadsOn);      bag2.startRow(minReads); bag2.endRow(new JPanel());

    GridBagView bag = new GridBagView();
//    bag.setInserts(new Insets(0, 0, 0, 0));
    if (phenoA != null  &&  phenoB != null) {
      bag.startRow(phenoHelp);  bag.startRow(phenoA);      bag.endRow(phenoB);
    }
    else {
      bag.endRow(phenoHelp);
    }

    JPanel p = new JPanel(new BorderLayout());
    p.add(bag2, BorderLayout.NORTH);
    p.add(bag, BorderLayout.SOUTH);
    add(p);
  }
  public void loadTo(SnpPhenoOpt opt) {
    opt.getMinAssoc().set(minAssOn.isSelected(), minAss.getInput());
    opt.getMinReads().set(minReadsOn.isSelected(), minReads.getInput());
    opt.getMaxPValue().set(maxPValOn.isSelected(), maxPVal.getInput());
    if (phenoA != null  &&  phenoB != null) {
      opt.setPhenoIdxA(phenoA.getSelectedIndex());
      opt.setPhenoIdxB(phenoB.getSelectedIndex());
    }
  }
  public void loadFrom(SnpPhenoOpt opt) {
    maxPVal.setValue(opt.getMaxPValue().getVal());
    // NOTE!!! setXxxOn after setXxx. This is due to xxx.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(xxxOn));
    maxPValOn.setSelected(opt.getMaxPValue().getOn());

    minReads.setValue(opt.getMinReads().getVal());
    minReadsOn.setSelected(opt.getMinReads().getOn());

    minAss.setValue(opt.getMinAssoc().getVal());
    minAssOn.setSelected(opt.getMinAssoc().getOn());

    String[] list = opt.getPhenoList();
    if (list != null
      && list.length >= MIN_NUM_PHENOTYPES
      && list.length <= MAX_NUM_PHENOTYPES) {
      phenoA = new JComboBox(list);
      phenoB = new JComboBox(list);
      int idxA = opt.getPhenoIdxA();
      int idxB = opt.getPhenoIdxB();
      if (idxA == idxB  && idxA == 0) {
        idxB = 1; // default to next
      }
      if (idxA >= 0 && idxA < list.length)
        phenoA.setSelectedIndex(idxA);
      if (idxB >= 0 && idxB < list.length)
        phenoB.setSelectedIndex(idxB);
      phenoA.setEditable(false);
      phenoB.setEditable(false);

    }
    if (list == null || list.length == 0) {
      phenoHelp.setText("Phenotypes are not imported");
    }
    if (list != null && list.length < MIN_NUM_PHENOTYPES) {
      phenoHelp.setText("Import at least two phenotypes");
    }
    if (list != null && list.length > MAX_NUM_PHENOTYPES) {
      phenoHelp.setText("Maximum allowed number of phenotypes: " + MAX_NUM_PHENOTYPES);
    }

  }
  public void setActionOnChange(UCController uc) {
    ListenerUtil.setOneActionListener(maxPValOn, new AdapterUCCToAL(uc));
    maxPVal.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    ListenerUtil.setOneActionListener(minReadsOn, new AdapterUCCToAL(uc));
    minReads.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    ListenerUtil.setOneActionListener(minAssOn, new AdapterUCCToAL(uc));
    minAss.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    if (phenoA != null)
      phenoA.addActionListener(new AdapterUCCToAL(uc));
    if (phenoB != null)
      phenoB.addActionListener(new AdapterUCCToAL(uc));
  }
}