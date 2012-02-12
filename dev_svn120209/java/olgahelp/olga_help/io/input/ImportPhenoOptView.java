package io.input;

import olga.SnpStation;

import javax.swingx.panelx.GridBagView;
import javax.swing.*;

import project.ucm.AdapterUCCToAL;
import project.ucm.AdapterUCCToALThread;
import project.ucm.UCShowHelpMssg;
import project.ProjectFrame;
import ucm.io.UCSelectPhenoFile;

import java.awt.*;

import dna.snp.pheno.SnpPhenoOpt;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 22/05/2009, 11:17:29 AM
 */
public class ImportPhenoOptView extends FileSelectOptView {
  private JCheckBox hasPhenoFile;
  private JLabel hasPhenoFileLbl;

  public ImportPhenoOptView(SnpStation model) {
    init();
    loadFrom(model);
    assemble();
  }

  private void init() {
    hasPhenoFile = new JCheckBox();
//    hasPhenoFile.setToolTipText("Import and use phenotypes if they are not in memory. Otherwise use the phenotypes that are already in memory.");
    hasPhenoFileLbl = new JLabel("Import when importing contigs");

    fileExt.setToolTipText("Phenotype file name extension");
    selectBttn.addActionListener(new AdapterUCCToAL(new UCSelectPhenoFile(this)));
  }

  private void assemble() {
    setLayout(new BorderLayout());

    JButton bttn;
    bttn = ProjectFrame.makeHelpButton();
    bttn.addActionListener(new AdapterUCCToALThread(new UCShowHelpMssg(
      "Select this check box if you want to import a phenotype file automatically the first time you load a contig file.\n"
        + "Note that:\n"
        + "  1. The phenotype file will be imported only once.\n"
        + "     If you wish to re-import the same file, press the 'Apply' button.\n"
      , this)));

    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    p.add(hasPhenoFile);
    p.add(hasPhenoFileLbl);
    p.add(bttn);

    GridBagView bag2 = new GridBagView();
    bag2.startRow(new JLabel()); bag2.middleRow(fileName); bag2.startRow(fileExt); bag2.endRow(selectBttn);
//    add(bag2, BorderLayout.NORTH);

    JPanel p2 = new JPanel(new BorderLayout());
    p2.add(p, BorderLayout.NORTH);
    p2.add(bag2, BorderLayout.SOUTH);

    add(p2, BorderLayout.NORTH);
  }

  public void loadTo(SnpPhenoOpt opt) {
    opt.setHasPhenoFile(hasPhenoFile.isSelected());
    opt.setFileName(fileName.getText());
    opt.setFileExt(fileExt.getText());
  }

  public void loadFrom(SnpPhenoOpt opt) {
    hasPhenoFile.setSelected(opt.getHasPhenoFile());
    fileName.setText(opt.getFileName());
    fileExt.setText(opt.getFileExt());
  }

  public void loadTo(SnpStation model) {
    loadTo(model.getSnpOpt().getPhenoOpt());
  }

  public void loadFrom(SnpStation model) {
    loadFrom(model.getSnpOpt().getPhenoOpt());
  }
}
