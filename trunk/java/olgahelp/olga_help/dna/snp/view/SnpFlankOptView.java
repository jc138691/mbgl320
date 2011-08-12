package dna.snp.view;

import project.ucm.UCController;
import project.ucm.AdapterUCCToAL;
import project.ucm.AdapterUCCToPCL;
import project.ucm.UCSelectCheckBoxOnChange;

import javax.utilx.log.Log;
import javax.swing.*;
import javax.swingx.text_fieldx.IntField;
import javax.swingx.eventx.ListenerUtil;
import javax.swingx.panelx.GridBagView;
import java.awt.*;

import dna.snp.SnpFilterFlankOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 05/05/2009, 12:00:26 PM
 */
public class SnpFlankOptView extends JPanel {
  public static Log log = Log.getLog(SnpFlankOptView.class);
  protected JCheckBox minorFreqOn;
  protected IntField  minorFreq;
  protected JCheckBox minReadsOn;
  protected IntField minReads;
//  protected JCheckBox minQltyOn;
//  protected IntField minQlty;
  protected JCheckBox minLenOn;
  protected IntField minLen;
  protected JCheckBox minBestLenOn;
  protected IntField minBestLen;
  protected JCheckBox maxInsertsOn;
  protected IntField maxInserts;
  protected JCheckBox maxDeletesOn;
  protected IntField maxDeletes;
  protected static final int INT_SIZE = 3;

  public SnpFlankOptView(SnpFilterFlankOpt model) {
    init();
    loadFrom(model);
    assemble();
  }
  public SnpFlankOptView() {
    init();
  }

  private void init() {
    setLayout(new FlowLayout(FlowLayout.LEFT));
    String toolTip = "Minimum allowed total number of reads (excluding padding) at a locus";
    minReadsOn = new JCheckBox("Min reads");       minReadsOn.setToolTipText(toolTip);
    minReads = new IntField(INT_SIZE, 0, 1000);    minReads.setToolTipText(toolTip);
    minReads.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(minReadsOn));

//    toolTip = "Ignore bases with quality values < (less than) this minimum allowed";
//    minQltyOn = new JCheckBox("Min qlty");         minQltyOn.setToolTipText(toolTip);
//    minQlty = new IntField(INT_SIZE, 0, 100);      minQlty.setToolTipText(toolTip);
//    minQlty.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(minQltyOn));

