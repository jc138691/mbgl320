package javax.swingx;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 10/09/2008, Time: 14:50:27
 */
public class JSplitPaneX {
  //http://www.devdaily.com/java/jwarehouse/jext-src-5.0/src/lib/com/jgoodies/uif_lite/component/UIFSplitPane.java.shtml
  public static void setEmptyDividerBorder(JSplitPane pane) {
    SplitPaneUI splitPaneUI = pane.getUI();
    if (splitPaneUI instanceof BasicSplitPaneUI) {
      BasicSplitPaneUI basicUI = (BasicSplitPaneUI) splitPaneUI;
      basicUI.getDivider().setBorder(new EmptyBorder(0, 0, 0, 0));
    }
  }
}
