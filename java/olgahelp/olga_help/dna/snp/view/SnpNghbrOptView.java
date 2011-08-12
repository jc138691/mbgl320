package dna.snp.view;

import dna.snp.SnpNghbrFilterOpt;

import javax.utilx.log.Log;
import javax.swing.*;
import javax.swingx.text_fieldx.IntField;
import javax.swingx.panelx.GridBagView;
import javax.swingx.eventx.ListenerUtil;

import project.ucm.UCSelectCheckBoxOnChange;
import project.ucm.UCController;
import project.ucm.AdapterUCCToAL;
import project.ucm.AdapterUCCToPCL;
import project.model.IntOpt;

import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 09/06/2009, 3:45:27 PM
 */
public class SnpNghbrOptView extends JPanel {
  public static Log log = Log.getLog(SnpNghbrOptView.class);
  protected JCheckBox minorFreqOn;
  protected IntField  minorFreq;
  protected JCheckBox sizeOn;
  protected IntField size;
  protected JCheckBox maxPadOn;
  protected IntField maxPad;
  protected static final int INT_SIZE = 2;

  public SnpNghbrOptView(SnpNghbrFilterOpt model) {
    init();
    loadFrom(model);
    assemble();
  }

  private void init() {
    setLayout(new FlowLayout(FlowLayout.LEFT));

    String toolTip;
    toolTip = "Max allowed neighbourhood minor-allele frequency [%]";
    minorFreqOn = new JCheckBox("Max minor freq");       minorFreqOn.setToolTipText(toolTip);
    minorFreq = new IntField(INT_SIZE, 1, 50);           minorFreq.setToolTipText(toolTip);
    minorFreq.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(minorFreqOn));

    toolTip = "Num of neighbourhood bases on each side of a SNP. Reject SNP if neighbourhood is not valid.";
    sizeOn = new JCheckBox("Size");                sizeOn.setToolTipText(toolTip);
    size = new IntField(INT_SIZE, 1, 50);          size.setToolTipText(toolTip);
    size.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(sizeOn));

    toolTip = "Maximum allowed neighbourhood padding [%]";
    maxPadOn = new JCheckBox("Max padding");   maxPadOn.setToolTipText(toolTip);
    maxPad = new IntField(INT_SIZE, 0, 100);   maxPad.setToolTipText(toolTip);
    maxPad.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(maxPadOn));
  }
  protected void assemble() {
    GridBagView bag2 = new GridBagView();
    bag2.setInserts(new Insets(0, 0, 0, 0));
    bag2.startRow(sizeOn);         bag2.endRow(size);
    bag2.startRow(minorFreqOn);   bag2.endRow(minorFreq);
    bag2.startRow(maxPadOn);      bag2.endRow(maxPad);

    JPanel p = new JPanel(new BorderLayout());
    p.add(bag2, BorderLayout.NORTH);
    add(p);
  }
  public void loadTo(SnpNghbrFilterOpt model) {
    model.getSize().set(sizeOn.isSelected(), size.getInput());
    model.getMaxMinorFreqPcnt().set(minorFreqOn.isSelected(), minorFreq.getInput());
    model.getMaxPaddingPcnt().set(maxPadOn.isSelected(), maxPad.getInput());
  }
  public void loadFrom(SnpNghbrFilterOpt model) {
    size.setValue(model.getSize().val());
    // NOTE!!! setXxxOn after setXxx. This is due to xxx.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(xxxOn));
    sizeOn.setSelected(model.getSize().on());

    minorFreq.setValue(model.getMaxMinorFreqPcnt().val());
    minorFreqOn.setSelected(model.getMaxMinorFreqPcnt().on());

    maxPad.setValue(model.getMaxPaddingPcnt().val());
    maxPadOn.setSelected(model.getMaxPaddingPcnt().on());
  }
  public void setActionOnChange(UCController uc) {
    ListenerUtil.setOneActionListener(sizeOn, new AdapterUCCToAL(uc));
    size.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));
    ListenerUtil.setOneActionListener(minorFreqOn, new AdapterUCCToAL(uc));
    minorFreq.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));
    ListenerUtil.setOneActionListener(maxPadOn, new AdapterUCCToAL(uc));
    maxPad.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));
  }

}