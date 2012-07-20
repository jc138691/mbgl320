package papers.hy_swave;
import atom.angular.Spin;
import atom.energy.LsConfHMtrx;
import atom.shell.Ls;
import math.func.arr.FuncArr;
import math.vec.DbgView;
import math.vec.Vec;
import scatt.jm_2008.e1.ScttMthdBaseE1;
import scatt.jm_2008.jm.ScttRes;

import javax.utilx.log.Log;
/**
* dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,8/06/11,12:40 PM
*/
public abstract class HyLikeSWave extends Jm2010CommonLcr {
public static Log log = Log.getLog(HyLikeSWave.class);
protected static FuncArr trgtWfsNt;
protected static int FROM_CH = 0;  // initial scattering channel
protected static Spin SPIN;
protected static Ls SYS_LS;
protected static final int MAX_ENG_NUM_FOR_SDCS = 10;
protected static boolean CALC_TRUE_CONTINUUM = false;
protected static Vec scttEngs;
protected static double RES_MAX_LEVEL = 0.5; // maximum abs(Delta)/abs(Energy_distance)to be called a resonance
protected static int REPLACE_TRGT_ENGS_N = -1;
protected static int EXCL_SYS_RESON_IDX = -1;
protected static boolean CALC_DENSITY = false;
protected static int CALC_DENSITY_MAX_NUM = 1;
protected static boolean SAVE_TRGT_ENGS = false;
protected static boolean H_OVERWRITE = false;

public void setUp() {
  super.setUp();
  log.info("log.info(HyLikeSWave)");

//    JmMethodJmBasisE3.log.setDbg();
  LsConfHMtrx.log.setDbg();

  DbgView.setMinVal(VEC_DBG_MIN_VAL);
  DbgView.setNumShow(VEC_DBG_NUM_SHOW);

//    log.setDbg();
}

abstract public void calc(int newN, int newNt);

public void setupScattRes(ScttRes res, ScttMthdBaseE1 method) {
  super.setupScattRes(res, method);
  res.setCalcLabel(makeLabelTrgtS2(method));
}
protected static String makeLabelTrgtS2(ScttMthdBaseE1 method) {
  return "S" + SYS_LS.getS21() + "_" + Jm2010Common.makeLabelBasisOptOpen(method);
}
protected static String makeLabelTrgtS2() {
  return Jm2010Common.makeLabelBasisOptN();
}

}
