package project.workflow.task;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/10/2008, Time: 12:24:20
 */
public class ProjectProgressMonitor {
  private static TaskProgressMonitor instance;
  public static TaskProgressMonitor getInstance() {
    return instance;
  }
  public static void setInstance(TaskProgressMonitor instance) {
    ProjectProgressMonitor.instance = instance;
  }

  public static int calcProgress(int curr, int minVal, int maxVal) {
    int res = 100 * curr / (maxVal - minVal);
    res = Math.min(res, 100);
    res = Math.max(res, 0);
    return res;
  }

}
