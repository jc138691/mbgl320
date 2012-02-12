package dna.table.view;

import project.ProjectFrame;
import project.ucm.UCController;
import project.ucm.AdapterUCCToAL;

import javax.swing.*;
import javax.swingx.eventx.ListenerUtil;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 03/04/2009, 10:02:00 AM
 */
public class TableNavigV extends JPanel {
  private JButton goDown;
  private JButton goUp;
  private JButton goBottom;
  private JButton goTop;

  public TableNavigV() {
    init();
    assemble();
  }

  private void init() {
    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
    setLayout(box);
    goDown = ProjectFrame.makeNavButtons(new JButton(ProjectFrame.loadGoDown16()));
//    goDown.setToolTipText("Page Down");
    goUp = ProjectFrame.makeNavButtons(new JButton(ProjectFrame.loadGoUp16()));
//    goUp.setToolTipText("Page Up");
    goBottom = ProjectFrame.makeNavButtons(new JButton(ProjectFrame.loadGoBottom16()));
//    goBottom.setToolTipText("Last Page");
    goTop = ProjectFrame.makeNavButtons(new JButton(ProjectFrame.loadGoTop16()));
//    goTop.setToolTipText("First Page");
  }
  private void assemble() {
    add(goTop);
    add(goUp);
    add(goDown);
    add(goBottom);
  }
  public void setGoDownAction(UCController uc, String help) {
    setAction(goDown, uc, help);
  }
  public void setGoUpAction(UCController uc, String help) {
    setAction(goUp, uc, help);
  }
  public void setGoTopAction(UCController uc, String help) {
    setAction(goTop, uc, help);
  }
  public void setGoBottomAction(UCController uc, String help) {
    setAction(goBottom, uc, help);
  }
  private void setAction(JButton bttn, UCController uc, String help) {
    ListenerUtil.setOneActionListener(bttn, new AdapterUCCToAL(uc));
    bttn.setToolTipText(help);
  }
}
