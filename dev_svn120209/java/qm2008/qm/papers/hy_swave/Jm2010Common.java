package papers.hy_swave;
import atom.energy.part_wave.PotHMtrx;
import math.func.FuncVec;
import math.vec.Vec;
import math.vec.grid.StepGridOpt;
import papers.project_setup.ProjCommon;
import papers.project_setup.ProjTestOpt;
import qm_station.QMS;
import qm_station.ui.scatt.CalcOptR;
import scatt.eng.EngModel;
import scatt.jm_2008.e1.ScttMthdBaseE1;
import scatt.jm_2008.e1.JmCalcOptE1;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
/**
 * Created by Dmitry.A.Konovalov@gmail.com, 11/02/2010, 10:33:47 AM
 */
public abstract class Jm2010Common extends ProjCommon {
protected static QMS project;
protected static JmCalcOptE1 calcOpt;
protected static LgrrOpt lgrrOptN;
protected static Vec vR;
protected static FuncVec pot;
protected static PotHMtrx trgtPotH;
protected static int N = 10;
protected static double LAMBDA = 1.1f;
protected static int R_FIRST = 0;
protected static int R_LAST = 100;
protected static int R_N = 1301;
public static double VEC_DBG_MIN_VAL = 1e-10;
public static int VEC_DBG_NUM_SHOW = 40;
protected static double MAX_INTGRL_ERR = 1e-6;  // [7Sep2011]changed from 1e-5 to 1e-6
protected final static int L = 0;
protected static int TARGET_Z = 1; // 1 for e-Hy
protected static double ENG_FIRST = 0.01;
protected static double ENG_LAST = 10;
protected static int ENG_N = 1000;
//  public abstract StepGridOpt makeGridOpt();
//  @Override
public StepGridOpt makeStepGridModelR() {
  StepGridOpt res = new StepGridOpt();
  res.setFirst(0);
  res.setLast(R_LAST);
  res.setNumPoints(R_N);
  return res;
}
public void setupScattRes(ScttRes res, ScttMthdBaseE1 method) {
  res.setHomeDir(HOME_DIR);
  res.setModelDir(MODEL_DIR);
  res.setModelName(MODEL_NAME);
  res.setMethod(method);
}
protected static String makeLabelBasisOptOpen(ScttMthdBaseE1 method) {
  if (method.getCalcOpt().getUseClosed()) {
    return lgrrOptN.makeLabel() + ".dat";
  } else {
    return lgrrOptN.makeLabel() + "_OPEN.dat";
  }
}
protected static String makeLabelBasisOptN() {
  return lgrrOptN.makeLabel() + ".dat";
}
public CalcOptR makeJmPotOptR() {
  CalcOptR res = new CalcOptR();
  res.setGridOpt(makeStepGridModelR());
  res.setBasisOpt(makeBasisOpt());
  res.setTestOpt(makeTestOpt());
  res.setGridEng(makeGridEng());
  return res;
}
public ProjTestOpt makeTestOpt() {
  ProjTestOpt res = new ProjTestOpt();
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
public LgrrOpt makeBasisOpt() {
  LgrrOpt res = new LgrrOpt();
  res.setL(0);
  res.setLambda(LAMBDA);
  res.setN(N);
  return res;
}
}
