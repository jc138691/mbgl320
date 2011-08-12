package javax.swingx;
import project.ucm.UCController;
import project.ucm.AdapterUCCToALThread;

import javax.swing.*;
import javax.swingx.panelx.GridBagView;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 12:31:16
 */
public class ApplyPanel extends GridBagView {
  private JButton bttn;
  private JComponent view;
  public ApplyPanel(JComponent view, UCController runOnApply) {
    init();
    this.view = view;
    bttn.addActionListener(new AdapterUCCToALThread(runOnApply));
    assemble();
  }
  private void init() {
    bttn = new JButton("Apply");
  }
  public void setFocusOnApply() {
    bttn.setFocusable(true);
    bttn.setSelected(true);
    bttn.grabFocus();
  }
  public void setButtonText(String txt)
  {
    bttn.setText(txt);
  }
  private void assemble() {
    endRow(view);
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder());
    panel.add(bttn, BorderLayout.EAST);
    endRow(panel);
//    setMinimumSize();
  }
}