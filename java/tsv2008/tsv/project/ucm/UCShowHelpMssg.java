package project.ucm;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 11/03/2009, 10:16:24 AM
 */
public class UCShowHelpMssg implements UCController {
  private final String txt;
  private final Component parent;
  public UCShowHelpMssg(String txt, Component parent)
  {
    this.txt = txt;
    this.parent = parent;
  }

  public boolean run()
  {
    JOptionPane.showMessageDialog(parent, txt
      , "Help", JOptionPane.INFORMATION_MESSAGE);
    return true;
  }
}