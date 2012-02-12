package ucm.seq.arr.view;

import olga.SnpStation;
import olga.OlgaMainUI;
import project.workflow.task.UCRunTask;
import project.workflow.task.TaskUI;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 22/04/2009, 2:18:19 PM
 */
public class UCRefreshViews extends UCRunTask<SnpStation> {
  public static Log log = Log.getLog(UCRefreshViews.class);
//  public UCRefreshViews() {
//    super(OlgaMainUI.getInstance().getCurrUI());
//  }

  public UCRefreshViews(TaskUI<SnpStation> ui) {
    super(ui);
  }

  public boolean run() {
    OlgaMainUI ui = OlgaMainUI.getInstance();
    boolean ok = true;
    if (ui.getSeqArrUI() != null)
      ok = new UCRefreshDnaTableView().run();
    if (ui.getContigArrUI() != null)
      ok = ok && new UCRefreshContigTableView().run();
    return ok;
  }
}