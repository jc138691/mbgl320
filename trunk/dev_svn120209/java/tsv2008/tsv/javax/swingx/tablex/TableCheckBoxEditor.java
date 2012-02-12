package javax.swingx.tablex;

import javax.swing.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 15/05/2009, 12:49:53 PM
 */
public class TableCheckBoxEditor extends DefaultCellEditor
  implements ItemListener {
  private JCheckBox button;

  public TableCheckBoxEditor(JCheckBox checkBox) {
    super(checkBox);
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row, int column) {
    if (value==null) return null;
    button = (JCheckBox)value;
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