package javax.swingx.progress;
import project.workflow.task.UCRunTask;
import project.workflow.task.TaskProgressMonitor;
import project.workflow.task.ProjectProgressMonitor;
import project.workflow.task.TaskProgressDlg;
import project.ucm.UCController;

import javax.swing.*;
import javax.swingx.dialog.CancelDialogUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 14/10/2008, Time: 12:32:46
 */
// using the Decorator pattern
public class UCMonitorTaskProgress<T> extends UCRunTask<T> {
  private UCRunTask<T> uc;
  private TaskProgressDlg dlg;
  public UCMonitorTaskProgress(UCRunTask<T> uc) {
    super(uc.getUi());
    this.uc = uc;
  }
  public boolean run() {
    if (uc.getUi() == null)
      return true;
    dlg = new TaskProgressDlg<T>(uc, true);  // start modal/blocking dlg
    dlg.center();
    dlg.setVisible(true);
    return dlg.getRunRes();
  }
}
