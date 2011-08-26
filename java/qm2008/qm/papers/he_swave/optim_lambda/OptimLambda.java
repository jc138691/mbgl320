package papers.he_swave.optim_lambda;
import atom.angular.Spin;
import atom.data.AtomHe;
import atom.energy.part_wave.PartHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.poet.HeSWaveAtom;
import atom.wf.log_cr.WFQuadrLcr;
import math.Calc;
import math.func.FuncVec;
import math.func.arr.FuncArrDbgView;
import math.func.simple.FuncPowInt;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import papers.he_swave.HeSWaveScatt;
import project.workflow.task.test.FlowTest;
import qm_station.QMSProject;
import scatt.jm_2008.jm.laguerre.JmLgrrLabelMaker;
import scatt.jm_2008.jm.laguerre.JmLgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.JmLgrrOrthLcr;
import scatt.jm_2008.jm.target.JmTrgtE3;
import stats.VecStats;

import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,19/08/11,8:45 AM
 */
public class OptimLambda extends HeSWaveScatt {
  public static Log log = Log.getLog(OptimLambda.class);
  public static final int NUM_HE_LEVELS = 5; //
  public static final double MIN_ERR = 1.e-8; //
  public static final double MIN_LAMBDA_ERR = 0.5e-6; //

  public static void main(String[] args) {
    // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
    OptimLambda runMe = new OptimLambda();
    runMe.setUp();
    runMe.testRun();
  }
  public void setUp() {
    super.setUp();
    log.info("log.info(HeSModelOptimLamdba)");
//    OptimLambda.log.setDbg();
//    log.setDbg();
  }
  @Override
  public void calcJm(int newN, int newNt) {
  }
  public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
    project = QMSProject.makeInstance("HeSWaveBasisHeIon", "110606");
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
    LCR_FIRST = -6;  //-5
    LCR_N = 1001;  //901
    R_LAST = 250;

//    // upto N=25
//    LCR_FIRST = -5;
//    LCR_N = 701;
//    R_LAST = 200;

    IGNORE_BUG_PoetHeAtom = true;
    SPIN = Spin.ELECTRON;

    double lamLL = 1;
    double lamRR = 10;
    int numLam = 100;
    FlowTest.setLog(log);

    Nc = 10;
    Nt = 20;
    LAMBDA = lamLL;
    double eLL = calcErr();        log.info("eLL=", eLL);
    LAMBDA = lamRR;
    double eRR = calcErr();        log.info("eRR=", eRR);
    double lamL = 0;
    double lamC = 0;
    double lamR = 0;
    double eL = 0;
    double eC = 0;
    double eR = 0;

