package dna.table.view.opt;

import olga.SnpStation;
import project.workflow.task.TaskOptView;
import project.ucm.AdapterUCCToALThread;
import project.ucm.UCShowHelpMssg;
import project.ucm.UCController;
import project.ProjectFrame;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swingx.tablex.*;
import javax.awtx.ColorMinMax;
import javax.utilx.log.Log;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 10/03/2009, 2:27:25 PM
 */
public class BaseQltyOptFormatView extends TaskOptView<SnpStation> {
  public static Log log = Log.getLog(BaseQltyOptFormatView.class);

  final static int MIN_GRAY = 0;
  final static int MAX_GRAY = 255;

  final static int ROW_PLAIN = 0;
  final static int ROW_GRAY_FONT = 1;
  final static int ROW_COLOR_FONT = 2;
  final static int ROW_COLOR_BGRND = 3;

  final static int COL_TYPE = 0;
  final static int COL_MIN_VAL = 1;
  final static int COL_MAX_VAL = 2;
  final static int COL_HELP = 3;

  private String[] columnNames = {"Rendering", "Min","Max", "Help"};
  private Object[][] data = {
    {"a", " ",  " ", new JButton() },
    {"b", new Integer(255),  new Integer(0), new JButton() },
    {"c", new Color(51, 102, 51),   new Color(51, 102, 51), new JButton() },
    {"d", Color.red, Color.red, new JButton() }
  };

  private TableColorRenderer colorRenderer;
  private TableColorEditor colorEditor;
  private TableIntEditor intEditor;

  private JRadioButton plain;
  private JRadioButton bgrnd;
  private JRadioButton grayFont;
  private JRadioButton colorFont;
  private UCController actionOnChange;
  //http://java.sun.com/docs/books/tutorial/uiswing/examples/components/TableDialogEditDemoProject/src/components/TableDialogEditDemo.java

  public BaseQltyOptFormatView(SnpStation model) {
//    super("Format");
    init();
    loadFrom(model);
    assemble();
  }

  private void init() {
    setLayout(new BorderLayout());

    colorRenderer = new TableColorRenderer(true);
    colorEditor = new TableColorEditor();
    intEditor = new TableIntEditor(MIN_GRAY, MAX_GRAY);

    plain = new JRadioButton("Plain text");
    grayFont = new JRadioButton("Gray font");
    colorFont = new JRadioButton("Color font");
    bgrnd = new JRadioButton("Color background");
    ButtonGroup group = new ButtonGroup();
    group.add(plain);
    group.add(bgrnd);
    group.add(grayFont);
    group.add(colorFont);
    grayFont.setSelected(true);  // default

    data[ROW_PLAIN][COL_TYPE] = plain;
    data[ROW_GRAY_FONT][COL_TYPE] = grayFont;
    data[ROW_COLOR_FONT][COL_TYPE] = colorFont;
    data[ROW_COLOR_BGRND][COL_TYPE] = bgrnd;

    JButton bttn;
    bttn = ProjectFrame.makeHelpButton();
    bttn.addActionListener(new AdapterUCCToALThread(new UCShowHelpMssg(
      "PLAIN TEXT Rendering: black font on white background.", this)));
    data[ROW_PLAIN][COL_HELP] = bttn;

    bttn = ProjectFrame.makeHelpButton();
    bttn.addActionListener(new AdapterUCCToALThread(new UCShowHelpMssg(
      "GRAY FONT Rendering: "
        +"\n* Background is WHITE, i.e. integer value = 255."
        +"\n* Select gray-value for MIN quality value, e.g. 255 (pure white) is invisible on the white background."
        +"\n* Select gray-value for MAX quality value, e.g. 0 (pure black)."
        +"\n* Font gray color is obtained by linear interpolation for a quality value between MIN and MAX."
      , this)));
    data[ROW_GRAY_FONT][COL_HELP] = bttn;

    bttn = ProjectFrame.makeHelpButton();
    bttn.addActionListener(new AdapterUCCToALThread(new UCShowHelpMssg(
      "COLOR FONT Rendering:"
        +"\n* Background is WHITE, i.e. Color(255, 255, 255), where Color(red,green,blue)"
        +"\n* Select colors to correspond to MIN and MAX quality values."
        +"\n* Font color is obtained by linear interpolation for a quality value between MIN and MAX."
      , this)));
    data[ROW_COLOR_FONT][COL_HELP] = bttn;

    bttn = ProjectFrame.makeHelpButton();
    bttn.addActionListener(new AdapterUCCToALThread(new UCShowHelpMssg(
      "COLOR BACKGROUND Rendering:"
        +"\n* Font is BLACK, i.e. Color(0, 0, 0), where Color(red,green,blue)"
        +"\n* Select colors to correspond to MIN and Max quality values."
        +"\n* Background color is obtained by linear interpolation for a quality value between MIN and MAX."
      , this)));
    data[ROW_COLOR_BGRND][COL_HELP] = bttn;

  }
  private void assemble() {
    JTable table = new JTable(new ThisTableModel()) {
      public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        int col = e.getColumn();
        if (col == COL_HELP)
          return;
        int row = e.getFirstRow();
        if (row == ROW_GRAY_FONT  &&  !grayFont.isSelected())
          grayFont.setSelected(true);
        else if (row == ROW_COLOR_FONT  &&  !colorFont.isSelected())
          colorFont.setSelected(true);
        else if (row == ROW_COLOR_BGRND  &&  !bgrnd.isSelected())
          bgrnd.setSelected(true);
        repaint();
        if (actionOnChange != null)
          actionOnChange.run();
      }
      public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == COL_MIN_VAL  || column == COL_MAX_VAL) {
          if (row == ROW_COLOR_FONT || row == ROW_COLOR_BGRND)
            return colorRenderer;
        }
        return super.getCellRenderer(row, column);
      }
      public TableCellEditor getCellEditor(int row, int column) {
        if (column == COL_MIN_VAL  || column == COL_MAX_VAL) {
          if (row == ROW_COLOR_FONT || row == ROW_COLOR_BGRND)
            return colorEditor;
        }
        if (column == COL_MIN_VAL  || column == COL_MAX_VAL) {
          if (row == ROW_GRAY_FONT)
            return intEditor;
        }
        return super.getCellEditor(row, column);
      }
    };
