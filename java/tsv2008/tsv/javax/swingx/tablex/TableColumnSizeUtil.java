package javax.swingx.tablex;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 09/03/2009, 1:12:02 PM
 */
public class TableColumnSizeUtil {
  private int cellMargin = 10; // without it the string text does not fit??
  private int maxColSize = 100;
  private int maxFirstColSize = 400;
  private int minColSize = 20;

//  private int cellMargin;
//  private int maxColSize;
//  private int maxFirstColSize;
//  private int minColSize;

  // adopted from
  //http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4/TableRenderDemo.java
  public void initColumnSizes(JTable table) {
    if (table == null)
      return;
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // no point resizing if not for AUTO_RESIZE_OFF
    TableModel model = table.getModel();
    if (model == null)
      return;
    TableColumn column = null;
    Component comp = null;
    int headerWidth = 0;
    int cellWidth = 0;
    JTableHeader header = table.getTableHeader();
    header.setReorderingAllowed(false);
    if (header == null)
      return;
    TableCellRenderer headerRenderer = header.getDefaultRenderer();
    TableCellRenderer cellRenderer = null;
    int tablePrefWidth = 0;
    for (int col = 0; col < table.getColumnCount(); col++) {
      column = table.getColumnModel().getColumn(col);
      comp = headerRenderer.getTableCellRendererComponent(table
        , column.getHeaderValue(), false, false, 0, 0);
      headerWidth = comp.getPreferredSize().width + cellMargin;
      int maxCellWidth = 0;
      for (int row = 0; row < table.getRowCount(); row++) {
        cellRenderer = table.getDefaultRenderer(model.getColumnClass(col));
        comp = cellRenderer.getTableCellRendererComponent(table, model.getValueAt(row, col)
          , false, false, row, col);
        cellWidth = comp.getPreferredSize().width + cellMargin;
        if (maxCellWidth < cellWidth)
          maxCellWidth = cellWidth;
      }
      column.setPreferredWidth(Math.max(headerWidth, maxCellWidth));
      tablePrefWidth += column.getPreferredWidth();
      column.setMaxWidth(maxColSize);
      if (col == 0) {
        column.setMaxWidth(maxFirstColSize);
      }
      column.setMinWidth(minColSize);
      column.setResizable(true);
    }
    Dimension prefSize = table.getPreferredSize();
    table.setPreferredSize(new Dimension(tablePrefWidth, prefSize.height));
    table.setMaximumSize(table.getPreferredSize());
//    table.setEnabled(false);
    table.setRowSelectionAllowed(true);
    table.setColumnSelectionAllowed(false);
    table.getTableHeader().setVisible(true);
    table.setDragEnabled(false);
    table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
  }

  public void setCellMargin(int cellMargin) {
    this.cellMargin = cellMargin;
  }

  public void setMaxColSize(int maxColSize) {
    this.maxColSize = maxColSize;
  }

  public void setMaxFirstColSize(int maxFirstColSize) {
    this.maxFirstColSize = maxFirstColSize;
  }

  public void setMinColSize(int minColSize) {
    this.minColSize = minColSize;
  }

  public int getCellMargin() {
    return cellMargin;
  }

  public int getMaxColSize() {
    return maxColSize;
  }

  public int getMaxFirstColSize() {
    return maxFirstColSize;
  }

  public int getMinColSize() {
    return minColSize;
  }
}
