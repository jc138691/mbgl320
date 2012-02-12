package javax.swingx.tablex;

import project.ucm.UCController;

import javax.swingx.panelx.GridBagView;
import javax.swingx.colorx.ColorOpt;
import javax.utilx.log.Log;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.TableModelEvent;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 15/05/2009, 12:24:58 PM
 */
public class OneColorEditor extends GridBagView    {
  public static Log log = Log.getLog(OneColorEditor.class);

  final private static int COL_SHOW = 0;
  final private static int COL_COLOR = 1;
  private JCheckBox show;
  private String[] columnNames = {"View",  "Color" };
  private Object[][] data = {
    {new JCheckBox(), new Color(51, 102, 51)}
  };
  private TableColorRenderer colorRenderer;
  private TableColorEditor colorEditor;
  private UCController actionOnChange;
  //http://java.sun.com/docs/books/tutorial/uiswing/examples/components/TableDialogEditDemoProject/src/components/TableDialogEditDemo.java

  public OneColorEditor(ColorOpt model, String title) {
//    super("Format");
    init(title);
    loadFrom(model);
    assemble();
  }
  public OneColorEditor(ColorOpt model) {
    this(model, null);
  }

  private void init(String title) {
    setLayout(new BorderLayout());
    colorRenderer = new TableColorRenderer(true);
    colorEditor = new TableColorEditor();
    if (title == null)
      show = new JCheckBox();
    else
      show = new JCheckBox(title);
    data[0][COL_SHOW] = show;
  }

  private void assemble() {
    JTable table = new JTable(new ThisTableModel()) {
      public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        repaint();
        if (actionOnChange != null)
          actionOnChange.run();
      }
      public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == COL_COLOR) {
          return colorRenderer;
        }
        return super.getCellRenderer(row, column);
      }
      public TableCellEditor getCellEditor(int row, int column) {
        if (column == COL_COLOR) {
          return colorEditor;
        }
        return super.getCellEditor(row, column);
      }
    };
    table.setDefaultRenderer(Color.class,  colorRenderer);
    table.setDefaultEditor(Color.class,  colorEditor);

    TableColumnModel cm = table.getColumnModel();
    cm.getColumn(COL_SHOW).setCellRenderer(new TableButtonRenderer());
    cm.getColumn(COL_SHOW).setCellEditor(new TableCheckBoxEditor(new JCheckBox()));
//    cm.getColumn(COL_SHOW).setPreferredWidth(30);
    cm.getColumn(COL_SHOW).setMinWidth(cm.getColumn(COL_SHOW).getPreferredWidth() + 100);
    cm.getColumn(COL_COLOR).setPreferredWidth(30);

//    add(table.getTableHeader(), BorderLayout.NORTH);
    add(table, BorderLayout.SOUTH);
    invalidate();
  }
  public void loadTo(ColorOpt opt) {
    opt.setOn(show.isSelected());
    opt.setRgb(((Color)data[0][COL_COLOR]).getRGB());
  }
  public void loadFrom(ColorOpt opt) {
    show.setSelected(opt.getOn());
    data[0][COL_COLOR] = new Color(opt.getRgb());
  }

  public void setActionOnChange(UCController actionOnChange) {
    this.actionOnChange = actionOnChange;
  }

  public void setToolTip(String s) {
    show.setToolTipText(s);
  }

  private class ThisTableModel extends AbstractTableModel {
    public int getColumnCount() {      return columnNames.length;    }
    public int getRowCount() {      return data.length;    }
    public String getColumnName(int col) {      return columnNames[col];    }
    public Object getValueAt(int row, int col) {      return data[row][col];    }
    public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
    }
    public boolean isCellEditable(int row, int col) {
      //Note that the data/cell address is constant,      //no matter where the cell appears onscreen.
      return true;
    }
    public void setValueAt(Object value, int row, int col) {
      data[row][col] = value;
      fireTableCellUpdated(row, col);
    }
  }
}