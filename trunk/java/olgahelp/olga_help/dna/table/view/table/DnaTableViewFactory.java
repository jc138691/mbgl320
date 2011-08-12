package dna.table.view.table;

import dna.seq.Seq;
import dna.seq.arr.SeqArr;
import dna.snp.Snp;
import dna.snp.SnpArr;
import dna.snp.SnpFilterFlankOpt;
import dna.table.DnaTableInfo;
import dna.table.view.opt.BaseFontOpt;
import dna.table.view.table.format.SeqArrCommonFormat;
import dna.table.view.table.format.SeqArrUtils;
import dna.table.view.table.format.TableFormatFactory;
import olga.SnpStation;
import olga.SnpStationProject;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.utilx.arraysx.StrMtrxDbgView;
import javax.utilx.log.Log;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 09/03/2009, 10:58:46 AM
 */
public class DnaTableViewFactory {
  public static Log log = Log.getLog(DnaTableViewFactory.class);

  public static final int NUM_SNP_HEADER_ROWS = 3;
  public static final int MAJOR_ROW_IDX = 0;
  public static final int MINOR_ROW_IDX = 1;
  public static final int SNP_ROW_IDX = 2;

  public static JTable makeDefaultTable(final AbstractTableModel model) {
    final TableCellRenderer renderer = new SeqArrCommonFormat();

    JTable table = new JTable(model) {
      public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
      }

      public TableCellRenderer getCellRenderer(int row, int column) {
        return renderer;
      }
    };
//    table.setValueAt("x", 0, 0);

    BaseFontOpt opt = SnpStationProject.getInstance().getBaseFontOpt();
    table.setFont(opt.getFont());
    table.setShowGrid(false);
    SeqArrUtils.setColumnSizes(table);
    table.setColumnSelectionAllowed(false);
    table.setRowSelectionAllowed(true);
    return table;
  }

  public static JTable makeSnpHeader(final SnpArr snps, final SnpFilterFlankOpt opt) {
    Character[] columnNames = new Character[snps.size()];
    Character[][] data = new Character[NUM_SNP_HEADER_ROWS][snps.size()];
    for (int col = 0; col < snps.size(); col++) {
      columnNames[col] = '?';
      Snp snp = snps.get(col);
      data[MAJOR_ROW_IDX][col] = snp.getMajorBase();

      char ch = ' ';
//      if (snp.isSnp() && snp.isValidSnp()) {
      if (snp.isSnp()) {
        if (snp.getMinorCount() == 0)
          ch = '?';   // this is a bug; should never happen
        else
          ch = snp.getMinorBase();
      }
      data[MINOR_ROW_IDX][col] = ch;
      data[SNP_ROW_IDX][col] = ' ';
    }
    DefaultTableModel model = new DefaultTableModel(data, columnNames);
    return makeDefaultTable(model);
  }

  public static JTable makeDnaTableView(final SeqArr dnaTable, TableFormatFactory formatFactory) {
    final DnaTableInfo info = dnaTable.calcInfo();
    final String[][] data = new String[dnaTable.size()][info.getMaxLen()];
    final String[] colNames = new String[info.getMaxLen()];
    loadColumnHeaders(colNames);

    for (int rowIdx = 0; rowIdx < dnaTable.size(); rowIdx++) {
      Seq seq = dnaTable.getByIdx(rowIdx);
      for (int colIdx = 0; colIdx < info.getMaxLen(); colIdx++) {
        if (seq.size() == 0 || colIdx >= seq.size()) {
          data[rowIdx][colIdx] = " ";
          continue;
        }
        data[rowIdx][colIdx] = seq.getBaseStr(colIdx);
      }
    }
    log.setDbg(); log.dbg("data[rowIdx][colIdx]=\n" + new StrMtrxDbgView(data));

    AbstractTableModel model = new AbstractTableModel() {  //http://www.codeguru.com/java/articles/128.shtml

      public int getColumnCount() {
        return info.getMaxLen();
      }

      public int getRowCount() {
        return data.length;
      }

      public String getColumnName(int col) {
        return colNames[col];
      }

      public Object getValueAt(int row, int col) {
        return data[row][col];
      }
    };

    SnpStation project = SnpStationProject.getInstance();
    project.setCurrTableInfo(info);

    final TableCellRenderer renderer = formatFactory.makeTableFormat(dnaTable, project);

    JTable table = new JTable(model) {
      //Implement table cell tool tips.
      public String getToolTipText(MouseEvent e) {    //http://java.sun.com/docs/books/tutorial/uiswing/components/table.html
//        String tip = super.getToolTipText(e);
//        if (tip != null  &&  tip.length() > 0)  // NOTE: renderer is the one that gets overwritten
//          return tip;
        java.awt.Point p = e.getPoint();
        int rowViewIdx = rowAtPoint(p);
        int colViewIdx = columnAtPoint(p);
        int tableRowIndex = convertRowIndexToModel(rowViewIdx);
        int tableColIndex = convertColumnIndexToModel(colViewIdx);
        int rowIdx = tableRowIndex;
        int colIdx = tableColIndex;
        Seq dna = dnaTable.getByIdx(rowIdx);
        int qlty = dna.getQlty(colIdx);
        if (qlty != -1)
          return Integer.toString(qlty);
//        return dna.getBaseStr(colIdx);
        return super.getToolTipText(e);
      }

      public TableCellRenderer getCellRenderer(int row, int column) {
        return renderer;
      }
    };
    table.setFont(project.getBaseFontOpt().getFont());
    table.setShowGrid(false);
    SeqArrUtils.loadWidthFromFont(table);
//    table.setTableHeader(null);    //http://www.exampledepot.com/egs/javax.swing.table/NoHead.html
    table.setColumnSelectionAllowed(false);
    table.setRowSelectionAllowed(true);
    return table;
  }

  private static void loadColumnHeaders(String[] colNames) {
    for (int c = 0; c < colNames.length; c++) {
      colNames[c] = "-";
      if ((c + 1) % 5 == 0) {
        colNames[c] = "+";
      }
    }
    for (int c = 0; c < colNames.length; c++) {
      if ((c + 1) % 10 == 0) {
        loadMarker(c, colNames);
      }
    }
  }

  private static void loadMarker(int c, String[] colNames) {
    String marker = Integer.toString(c + 1);
    for (int i = 0; i < marker.length(); i++) {
      if (c + i < colNames.length) {
        if (i == marker.length() - 1) // last
          colNames[c + i] = "0";
        else
          colNames[c + i] = marker.substring(i, i + 1);
      }
    }
  }

}
