package project.workflow.task;
import project.ucm.UCController;

import javax.utilx.log.LogGroup;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 10/09/2008, Time: 15:29:00
 */
public abstract class UCRunTask<T> extends LogGroup implements UCController {
  private TaskUI<T> ui;
  public UCRunTask(TaskUI<T> ui) {
    setupLogs();
    this.ui = ui;
  }
  protected void setupLogs() {
  }
  public TaskOptView<T> getOptView() {
    if (ui == null)
      return null;
    return ui.getOptView();
  }
  public void setUi(DefaultTaskUI<T> ui) {
    this.ui = ui;
  }
  public TaskUI<T> getUi() {
    return ui;
  }
}