//    TableUtils.initColumnSizes(table);

//    table.setPreferredScrollableViewportSize(new Dimension(500, 70));
//    table.setFillsViewportHeight(true);
    table.setDefaultRenderer(Color.class,  colorRenderer);
    table.setDefaultEditor(Color.class,  colorEditor);
    table.setDefaultEditor(Integer.class, intEditor);

    TableColumnModel cm = table.getColumnModel();
    cm.getColumn(COL_TYPE).setCellRenderer(new TableButtonRenderer());
    cm.getColumn(COL_TYPE).setCellEditor(new TableRadioBttnEditor(new JCheckBox()));
    cm.getColumn(COL_HELP).setCellRenderer(new TableButtonRenderer());
    cm.getColumn(COL_HELP).setCellEditor(new TableButtonEditor(new JCheckBox()));

    cm.getColumn(COL_TYPE).setPreferredWidth(150);
    cm.getColumn(COL_MIN_VAL).setPreferredWidth(40);
    cm.getColumn(COL_MAX_VAL).setPreferredWidth(40);
    cm.getColumn(COL_HELP).setPreferredWidth(40);

    add(table.getTableHeader(), BorderLayout.NORTH);
    add(table, BorderLayout.SOUTH);
    invalidate();
  }
  public void loadTo(SnpStation model) {
//    log.setDbg(); log.info("BaseQltyOptFormatView.loadTo(){");

    BaseQltyOpt opt = model.getBaseQltyOpt();
    opt.setViewFormat(plain.isSelected() ? BaseQltyOpt.FORMAT_PLAIN : opt.getViewFormat());
    opt.setViewFormat(grayFont.isSelected() ? BaseQltyOpt.FORMAT_GRAY_FONT : opt.getViewFormat());
    opt.setViewFormat(colorFont.isSelected() ? BaseQltyOpt.FORMAT_COLOR_FONT : opt.getViewFormat());
    opt.setViewFormat(bgrnd.isSelected() ? BaseQltyOpt.FORMAT_COLOR_BGRND : opt.getViewFormat());

    ColorMinMax mm = opt.getColorFont();
    mm.setMinRgb(((Color)data[ROW_COLOR_FONT][COL_MIN_VAL]).getRGB());
    mm.setMaxRgb(((Color)data[ROW_COLOR_FONT][COL_MAX_VAL]).getRGB());

    mm = opt.getColorBgrnd();
    mm.setMinRgb(((Color)data[ROW_COLOR_BGRND][COL_MIN_VAL]).getRGB());
    mm.setMaxRgb(((Color)data[ROW_COLOR_BGRND][COL_MAX_VAL]).getRGB());

    mm = opt.getGray();
    mm.setMinRgb((Integer)data[ROW_GRAY_FONT][COL_MIN_VAL]);
    mm.setMaxRgb((Integer)data[ROW_GRAY_FONT][COL_MAX_VAL]);
  }
  public void loadFrom(SnpStation model) {
    BaseQltyOpt opt = model.getBaseQltyOpt();
    int type = opt.getViewFormat();
    plain.setSelected(type == BaseQltyOpt.FORMAT_PLAIN);
    grayFont.setSelected(type == BaseQltyOpt.FORMAT_GRAY_FONT);
    colorFont.setSelected(type == BaseQltyOpt.FORMAT_COLOR_FONT);
    bgrnd.setSelected(type == BaseQltyOpt.FORMAT_COLOR_BGRND);

    data[ROW_GRAY_FONT][COL_MIN_VAL] = opt.getGray().getMinRgb();
    data[ROW_GRAY_FONT][COL_MAX_VAL] = opt.getGray().getMaxRgb();

    data[ROW_COLOR_FONT][COL_MIN_VAL] = new Color(opt.getColorFont().getMinRgb());
    data[ROW_COLOR_FONT][COL_MAX_VAL] = new Color(opt.getColorFont().getMaxRgb());

    data[ROW_COLOR_BGRND][COL_MIN_VAL] = new Color(opt.getColorBgrnd().getMinRgb());
    data[ROW_COLOR_BGRND][COL_MAX_VAL] = new Color(opt.getColorBgrnd().getMaxRgb());
  }

  public void setActionOnChange(UCController actionOnChange) {
    this.actionOnChange = actionOnChange;
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
      if (row == ROW_PLAIN &&  col == COL_MIN_VAL)
        return false;
      if (row == ROW_PLAIN &&  col == COL_MAX_VAL)
        return false;
      //Note that the data/cell address is constant,      //no matter where the cell appears onscreen.
      return true;
    }
    public void setValueAt(Object value, int row, int col) {
      data[row][col] = value;
      fireTableCellUpdated(row, col);
    }
  }



}