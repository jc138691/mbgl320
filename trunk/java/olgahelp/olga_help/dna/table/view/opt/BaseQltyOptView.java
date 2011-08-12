package dna.table.view.opt;

import project.workflow.task.TaskOptView;
import project.ucm.UCController;

import olga.SnpStation;

import javax.utilx.log.Log;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 3/03/2009 at 09:55:28.
 */
public class BaseQltyOptView extends TaskOptView<SnpStation> {
  public static Log log = Log.getLog(BaseQltyOptView.class);
  private BaseQltyOptFormatView formatView;

  public BaseQltyOptView(SnpStation model) {
    init();
    loadFrom(model);
    assemble();
  }

  private void init() {
    setLayout(new FlowLayout(FlowLayout.LEFT));
  }
  private void assemble() {
    add(formatView);
  }
  public void loadTo(SnpStation model) {
//    log.setDbg(); log.info("BaseQltyOptView.loadTo(){");
    formatView.loadTo(model);
  }
  public void loadFrom(SnpStation model) {
    formatView = new BaseQltyOptFormatView(model);
  }

  public void setActionOnChange(UCController uc) {
    formatView.setActionOnChange(uc);
  }
}
