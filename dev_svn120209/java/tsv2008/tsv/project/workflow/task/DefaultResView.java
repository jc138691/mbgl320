package project.workflow.task;
import math.func.view.FuncPlotView;

import javax.swing.*;
import javax.utilx.log.Log;
import javax.swingx.textx.TextView;
import javax.swingx.JTabbedPaneX;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 17:10:57
 */
public class DefaultResView extends JPanel {
  public static Log log = Log.getLog(DefaultResView.class);
  private JTabbedPaneX ui;

  private TextView sysView;
  private int sysIdx = -1;
  private JRadioButton sysFocus;

  private TextView usrView;
  private int usrIdx = -1;
  private JRadioButton usrFocus;

  private FuncPlotView plotView;
  private int plotIdx = -1;
  private JRadioButton plotFocus;

  public DefaultResView() {
    init();
    setSysView(new TextView());
    setUsrView(new TextView());
  }

  public void reset() {
    ui.removeAll();
    sysView = null;       sysIdx = -1;
    usrView = null;       usrIdx = -1;
    plotView = null;       plotIdx = -1;
  }
  private void init() {
    setLayout(new GridLayout(1, 1));
    setBorder(BorderFactory.createEmptyBorder());
//    setBorder(BorderFactory.createEmptyBorder(0,10,10,10));

    sysFocus = new JRadioButton();
    usrFocus = new JRadioButton();
    plotFocus = new JRadioButton();
    ButtonGroup group = new ButtonGroup();
    group.add(sysFocus);
    group.add(usrFocus);
    group.add(plotFocus);
    ui = new JTabbedPaneX(JTabbedPane.TOP);
    add(ui);
  }
  private void rebuild() {  log.dbg("rebuild()");
    usrIdx = ui.processView(usrView, usrIdx, usrFocus.isSelected(), "User", "User console output");
    sysIdx = ui.processView(sysView, sysIdx, sysFocus.isSelected(), "System", "System/Debugging output");
    plotIdx = ui.processView(plotView, plotIdx, plotFocus.isSelected(), "Plot", "Graphs");
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
  public void setSysView(TextView view) {
    this.sysView = view;
    sysFocus.setSelected(true);
    rebuild();
  }
  public void setPlotView(FuncPlotView view) {
    this.plotView = view;
    plotFocus.setSelected(true);
    rebuild();
  }
  public TextView getSysView() {
    return sysView;
  }

  public void setUsrView(TextView view) {
    this.usrView = view;
    usrFocus.setSelected(true);
    rebuild();
  }
  public TextView getUsrView() {
    return usrView;
  }
}
