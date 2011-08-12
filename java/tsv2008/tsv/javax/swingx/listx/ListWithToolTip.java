package javax.swingx.listx;
import javax.swing.*;
import javax.utilx.log.Log;
import java.awt.event.MouseEvent;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 18/11/2008, Time: 10:02:46
 */
public class ListWithToolTip extends JList {
  public static Log log = Log.getLog(ListWithToolTip.class);
  private String[] tips;
  public ListWithToolTip(String[] names, String[] tips) {
    super(names);
    this.tips = tips;
    if (names == null  || tips == null) {
      throw new IllegalArgumentException(log.error("names == null  || tips == null"));
    }
    if (names.length != tips.length) {
      throw new IllegalArgumentException(log.error("names.length != tips.length"));
    }
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }
  public String getToolTipText(MouseEvent e) {
    int idx = locationToIndex(e.getPoint());
    if (-1 < idx) {
      return tips[idx];
    }
    return null;
  }
}
