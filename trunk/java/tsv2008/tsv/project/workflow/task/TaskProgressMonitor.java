package project.workflow.task;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/10/2008, Time: 12:21:30
 */
public interface TaskProgressMonitor {
  public boolean isCanceled(int progress, int minVal, int maxVal);
  public void setTitleText(String title);
}
