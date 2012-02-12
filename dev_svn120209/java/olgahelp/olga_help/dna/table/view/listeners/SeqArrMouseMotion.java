package dna.table.view.listeners;

import dna.seq.arr.SeqArr;
import dna.seq.Seq;

import javax.swing.*;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.*;

import olga.OlgaMainUI;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 27/02/2009 at 14:25:32.
 */
public class SeqArrMouseMotion implements MouseMotionListener {
  private SeqArr seqArr;

  public SeqArrMouseMotion(SeqArr dnaTable) {
    this.seqArr = dnaTable;
  }
  public void mouseDragged(MouseEvent e) {
  }
  public void mouseMoved(MouseEvent e) {
    OlgaMainUI.getInstance().setStatus(" ");
    if (e == null)
      return;
    Point p = e.getPoint();
    if (p == null)
      return;
    JTable table = (JTable)e.getSource();
    int rowViewIdx = table.rowAtPoint(p);
    int colViewIdx = table.columnAtPoint(p);
    int rowIdx = table.convertRowIndexToModel(rowViewIdx);
    int colIdx = table.convertColumnIndexToModel(colViewIdx);

    Seq dna = seqArr.getByIdx(rowIdx);
    String id = dna.getId();
    if (id == null)
      id = "";

    String tableId = seqArr.getId();
    String mssg = "";
    if (tableId != null  &&  tableId.length() > 0) {
      mssg = tableId + ". ";
    }

    mssg += (id + "[" + (rowIdx + 1) +", "+ (colIdx+1) +"]");
    if (colIdx < dna.size()) {
      mssg += ("=" + dna.getBaseStr(colIdx));
    }

    int qlty = dna.getQlty(colIdx);
    if (qlty == -1) {
      OlgaMainUI.getInstance().setStatus(mssg);
    }
    else {
      OlgaMainUI.getInstance().setStatus(mssg + "(" + qlty + ")");
    }
  }
}
