package dna.table.view.opt;

import olga.SnpStation;
import project.workflow.task.TaskOptView;
import project.ucm.UCController;
import project.ucm.AdapterUCCToCL;

import javax.utilx.log.Log;
import javax.awtx.fontx.FontSelector;
import java.awt.*;

/**
 * jc138691, 03/03/2009, 10:55:38 AM
 */
public class BaseFontOptView extends TaskOptView<SnpStation> {
  public static Log log = Log.getLog(BaseFontOptView.class);
  private FontSelector fontView;
  public BaseFontOptView(SnpStation model) {
//    super("Font");
    init();
    loadFrom(model);
    assemble();
  }

  private void init() {
    setLayout(new FlowLayout(FlowLayout.LEFT));
    fontView = new FontSelector();
    fontView.setSampleText("ACTGactgXx");
//    UCController uc = new UCMonitorTaskProgress<SnpStation>(new UCRefreshViews());
//    fontView.setOneChangeListener(new AdapterUCCToCL(uc));
  }
  private void assemble() {
    add(fontView);
  }
  public void loadTo(SnpStation model) {
    BaseFontOpt opt = model.getBaseFontOpt();
    opt.setName(fontView.getFontName());
    opt.setStyle(fontView.getFontStyle());
    opt.setSize(fontView.getFontSize());
  }
  public void loadFrom(SnpStation model) {
    BaseFontOpt opt = model.getBaseFontOpt();
    fontView.setFontName(opt.getName());
    fontView.setFontStyle(opt.getStyle());
    fontView.setFontSize(opt.getSize());
//    fontView.repaint();
  }

  public void setActionOnChange(UCController uc) {
    fontView.setChangeListener(new AdapterUCCToCL(uc));
  }
}