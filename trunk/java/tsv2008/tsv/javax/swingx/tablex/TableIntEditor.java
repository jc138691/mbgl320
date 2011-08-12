package javax.swingx.tablex;

import javax.swing.*;
import javax.swingx.text_fieldx.IntField;
import java.text.NumberFormat;
import java.text.ParseException;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 11/03/2009, 9:46:41 AM
 */
/*
* TableIntEditor is a 1.4 class used by TableFTFEditDemo.java.
 * Implements a cell editor that uses a formatted text field
 * to edit Integer values.
 */
public class TableIntEditor extends DefaultCellEditor {
  private IntField ftf;
  private NumberFormat format;

  public TableIntEditor(int min, int max) {
    super(new IntField(min, max));
    ftf = (IntField)getComponent();

    format = ftf.getFormat();
  }

  //Override to invoke setValue on the formatted text field.
  public Component getTableCellEditorComponent(JTable table,
                                               Object value, boolean isSelected,
                                               int row, int column) {
    JFormattedTextField ftf =
      (JFormattedTextField)super.getTableCellEditorComponent(
        table, value, isSelected, row, column);
    ftf.setValue(value);
    return ftf;
  }

  //Override to ensure that the value remains an Integer.
  public Object getCellEditorValue() {
    JFormattedTextField ftf = (JFormattedTextField)getComponent();
    Object obj = ftf.getValue();
    if (obj instanceof Integer) {
      return obj;
    } else if (obj instanceof Number) {
      return ((Number)obj).intValue();
    } else {
      try {
        return format.parseObject(obj.toString());
      } catch (ParseException exc) {
        System.err.println("getCellEditorValue: can't parse o: " + obj);
        return null;
      }
    }
  }

  //Override to check whether the edit is valid,
  //setting the value if it is and complaining if
  //it isn't.  If it's OK for the editor to go
  //away, we need to invoke the superclass's version
  //of this method so that everything gets cleaned up.
  public boolean stopCellEditing() {
    IntField ftf = (IntField)getComponent();
    if (ftf.isEditValid()) {
      try {
        ftf.commitEdit();
      } catch (java.text.ParseException exc) { }

    } else { //text is invalid
      if (!ftf.userSaysRevert()) { //user wants to edit
        return false; //don't let the editor go away
      }
    }
    return super.stopCellEditing();
  }

}
