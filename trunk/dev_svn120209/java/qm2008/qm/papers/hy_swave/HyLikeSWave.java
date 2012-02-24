package papers.hy_swave;
import atom.angular.Spin;
import atom.energy.ConfHMtrx;
import atom.shell.Ls;
import atom.wf.coulomb.CoulombNmrvLcr;
import atom.wf.coulomb.CoulombNmrvR;
import atom.wf.log_cr.RkLcrFlowTest;
import atom.wf.log_cr.YkLcrFlowTest;
import atom.wf.log_cr.test.RkLcrTest;
import atom.wf.log_cr.test.YkLcrTest;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.simple.FuncPowInt;
import math.vec.DbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import project.workflow.task.test.FlowTest;
import qm_station.jm.PotEigVecLcrTest;
import scatt.jm_2008.e2.JmMethodBaseE2;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.laguerre.JmLgrrLabelMaker;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcrTest;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;

import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,8/06/11,12:40 PM
 */
public abstract class HyLikeSWave extends Jm2010CommonLcr {
  public static Log log = Log.getLog(HyLikeSWave.class);
  protected static FuncArr trgtBasisNt;
  protected static LgrrOrthLcr orthonNt;  // for N_t
  protected static int Nt = 20;
  protected static int FROM_CH = 0;  // initial scattering channel
  protected static Spin SPIN;
  protected static Ls SYS_LS;
  protected static final int MAX_ENG_NUM_FOR_SDCS = 10;
  protected static boolean CALC_TRUE_CONTINUUM = false;
  protected static Vec scttEngs;
  protected static double RES_MAX_LEVEL = 0.5; // maximum abs(Delta)/abs(Energy_distance)to be called a resonance
  protected static int KEEP_CLOSED_N = 10;  // number of closed channels to keep
  protected static boolean USE_CLOSED_CHANNELS = true;
  protected static int EXCL_SYS_RESON_IDX = -1;
  protected static boolean CALC_DENSITY = false;
  protected static int CALC_DENSITY_MAX_NUM = 1;
  protected static boolean SAVE_TRGT_ENGS = false;
  protected static boolean H_OVERWRITE = false;

  public void setUp() {
    super.setUp();
    log.info("log.info(HyLikeSWave)");

//    JmMethodFanoE2.log.setDbg();
//    JmMethodJmBasisE3.log.setDbg();
//    JmMethodE1.log.setDbg();
//    Jm2010CommonLcr.log.setDbg();
//    JmCh.log.setDbg();
//    ConfArrFactoryE2.log.setDbg();
//    SysE2_OLD.log.setDbg();
//    StepGrid.log.setDbg();
//    CoulombNmrvLcr.log.setDbg();

//    JmMethodJmBasisE3.log.setDbg();
    ConfHMtrx.log.setDbg();

    DbgView.setMinVal(VEC_DBG_MIN_VAL);
    DbgView.setNumShow(VEC_DBG_NUM_SHOW);

//    log.setDbg();
  }

  abstract public void calcJm(int newN, int newNt);

  protected void jmHyTestOk(int trgtZ) {
    FlowTest.setLog(log);

    calcOpt.setUseClosed(USE_CLOSED_CHANNELS);

    basisOptN = new JmLgrrLabelMaker(basisOptN, Nt);    log.dbg("basisOptN =", basisOptN); // this is just for the file name label
    LgrrModel lgrrOptNt = new LgrrModel(basisOptN); // for the target N, i.e. N_t
    lgrrOptNt.setN(Nt);                             log.dbg("Laguerr model (N_t)=", lgrrOptNt);

    orthonNt = new LgrrOrthLcr(quadrLcr, lgrrOptNt); log.dbg("LgrrOrthLcr(N_t) = ", orthonNt);
    FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());      // LOCK MAX ERR
    {
      if (!new LgrrOrthLcrTest(orthonNt).ok()) return;
//      if (!new PotEigVecLcrTest(AtomHy.Z, orthonNt).ok()) return;
      if (!new PotEigVecLcrTest(trgtZ, orthonNt).ok()) return;
    }
    FlowTest.unlockMaxErr();                             // FREE MAX ERR

//    potFunc = new FuncPowInt(-AtomHy.Z, -1);  // f(r)=-1./r
    potFunc = new FuncPowInt(-trgtZ, -1);  // f(r)=-1./r
    pot = new FuncVec(rVec, potFunc);                       log.dbg("-1/r=", new VecDbgView(pot));

    if (!new YkLcrTest().ok()) return;
    if (!new YkLcrFlowTest(quadrLcr).ok()) return;

    if (!new RkLcrTest().ok()) return;
    if (!new RkLcrFlowTest(quadrLcr).ok()) return;

    if (!new CoulombNmrvR().ok()) return;
    if (!new CoulombNmrvLcr().ok()) return;

  }

  public void setupJmRes(ScattRes res, JmMethodBaseE2 method) {
    super.setupScattRes(res, method);
    res.setCalcLabel(makeLabelTrgtS2(method));
  }
  protected static String makeLabelTrgtS2(JmMethodBaseE2 method) {
    return "S" + SYS_LS.getS21() + "_" + Jm2010Common.makeLabelBasisOptOpen(method);
  }
  protected static String makeLabelTrgtS2() {
    return Jm2010Common.makeLabelBasisOptN();
  }

}