    // three point search
    for (int i = 0; i < numLam; i++) {
      double step = (lamRR - lamLL )/4.;
      if (step < MIN_LAMBDA_ERR) {
        break;
      }
      lamL = lamLL + step;
      LAMBDA = lamL;
      eL = calcErr();  log.info("eL=", eL);

      lamC = lamLL + 2 * step;
      LAMBDA = lamC;
      eC = calcErr();  log.info("eC=", eC);

      lamR = lamLL + 3 * step;
      LAMBDA = lamR;
      eR = calcErr();  log.info("eR=", eR);

//      double diff = Math.abs(eL-eC) + Math.abs(eC-eR);
//      if (diff < MIN_ERR) {
//        break;
//      }

      if (eLL >= eL  &&  eL >= eC  && eC >= eR  && eR >= eRR) {
        lamLL = lamR;
        eLL = eR;        log.info("new eLL=", eLL);
      } else if (eLL <= eL  &&  eL <= eC  && eC <= eR  && eR <= eRR) {
          lamRR = lamL;
          eRR = eL;        log.info("new eRR=", eLL);
      } else {
        if (eR < eRR  &&  eC < eR) {
          lamRR = lamR;
          eRR = eR;        log.info("new eRR=", eRR);
        }
        if (eLL > eL  &&  eL > eC) {
          lamLL = lamL;
          eLL = eL;        log.info("new eLL=", eLL);
        }
        if (eL > eC  &&  eC > eR) {
          lamLL = lamC;
          eLL = eC;        log.info("new eLL=", eLL);
        }
        if (eC < eR  &&  eL < eC) {
          lamRR = lamC;
          eRR = eC;        log.info("new eRR=", eRR);
        }
      }
      log.info("LL=", eLL);
      log.info("eL=", eL);
      log.info("eC=", eC);
      log.info("eR=", eR);
      log.info("RR=", eRR);
      log.info("laLL=", lamLL);
      log.info("lamL=", lamL);
      log.info("lamC=", lamC);
      log.info("lamR=", lamR);
      log.info("laRR=", lamRR);
    }
    log.info("Nc=", Nc);
    log.info("Nt=", Nt);
    int dbg2 = 1;

  }

  public double calcErr() {
//    return calcErrHeIon();
    return calcErrJm();
  }
  public double calcErrHeIon() {
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

    Vec trgtEngs = jmTrgt.getEngs();                        log.info("trgtEngs=", new VecDbgView(trgtEngs));
    log.info("E_SORTED=", new VecDbgView(new Vec(HeSWaveAtom.E_SORTED)));
//    double err = VecStats.rmse(HeSWaveAtom.E_SORTED, trgtEngs.getArr(), NUM_HE_LEVELS);   log.info("err=", err);
//    double err = VecStats.maxAbsErr(HeSWaveAtom.E_SORTED, trgtEngs.getArr(), NUM_HE_LEVELS);   log.info("err=", err);
    double err = VecStats.maxPosErr(trgtEngs.getArr(), HeSWaveAtom.E_SORTED, NUM_HE_LEVELS);   log.info("err=", err);
    return err;
  }
  public double calcErrJm() {
    initProject();
    // from jmPotTestOk
    StepGridModel sg = jmOpt.getGrid();           log.dbg("x step grid model =", sg);
    StepGrid x = new StepGrid(sg);                 log.dbg("x grid =", x);
    quadrLcr = new WFQuadrLcr(x);                  log.dbg("x weights =", quadrLcr);
    rVec = quadrLcr.getR();                        log.dbg("r grid =", rVec);
    basisOptN = jmOpt.getJmModel();                 log.dbg("Laguerr model =", basisOptN);

    // Nc-part
    JmLgrrModel lgrrOptNc = new JmLgrrModel(basisOptN); // for the target N, i.e. N_t
    lgrrOptNc.setN(Nc);                                    log.dbg("Laguerr model (N_c)=", lgrrOptNc);
    orthonNc = new JmLgrrOrthLcr(quadrLcr, lgrrOptNc);     log.dbg("JmLgrrOrthLcr(N_c) = ", orthonNc);

    // Nt-part
    basisOptN = new JmLgrrLabelMaker(basisOptN, Nt);    log.dbg("basisOptN =", basisOptN); // this is just for the file name label
    JmLgrrModel lgrrOptNt = new JmLgrrModel(basisOptN); // for the target N, i.e. N_t
    lgrrOptNt.setN(Nt);                             log.dbg("Laguerr model (N_t)=", lgrrOptNt);
    orthonNt = new JmLgrrOrthLcr(quadrLcr, lgrrOptNt); log.dbg("JmLgrrOrthLcr(N_t) = ", orthonNt);
    potFunc = new FuncPowInt(-AtomHe.Z, -1);  // f(r)=-1./r
    pot = new FuncVec(rVec, potFunc);                       log.dbg("-1/r=", new VecDbgView(pot));

    // making target  JM basis
    trgtBasisNt = orthonNt;
    SlaterLcr slater = new SlaterLcr(quadrLcr);
    JmTrgtE3 jmTrgt = makeTrgtBasisNt(slater, trgtBasisNt);

    Vec trgtEngs = jmTrgt.getEngs();                        log.info("trgtEngs=", trgtEngs);
    log.info("E_SORTED=", new Vec(HeSWaveAtom.E_SORTED));
//    double err = VecStats.rmse(HeSWaveAtom.E_SORTED, trgtEngs.getArr(), NUM_HE_LEVELS);   log.info("err=", err);
//    double err = VecStats.maxAbsErr(trgtEngs.getArr(), HeSWaveAtom.E_SORTED, NUM_HE_LEVELS);   log.info("err=", err);
    double err = VecStats.maxPosErr(trgtEngs.getArr(), HeSWaveAtom.E_SORTED, NUM_HE_LEVELS);   log.info("err=", err);
    return err;
  }


}