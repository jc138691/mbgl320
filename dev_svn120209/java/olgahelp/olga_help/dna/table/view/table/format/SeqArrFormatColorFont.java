package dna.table.view.table.format;

import dna.table.view.opt.BaseQltyOpt;
import dna.table.view.opt.SeqQltyColorScale;
import dna.seq.arr.SeqArr;
import dna.table.DnaTableInfo;
import dna.seq.Seq;

import javax.swing.*;
import java.awt.*;

import olga.SnpStation;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 13/03/2009, 12:49:08 PM
 */
public class SeqArrFormatColorFont extends SeqArrFormatPlain {
  private SeqArr seqArr;
  private SeqQltyColorScale scale;

  public SeqArrFormatColorFont(SeqArr seqArr, SnpStation project) {
    this.seqArr = seqArr;
    setOpaque(true);
    loadFrom(project);
  }

  private void loadFrom(SnpStation project) {
    BaseQltyOpt opt = project.getBaseQltyOpt();
    DnaTableInfo info = project.getCurrTableInfo();
    scale = new SeqQltyColorScale(info.getMinQlty(), info.getMaxQlty(), opt.getColorFont());
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIdx, int colIdx) 	{
    try {
      Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIdx, colIdx);
      Seq seq = seqArr.getByIdx(rowIdx);
      int qlty = seq.getQlty(colIdx);
      if (qlty != -1) {
        cell.setForeground(scale.makeColor(qlty));
      }
      else {
        cell.setForeground(Color.BLACK);
      }
      return cell;
    }
    catch(Exception e) {
      int dbg = 1;
      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIdx, colIdx);
    }
  }
}