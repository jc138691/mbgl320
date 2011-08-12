package dna.table.view.table.format;

import dna.seq.arr.SeqArr;

import javax.swing.table.TableCellRenderer;

import olga.SnpStation;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 06/05/2009, 12:12:57 PM
 */
public interface TableFormatFactory {
  public TableCellRenderer makeTableFormat(SeqArr dnaTable, SnpStation project);
}
