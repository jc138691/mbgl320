package javax.swingx.dialog;
import project.ucm.UCController;
import project.ucm.AdapterUCCToALThread;

import javax.swing.*;
import javax.swingx.util.WndUtils;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 22/10/2008, Time: 11:24:52
 */
public class CancelDialogUI extends JDialog {
  private JButton refreshBttn;
  private JButton cancelBttn;
  private JFrame parentFrame;
  private JComponent view;

  public CancelDialogUI(JComponent view, JFrame frame, boolean mod) {
    super(frame, mod);
    init();
    parentFrame = frame;
    this.view = view;
    assemble();
  }
  public CancelDialogUI(JFrame frame, boolean mod) {
    super(frame, mod);
    init();
    parentFrame = frame;
  }
  public void center() {
    WndUtils.center(this, parentFrame);
  }
  private void init()  {
    setLayout(new BorderLayout());
    cancelBttn = new JButton("Cancel");
    refreshBttn = new JButton("Refresh");
  }
  public void setBttnText(String txt) {
    cancelBttn.setText(txt);
  }
  public void setFocusOnApply() {
    cancelBttn.setFocusable(true);
    cancelBttn.setSelected(true);
    cancelBttn.grabFocus();
  }
  protected void assemble() {
    if (parentFrame != null) {
      parentFrame.getRootPane().setDefaultButton(cancelBttn);
    }
    JPanel okPanel = new JPanel();
    okPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    okPanel.add(refreshBttn);
    okPanel.add(cancelBttn);
    add(view, BorderLayout.CENTER);
    add(okPanel, BorderLayout.SOUTH);
    pack();
    setResizable(false);                            //Make it so the frame size is not changeable
    cancelBttn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
  }

  public void setRefreshAction(ActionListener al)  {
    refreshBttn.addActionListener(al);
  }
  public void setCancelAction(ActionListener al)  {
    cancelBttn.addActionListener(al);
  }
  public void setCancelAction(UCController uc)    {
    cancelBttn.addActionListener(new AdapterUCCToALThread(uc));
  }
  public void setView(JComponent view) {
    this.view = view;
  }
}

