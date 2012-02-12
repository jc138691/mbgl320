package qm_station;

import javax.swing.*;
import javax.iox.FileX;
import javax.iox.TextFile;
import javax.utilx.log.Log;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 4/09/2008, Time: 16:52:50
 */
public class QMSResults {
  public static Log log = Log.getLog(QMSResults.class);

  public static boolean save() {
    QMSMainUI ui = QMSMainUI.getInstance();
    JTable table = ui.getSelectedResultsTable();
    if (table == null)   {
      JOptionPane.showMessageDialog(QMSFrame.getInstance()
        , "First Calculate/Select/View results you want to save.");
      return false;
    }

    QMS model = QMSProject.getInstance();
    String name = model.getResultsFileName();
    File file = model.makeFile(name);

    TextFile to = new TextFile();
    to.setFileName(file.getName());

    String header = writeFileHeader(to, model);
//    write(to, table, model.getColumnDelim(), false);

    JFrame frame = QMSFrame.getInstance();
    to.write(file, frame);
    ui.setStatus(header);
    return true;
  }

  private static String writeFileHeader(TextFile to, QMS project)
  {
    int SHOW_LEVELS = 3;
    String file = FileX.getShortFileName(project.getResultsFileName(), SHOW_LEVELS);
    SimpleDateFormat date = new SimpleDateFormat();
    try {
//      date.applyPattern("HH:mm  EEE MMM d yyyy");
      date.applyPattern("HH:mm d.MMM.yyyy");
    }
    catch (IllegalArgumentException e) {
    }
    String res = file + " saved from " + project.getAppName() + "_" + project.getAppVersion()
      + " at " + date.format(new Date());
    to.addLine(res);
    return res;
  }

//  public static void write(TextFile to, JTable table, char delim, boolean columnHeaders) {
//    if (table == null)
//      return;
//    StringBuffer buff = new StringBuffer();
//    buff.setLength(0);
//    if (columnHeaders) {
//      for (int c = 0; c < table.getColumnCount(); c++) {
//        buff.append(table.getColumnName(c));
//        if (c != table.getColumnCount() - 1)
//          buff.append(delim);
//      }
//      to.addLine(buff.toCsv());
//    }
//    for (int r = 0; r < table.getRowCount(); r++) {
//      buff.setLength(0);
//      for (int c = 0; c < table.getColumnCount(); c++) {
//        String cell = (String) table.getValueAt(r, c);
//        buff.append(cell);
//        if (c != table.getColumnCount() - 1)
//          buff.append(delim);
//      }
//      to.addLine(buff.toCsv());
//    }
//  }
}
