package dna.snp.view;

import project.ucm.UCController;
import project.ucm.AdapterUCCToAL;
import project.ucm.AdapterUCCToPCL;
import project.ucm.UCSelectCheckBoxOnChange;

import javax.utilx.log.Log;
import javax.swingx.text_fieldx.IntField;
import javax.swingx.eventx.ListenerUtil;
import javax.swingx.panelx.GridBagView;
import javax.swingx.tablex.OneColorEditor;
import javax.swing.*;
import java.awt.*;

import dna.snp.SnpFilterOpt;
import dna.snp.SnpOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 05/05/2009, 11:29:58 AM
 */
public class SnpFilterOptView extends JPanel {
  public static Log log = Log.getLog(SnpFilterOptView.class);
  protected OneColorEditor rejected;
  protected OneColorEditor rejectedQlty;

  protected JCheckBox minReadsOn;
  protected IntField minReads;

  protected JCheckBox minMinorReadsOn;
  protected IntField minMinorReads;

  protected JCheckBox minQltyOn;
  protected IntField minQlty;

  protected JCheckBox maxPadOn;
  protected IntField maxPad;

  protected JCheckBox trimLenOn;
  protected IntField trimLen;

  protected static final int INT_SIZE = 2;
  protected JCheckBox minorFreqOn;
  protected IntField  minorFreq;

  private OneColorEditor viewSnp;
  private JRadioButton tintFont;
  private JRadioButton colorFont;

  protected JCheckBox bestSnpPerContig;

  public SnpFilterOptView(SnpOpt model) {
    init();
    loadFrom(model);
    assemble();
  }
  public SnpFilterOptView() {
    init();
  }

  private void init() {
    setLayout(new FlowLayout(FlowLayout.LEFT));
    String toolTip = "Minimum accepted total number of reads (excluding padding) at a locus";
    minReadsOn = new JCheckBox("Min reads");       minReadsOn.setToolTipText(toolTip);
    minReads = new IntField(INT_SIZE, 0, 1000);    minReads.setToolTipText(toolTip);
    minReads.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(minReadsOn));

