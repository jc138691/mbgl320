package dna.table.view.table;

import dna.seq.arr.SeqArr;
import dna.table.view.table.DnaTableViewFactory;
import dna.table.view.table.format.SeqArrFormatFactory;
import dna.table.view.TableNavigV;
import dna.table.view.listeners.SeqArrMouseMotion;

import javax.swing.*;
import javax.utilx.log.Log;
import java.awt.*;

import olga.SnpStation;
import olga.SnpStationProject;
import io.fasta.FastaOpt;
import ucm.seq.arr.view.UCShowPage;
import ucm.seq.arr.view.UCShowPageSeqArr;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 09/03/2009, 9:44:15 AM
 */
public class SeqArrView extends JPanel {
  public static Log log = Log.getLog(SeqArrView.class);
  protected JTable tableView;
  //  protected JTable rowHeaderView;
  protected SeqArr seqArr;
  //  protected TableNavigH navH;
  protected TableNavigV navV;
  protected JScrollPane scrollPane;
  protected JTextField infoText;
  private SeqPageView pageView;

  public SeqArrView(SeqArr arr) {
    this.seqArr = arr;
    init();
    loadView();
  }
  public SeqArrView() {
    init();
  }

  public void loadView() {
    SnpStation project = SnpStationProject.getInstance();
    FastaOpt cafOpt = project.getFastaOpt();
//    rowHeaderView = DnaTableViewFactory.makeRowHeaders(seqArr);
    tableView = DnaTableViewFactory.makeDnaTableView(seqArr, new SeqArrFormatFactory());
    tableView.addMouseMotionListener(new SeqArrMouseMotion(seqArr));

    navV.setGoDownAction(new UCShowPageSeqArr(UCShowPage.MOVE_NEXT, cafOpt), "Show next loaded contig");
    navV.setGoUpAction(new UCShowPageSeqArr(UCShowPage.MOVE_PREV, cafOpt), "Show previous contig");
    navV.setGoTopAction(new UCShowPageSeqArr(UCShowPage.MOVE_FIRST, cafOpt), "Show first loaded contig");
    navV.setGoBottomAction(new UCShowPageSeqArr(UCShowPage.MOVE_LAST, cafOpt), "Show last loaded contig");

    assemble();
//    OlgaFrame.getInstance().repaint();
  }
  public final SeqArr getSeqArr() {
    return seqArr;
  }

  private void init() {
//    navH = new TableNavigH();
    navV = new TableNavigV();
    infoText = new JTextField("Help messages");
    infoText.setEditable(false);
  }

  public void setInfoHelp(String s) {
    infoText.setText(s);
  }


  protected void assemble() {
    removeAll();
    setLayout(new BorderLayout());
    add(infoText, BorderLayout.NORTH);

    pageView = new SeqPageView(tableView);
    add(pageView, BorderLayout.CENTER);
    scrollPane = pageView.getScrollPane();

    JPanel panel3 = new JPanel(new BorderLayout());
    panel3.add(navV, BorderLayout.NORTH);
//    panel3.add(navH, BorderLayout.SOUTH);
    add(panel3, BorderLayout.EAST);
  }

  public Point getViewPos() {
    return scrollPane.getViewport().getViewPosition();
  }

  public void setViewPos(Point pos) {
    if (pos == null)
      return;
    scrollPane.getViewport().setViewPosition(pos);
  }
}

