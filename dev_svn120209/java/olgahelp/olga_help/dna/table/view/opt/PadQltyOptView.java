package dna.table.view.opt;

import dna.snp.SnpOpt;

import javax.swing.*;
import javax.utilx.log.Log;
import javax.swingx.panelx.GridBagView;
import javax.swingx.eventx.ListenerUtil;
import java.awt.*;

import project.ucm.UCController;
import project.ucm.AdapterUCCToAL;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 03/06/2009, 10:15:51 AM
 */
public class PadQltyOptView extends JPanel {
  public static Log log = Log.getLog(PadQltyOptView.class);
//  private OneColorEditor viewSnp;
  private JRadioButton mean;
  private JRadioButton median;

  public PadQltyOptView(SnpOpt model) {
    init();
    loadFrom(model);
    assemble();
  }

  private void init() {
    setLayout(new FlowLayout(FlowLayout.LEFT));
    mean = new JRadioButton("Mean");
    median = new JRadioButton("Median");
    ButtonGroup group = new ButtonGroup();
    group.add(mean);
    group.add(median);
    mean.setSelected(true);  // default
  }
  private void assemble() {
    GridBagView bag = new GridBagView();
    bag.startRow(new JLabel("Quality"));
    bag.startRow(mean);
    bag.endRow(median);
    add(bag);
  }
  public void loadTo(SnpOpt opt) {
    opt.setPadQltyRule(mean.isSelected() ? SnpOpt.PADDING_QLTY_MEAN : opt.getPadQltyRule());
    opt.setPadQltyRule(median.isSelected() ? SnpOpt.PADDING_QLTY_MEDIAN : opt.getPadQltyRule());
//    viewSnp.loadTo(opt.getViewSnp());
  }
  public void loadFrom(SnpOpt opt) {
    int type = opt.getViewFormat();
    mean.setSelected(type == SnpOpt.FORMAT_TINT_BGRND);
    median.setSelected(type == SnpOpt.FORMAT_COLOR_BGRND);
//    viewSnp = new OneColorEditor(opt.getViewSnp(), "View SNP as");
  }
  public void setActionOnChange(UCController uc) {
//    viewSnp.setActionOnChange(uc);
    ListenerUtil.setOneActionListener(mean, new AdapterUCCToAL(uc));
    ListenerUtil.setOneActionListener(median, new AdapterUCCToAL(uc));
  }
}