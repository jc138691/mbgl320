package dna.table.view.opt;

import javax.swing.*;
import javax.swingx.panelx.GridBagView;

import dna.table.DnaTableInfo;

/**
 * jc138691, 03/03/2009, 10:56:01 AM
 */
//public class DnaTableInfoView extends TaskOptView<SnpStation> { //GridBagView
public class DnaTableInfoView extends GridBagView {
  private JTextArea text;

  public DnaTableInfoView(DnaTableInfo model) {
    init();
    loadFrom(model);
    assemble();
  }

  private void init() {
    text = new JTextArea();
    text.setEditable(false);
  }
  private void assemble() {
    endRow(new JScrollPane(text));
  }
  public void loadTo(DnaTableInfo model) {
  }
  public void loadFrom(DnaTableInfo info) {
    text.setText("");
    text.append("Num of rows : " + info.getNumRows());
    text.append("\nLength  [min, max]: [" + info.getMinLen() + ", " + info.getMaxLen() + "]");
    text.append("\nQuality [min, max]: [" + info.getMinQlty() + ", " + info.getMaxQlty() + "]");
  }
}