    toolTip = "Minimum allowed length of each flank";
    minLenOn = new JCheckBox("Min len");       minLenOn.setToolTipText(toolTip);
    minLen = new IntField(INT_SIZE, 0, 1000);  minLen.setToolTipText(toolTip);
    minLen.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(minLenOn));

    toolTip = "Minimum allowed length of the longest flank";
    minBestLenOn = new JCheckBox("Min longest len");       minBestLenOn.setToolTipText(toolTip);
    minBestLen = new IntField(INT_SIZE, 0, 1000);  minBestLen.setToolTipText(toolTip);
    minBestLen.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(minBestLenOn));

    toolTip = "Minimum minor allele frequency to export flanks using IUPAC codes [%]";
    minorFreqOn = new JCheckBox("IUPAC Min minor freq");       minorFreqOn.setToolTipText(toolTip);
    minorFreq = new IntField(INT_SIZE, 1, 50);  minorFreq.setToolTipText(toolTip);
    minorFreq.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(minorFreqOn));

    toolTip = "Excessive padding: Ignore inserts (i.e. close the gap) if % nucleotides is less than max allowed.";
    maxInsertsOn = new JCheckBox("Max % insertions ignored");   maxInsertsOn.setToolTipText(toolTip);
    maxInserts = new IntField(INT_SIZE, 0, 50);   maxInserts.setToolTipText(toolTip);
    maxInserts.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(maxInsertsOn));

    toolTip = "Minor padding: Ignore deletes if % padding is less than max allowed.";
    maxDeletesOn = new JCheckBox("Max % deletions ignored");   maxDeletesOn.setToolTipText(toolTip);
    maxDeletes = new IntField(INT_SIZE, 0, 50);   maxDeletes.setToolTipText(toolTip);
    maxDeletes.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(maxDeletesOn));
  }
  protected void assemble() {
//    GridBagView bag = new GridBagView();
//    bag.setInserts(new Insets(0, 0, 0, 0));
//    bag.endRow(rejected);
//    bag.endRow(rejectedQlty);

    GridBagView bag2 = new GridBagView();
    bag2.setInserts(new Insets(0, 0, 0, 0));
//    bag2.startRow(minQltyOn);     bag2.startRow(minQlty);    bag2.startRow(new JLabel("   "));
    bag2.startRow(minReadsOn);    bag2.startRow(minReads);  bag2.startRow(new JLabel("   "));
    bag2.startRow(minorFreqOn);   bag2.startRow(minorFreq);  bag2.startRow(new JLabel("   "));
    bag2.startRow(maxInsertsOn);  bag2.endRow(maxInserts);

    bag2.startRow(new JLabel());  bag2.startRow(new JLabel());    bag2.startRow(new JLabel("   "));
    bag2.startRow(minLenOn);         bag2.startRow(minLen);       bag2.startRow(new JLabel("   "));
    bag2.startRow(maxDeletesOn);  bag2.endRow(maxDeletes);

    bag2.startRow(new JLabel());  bag2.startRow(new JLabel());    bag2.startRow(new JLabel("   "));
    bag2.startRow(minBestLenOn);  bag2.startRow(minBestLen);       bag2.startRow(new JLabel("   "));
    bag2.startRow(new JLabel());  bag2.endRow(new JLabel("   "));

    JPanel p = new JPanel(new BorderLayout());
    p.add(bag2, BorderLayout.NORTH);
//    p.add(bag, BorderLayout.WEST);
    add(p);
  }
  public void loadTo(SnpFilterFlankOpt model) {
    model.getIupacMinMinorFreqPcnt().set(minorFreqOn.isSelected(), minorFreq.getInput());
    model.getMinReads().set(minReadsOn.isSelected(), minReads.getInput());
//    model.getMinQlty().set(minQltyOn.isSelected(), minQlty.getInput());
    model.getMinLen().set(minLenOn.isSelected(), minLen.getInput());
    model.getMinBestLen().set(minBestLenOn.isSelected(), minBestLen.getInput());
    model.getMaxInsertsPcnt().set(maxInsertsOn.isSelected(), maxInserts.getInput());
    model.getMaxDeletesPcnt().set(maxDeletesOn.isSelected(), maxDeletes.getInput());
  }
  public void loadFrom(SnpFilterFlankOpt model) {
    minorFreq.setValue(model.getIupacMinMinorFreqPcnt().val());
    // NOTE!!! setXxxOn after setXxx. This is due to xxx.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(xxxOn));
    minorFreqOn.setSelected(model.getIupacMinMinorFreqPcnt().on());

    minReads.setValue(model.getMinReads().val());
    minReadsOn.setSelected(model.getMinReads().on());

//    minQlty.setValue(model.getMinQlty().val());
//    minQltyOn.setSelected(model.getMinQlty().on());

    minLen.setValue(model.getMinLen().val());
    minLenOn.setSelected(model.getMinLen().on());

    minBestLen.setValue(model.getMinBestLen().val());
    minBestLenOn.setSelected(model.getMinBestLen().on());

    maxInserts.setValue(model.getMaxInsertsPcnt().val());
    // NOTE!!! setXxxOn after setXxx. This is due to xxx.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(xxxOn));
    maxInsertsOn.setSelected(model.getMaxInsertsPcnt().on());

    maxDeletes.setValue(model.getMaxDeletesPcnt().val());
    maxDeletesOn.setSelected(model.getMaxDeletesPcnt().on());
  }
  public void setActionOnChange(UCController uc) {
    ListenerUtil.setOneActionListener(minorFreqOn, new AdapterUCCToAL(uc));
    minorFreq.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    ListenerUtil.setOneActionListener(minReadsOn, new AdapterUCCToAL(uc));
    minReads.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

//    ListenerUtil.setOneActionListener(minQltyOn, new AdapterUCCToAL(uc));
//    minQlty.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    ListenerUtil.setOneActionListener(minLenOn, new AdapterUCCToAL(uc));
    minLen.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    ListenerUtil.setOneActionListener(minBestLenOn, new AdapterUCCToAL(uc));
    minBestLen.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    ListenerUtil.setOneActionListener(maxInsertsOn, new AdapterUCCToAL(uc));
    maxInserts.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    ListenerUtil.setOneActionListener(maxDeletesOn, new AdapterUCCToAL(uc));
    maxDeletes.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));
  }

}