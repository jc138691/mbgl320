package project.ucm;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 14:50:51
 */
public class UCDShowHelpAbout implements ActionListener {
  private String mssg;
  private JFrame frame;

  public void setHelpMessage(String mssg) {
    this.mssg = mssg;
  }
  public String  getHelpMessage() {
    return  mssg;
  }
  public UCDShowHelpAbout(JFrame frame) {
    this.frame = frame;
  }
  public void actionPerformed(ActionEvent event) {
    JDialog dlg = new JDialog(frame, "About", true);
    JPanel panel = new JPanel();
    panel.add(new JLabel(getHelpMessage()));
//    dlg.getContentPane().add(panel);
    dlg.setLayout(new BorderLayout());
    dlg.add(panel, BorderLayout.CENTER);
    dlg.pack();
    dlg.setResizable(false);
    Point loc = frame.getLocation();
    Rectangle recD = dlg.getBounds();
    Rectangle recF = frame.getBounds();
    double x = loc.getX() + (recF.getWidth() - recD.getWidth()) / 2;
    double y = loc.getY() + (recF.getHeight() - recD.getHeight()) / 2;
    dlg.setLocation(new Point((int) x, (int) y));
    dlg.setVisible(true);
  }
}