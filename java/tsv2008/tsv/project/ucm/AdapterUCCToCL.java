package project.ucm;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 23/03/2009, 12:13:49 PM
 */
public class AdapterUCCToCL implements ChangeListener {
  private UCController uc;
  public AdapterUCCToCL(UCController uc) {
    this.uc = uc;
  }
  public void stateChanged(ChangeEvent e) {
    if (uc != null)
      uc.run();
  }
}