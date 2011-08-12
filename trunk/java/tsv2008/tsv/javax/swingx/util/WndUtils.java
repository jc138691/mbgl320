package javax.swingx.util;
import javax.swing.*;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 22/10/2008, Time: 12:24:50
 */
public class WndUtils {
  public static void center(Component comp, JFrame parentFrame) {
    if (parentFrame == null)
      return;
    Point loc = parentFrame.getLocation();
    Rectangle recD = comp.getBounds();
    Rectangle recF = parentFrame.getBounds();
    if (recD.width > recF.width
      || recD.height > recF.height) {
      comp.setBounds(recF);
      return;
    }
    double x = loc.getX() + (recF.getWidth() - recD.getWidth()) / 2;
    double y = loc.getY() + (recF.getHeight() - recD.getHeight()) / 2;
    comp.setLocation(new Point((int) x, (int) y));
  }
}
