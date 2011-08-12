package javax.swingx.eventx;

import project.ucm.AdapterUCCToCL;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 05/05/2009, 2:08:25 PM
 */
public class ListenerUtil {
  public static void setOneChangeListener(AbstractButton bttn, ChangeListener listener) {
    ChangeListener[] arr = bttn.getChangeListeners();
    if (arr != null) {
      for (ChangeListener cl : arr) {
        bttn.removeChangeListener(cl);
      }
    }
    bttn.addChangeListener(listener);
  }
  public static void setOneActionListener(AbstractButton bttn, ActionListener listener) {
    ActionListener[] arr = bttn.getActionListeners();
    if (arr != null) {
      for (ActionListener cl : arr) {
        bttn.removeActionListener(cl);
      }
    }
    bttn.addActionListener(listener);
  }
}
