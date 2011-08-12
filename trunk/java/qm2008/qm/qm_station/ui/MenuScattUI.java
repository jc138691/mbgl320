package qm_station.ui;
import project.workflow.task.DefaultTaskUI;

import javax.swing.*;
import javax.swingx.JTabbedPaneX;
import javax.utilx.log.Log;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 10:53:20
 */
public class MenuScattUI extends JPanel {
  public static Log log = Log.getLog(MenuScattUI.class);
  private JTabbedPaneX ui;

  private DefaultTaskUI potR;
  private int potRIdx = -1;
  private JRadioButton potRFocus;

  private DefaultTaskUI potLCR;
  private int potLcrIdx = -1;
  private JRadioButton potLcrFocus;

  public MenuScattUI() {
    init();
  }
  public void reset() {
    ui.removeAll();
    potR = null;       potRIdx = -1;
    potLCR = null;       potLcrIdx = -1;
  }
  private void init() {
    setLayout(new GridLayout(1, 1));
    potRFocus = new JRadioButton();
    potLcrFocus = new JRadioButton();
    ButtonGroup group = new ButtonGroup();
    group.add(potRFocus);
    group.add(potLcrFocus);
    ui = new JTabbedPaneX(JTabbedPane.TOP);
    add(ui);
  }
  private void rebuild() {  log.dbg("rebuild()");
    potRIdx = ui.processView(potR, potRIdx, potRFocus.isSelected(), "Potential in r", "Potential Scattering in r");
    potLcrIdx = ui.processView(potLCR, potLcrIdx, potLcrFocus.isSelected(), "Potential in LCR", "Potential Scattering in LCR");
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
  public void setPotR(DefaultTaskUI unitUI) {
    this.potR = unitUI;
    potRFocus.setSelected(true);
    rebuild();
  }
  public void setPotLCR(DefaultTaskUI unitUI) {
    this.potLCR = unitUI;
    potLcrFocus.setSelected(true);
    rebuild();
  }

}
