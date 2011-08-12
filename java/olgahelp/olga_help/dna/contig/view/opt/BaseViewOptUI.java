package dna.contig.view.opt;

import dna.table.view.opt.BaseQltyOptView;
import dna.table.view.opt.BaseFontOptView;

import javax.swingx.panelx.GridBagView;
import javax.swingx.JTabbedPaneX;
import javax.utilx.log.Log;
import javax.swing.*;
import java.awt.*;

import project.ucm.UCController;
import olga.SnpStation;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 29/05/2009, 11:47:48 AM
 */
public class BaseViewOptUI extends GridBagView {
  public static Log log = Log.getLog(BaseViewOptUI.class);
  private JTabbedPaneX ui;

  private BaseQltyOptView qltyView;
  private int qltyIdx = -1;
  private JRadioButton qltyFocus;

  private BaseFontOptView fontView;
  private int fontIdx = -1;
  private JRadioButton fontFocus;

//  private PadQltyOptView padView;
//  private int padIdx = -1;
//  private JRadioButton padFocus;
//
  public BaseViewOptUI(SnpStation model) {
    init();
    loadFrom(model);
    assemble();       // called from loadFrom(project);
  }

  public void loadTo(SnpStation model) {  // <---- REMEMBER TO UPDATE when adding new option views
    if (fontView != null)
      fontView.loadTo(model);
    if (qltyView != null)
      qltyView.loadTo(model);
//    if (padView != null)
//      padView.loadTo(model.getSnpOpt());
  }
  public void loadFrom(SnpStation model) {
    reset();
    fontView = new BaseFontOptView(model);
    qltyView = new BaseQltyOptView(model);
//    padView = new PadQltyOptView(model.getSnpOpt());
    assemble();
  }
  private void assemble() {
    setQltyView(qltyView);
//    setPadView(padView);
    setFontView(fontView);
  }

  public void reset() {
    ui.removeAll();
    fontView = null;       fontIdx = -1;
//    padView = null;    padIdx = -1;
    qltyView = null;       qltyIdx = -1;
  }
  private void init() {
    setLayout(new GridLayout(1, 1));
    fontFocus = new JRadioButton();
//    padFocus = new JRadioButton();
    qltyFocus = new JRadioButton();
    ButtonGroup group = new ButtonGroup();
    group.add(fontFocus);
    group.add(qltyFocus);
//    ui = new JTabbedPaneX(JTabbedPane.TOP);
    ui = new JTabbedPaneX(JTabbedPane.LEFT);
    add(ui);
  }
  private void rebuild() {  log.dbg("rebuild()");
    fontIdx = ui.processView(fontView, fontIdx, fontFocus.isSelected(), "Font", "Font options");
//    padIdx = ui.processView(padView, padIdx, padFocus.isSelected(), "Padding", "Padding quality options");
    qltyIdx = ui.processView(qltyView, qltyIdx, qltyFocus.isSelected(), "Quality", "Quality score viewing options");
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
  public void setFontView(BaseFontOptView view) {
    this.fontView = view;
    fontFocus.setSelected(true);
    rebuild();
  }
//  public void setPadView(PadQltyOptView view) {
//    this.padView = view;
//    padFocus.setSelected(true);
//    rebuild();
//  }
  public void setQltyView(BaseQltyOptView view) {
    this.qltyView = view;
    qltyFocus.setSelected(true);
    rebuild();
  }

  public void setActionOnChange(UCController uc) {
    fontView.setActionOnChange(uc);
//    padView.setActionOnChange(uc);
    qltyView.setActionOnChange(uc);
  }

  public BaseQltyOptView getQltyView() {
    return qltyView;
  }
}

