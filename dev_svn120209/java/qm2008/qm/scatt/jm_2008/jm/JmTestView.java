package scatt.jm_2008.jm;
import project.ucm.UCController;
import project.ucm.AdapterUCCToALThread;

import javax.swingx.panelx.GridBagView;
import javax.swingx.text_fieldx.ScientificField;
import javax.swingx.buttonx.ButtonUtils;
import javax.utilx.log.Log;
import javax.swing.*;
import java.text.DecimalFormat;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 7/10/2008, Time: 16:43:02
 */
public class JmTestView extends GridBagView {
  public static Log log = Log.getLog(JmTestView.class);
  private static final int FLOAT_SIZE = 3;
  private static final int INT_SIZE = 3;

  private JButton testBttn;

  private JLabel maxErrLbl;
  protected ScientificField maxErr;

//  private JLabel maxNLbl;
//  private IntField maxN;

  public JmTestView(TestModel model)    {
    super("Self-test");
    init();
    loadFrom(model);
    assemble();
  }
  private void assemble() {
    startRow(maxErrLbl); endRow(maxErr);
//    startRow(maxNLbl);   endRow(maxN);
    startRow(new JLabel());   endRow(testBttn);
  }
  private void init()   {
    testBttn = ButtonUtils.makeFlat("Run");
    testBttn.setToolTipText("Run all relevant self-tests");

    maxErrLbl = new JLabel("Integr.");
    maxErrLbl.setToolTipText("Integration");
    maxErr = new ScientificField(FLOAT_SIZE, 1e-10f, 0.01f, new DecimalFormat("0.#E0"));
    maxErr.setToolTipText("Maximum permitted integration error");

//    maxNLbl = new JLabel("maxN");
//    maxN = new IntField(INT_SIZE, 1, 30);
//    maxN.setToolTipText("Maximum n in L^v_n(x), i.e. 0 <= n <= maxN");
  }
  public void loadTo(TestModel model) {
    model.setMaxIntgrlErr(maxErr.getInput());
  }
  public void loadFrom(TestModel model) {
    maxErr.setValue(model.getMaxIntgrlErr());
//    maxN.setValue(model.getMaxN());
  }
  public void runTest(UCController uc) {
    testBttn.addActionListener(new AdapterUCCToALThread(uc));
  }
}
