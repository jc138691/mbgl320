package io.input;

import olga.SnpStation;
import project.ucm.AdapterUCCToAL;

import ucm.io.UCSelectCafFile;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 27/03/2009, 2:13:56 PM
 */
public class ImportCafOptView extends ImportContigsOptView {
  public ImportCafOptView(SnpStation model) {
    init();
    loadFrom(model);
    assemble();
  }

  private void init() {
    fileExt.setToolTipText("CAF file name extension");
    selectBttn.addActionListener(new AdapterUCCToAL(new UCSelectCafFile(this)));
  }

  public void loadTo(SnpStation model) {
    super.loadTo(model.getCafOpt());
  }

  public void loadFrom(SnpStation model) {
    super.loadFrom(model.getCafOpt());
  }
}