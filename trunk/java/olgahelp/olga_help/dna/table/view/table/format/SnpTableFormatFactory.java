package dna.table.view.table.format;

import dna.seq.arr.SeqArr;
import dna.snp.SnpOpt;
import dna.contig.Contig;

import javax.swing.table.TableCellRenderer;
import javax.utilx.log.Log;

import olga.SnpStation;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 06/05/2009, 12:09:07 PM
 */
public class SnpTableFormatFactory extends SeqArrFormatFactory {
  public static Log log = Log.getLog(SnpTableFormatFactory.class);

  public TableCellRenderer makeTableFormat(SeqArr dnaTable, SnpStation project) {
    TableCellRenderer parent = super.makeTableFormat(dnaTable, project);
    SnpOpt opt = project.getSnpOpt();
//    if (!opt.getViewSnp().getOn()) {
//      return parent;
//    }
    if (!(dnaTable instanceof Contig)){
      throw new IllegalArgumentException(log.error("error"));
    }
//    SnpArr snps = SnpTableFactory.makeFrom((Contig)seqArr, opt);

    int format = opt.getViewFormat();
    if (format == SnpOpt.FORMAT_TINT_BGRND)
      return new SnpTableRendererTint((Contig)dnaTable, opt, parent);
    return new SnpTableRendererColor((Contig)dnaTable, opt, parent);
  }
}
