package dna.table.view.table.format;

import dna.table.view.opt.SeqQltyColorScale;
import dna.table.view.opt.BaseQltyOpt;
import dna.seq.arr.SeqArr;
import dna.table.DnaTableInfo;
import dna.seq.Seq;

import javax.swing.*;
import javax.awtx.ColorMinMax;
import java.awt.*;

import olga.SnpStation;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 13/03/2009, 1:38:28 PM
 */
public class SeqArrFormatColorBgrnd extends SeqArrFormatPlain {
  private SeqArr seqArr;
  private SeqQltyColorScale scale;
  private  Color minC;
  private  Color maxC;

  public SeqArrFormatColorBgrnd(SeqArr dnaTable, SnpStation project) {
    this.seqArr = dnaTable;
    setOpaque(true);
    loadFrom(project);
  }

  private void loadFrom(SnpStation project) {
    BaseQltyOpt opt = project.getBaseQltyOpt();
    ColorMinMax minMax = opt.getColorBgrnd();
    DnaTableInfo info = project.getCurrTableInfo();
    scale = new SeqQltyColorScale(info.getMinQlty(), info.getMaxQlty(), minMax);
    minC = new Color(minMax.getMinRgb());
    maxC = new Color(minMax.getMaxRgb());
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIdx, int colIdx) 	{
    try {
      Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIdx, colIdx);
      Seq seq = seqArr.getByIdx(rowIdx);
      int qlty = seq.getQlty(colIdx);
      if (qlty != -1) {
        cell.setBackground(scale.makeColor(qlty));
      }
      else {
        cell.setBackground(maxC);
      }
      return cell;
    }
    catch(Exception e) {
      int dbg = 1;
      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIdx, colIdx);
    }
  }
}