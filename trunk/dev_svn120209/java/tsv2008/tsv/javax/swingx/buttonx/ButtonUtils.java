package javax.swingx.buttonx;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 29/09/2008, Time: 10:12:01
 */
public class ButtonUtils {
  public static JButton makeFlat(String text) {
    return makeFlat(new JButton(text));
  }
  public static JButton makeFlat(JButton from) {
    JButton res = makeToolButton(from);
    res.setContentAreaFilled(false);   //http://www.pushing-pixels.org/?p=38
    return res;
  }
  public static JButton makeToolButton(JButton button) {
    button.setBorderPainted(false);
    button.setFocusPainted(false);
//    button.setRolloverEnabled(true);
    button.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        if (button.isEnabled())
          button.setBorderPainted(true);
      }
      public void mouseExited(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        button.setBorderPainted(false);
      }
    });
    return button;
  }
}
