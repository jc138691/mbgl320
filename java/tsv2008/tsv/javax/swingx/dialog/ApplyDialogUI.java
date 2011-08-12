package javax.swingx.dialog;
import project.ucm.UCController;
import project.ucm.AdapterUCCToALThread;

import javax.swing.*;
import javax.swingx.util.WndUtils;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 12/09/2008, Time: 10:35:42
 */
public class ApplyDialogUI extends JDialog {
  private boolean apply = false;
  private JButton applyBttn = new JButton("Apply");
  private JFrame parentFrame;
  private JComponent view;
//  public static void main(String[] args) {
//    JFrame frame = new JFrame();
//    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    JPanel panel = new JPanel();
//    panel.add(new JLabel("TEST LABEL"));
//    ApplyDialogUI dlg = new ApplyDialogUI(panel, frame, true);
//    dlg.setVisible(true);
//  }

  public ApplyDialogUI(JComponent view, JFrame frame, boolean mod) {
    super(frame, mod);
    init();
    parentFrame = frame;
    this.view = view;
    assemble();
  }
  private void init()
  {
    setLayout(new BorderLayout());
  }
  public void setApplyBttnText(String txt) {
    applyBttn.setText(txt);
  }
  public boolean apply() {
    return apply;
  }
  public void setFocusOnApply() {
    applyBttn.setFocusable(true);
    applyBttn.setSelected(true);
    applyBttn.grabFocus();
  }
  public void center() {
    WndUtils.center(this, parentFrame);
  }
  private void assemble() {
    if (parentFrame != null) {
      parentFrame.getRootPane().setDefaultButton(applyBttn);
//      parentFrame.ge.setDefaultButton(applyBttn);
    }
    JPanel okPanel = new JPanel();
    JButton cancel = new JButton("Cancel");
//    getContentPane().setLayout(new BorderLayout());
    okPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    okPanel.add(applyBttn);
    okPanel.add(cancel);
//    getContentPane().add(view);
//    getContentPane().add(okPanel, BorderLayout.SOUTH);
    add(view, BorderLayout.CENTER);
    add(okPanel, BorderLayout.SOUTH);
    pack();
    setResizable(false);                            //Make it so the frame size is not changeable
    applyBttn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        apply = true;
        dispose();
      }
    });
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        apply = false;
        dispose();
      }
    });
  }

  public void removeActionListeners()
  {
    ActionListener a[] = applyBttn.getActionListeners();
    for (int i = 0; i < a.length; i++) {
      ActionListener actionListener = a[i];
      applyBttn.removeActionListener(actionListener);
    }
  }
  public void runOnApply(ActionListener al)
  {
    applyBttn.addActionListener(al);
  }
  public void runOnApply(UCController uc)
  {
    applyBttn.addActionListener(new AdapterUCCToALThread(uc));
  }
}

