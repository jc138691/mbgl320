package dna.snp.view;

import olga.SnpStation;
import olga.OlgaFrame;
import project.ucm.AdapterUCCToALThread;
import project.ProjectFrame;
import project.workflow.task.TaskOptView;
import ucm.io.UCSelectExportSnpsFile;
import ucm.io.UCSelectExportSnpsType;

import javax.swingx.panelx.GridBagView;
import javax.swingx.text_fieldx.IntField;
import javax.swingx.progress.UCMonitorUCProgress;
import javax.swing.*;
import java.awt.*;

import dna.snp.SnpOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 21/05/2009, 9:55:59 AM
 */
public class SnpExportOptView extends TaskOptView<SnpStation>  {
  protected static final int FILE_NAME_LEN = 25;
  protected static final int EXT_LEN = 3;
  protected JTextField fileName;
  protected JTextField fileExt;
  protected JTextArea exportPreview;
  protected JButton selectBttn;
  protected JButton exportBttn;
  private static final int N_COLS = 10;
  private static final int N_ROWS = 2;
  protected static final int INT_SIZE = 6;
  private IntField nContigs;
  private JLabel nContigsLbl;

  public SnpExportOptView(SnpOpt model) {
    init();
    loadFrom(model);
    assemble();
  }

  private void init() {
    setBorder(BorderFactory.createEmptyBorder());

    fileName = new JTextField(FILE_NAME_LEN);
    fileExt = new JTextField(EXT_LEN);
    exportPreview = new JTextArea(N_ROWS, N_COLS);
    exportPreview.setEditable(false);

    selectBttn = ProjectFrame.makeLocateFileButton();
    exportBttn = new JButton("Export");

    fileExt.setToolTipText("file name extension");
    selectBttn.addActionListener(new AdapterUCCToALThread(new UCSelectExportSnpsFile(this)));
    exportBttn.addActionListener(new AdapterUCCToALThread(
//      new UCSelectExportSnpsType(this)));
    new UCMonitorUCProgress(new UCSelectExportSnpsType(this), OlgaFrame.getInstance())));

    String toolTip = "Number of contigs to be processed when exporting SNPs: '-1' means ALL";
    nContigsLbl = new JLabel("Num contigs:");    nContigsLbl.setToolTipText(toolTip);
    nContigs = new IntField(INT_SIZE, -1, 1000000);  nContigs.setToolTipText(toolTip);
  }

  private void assemble() {
    setLayout(new BorderLayout());
    GridBagView bag = new GridBagView();
    bag.setInserts(new Insets(0, 1, 0, 1));
    bag.startRow(exportBttn);
    bag.startRow(new JLabel(" SNPs from ")); 
    bag.startRow(nContigs);
    bag.startRow(new JLabel(" contigs to "));
    bag.middleRow(fileName); bag.startRow(fileExt); bag.endRow(selectBttn);

    add(new JScrollPane(exportPreview), BorderLayout.CENTER);
    add(bag, BorderLayout.SOUTH);
  }

  public void loadTo(SnpOpt model) {
    model.setExportFileName(fileName.getText());
    model.setExportFileExt(fileExt.getText());
    model.setExportNumContigs(nContigs.getInput());
  }

  public void loadFrom(SnpOpt model) {
    exportPreview.setText("Export preview: " + model.getExportPreview());
    exportPreview.setCaretPosition(0);
    fileName.setText(model.getExportFileName());
    fileExt.setText(model.getExportFileExt());
    nContigs.setValue(model.getExportNumContigs());
  }

  public void loadTo(SnpStation model) {
    loadTo(model.getSnpOpt());
  }
  public void loadFrom(SnpStation model) {
    loadFrom(model.getSnpOpt());
  }
}