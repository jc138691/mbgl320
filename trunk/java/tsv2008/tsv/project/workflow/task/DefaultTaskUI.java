package project.workflow.task;
import javax.swing.*;

import project.workflow.task.DefaultResView;
//import qm_station.ui.unit.FlowOptView;

/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 11:02:02
 */
public class DefaultTaskUI<T> extends TaskUI<T> {
  public DefaultTaskUI(TaskOptView<T> optView, JFrame frame) {
    super(optView, frame, new DefaultResView());
  }
  public DefaultTaskUI(TaskOptView<T> optView, JFrame frame, int orientation) {
    super(optView, frame, new DefaultResView(), orientation);
  }

  public DefaultResView getOutView() {
    return (DefaultResView)getResView();
  }
}
