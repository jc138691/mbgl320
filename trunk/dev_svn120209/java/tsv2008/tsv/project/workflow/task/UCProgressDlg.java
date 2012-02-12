package project.workflow.task;

import project.ucm.UCController;

import javax.swingx.dialog.CancelDialogUI;
import javax.swing.*;
import javax.langx.MemoryInfoView;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 04/06/2009, 11:13:47 AM
 */
public class UCProgressDlg extends CancelDialogUI
//  implements ActionListener     // run this.actionPerformed()   on "Cancel" button
  implements PropertyChangeListener {
  private final int TEXT_LEN = 20;
  private JTextField taskOutput;
  private MemoryInfoView memView;
  private JProgressBar progressBar;
  private ThisUcTask ucTask;
  private UCController uc;
  private static String DEAFULT_TITLE = "Task Progress Monitor";

  public UCProgressDlg(UCController uc, boolean mod, JFrame frame) {
    super(frame, mod);
    this.uc = uc;
    setView(makePanel());
    setTitle(DEAFULT_TITLE);
    assemble();

    progressBar.setIndeterminate(true);
    setBttnText("Cancel");
    setCancelAction(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        ucTask.setCanceled(true);
      } });
    setRefreshAction(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        memView.refresh();
      } });
    ucTask = new ThisUcTask();  //Instances of javax.swing.SwingWorker are not reusuable, so //we create new instances as needed.
    ucTask.addPropertyChangeListener(this);
    ucTask.execute();
  }
  private JPanel makePanel() {
    memView = new MemoryInfoView();
    progressBar = new JProgressBar(0, 100);
    progressBar.setValue(0);
    //Call setStringPainted now so that the progress bar height
    //stays the same whether or not the string is shown.
    progressBar.setStringPainted(true);
    taskOutput = new JTextField(TEXT_LEN);
    taskOutput.setMargin(new Insets(5,5,5,5));
    taskOutput.setEditable(false);
    JPanel panel = new JPanel();
    panel.add(progressBar);
    JPanel res = new JPanel(new BorderLayout());
    res.add(memView, BorderLayout.PAGE_START);
    res.add(taskOutput, BorderLayout.CENTER);
    res.add(panel, BorderLayout.PAGE_END);
    return res;
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if ("progress".equals(evt.getPropertyName())) {
//        if ("progress" == evt.getPropertyName()) {
      int progress = (Integer) evt.getNewValue();
      progressBar.setIndeterminate(false);
      progressBar.setValue(progress);
      taskOutput.setText(String.format("Completed %d%% of the task.\n", progress));
    }
  }
  public boolean getRunRes() {
    return ucTask.getRunRes();
  }

  // http://java.sun.com/docs/books/tutorial/uiswing/examples/components/ProgressBarDemo2Project/src/components/ProgressBarDemo2.java
  // launch from http://java.sun.com/docs/books/tutorial/uiswing/examples/components/index.html#ProgressMonitorDemo
  // code http://java.sun.com/docs/books/tutorial/uiswing/examples/components/ProgressMonitorDemoProject/src/components/ProgressMonitorDemo.java
  private class ThisUcTask extends SwingWorker<Boolean, Void> implements TaskProgressMonitor {
    private boolean canceled = false;
    private boolean runRes;
    private int refreshScale = 2;

    public Boolean doInBackground() { //Main task. Executed in background thread.
      ProjectProgressMonitor.setInstance(this);
      try {
        runRes = uc.run();
      }    catch(OutOfMemoryError bounded){
        memView.refresh();
        taskOutput.setText("out of memory");
      }
      return runRes;
    }
    public void done() {//Executed in event dispatch thread
      taskOutput.setText("Done!\n");
      setVisible(false);
      dispose();
    }
    public boolean isCanceled(int curr, int minVal, int maxVal) {
      setProgress(ProjectProgressMonitor.calcProgress(curr, minVal, maxVal));  // worker's setProgress is final.
      if (curr % refreshScale == 0)  {
        memView.refresh();
        refreshScale *= 2;
        if (refreshScale == 0)
          refreshScale = Integer.MAX_VALUE / 100;
      }
      return canceled;
    }

    public void setTitleText(String title) {
      setTitle(title);
    }

    public void setCanceled(boolean canceled) {
      this.canceled = canceled;
    }

    public boolean getRunRes() {
      return runRes;
    }
  }
}
