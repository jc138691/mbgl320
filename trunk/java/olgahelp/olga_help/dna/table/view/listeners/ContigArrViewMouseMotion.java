package dna.table.view.listeners;

import dna.contig.Contig;
import dna.snp.Snp;
import dna.snp.SnpOpt;

import java.awt.event.MouseEvent;
import java.awt.*;

import olga.OlgaMainUI;

import javax.swing.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 14/05/2009, 11:15:55 AM
 */
public class ContigArrViewMouseMotion extends SeqArrMouseMotion {
  private Contig contig;
  private SnpOpt opt;
  public ContigArrViewMouseMotion(Contig contig, SnpOpt opt) {
    super(contig);
    this.contig = contig;
    this.opt = opt;
  }

  public void mouseMoved(MouseEvent e) {
    super.mouseMoved(e);
    if (e == null)
      return;
    Point p = e.getPoint();
    if (p == null)
      return;
    
    JTable table = (JTable)e.getSource();
    int colViewIdx = table.columnAtPoint(p);
    int colIdx = table.convertColumnIndexToModel(colViewIdx);

    Snp snp = contig.getSnpArr().get(colIdx);
    String curr = OlgaMainUI.getInstance().getStatus();

    String mssg = curr + ", " + snp.getHelpMssg(opt);
    if (snp.isSnp()) {
      mssg += (", " + snp.toString(opt));
    }
    OlgaMainUI.getInstance().setStatus(mssg);
  }

}
