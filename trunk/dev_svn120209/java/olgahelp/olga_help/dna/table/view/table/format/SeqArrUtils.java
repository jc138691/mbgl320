package dna.table.view.table.format;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 09/03/2009, 1:17:55 PM
 */
public class SeqArrUtils {
//  private static int cellMargin = 0; // without it the string text does not fit??
  //  private static int maxColSize = 100;
  private static int minColSize = 10;

  static private char[] genomChars = {'A', 'C', 'G', 'T', 'a', 'c', 't', 'g'};
  private static final int ID_IDX = 1;

  public static void loadWidthFromFont(JTable table) {
    Font font = table.getFont();
//    Font font = new Font("Serif", Font.PLAIN, 12);
//    Font font = new Font("Monospace", Font.PLAIN, 20);
//    table.setFont(font);

    FontMetrics fm = table.getFontMetrics(font);
    int w = 0;
    for (int i = 0; i < genomChars.length; i++) {
      int currW = fm.charWidth(genomChars[i]);
      if (w < currW)
        w = currW;
    }
    minColSize = w;
    setColumnSizes(table);
  }
  public static void loadWidthFromText(JTable table) {
    Font font = table.getFont();
    FontMetrics fm = table.getFontMetrics(font);
    TableModel model = table.getModel();
    int w = 0;
    for (int i = 0; i < model.getRowCount(); i++) {
      int currW = fm.stringWidth((String)model.getValueAt(i, ID_IDX));
      if (w < currW)
        w = currW;
    }
    minColSize = w;
    setColumnSizes(table);
  }


  public static void setColumnSizes(JTable table) {
    if (table == null)
      return;
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // no point resizing if not for AUTO_RESIZE_OFF
    TableModel model = table.getModel();
    if (model == null)
      return;
    JTableHeader header = table.getTableHeader();
    header.setReorderingAllowed(false);
    if (header == null)
      return;
    setWidth(table.getColumnModel());
    setWidth(header.getColumnModel());

//    table.setEnabled(false);
    table.setRowSelectionAllowed(true);
    table.setColumnSelectionAllowed(false);
    header.setVisible(true);
    table.setDragEnabled(false);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//    table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

    int gapWidth = 0;
    int gapHeight = 0;
    table.setIntercellSpacing(new Dimension(gapWidth, gapHeight));
  }

  private static void setWidth(TableColumnModel model) {
    for (int col = 0; col < model.getColumnCount(); col++) {
      TableColumn column = model.getColumn(col);
      column.setPreferredWidth(minColSize);
      column.setMaxWidth(minColSize);
      column.setMinWidth(minColSize);
      column.setResizable(false);
//      column.setResizable(true);
    }
  }
  public static void copyWidth(TableColumnModel to, TableColumnModel from) {
    for (int col = 0; col < from.getColumnCount(); col++) {
      TableColumn colTo = to.getColumn(col);
      TableColumn colFrom = from.getColumn(col);
      colTo.setPreferredWidth(colFrom.getPreferredWidth());
      colTo.setMaxWidth(colFrom.getMaxWidth());
      colTo.setMinWidth(colFrom.getMinWidth());
      colTo.setResizable(colFrom.getResizable());
    }
  }
}
