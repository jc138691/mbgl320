package io.input;

import javax.swing.*;
import javax.swingx.text_fieldx.IntField;
import javax.swingx.panelx.GridBagView;

import java.awt.*;

import io.DnaFileOpt;
import dna.snp.pheno.SnpPhenoOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 08/10/2009, 10:14:04 AM
 */
public abstract class ImportContigsOptView extends FileSelectOptView {
  private static final int INT_SIZE = 3;
  private static final int OFFSET_SIZE = 7;
  private IntField nRecs;     // num of records
  private JLabel nRecsLbl;
  private IntField firstRec;
  private JLabel firstRecLbl;

  public ImportContigsOptView() {
    init();
//    loadFrom(model);
//    assemble();
  }

  private void init() {
    fileExt.setToolTipText("CAF file name extension");

    String toolTip = "Number of contigs to be imported for viewing";
    nRecsLbl = new JLabel("Num contigs");   nRecsLbl.setToolTipText(toolTip);
    nRecs = new IntField(INT_SIZE, 1, 100);  nRecs.setToolTipText(toolTip);

    toolTip = "Starting from this contig number";
    firstRecLbl = new JLabel("First contig");           firstRecLbl.setToolTipText(toolTip);
    firstRec = new IntField(OFFSET_SIZE, 1, 1000000);   firstRec.setToolTipText(toolTip);
  }

  protected void assemble() {
    setLayout(new BorderLayout());
    GridBagView bag = new GridBagView();
    bag.middleRow(fileName); bag.startRow(fileExt); bag.endRow(selectBttn);

    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p.add(nRecsLbl);   p.add(nRecs);    p.add(new JLabel("   "));
    p.add(firstRecLbl);   p.add(firstRec); p.add(new JLabel("   "));

    JPanel p2 = new JPanel(new BorderLayout());
    p2.add(bag, BorderLayout.NORTH);
    p2.add(p, BorderLayout.SOUTH);

    add(p2, BorderLayout.NORTH);
  }

  public void loadTo(DnaFileOpt opt) {
    opt.setFileName(fileName.getText());
    opt.setFileExt(fileExt.getText());
    opt.setPageSize(nRecs.getInput());
    opt.setPageOffsetIdx(firstRec.getInput() - 1);
  }

  public void loadFrom(DnaFileOpt opt) {
    fileName.setText(opt.getFileName());
    fileExt.setText(opt.getFileExt());
    nRecs.setValue(opt.getPageSize());
    firstRec.setValue(opt.getPageOffsetIdx() + 1);
  }
}