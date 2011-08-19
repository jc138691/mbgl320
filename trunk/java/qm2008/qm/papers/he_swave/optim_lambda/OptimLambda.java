package papers.he_swave.optim_lambda;
import atom.AtomUtil;
import atom.angular.Spin;
import atom.data.AtomHe;
import atom.data.AtomHy;
import atom.energy.ConfHMtrx;
import atom.energy.part_wave.PartHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.poet.HeSWaveAtom;
import atom.wf.log_cr.LcrFactory;
import atom.wf.log_cr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.func.simple.FuncPowInt;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import papers.he_swave.HeSWaveScatt;
import project.workflow.task.test.FlowTest;
import qm_station.QMSProject;
import scatt.jm_2008.e3.JmMethodAnyBasisE3;
import scatt.jm_2008.jm.JmRes;
import scatt.jm_2008.jm.laguerre.JmLgrrLabelMaker;
import scatt.jm_2008.jm.laguerre.JmLgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.JmLgrrOrthLcr;
import scatt.jm_2008.jm.target.JmTrgtE3;
import scatt.jm_2008.jm.theory.JmD;
import stats.VecStats;
import sun.security.krb5.internal.TGSRep;

import javax.iox.FileX;
import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,19/08/11,8:45 AM
 */
public class OptimLambda extends HeSWaveScatt {
  public static Log log = Log.getLog(OptimLambda.class);
  public static void main(String[] args) {
    // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
    OptimLambda runMe = new OptimLambda();
    runMe.setUp();
    runMe.testRun();
  }
  public void setUp() {
    super.setUp();
    log.info("log.info(HeSModelOptimLamdba)");
    OptimLambda.log.setDbg();
    log.setDbg();
  }
  @Override
  public void calcJm(int newN, int newNt) {
  }
  public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
    project = QMSProject.makeInstance("HeSWaveBasisHePlus", "110606");
    TARGET_Z = AtomHe.Z;
    HOME_DIR = "C:\\dev\\physics\\papers\\output";
    MODEL_NAME = "HeSModelOptimLamdba";
    MODEL_DIR = "HeSModelOptimLamdba";
    runJob();
  }

  public void runJob() {
//    // Nt= 80
//    int currN = 81;
//    LCR_FIRST = -7;
//    LCR_N = 1601;
//    R_LAST = 450;

//    // Nt= 70
//    int currN = 71;
//    LCR_FIRST = -5;
//    LCR_N = 1201;
//    R_LAST = 330;

    // upto N=50
//    LCR_FIRST = -5;  //-5
//    LCR_N = 1001;  //901
//    R_LAST = 250;

    // upto N=40
    LCR_FIRST = -5;
    LCR_N = 701;
    R_LAST = 200;

    int currNt = 20;
    int currN = 21;
//    int currN = currNt + 1;
    IGNORE_BUG_PoetHeAtom = true;

    SPIN = Spin.ELECTRON;
    calcJm(currN, currNt);


    int minNt = 15;
    int maxNt = 20;
    int stepNt = 5;
    double minLam = 1.3;
    double maxLam = 1.8;
    int numLam = 51;
    StepGrid lamArr  = new StepGrid(minLam, maxLam, numLam);
    Vec errArr = new Vec(numLam);

    int NUM_LEVELS = 7; //
    LAMBDA = minLam;

    FlowTest.setLog(log);
    for (int newNt = minNt; newNt <= maxNt; newNt += stepNt) {
      Nt = newNt;
      Nc = Nt;

      for (int i = 0; i < lamArr.size(); i++) {
        LAMBDA = lamArr.get(i);

        initProject();

        // from jmPotTestOk
        StepGridModel sg = jmOpt.getGrid();           log.dbg("x step grid model =", sg);
        StepGrid x = new StepGrid(sg);                 log.dbg("x grid =", x);
        quadrLcr = new WFQuadrLcr(x);                  log.dbg("x weights =", quadrLcr);
        rVec = quadrLcr.getR();                        log.dbg("r grid =", rVec);
        basisOptN = jmOpt.getJmModel();                 log.dbg("Laguerr model =", basisOptN);

        // from jmHyTestOk
        basisOptN = new JmLgrrLabelMaker(basisOptN, Nt);    log.dbg("basisOptN =", basisOptN); // this is just for the file name label
        JmLgrrModel lgrrOptNt = new JmLgrrModel(basisOptN); // for the target N, i.e. N_t
        lgrrOptNt.setN(Nt);                             log.dbg("Laguerr model (N_t)=", lgrrOptNt);
        orthonNt = new JmLgrrOrthLcr(quadrLcr, lgrrOptNt); log.dbg("JmLgrrOrthLcr(N_t) = ", orthonNt);
        potFunc = new FuncPowInt(-AtomHe.Z, -1);  // f(r)=-1./r
        pot = new FuncVec(rVec, potFunc);                       log.dbg("-1/r=", new VecDbgView(pot));

        // Making He+ eigen-states
        trgtPartH = new PartHMtrxLcr(L, orthonNt, pot);       //log.dbg("trgtPartH=", trgtPartH);
        Vec basisEngs = trgtPartH.getEigVal();                log.dbg("basisEngs=", new VecDbgView(basisEngs));
        trgtBasisNt = trgtPartH.getEigFuncArr();              log.dbg("targetNt=", new FuncArrDbgView(trgtBasisNt));
        SlaterLcr slater = new SlaterLcr(quadrLcr);
        JmTrgtE3 jmTrgt = makeTrgtBasisNt(slater, trgtBasisNt);

        Vec trgtEngs = jmTrgt.getEngs();                        log.dbg("trgtEngs=", new VecDbgView(trgtEngs));
        double err = VecStats.rmse(HeSWaveAtom.E_SORTED, trgtEngs.getArr(), NUM_LEVELS);   log.info("err=", err);
        errArr.set(i, err);
        int dbg = 1;
      }
      log.info("trgtEngs=", new VecDbgView(errArr));
      int idx = errArr.minIdx();
      log.info("min err=", errArr.get(idx));
      log.info("best LAMBDA=", lamArr.get(idx));
      log.info("idx=", idx);
      log.info("Nt=", Nt);
      int dbg2 = 1;
    }

  }




}