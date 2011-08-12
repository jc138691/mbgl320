package javax.swingx.tablex;

import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 10/03/2009, 2:56:32 PM
 */
//from http://www.esus.com/docs/GetQuestionPage.jsp?uid=1286
public class TableButtonRenderer implements TableCellRenderer {
  public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
    if (value==null) return null;
    return (Component)value;
  }
}