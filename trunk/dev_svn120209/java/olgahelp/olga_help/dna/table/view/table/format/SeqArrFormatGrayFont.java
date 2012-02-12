package dna.table.view.table.format;

import dna.table.view.opt.SeqQltyGrayScale;
import dna.table.view.opt.BaseQltyOpt;
import dna.seq.arr.SeqArr;
import dna.table.DnaTableInfo;
import dna.seq.Seq;

import javax.swing.*;
import javax.awtx.ColorMinMax;
import java.awt.*;

import olga.SnpStation;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 10/03/2009, 11:45:06 AM
 */
public class SeqArrFormatGrayFont extends SeqArrFormatPlain {
  private SeqArr seqArr;
  private SeqQltyGrayScale scale;

  public SeqArrFormatGrayFont(SeqArr seqArr, SnpStation project) {
    this.seqArr = seqArr;
    setOpaque(true);
    loadFrom(project);
  }

  private void loadFrom(SnpStation project) {
    BaseQltyOpt opt = project.getBaseQltyOpt();
    ColorMinMax minMax = opt.getGray();
    DnaTableInfo info = project.getCurrTableInfo();
    scale = new SeqQltyGrayScale(info.getMinQlty(), info.getMaxQlty(), minMax);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIdx, int colIdx) 	{
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
}