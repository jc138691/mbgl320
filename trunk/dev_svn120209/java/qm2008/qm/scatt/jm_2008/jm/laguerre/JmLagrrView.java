package scatt.jm_2008.jm.laguerre;
import javax.swingx.text_fieldx.FloatField;
import javax.swingx.text_fieldx.IntField;
import javax.swingx.panelx.GridBagView;
import javax.utilx.log.Log;
import javax.swing.*;
/**
 * Copyright dmitry.konovalov@jcu.edu.au Date: 15/09/2008, Time: 15:53:42
 */
public class JmLagrrView extends GridBagView {
  public static Log log = Log.getLog(JmLagrrView.class);
  private static final int FLOAT_SIZE = 3;
  private static final int INT_SIZE = 3;

  private JLabel lambdaLbl;
  protected FloatField lambda;

  private JLabel maxNLbl;
  private IntField maxN;

  private JLabel LLbl;
  private IntField L;

  public JmLagrrView(LgrrModel model)    {
    super("JM-Laguerre");
    init();
    loadFrom(model);
    assemble();
  }
  private void assemble() {
    startRow(lambdaLbl); endRow(lambda);
    startRow(LLbl);      endRow(L);
    startRow(maxNLbl);   endRow(maxN);
  }
  private void init()   {
    lambdaLbl = new JLabel("lambda");
    lambda = new FloatField(FLOAT_SIZE, 0.1f, 10);
    lambda.setToolTipText("Lambda parameter, x = lambda * r");

    LLbl = new JLabel("L");
    L = new IntField(INT_SIZE, 0, 10);
    L.setToolTipText("v = (2 * L + 1) in L^v_n(x)");

    maxNLbl = new JLabel("maxN");
    maxN = new IntField(INT_SIZE, 1, 30);
    maxN.setToolTipText("Maximum n in L^v_n(x), i.e. 0 <= n <= maxN");
  }
  public void loadTo(LgrrModel model) {
    model.setLambda(lambda.getInput());
    model.setL(L.getInput());
    model.setN(maxN.getInput());
  }
  public void loadFrom(LgrrModel model) {
    lambda.setValue(model.getLambda());
    L.setValue(model.getL());
    maxN.setValue(model.getN());
  }
}
