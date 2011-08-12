package ucm.seq.arr.view;

import olga.SnpStation;
import olga.SnpStationProject;
import project.workflow.task.UCRunTask;
import project.workflow.task.TaskUI;

import javax.utilx.log.Log;

import dna.table.PageOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 29/05/2009, 12:39:32 PM
 */
public class UCShowPage extends UCRunTask<SnpStation> {
  public final static int MOVE_NEXT = 0;
  public final static int MOVE_PREV = 1;
  public final static int MOVE_FIRST = 2;
  public final static int MOVE_LAST = 3;
  public static Log log = Log.getLog(UCShowPage.class);
  private int action;
  private PageOpt pageOpt;

  public UCShowPage(int action, PageOpt page, TaskUI<SnpStation> ui) {
    super(ui);
    this.action = action;
    this.pageOpt = page;
  }

  public boolean run() {
    switch (action) {
      case MOVE_NEXT : pageOpt.moveNextLine(); break;
      case MOVE_PREV : pageOpt.movePrevLine(); break;
      case MOVE_FIRST : pageOpt.moveFirstLine(); break;
      case MOVE_LAST : pageOpt.moveLastLine(); break;
      default: return false;
    }
    SnpStation project = SnpStationProject.getInstance();
    return project.saveProjectToDefaultLocation();
  }
}