package dna.table.view;

import project.ProjectFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 03/04/2009, 10:01:47 AM
 */
public class TableNavigH extends JPanel {
  private JButton goNext;
  private JButton goPrev;
  private JButton goLast;
  private JButton goFirst;

  public TableNavigH() {
    init();
    assemble();
  }

  private void init() {
    BoxLayout box = new BoxLayout(this, BoxLayout.Y_AXIS);
    setLayout(box);

    goNext = ProjectFrame.makeNavButtons(new JButton(ProjectFrame.loadGoNext16()));
//    goDown.setToolTipText("Page Down");
    goPrev = ProjectFrame.makeNavButtons(new JButton(ProjectFrame.loadGoPrev16()));
//    goUp.setToolTipText("Page Up");
    goLast = ProjectFrame.makeNavButtons(new JButton(ProjectFrame.loadGoLast16()));
//    goBottom.setToolTipText("Last Page");
    goFirst = ProjectFrame.makeNavButtons(new JButton(ProjectFrame.loadGoFirst16()));
//    goTop.setToolTipText("First Page");
  }
  private void assemble() {
    add(goFirst);
    add(goPrev);
    add(goNext);
    add(goLast);
  }



}
