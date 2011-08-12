package javax.swingx;
import javax.swing.*;
import javax.utilx.log.Log;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 10:55:22
 */
public class JTabbedPaneX extends JTabbedPane {
  public static Log log = Log.getLog(JTabbedPaneX.class);
  public JTabbedPaneX(int tabPlacement) {
    super(tabPlacement);
//    add(new JPanel());
  }
  public int processView(Component view, int idx, boolean focus
    , String title, String tip) {  log.dbg("processView(view=", view);
    if (view == null) {
      return idx;
    }
    log.dbg("idx=", idx);        log.dbg("focus=", focus);
    log.dbg("title=", title);    log.dbg("tip=", tip);

    if (idx == -1) {
      idx = getTabCount();         log.dbg("idx=getTabCount()=", idx);
      addTab(title, null, view, tip);
    } else {
      setComponentAt(idx, view);   log.dbg("setComponentAt(idx=", idx);
    }
    if (focus)  {
      setSelectedIndex(idx);       log.dbg("setSelectedIndex(idx=", idx);
    }
    return idx;
  }
}