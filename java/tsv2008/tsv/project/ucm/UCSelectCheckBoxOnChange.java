package project.ucm;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 15/05/2009, 11:37:27 AM
 */
public class UCSelectCheckBoxOnChange implements PropertyChangeListener {
  private final JCheckBox box;
  public UCSelectCheckBoxOnChange(JCheckBox box) {
    this.box = box;
  }

  public void propertyChange(PropertyChangeEvent evt) {
    box.setSelected(true);
  }
}