    toolTip = "Minimum accepted number of minor-allele reads at a locus";
    minMinorReadsOn = new JCheckBox("Min minor reads");       minMinorReadsOn.setToolTipText(toolTip);
    minMinorReads = new IntField(INT_SIZE, 0, 1000);    minMinorReads.setToolTipText(toolTip);
    minMinorReads.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(minMinorReadsOn));

    toolTip = "Ignore bases with quality values < (less than) this minimum allowed";
    minQltyOn = new JCheckBox("Min qlty");         minQltyOn.setToolTipText(toolTip);
    minQlty = new IntField(INT_SIZE, 0, 100);      minQlty.setToolTipText(toolTip);
    minQlty.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(minQltyOn));

    toolTip = "Maximum padding [%]";
    maxPadOn = new JCheckBox("Max padding");   maxPadOn.setToolTipText(toolTip);
    maxPad = new IntField(INT_SIZE, 0, 100);   maxPad.setToolTipText(toolTip);
    maxPad.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(maxPadOn));

    toolTip = "In this filtering, ignore that many bases from both ends";
    trimLenOn = new JCheckBox("Trim len");   trimLenOn.setToolTipText(toolTip);
    trimLen = new IntField(INT_SIZE, 0, 100);   trimLen.setToolTipText(toolTip);
    trimLen.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(trimLenOn));

    tintFont = new JRadioButton("Tint");
    colorFont = new JRadioButton("Color");
    ButtonGroup group = new ButtonGroup();
    group.add(tintFont);
    group.add(colorFont);
    tintFont.setSelected(true);  // default

    toolTip = "Minimum minor allele frequency [%]";
    minorFreqOn = new JCheckBox("Min minor freq");       minorFreqOn.setToolTipText(toolTip);
    minorFreq = new IntField(INT_SIZE, 1, 50);  minorFreq.setToolTipText(toolTip);
    minorFreq.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(minorFreqOn));

    toolTip = "Show only the best (largest minorAlleleFreq) SNP per contig";
    bestSnpPerContig = new JCheckBox("Best SNP");       bestSnpPerContig.setToolTipText(toolTip);

    toolTip = "Reject SNP if padding exceeds maximum allowed [%]";
    maxPadOn.setToolTipText(toolTip);   maxPad.setToolTipText(toolTip);
  }
  protected void assemble() {
    GridBagView bag = new GridBagView();
    bag.setInserts(new Insets(0, 0, 0, 0));
    bag.startRow(rejected);      bag.startRow(new JLabel(""));    bag.endRow(new JLabel(""));
    bag.startRow(rejectedQlty);  bag.startRow(new JLabel(""));    bag.endRow(new JLabel(""));
    bag.startRow(viewSnp);       bag.startRow(tintFont);          bag.endRow(colorFont);

    GridBagView bag2 = new GridBagView();
    bag2.setInserts(new Insets(0, 0, 0, 0));
    bag2.startRow(minReadsOn);    bag2.startRow(minReads);  bag2.startRow(new JLabel("   "));
    bag2.startRow(minorFreqOn);   bag2.startRow(minorFreq);  bag2.startRow(new JLabel("   "));
    bag2.startRow(trimLenOn);     bag2.startRow(trimLen);    bag2.startRow(new JLabel("   "));
    bag2.startRow(new JLabel("   ")); bag2.endRow(new JLabel("   "));

    bag2.startRow(minQltyOn);        bag2.startRow(minQlty);    bag2.startRow(new JLabel("   "));
    bag2.startRow(minMinorReadsOn);  bag2.startRow(minMinorReads);    bag2.startRow(new JLabel("   "));
    bag2.startRow(maxPadOn);         bag2.startRow(maxPad);    bag2.startRow(new JLabel("   "));
    bag2.startRow(bestSnpPerContig); bag2.endRow(new JLabel("   "));

    JPanel p = new JPanel(new BorderLayout());
    p.add(bag2, BorderLayout.NORTH);
    p.add(bag, BorderLayout.WEST);
    add(p);
  }
  public void loadTo(SnpOpt model) {
    SnpFilterOpt opt = model.getSnpFilter();
    opt.getMinReads().set(minReadsOn.isSelected(), minReads.getInput());
    opt.getMinMinorReads().set(minMinorReadsOn.isSelected(), minMinorReads.getInput());
    opt.getTrimLen().set(trimLenOn.isSelected(), trimLen.getInput());
    opt.getMinQlty().set(minQltyOn.isSelected(), minQlty.getInput());
    opt.getPaddingPcnt().set(maxPadOn.isSelected(), maxPad.getInput());
    opt.getMinorFreqPcnt().set(minorFreqOn.isSelected(), minorFreq.getInput());

    rejected.loadTo(opt.getRejected());
    rejectedQlty.loadTo(opt.getRejectedQlty());

    opt.setBestSnpPerContig(bestSnpPerContig.isSelected());
    model.setViewFormat(tintFont.isSelected() ? SnpOpt.FORMAT_TINT_BGRND : model.getViewFormat());
    model.setViewFormat(colorFont.isSelected() ? SnpOpt.FORMAT_COLOR_BGRND : model.getViewFormat());
    viewSnp.loadTo(model.getViewSnp());
  }
  public void loadFrom(SnpOpt model) {
    SnpFilterOpt opt = model.getSnpFilter();
    minReads.setValue(opt.getMinReads().val());
    // NOTE!!! setXxxOn after setXxx. This is due to xxx.addPropertyChangeListener("value", new UCSelectCheckBoxOnChange(xxxOn));
    minReadsOn.setSelected(opt.getMinReads().on());

    minMinorReads.setValue(opt.getMinMinorReads().val());
    minMinorReadsOn.setSelected(opt.getMinMinorReads().on());

    minQlty.setValue(opt.getMinQlty().val());
    minQltyOn.setSelected(opt.getMinQlty().on());

    trimLen.setValue(opt.getTrimLen().val());
    trimLenOn.setSelected(opt.getTrimLen().on());

    maxPad.setValue(opt.getPaddingPcnt().val());
    maxPadOn.setSelected(opt.getPaddingPcnt().on());

    rejected = new OneColorEditor(opt.getRejected(), "View rejected SNP as");
    rejectedQlty = new OneColorEditor(opt.getRejectedQlty(), "View ignored as");
    rejectedQlty.setToolTip("This works together with the 'Min qlty' option [no effect if MinQlty is not selected].");

    minorFreq.setValue(opt.getMinorFreqPcnt().val());
    minorFreqOn.setSelected(opt.getMinorFreqPcnt().on());

    bestSnpPerContig.setSelected(opt.getBestSnpPerContig());
    int type = model.getViewFormat();
    tintFont.setSelected(type == SnpOpt.FORMAT_TINT_BGRND);
    colorFont.setSelected(type == SnpOpt.FORMAT_COLOR_BGRND);
    viewSnp = new OneColorEditor(model.getViewSnp(), "View SNP as");
  }

  public void setActionOnChange(UCController uc) {

    ListenerUtil.setOneActionListener(minReadsOn, new AdapterUCCToAL(uc));
    minReads.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    ListenerUtil.setOneActionListener(minMinorReadsOn, new AdapterUCCToAL(uc));
    minMinorReads.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    ListenerUtil.setOneActionListener(minQltyOn, new AdapterUCCToAL(uc));
    minQlty.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    ListenerUtil.setOneActionListener(trimLenOn, new AdapterUCCToAL(uc));
    trimLen.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    ListenerUtil.setOneActionListener(maxPadOn, new AdapterUCCToAL(uc));
    maxPad.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    rejected.setActionOnChange(uc);
    rejectedQlty.setActionOnChange(uc);

    ListenerUtil.setOneActionListener(minorFreqOn, new AdapterUCCToAL(uc));
    minorFreq.addPropertyChangeListener("value", new AdapterUCCToPCL(uc));

    ListenerUtil.setOneActionListener(bestSnpPerContig, new AdapterUCCToAL(uc));

    viewSnp.setActionOnChange(uc);
    ListenerUtil.setOneActionListener(tintFont, new AdapterUCCToAL(uc));
    ListenerUtil.setOneActionListener(colorFont, new AdapterUCCToAL(uc));
  }

}