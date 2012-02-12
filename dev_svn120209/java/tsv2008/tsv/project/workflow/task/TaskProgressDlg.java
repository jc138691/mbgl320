package project.workflow.task;

import javax.swingx.dialog.CancelDialogUI;
import javax.swing.*;
import javax.langx.MemoryInfoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 17/03/2009, 12:04:49 PM
 */
public class TaskProgressDlg<T> extends UCProgressDlg  {
  public TaskProgressDlg(UCRunTask<T> uc, boolean mod) {
    super(uc, mod, uc.getUi().getFrame());
  }
}
