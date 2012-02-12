package javax.swingx.tablex;

import javax.swing.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 11/03/2009, 10:33:11 AM
 */
public class TableButtonEditor extends DefaultCellEditor
  implements ItemListener {
  private JButton button;

  public TableButtonEditor(JCheckBox checkBox) {
    super(checkBox);
  }
  public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row, int column) {
    if (value==null) return null;
    button = (JButton)value;
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