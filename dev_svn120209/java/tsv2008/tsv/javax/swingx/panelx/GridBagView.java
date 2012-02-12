package javax.swingx.panelx;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 12:32:11
 */
public class GridBagView extends JPanel {
  private GridBagLayout gridbag = new GridBagLayout();
  private GridBagConstraints startRow = new GridBagConstraints();
  private GridBagConstraints middleRow = new GridBagConstraints();
  private GridBagConstraints endRow = new GridBagConstraints();
  public GridBagView() {
    super();
    init(null);
  }
  public GridBagLayout getGridBagLayout() {
    return gridbag;
  }
  public GridBagConstraints getEnd() {
    return endRow;
  }
  public GridBagConstraints getMiddle() {
    return middleRow;
  }
  public GridBagConstraints getStart() {
    return startRow;
  }
  public GridBagView(String title) {
    super();
    init(title);
  }
  private void init(String title) {
    super.setLayout(gridbag);

    // see  http://www.jakemiles.com/?page=gridBagLayout

    // int top, int left, int bottom, int right)
    //startRow_.insets = new Insets(4, 4, 4, 4);
    startRow.insets = new Insets(2, 3, 2, 3);
    startRow.fill = GridBagConstraints.BOTH;
    startRow.anchor = GridBagConstraints.NORTHEAST;

    middleRow.insets = startRow.insets;
    middleRow.fill = GridBagConstraints.BOTH;
    middleRow.anchor = GridBagConstraints.NORTHWEST;
    middleRow.weightx = 1;  // NOTE!!!
    middleRow.weighty = 1;  // NOTE!!!

    endRow.gridwidth = GridBagConstraints.REMAINDER;
    endRow.insets = startRow.insets;
    endRow.fill = GridBagConstraints.BOTH;
    endRow.anchor = GridBagConstraints.NORTHWEST;

    if (title != null) {
      Border border = BorderFactory.createEtchedBorder();
      Border titled = BorderFactory.createTitledBorder(border, title);
      setBorder(titled);
    }
  }
  public void setInserts(Insets insets) {
    getStart().insets = insets;
    getMiddle().insets = insets;
    getEnd().insets = insets;
  }
  public void startRow(Component comp) {
    addToGridBag(comp, startRow);
  }
  public void middleRow(Component comp) {
    addToGridBag(comp, middleRow);
  }
  public void endRow(Component comp) {
    addToGridBag(comp, endRow);
  }
  public void addToGridBag(Component comp, GridBagConstraints c) {
    gridbag.setConstraints(comp, c);
    add(comp);
//    add(comp, c);
  }
}
