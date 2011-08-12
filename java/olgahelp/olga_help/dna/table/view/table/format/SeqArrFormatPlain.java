package dna.table.view.table.format;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 28/09/2009, 10:25:06 AM
 */
public class SeqArrFormatPlain extends SeqArrCommonFormat {
  public SeqArrFormatPlain() {
    setOpaque(true);
  }
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIdx, int colIdx) 	{
    Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIdx, colIdx);
    cell.setBackground(Color.WHITE);
    cell.setForeground(Color.BLACK);
    return cell;
  }
}