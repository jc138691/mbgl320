package javax.swingx;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swingx.tablex.TableViewI;
import javax.swingx.tablex.TableUtils;
import javax.utilx.log.Log;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 11:49:13
 */
public class TableViewWithOpt extends ViewWithOpt
  implements TableViewI
{
  public static Log log = Log.getLog(TableViewWithOpt.class);
  private JTable table;

  final public void setTableView(JTable t) {
    super.setView(t);
    table = t;
  }
  final public JTable getTable() {
    return table;
  }
  public void loadTable(DefaultTableModel m) {
    setTableView(new JTable(m));
    TableUtils.initColumnSizes(getTable());
    assembleWithOpt(getOrientation());
  }
}