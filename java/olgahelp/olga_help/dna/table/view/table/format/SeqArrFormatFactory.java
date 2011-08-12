package dna.table.view.table.format;

import dna.table.view.opt.BaseQltyOpt;
import dna.seq.arr.SeqArr;

import javax.swing.table.TableCellRenderer;

import olga.SnpStation;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 04/03/2009, 12:34:29 PM
 */
public class SeqArrFormatFactory implements TableFormatFactory {
  public TableCellRenderer makeTableFormat(SeqArr seqArr, SnpStation project) {
    BaseQltyOpt opt = project.getBaseQltyOpt();
    int format = opt.getViewFormat();
    if (format == BaseQltyOpt.FORMAT_GRAY_FONT)
      return new SeqArrFormatGrayFont(seqArr, project);
    if (format == BaseQltyOpt.FORMAT_COLOR_FONT)
      return new SeqArrFormatColorFont(seqArr, project);
    if (format == BaseQltyOpt.FORMAT_COLOR_BGRND)
      return new SeqArrFormatColorBgrnd(seqArr, project);
    return new SeqArrFormatPlain();
  }
}
