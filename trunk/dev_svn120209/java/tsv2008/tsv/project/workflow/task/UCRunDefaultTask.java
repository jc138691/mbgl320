package project.workflow.task;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 13/03/2009, 10:43:09 AM
 */
public abstract class UCRunDefaultTask<T> extends UCRunTask<T> {
  public UCRunDefaultTask(DefaultTaskUI<T> ui) {
    super(ui);
  }
  public DefaultResView getOutView() {
    return (DefaultResView)getUi().getResView();
  }
  public DefaultTaskUI<T> getDefaultUi() {
    return (DefaultTaskUI<T>)getUi();
  }
}