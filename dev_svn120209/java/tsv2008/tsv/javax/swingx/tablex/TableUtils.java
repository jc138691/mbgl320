package javax.swingx.tablex;
import javax.swing.*;
import javax.swing.table.*;
import javax.utilx.arraysx.StrMtrx;
import javax.utilx.arraysx.StrVec;
import java.awt.*;
import java.text.DecimalFormat;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 9/09/2008, Time: 11:53:24
 */
public class TableUtils {
  public static String EMPTY = " ";

  public static void initColumnSizes(JTable table) {
    new TableColumnSizeUtil().initColumnSizes(table);
  }
  public static Dimension getPreferredSize(JTable table) {
    int MARGIN = 5;
//    TableModel model = table.getModel();
    TableColumn column = null;
    Component comp = null;
    JTableHeader header = table.getTableHeader();
    TableCellRenderer headerRenderer = header.getDefaultRenderer();
    int col = 0;
    column = table.getColumnModel().getColumn(col);
    comp = headerRenderer.getTableCellRendererComponent(table
      , column.getHeaderValue(), false, false, 0, 0);
    int headerHeight = comp.getPreferredSize().height;
    Dimension ps = table.getPreferredSize();
    return new Dimension(ps.width + MARGIN, ps.height + headerHeight + MARGIN);
  }

  public static JTable assemble(JTable[] views) {
    return assemble(null, null, views, null);
  }

  public static JTable assemble(int[] shiftRow, int[] shiftCol, JTable[] views, ImageIcon img) {
    int nRow = 0;
    int nCol = 0;
    for (int i = 0; i < views.length; i++) {
      JTable t = views[i];
      nRow += t.getRowCount();
      if (shiftRow != null  &&  i < shiftRow.length)
        nRow += shiftRow[i];
      int newCol = t.getColumnCount();
      if (shiftCol != null  &&  i < shiftCol.length)
        newCol += shiftCol[i];
      if (nCol < newCol)
        nCol = newCol;
    }
    String[][] rowData = new String[nRow][nCol];
    StrMtrx.set(rowData, EMPTY);
    String[] columnNames = new String[nCol];
    StrVec.set(columnNames, EMPTY);
    int startRow = 0;
    for (int i = 0; i < views.length; i++) {
      JTable t = views[i];
      if (shiftRow != null  &&  i < shiftRow.length)
        startRow += shiftRow[i];
      int startCol = 0;
      if (shiftCol != null  &&  i < shiftCol.length)
        startCol = shiftCol[i];

      for (int r = 0; r < t.getRowCount(); r++) {
        for (int c = 0; c < t.getColumnCount(); c++) {
          if (r == 0)
            columnNames[startCol + c] = t.getColumnName(c);
          rowData[startRow + r][startCol + c] = (String) t.getValueAt(r, c);
        }
      }
      startRow += t.getRowCount();
    }
    return new ThisTable(new ReadOnlyTableModel(rowData, columnNames), img);
  }

  public static void copyColumnNames(JTable to, int rowStartIdx, int colStartIdx, JTable from) {
    int nCols = to.getColumnCount();
    for (int i = 0; i < from.getColumnCount(); i++) {
      String name = from.getColumnName(i);
      int colIdx = i;
      if (colStartIdx > 0)
        colIdx = i + colStartIdx;
      if (rowStartIdx > -1)
        to.setValueAt(name, rowStartIdx, colIdx);
      if (colIdx < nCols)
        to.getColumnModel().getColumn(colIdx).setHeaderValue(name);
    }
  }

  public static void writeRow(JTable table, int rowStart, int colStart, double[] arr, DecimalFormat nf) {
    for (int i = 0; i < arr.length; i++) {
      table.setValueAt(nf.format(arr[i]), rowStart, colStart + i);
    }
  }
  public static void writeColumn(JTable table, int rowStart, int colStart, int[] arr) {
    for (int i = 0; i < arr.length; i++) {
      table.setValueAt(arr[i], rowStart + i, colStart);
    }
  }

}

class ThisTable extends JTable    {
  private ImageIcon image;
  public ThisTable(DefaultTableModel model, ImageIcon img) {
    super(model);
    this.image = img;
    setOpaque(image == null);
  }
  public Component prepareRenderer(TableCellRenderer renderer, int row, int column)      {
    Component c = super.prepareRenderer( renderer, row, column);
    // We want renderer component to be transparent so background image is visible
    if( c instanceof JComponent )
      ((JComponent)c).setOpaque(false);
    return c;
  }

  // Hard coded value. In your sub-class add a function for this.
  //    ImageIcon image = new ImageIcon( "codeguruwm.gif" );
  public void paint( Graphics g )    {
    if (image != null) {
      // First draw the background image - tiled
      Dimension d = getSize();
      for( int x = 0; x < d.width; x += image.getIconWidth() )
        for( int y = 0; y < d.height; y += image.getIconHeight() )
          g.drawImage( image.getImage(), x, y, null, null );
    }
    // Now let the regular paint code do it's work
    super.paint(g);
  }
}

