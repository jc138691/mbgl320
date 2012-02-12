package javax.swingx.tablex;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 10/03/2009, 2:58:08 PM
 */
// from http://www.esus.com/docs/GetQuestionPage.jsp?uid=1286
public class TableRadioBttnEditor extends    DefaultCellEditor
  implements ItemListener {
  private JRadioButton button;

  public TableRadioBttnEditor(JCheckBox checkBox) {
    super(checkBox);
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row, int column) {
    if (value==null) return null;
    button = (JRadioButton)value;
    button.addItemListener(this);
    return (Component)value;
  }

  public Object getCellEditorValue() {
    button.removeItemListener(this);
    return button;
  }

  public void itemStateChanged(ItemEvent e) {
    super.fireEditingStopped();
  }
}