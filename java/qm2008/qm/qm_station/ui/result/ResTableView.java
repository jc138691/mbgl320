package qm_station.ui.result;
import javax.swing.*;
import java.awt.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 17:02:27
 */
public class ResTableView extends JPanel {
  private JTable table;
  public ResTableView() {
    super(new BorderLayout());
    init();
    assemble();
  }
  private void assemble() {
    add(new JScrollPane(table), BorderLayout.CENTER);
  }
  private void init() {
    table = new JTable();
  }
}