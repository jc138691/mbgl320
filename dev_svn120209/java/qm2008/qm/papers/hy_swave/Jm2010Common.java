package papers.hy_swave;
import atom.energy.pw.PotHMtrx;
import math.func.FuncVec;
import math.vec.IntVec;
import math.vec.Vec;
import math.vec.grid.StepGridOpt;
import papers.project_setup.ProjCommon;
import papers.project_setup.ProjTestOpt;
import qm_station.QMS;
import qm_station.ui.scatt.CalcOptR;
import scatt.eng.EngOpt;
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
protected static double LAMBDA = 2;
protected static double LAMBDA_NC = 4;
protected static int R_FIRST = 0;
protected static int R_LAST = 100;
protected static int R_N = 1301;
public static double VEC_DBG_MIN_VAL = 1e-10;
public static int VEC_DBG_NUM_SHOW = 40;
protected static double MAX_INTGRL_ERR = 1e-6;  // [7Sep2011]changed from 1e-5 to 1e-6
protected final static int L = 0;
protected static int TARGET_Z = 1; // 1 for e-Hy
protected static double SCTT_ENG_MIN = 0.01;
protected static double SCTT_ENG_MAX = 10;
protected static int SCTT_ENG_N = 1000;
protected static IntVec AUTO_ENG_POINTS;  // num of point BETWEEN consecutive given engs

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
public EngOpt makeGridEng() {
  EngOpt res = new EngOpt();
  res.setFirst(SCTT_ENG_MIN);
  res.setLast(SCTT_ENG_MAX);
  res.setNumPoints(SCTT_ENG_N);
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
