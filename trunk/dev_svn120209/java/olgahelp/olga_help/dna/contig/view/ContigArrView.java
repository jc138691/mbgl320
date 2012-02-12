package dna.contig.view;

import dna.table.view.table.DnaTableViewFactory;
import dna.table.view.table.SeqArrView;
import dna.table.view.table.format.SnpTableFormatFactory;
import dna.table.view.listeners.ContigArrViewMouseMotion;
import dna.table.view.listeners.ContigArrViewMouseListener;
import dna.table.PageOpt;
import dna.contig.Contig;
import dna.contig.ContigArr;
import dna.snp.SnpArr;
import dna.snp.SnpTableFactory;
import dna.snp.SnpOpt;

import javax.utilx.log.Log;
import javax.swing.*;

import olga.SnpStation;
import olga.SnpStationProject;

import java.awt.*;

import ucm.seq.arr.view.UCShowPage;
import ucm.seq.arr.view.UCShowPageContig;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 21/04/2009, 2:24:52 PM
 */
public class ContigArrView extends SeqArrView {
  public static Log log = Log.getLog(ContigArrView.class);
  protected JTable colHeaderView;
  protected ContigArr contigArr;
  private PageOpt pageOpt;
  private ContigPageView pageView;
  private int selectedColIdx = -1;
  private ContigArrViewMouseListener mouseListener;

  public ContigArrView(ContigArr arr, PageOpt pageOpt) {
    this.contigArr = arr;
    this.pageOpt = pageOpt;
    loadView();
  }

  public void loadView() {
    if (contigArr.size() == 0) {
      seqArr = contigArr.getSrcReads();
      super.loadView();
      return;
    }
    SnpStation project = SnpStationProject.getInstance();
    SnpOpt opt = project.getSnpOpt();

    int nContigs = contigArr.size();
    pageOpt.makeValidCurrPos(nContigs - 1);
    Contig contig = contigArr.getContig(pageOpt.getPageLineIdx()); // NOTE!
    seqArr = contig;

//    SnpArr snps = new SnpTableFactory().makeFrom(contig, opt, true);
    SnpArr snps = makeSnps(contig, opt); // @override
    contig.setSnpArr(snps);

    opt.setExportPreview(contig.makeExportPreview(opt));

    tableView = DnaTableViewFactory.makeDnaTableView(seqArr, new SnpTableFormatFactory());
    tableView.addMouseMotionListener(new ContigArrViewMouseMotion(contig, opt));
    mouseListener = new ContigArrViewMouseListener(contig, opt);
    tableView.addMouseListener(mouseListener);
//    rowHeaderView = DnaTableViewFactory.makeRowHeaders(seqArr);
    colHeaderView = DnaTableViewFactory.makeSnpHeader(snps, opt.getFlankFilter());
    colHeaderView.setBackground(new Color(230, 250, 230));

    navV.setGoDownAction(new UCShowPageContig(UCShowPage.MOVE_NEXT, pageOpt), "Show next loaded contig");
    navV.setGoUpAction(new UCShowPageContig(UCShowPage.MOVE_PREV, pageOpt), "Show previous contig");
    navV.setGoTopAction(new UCShowPageContig(UCShowPage.MOVE_FIRST, pageOpt), "Show first loaded contig");
    navV.setGoBottomAction(new UCShowPageContig(UCShowPage.MOVE_LAST, pageOpt), "Show last loaded contig");

    assemble();
//    OlgaFrame.getInstance().repaint();
  }
  public SnpArr makeSnps(Contig contig, SnpOpt opt) {
    return new SnpTableFactory().makeFrom(contig, opt, true);
  }
  public final ContigArr getContigArr() {
    return contigArr;
  }
  public JTable getColHeaderView() {
    return colHeaderView;
  }

  protected void assemble() {
    removeAll();
    setLayout(new BorderLayout());
    add(infoText, BorderLayout.NORTH);

    pageView = new ContigPageView(tableView, colHeaderView);
    add(pageView, BorderLayout.CENTER);
    scrollPane = pageView.getScrollPane();

    JPanel panel3 = new JPanel(new BorderLayout());
    panel3.add(navV, BorderLayout.NORTH);
//    panel3.add(navH, BorderLayout.SOUTH);
    add(panel3, BorderLayout.EAST);

    setInfoHelp("Help: First row - Major alleles, Second - Minor, Third - clicked SNP");
    repaint();
  }


  public void setViewPos(Point pos) {
    if (pos == null)
      return;
    pageView.setViewPos(pos);
  }

  public int getSelectedColIdx() {
    return selectedColIdx;
  }

  public void setSelectedColIdx(int selectedColIdx) {
    this.selectedColIdx = selectedColIdx;
  }

  public void showSnpAt(int colIdx) {
    if (mouseListener != null)
      mouseListener.showSnpAt(colIdx);
  }
}


