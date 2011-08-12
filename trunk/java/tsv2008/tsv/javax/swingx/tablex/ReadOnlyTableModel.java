package javax.swingx.tablex;
import javax.swing.table.DefaultTableModel;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 11:55:22
 */
public class ReadOnlyTableModel extends DefaultTableModel
{
  public ReadOnlyTableModel(Object[][] data, Object[] columnNames) {
    super(data, columnNames);
  }
  public boolean isCellEditable(int row, int column) { return false; }
}
