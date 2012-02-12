package javax.langx;

import javax.swingx.panelx.GridBagView;
import javax.swing.*;

/**
 * Created by Dmitry.A.Konovalov@gmail.com using IntelliJ, 17/03/2009, 1:12:27 PM
 */
public class MemoryInfoView extends GridBagView {
  private final static int FIELD_LEN = 20;
  private final static int SCALE = 1000 * 1000;
  private JTextField maxMem;
  private JTextField freeMem;
  private JTextField totMem;
  public MemoryInfoView() {
    super("Java Vertual Machine Memory");
    init();
    refresh();
    assemble();
  }

  private void init() {
    maxMem = new JTextField(FIELD_LEN);
    maxMem.setEditable(false);
    freeMem = new JTextField(FIELD_LEN);
    freeMem.setEditable(false);
    totMem = new JTextField(FIELD_LEN);
    totMem.setEditable(false);
  }
  private void assemble() {
    startRow(new JLabel("Free:")); startRow(freeMem); endRow(new JLabel("MB"));
    startRow(new JLabel("Allocated:")); startRow(totMem); endRow(new JLabel("MB"));
    startRow(new JLabel("Max:")); startRow(maxMem); endRow(new JLabel("MB"));
  }
  public void refresh() {
    Runtime rt = Runtime.getRuntime();
    maxMem.setText("" + rt.maxMemory() / SCALE);
    freeMem.setText("" + rt.freeMemory() / SCALE);
    totMem.setText("" + rt.totalMemory() / SCALE);
  }
}
