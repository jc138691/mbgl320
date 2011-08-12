package io.input;

import olga.SnpStation;
import project.ucm.AdapterUCCToAL;
import ucm.io.UCSelectAceFile;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 08/10/2009, 9:48:09 AM
 */
public class ImportAceOptView extends ImportContigsOptView {
  public ImportAceOptView(SnpStation model) {
    init();
    loadFrom(model);
    assemble();
  }
  private void init() {
    fileExt.setToolTipText("ACE file name extension");
    selectBttn.addActionListener(new AdapterUCCToAL(new UCSelectAceFile(this)));
  }
  public void loadTo(SnpStation model) {
    super.loadTo(model.getAceOpt());
  }
  public void loadFrom(SnpStation model) {
    super.loadFrom(model.getAceOpt());
  }
}