package project.ucm;
import project.Project;

import javax.swing.*;
import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 13:58:28
 */
public class UCSelectLookFeel implements UCController {
  public static Log log = Log.getLog(UCSelectLookFeel.class);
  private final JFrame frame;
  private final Project model;
  private final String plafName;
  public UCSelectLookFeel(JFrame frame, Project model, String plafName) {
    this.frame = frame;
    this.model = model;
    this.plafName = plafName;
  }
  public boolean run() {
    if (frame == null)
      return true;
    try {
      model.setLookFeel(plafName);
      model.saveProjectToDefaultLocation();
      UIManager.setLookAndFeel(plafName);
      SwingUtilities.updateComponentTreeUI(frame);
      frame.validate();
    }
    catch (Exception e) {
      throw new IllegalArgumentException(log.error("UCSelectLookFeel error=" + e));
    }
    return true;
  }
}