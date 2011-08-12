package ucm.io;

import project.ucm.UCController;
import project.workflow.task.DefaultTaskUI;

import javax.utilx.log.Log;

import olga.OlgaMainUI;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 13/02/2009 at 14:55:57.
 */
public class UCShowImportOptUI implements UCController {
  public static Log log = Log.getLog(UCShowImportOptUI.class);

  public boolean run() {log.dbg("UCShowImportOptUI.run()");
//    SnpStation project = SnpStationProject.getInstance();
    OlgaMainUI ui = OlgaMainUI.getInstance();
    DefaultTaskUI view = ui.getImportUI();
    ui.setImportUI(view); // gets focus
    return true;
  }
}