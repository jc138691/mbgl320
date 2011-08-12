package io.input;

import project.ucm.AdapterUCCToAL;
import project.ProjectFrame;
import olga.SnpStation;

import javax.swing.*;
import javax.swingx.panelx.GridBagView;
import javax.swingx.text_fieldx.IntField;

import ucm.io.UCSelectFastaFiles;
import ucm.io.UCSelectQltyFile;
import io.fasta.FastaOpt;

import java.awt.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ IDEA on 13/02/2009 at 14:44:06.
 */
public class ImportFastaOptView extends FileSelectOptView {
  private static final int INT_SIZE = 3;
  private static final int OFFSET_SIZE = 7;
  private JCheckBox qltyOn;
  private JTextField qltyFileName;
  private JTextField qltyFileExt;
  private JButton qltyBttn;

  private IntField nRecs;     // num of records
  private JLabel nRecsLbl;
  private IntField firstRec;
  private JLabel firstRecLbl;

  public ImportFastaOptView(SnpStation model) {
    init();
    loadFrom(model);
    assemble();
  }

  private void init() {
    fileExt.setToolTipText("FASTA file name extension");
    selectBttn.addActionListener(new AdapterUCCToAL(new UCSelectFastaFiles(this)));

    qltyOn = new JCheckBox("Quality");
    qltyOn.setToolTipText("Phred-like quality scores");
    qltyFileName = new JTextField(FILE_NAME_LEN);
    qltyFileExt = new JTextField(EXT_LEN);
    qltyFileExt.setToolTipText("Quality file name extension");
    qltyBttn = ProjectFrame.makeLocateFileButton();
    qltyBttn.addActionListener(new AdapterUCCToAL(new UCSelectQltyFile(this)));

    String toolTip = "Number of FASTA sequencies to be imported for viewing";
    nRecsLbl = new JLabel("Num seqs");   nRecsLbl.setToolTipText(toolTip);
    nRecs = new IntField(INT_SIZE, 1, 100);  nRecs.setToolTipText(toolTip);

    toolTip = "Starting from this sequence number";
    firstRecLbl = new JLabel("First seq");           firstRecLbl.setToolTipText(toolTip);
    firstRec = new IntField(OFFSET_SIZE, 1, 1000000);   firstRec.setToolTipText(toolTip);
  }

  private void assemble() {
    setLayout(new BorderLayout());

    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p.add(nRecsLbl);   p.add(nRecs);    p.add(new JLabel("   "));
    p.add(firstRecLbl);   p.add(firstRec);

    GridBagView bag = new GridBagView();
    bag.startRow(new JPanel()); bag.middleRow(fileName);     bag.startRow(fileExt);     bag.endRow(selectBttn);
    bag.startRow(qltyOn);       bag.middleRow(qltyFileName); bag.startRow(qltyFileExt); bag.endRow(qltyBttn);
    bag.startRow(new JPanel()); bag.middleRow(p);  
    add(bag, BorderLayout.NORTH);
  }

  public void loadTo(SnpStation model) {
    FastaOpt opt = model.getFastaOpt();
    opt.setHasQltyFile(qltyOn.isSelected());
    opt.setFileName(fileName.getText());
    opt.setQltyFileName(qltyFileName.getText());
    opt.setFileExt(fileExt.getText());
    opt.setQltyFileExt(qltyFileExt.getText());

    opt.setPageSize(nRecs.getInput());
    opt.setPageOffsetIdx(firstRec.getInput() - 1);
  }

  public void loadFrom(SnpStation model) {
    FastaOpt opt = model.getFastaOpt();
    fileName.setText(opt.getFileName());
    fileExt.setText(opt.getFileExt());
    qltyOn.setSelected(opt.getHasQltyFile());
    qltyFileName.setText(opt.getQltyFileName());
    qltyFileExt.setText(opt.getQltyFileExt());

    nRecs.setValue(opt.getPageSize());
    firstRec.setValue(opt.getPageOffsetIdx() + 1);
  }
}
