package project.ucm;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 11/05/2009, 1:16:48 PM
 */
public class AdapterUCCToPCL implements PropertyChangeListener {
  private UCController uc;
  public AdapterUCCToPCL(UCController uc) {
    this.uc = uc;
  }
  public void propertyChange(PropertyChangeEvent evt) {
    if (uc != null)
      uc.run();
  }
}