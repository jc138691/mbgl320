package papers.hy_swave;

import atom.energy.part_wave.PartHMtrx;
import math.func.FuncVec;
import math.vec.Vec;
import math.vec.grid.StepGridModel;
import project.workflow.task.test.FlowTest;
import qm_station.QMS;
import scatt.eng.EngModel;
import qm_station.ui.scatt.JmOptR;
import scatt.jm_2008.e1.JmOptE1;
import scatt.jm_2008.e2.JmMethodBaseE2;
import scatt.jm_2008.jm.JmRes;
import scatt.jm_2008.jm.JmTestModel;
import scatt.jm_2008.jm.laguerre.JmLgrrModel;

import javax.utilx.log.Log;

/**
 * Created by Dmitry.A.Konovalov@gmail.com, 11/02/2010, 10:33:47 AM
 */
public abstract class Jm2010Common extends FlowTest {
  protected static String HOME_DIR = "HOME_DIR";
  protected static String MODEL_NAME = "MODEL_NAME";
  protected static String MODEL_DIR = "MODEL_DIR";
  protected static QMS project;
  protected static JmOptE1 jmOpt;
  protected static JmTestModel testOpt;
  protected static JmLgrrModel basisOptN;
  protected static Vec rVec;
  protected static FuncVec pot;
  protected static PartHMtrx trgtPartH;

  protected static int N = 10;
  protected static double LAMBDA = 1.1f;

  protected static int R_FIRST = 0;
  protected static int R_LAST = 100;
  protected static int R_N = 1301;

  public static double VEC_DBG_MIN_VAL = 1e-10;
  public static int VEC_DBG_NUM_SHOW = 40;
  protected static float MAX_INTGRL_ERR = 0.000001f;  // [7Sep2011]changed from 1e-5 to 1e-6
  protected final static int L = 0;
  protected static int TARGET_Z = 1; // 1 for e-Hy

  protected static double ENG_FIRST = 0.01;
  protected static double ENG_LAST = 10;
  protected static int ENG_N = 1000;

  public void setUp() {
    Log.addGlobal(System.out);
  }
//  public abstract StepGridModel makeStepGridModel();
//  @Override
  public StepGridModel makeStepGridModelR() {
    StepGridModel res = new StepGridModel();
    res.setFirst(0);
    res.setLast(R_LAST);
    res.setNumPoints(R_N);
    return res;
  }
  public void setupJmRes(JmRes res, JmMethodBaseE2 method) {
    res.setHomeDir(HOME_DIR);
    res.setModelDir(MODEL_DIR);
    res.setModelName(MODEL_NAME);
    res.setMethodE2(method);
  }
  protected static String makeLabelBasisOptOpen(JmMethodBaseE2 method) {
    if (method.getJmOpt().getUseClosed()) {
      return basisOptN.makeLabel() + ".dat";
    }
    else {
      return basisOptN.makeLabel() + "_OPEN.dat";
    }
  }
  protected static String makeLabelBasisOptN() {
      return basisOptN.makeLabel() + ".dat";
  }

  public JmOptR makeJmPotOptR() {
    JmOptR res = new JmOptR();
    res.setGrid(makeStepGridModelR());
    res.setJmModel(makeJmLagrr());
    res.setJmTest(makeJmTest());
    res.setGridEng(makeGridEng());
    return res;
  }
  public JmTestModel makeJmTest() {
    JmTestModel res = new JmTestModel();
    res.setMaxIntgrlErr(MAX_INTGRL_ERR);
    return res;
  }
  public EngModel makeGridEng() {
    EngModel res = new EngModel();
    res.setFirst(ENG_FIRST);
    res.setLast(ENG_LAST);
    res.setNumPoints(ENG_N);
    return res;
  }
  public JmLgrrModel makeJmLagrr() {
    JmLgrrModel res = new JmLgrrModel();
    res.setL(0);
    res.setLambda(LAMBDA);
    res.setN(N);
    return res;
  }

}
