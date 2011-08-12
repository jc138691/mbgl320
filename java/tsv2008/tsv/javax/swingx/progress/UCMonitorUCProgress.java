package javax.swingx.progress;

import project.workflow.task.UCRunTask;
import project.workflow.task.TaskProgressDlg;
import project.workflow.task.UCProgressDlg;
import project.ucm.UCController;

import javax.swing.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 04/06/2009, 11:19:04 AM
 */
public class UCMonitorUCProgress implements UCController {
  private UCController uc;
  private UCProgressDlg dlg;
  private JFrame frame;
  public UCMonitorUCProgress(UCController uc, JFrame frame) {
    this.uc = uc;
    this.frame = frame;
  }
  public boolean run() {
    dlg = new UCProgressDlg(uc, true, frame);  // start modal/blocking dlg
    dlg.center();
    dlg.setVisible(true);
    return dlg.getRunRes();
  }
}