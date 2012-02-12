package dna.table.view.table.format;

import dna.snp.*;
import dna.seq.Seq;
import dna.contig.Contig;

import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 07/05/2009, 4:27:10 PM
 */
public class SnpTableRendererColor extends SeqArrCommonFormat {
  protected Contig contig;
  protected TableCellRenderer parent;
  protected Color viewSnp;
  protected SnpFilterColors snpColors;
  //  protected SnpFilterColors flankColors;
  protected SnpOpt opt;

  public SnpTableRendererColor(Contig contig, SnpOpt opt, TableCellRenderer parent) {
    this.contig = contig;
    this.parent = parent;
    setOpaque(true);
    loadFrom(opt);
  }

  private void loadFrom(SnpOpt opt) {
    this.opt = opt;
    snpColors = opt.getSnpFilter().makeColors();
//    flankColors = opt.getFlankFilter().makeColors();
    viewSnp = new Color(opt.getViewSnp().getRgb());
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIdx, int colIdx) 	{
//    if (rowIdx == 33  && colIdx == 474) {
//      int dbg = 1;
//    }
//    if (rowIdx == 34  && colIdx == 486) {
//      int dbg = 1;
//    }
//    if (rowIdx == 33) {
//      int dbg2 = 1;
//    }
    try {
      Component cell = parent.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIdx, colIdx);
      Snp snp = null;
//      try {
      snp = contig.getSnpArr().get(colIdx);
//      }
//      catch(Exception e) {
//        int dbg = 1;
//      }

      Seq seq = contig.getByIdx(rowIdx);
      SnpFilterOpt filter = opt.getSnpFilter();
      if (filter.getTrimLen().on()  &&  filter.getRejectedQlty().getOn()) {
        int trimLen = filter.getTrimLen().val();
        int L = seq.getOffsetL(colIdx);
        int R = seq.getOffsetR(colIdx);
        if ((L >= 0  && L < trimLen)
          || (R >= 0 &&  R < trimLen))   {
          renderCell(cell, snpColors.getRejectedQlty());
          return cell;
        }
      }
      if (!seq.isPadding(colIdx) && filter.getMinQlty().on()  &&  filter.getRejectedQlty().getOn()) {
        int qlty = seq.getQlty(colIdx);
        if (qlty != -1  &&  qlty < filter.getMinQlty().val()) {
          renderCell(cell, snpColors.getRejectedQlty());
          return cell;
        }
      }

      if (snp.isSnp())
        return getSnp(cell, rowIdx, colIdx);
      return getFlank(cell, rowIdx, colIdx);
    }
    catch(Exception e) {
      int dbg = 1;
      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIdx, colIdx);
    }
  }

  public Component getSnp(Component cell, int rowIdx, int colIdx) 	{
    Snp snp = contig.getSnpArr().get(colIdx);
    if (!snp.isSnp())
      return cell;

    SnpFilterOpt filter = opt.getSnpFilter();
    Seq dna = contig.getByIdx(rowIdx);
    if (snp.isValidSnp()) {     // VALID SNP
      if (opt.getViewSnp().getOn())
        renderCell(cell, viewSnp);
    }
    else {  // NOT A VALID SNP
      if (filter.getRejected().getOn())
        renderCell(cell, snpColors.getRejected());
    }

    if (colIdx >= dna.size())
      return cell;
    return cell;
  }
  public Component getFlank(Component cell, int rowIdx, int colIdx) 	{
    SnpFilterFlankOpt filter = opt.getFlankFilter();
    Seq dna = contig.getByIdx(rowIdx);

    if (colIdx >= dna.size())
      return cell;

    Snp snp = contig.getSnpArr().get(colIdx);
    if (!snp.isSnp()) {  // not a SNP
//      if (!dna.isPadding(colIdx) && filter.getMinQltyOn()  &&  filter.getRejectedQlty().getOn()) {
//        int qlty = dna.getQlty(colIdx);
//        if (qlty != -1  &&  qlty < filter.getMinQlty()) {
//          renderCell(cell, flankColors.getRejectedQlty());
//        }
//      }
      return cell;
    }

    if (snp.isValidSnp()) {     // VALID SNP
      if (opt.getViewSnp().getOn())
        renderCell(cell, viewSnp);
    }
    else {  // NOT A VALID SNP
//      if (filter.getRejected().getOn())
//        renderCell(cell, flankColors.getRejected());
    }

    return cell;
  }

  protected void renderCell(Component cell, Color color) {
    cell.setBackground(color);
  }
}