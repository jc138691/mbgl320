package project.workflow.task;

import project.ucm.AdapterUCCToALThread;

import javax.swing.*;
import javax.utilx.log.Log;
import javax.swingx.JSplitPaneX;
import java.awt.*;

/**
 * jc138691, 03/03/2009, 10:14:23 AM
 */
public class TaskUI<T> extends JPanel {
  public static Log log = Log.getLog(DefaultTaskUI.class);
  private JFrame frame;
  private JPanel optWithAction;
  private JPanel resView;
  private TaskOptView<T> optView;
  private int orientation;
  JSplitPane split;

  public TaskUI(TaskOptView<T> optView, JFrame frame, JPanel resView) {
    init();
    this.frame = frame;
    this.resView = resView;
    this.optView = optView;
    this.orientation = JSplitPane.HORIZONTAL_SPLIT;

//    optWithAction = new PrivateOptViewWithActionBttn(optView);
    optWithAction = optView;

    assemble();
  }
  public TaskUI(TaskOptView<T> optView, JFrame frame, JPanel resView, int orientation) {
    init();
    this.frame = frame;
    this.resView = resView;
    this.optView = optView;
    this.orientation = orientation;

//    optWithAction = new PrivateOptViewWithActionBttn(optView);
    optWithAction = optView;       // only show optionView with the "Apply" button if UCController was added to the button

    assemble();
  }

  private void init() {
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder());
  }

  public void assemble() {
    removeAll();
    JPanel viewL = new JPanel(new BorderLayout());
    viewL.setBorder(BorderFactory.createEmptyBorder());

    viewL.add(optWithAction, BorderLayout.NORTH);

    split = new JSplitPane();
    JSplitPaneX.setEmptyDividerBorder(split);
    split.setOneTouchExpandable(false);
    split.setEnabled(false);
    split.setOrientation(orientation);
    split.setLeftComponent(viewL);
//    split.setRightComponent(new JScrollPane(viewR));
    split.setRightComponent(resView);
    split.setBorder(BorderFactory.createEmptyBorder());

    add(split, BorderLayout.CENTER);

    // VERY IMPORTANT! apply is running in an separate thread
    // so if frame repaint is not called, old view is displayed even after apply has finished
    if (frame != null)
      frame.repaint();
  }

  public JFrame getFrame()   {
    return frame;
  }
  public void setApplyAction(UCRunTask uc) {
    PrivateOptViewWithActionBttn actionView = new PrivateOptViewWithActionBttn(optView);
    actionView.setRunApply(uc);
    optWithAction = actionView;
    assemble();
  }
  public JPanel getResView() {
    return resView;
  }
  public TaskOptView<T> getOptView() {
    return optView;
  }

  public void setOneTouchExpandable(boolean b) {
    split.setOneTouchExpandable(b);
  }


  // PRIVATE HELPER CLASS!!!!!!!!!!!!
  private class PrivateOptViewWithActionBttn extends JPanel {
    private JButton apply;
    private JComponent view;
    public PrivateOptViewWithActionBttn(JComponent view) {
      init();
      this.view = view;
      assemble();
    }

    private void init() {
      setBorder(BorderFactory.createEmptyBorder());
      apply = new JButton("Apply");
      apply.setToolTipText("Complete task with the displayed parameters");
      apply.setEnabled(false);
    }
    public void setFocusOnApply() {
      apply.setFocusable(true);
      apply.setSelected(true);
      apply.grabFocus();
    }
    private void assemble() {
      setLayout(new BorderLayout());
      add(view, BorderLayout.NORTH);

      JPanel panel = new JPanel(new BorderLayout());
      panel.setBorder(BorderFactory.createEmptyBorder());
      panel.add(apply, BorderLayout.NORTH);
      add(panel, BorderLayout.EAST);
    }
    public void setRunApply(UCRunTask uc) {
      if (apply.getActionListeners().length > 0) {
        throw new IllegalArgumentException(log.error("CHECK THIS: asking for more than one action?"));
      }
      apply.addActionListener(new AdapterUCCToALThread(uc));
      apply.setEnabled(true);
    }
  }
}