package dna.table.view.listeners;

import dna.contig.Contig;
import dna.contig.view.ContigArrUI;
import dna.contig.view.ContigArrView;
import dna.snp.SnpOpt;
import dna.snp.Snp;
import dna.snp.SnpFilterFlankOpt;
import dna.snp.SnpArr;
import dna.snp.flank.FlankArr;
import dna.snp.flank.Flank;
import dna.table.view.table.DnaTableViewFactory;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.MouseEvent;
import java.awt.*;

import olga.OlgaMainUI;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 23/10/2009, 10:54:52 AM
 */
public class ContigArrViewMouseListener extends SeqArrMouseListener {
  private Contig contig;
  private SnpOpt opt;
  public ContigArrViewMouseListener(Contig contig, SnpOpt opt) {
    super(contig);
    this.contig = contig;
    this.opt = opt;
  }
  public void mouseClicked(MouseEvent e) {
    if (e == null)
      return;
    Point p = e.getPoint();
    if (p == null)
      return;
    JTable table = (JTable)e.getSource();
    int colViewIdx = table.columnAtPoint(p);
    int colIdx = table.convertColumnIndexToModel(colViewIdx);
    showSnpAt(colIdx);
  }
  public void showSnpAt(int colIdx) {
    ContigArrUI ui = OlgaMainUI.getInstance().getContigArrUI();
    if (ui == null)
      return;
    ContigArrView view = ui.getContigTableView();
    JTable header = view.getColHeaderView();
    initSnpHeader(header);
    if (colIdx < 0 || contig == null  || colIdx >= contig.getSnpArr().size())
      return;

    view.setSelectedColIdx(colIdx);
    SnpArr snpArr = contig.getSnpArr();
    loadSnp(colIdx, snpArr, header);
  }

  private void loadSnp(int snpColIdx, SnpArr snpArr, JTable table) {
    Snp snp = snpArr.get(snpColIdx);
    if (!snp.isSnp()) {
      return; // not a snp
    }
    SnpFilterFlankOpt flankOpt = opt.getFlankFilter();
    TableModel model = table.getModel();
    model.setValueAt(" ", DnaTableViewFactory.SNP_ROW_IDX, snpColIdx);
    FlankArr flank = snp.getFlankL();
    int countGapsL = 0;
    if (flank != null) {
      for (int i = flank.size()-1; i >= 0; i--) {
        Flank f = flank.get(i);
        String baseStr = flank.toString(f, flankOpt);
        int colIdx = snpColIdx - flank.size() + i - countGapsL;
        if (snpArr.get(colIdx).getMajorCount() == 0) {
          countGapsL++;  // align when there are gaps in majors
          i++;
          continue;
        }
        model.setValueAt(baseStr, DnaTableViewFactory.SNP_ROW_IDX, colIdx);
      }
    }
    flank = snp.getFlankR();
    int countGapsR = 0;
    if (flank != null) {
      for (int i = 0; i < flank.size(); i++) {
        Flank f = flank.get(i);
        String baseStr = flank.toString(f, flankOpt);
        int colIdx = snpColIdx + i + 1 + countGapsR;
        if (snpArr.get(colIdx).getMajorCount() == 0) {
          countGapsR++;  // align when there are gaps in majors
          i--;
          continue;
        }
        model.setValueAt(baseStr, DnaTableViewFactory.SNP_ROW_IDX, colIdx);
      }
    }
  }

  private void initSnpHeader(JTable table) {
    TableModel model = table.getModel();
    for (int c = 0; c < model.getColumnCount(); c++) {
      model.setValueAt(" ", DnaTableViewFactory.SNP_ROW_IDX, c);
    }
  }
}
