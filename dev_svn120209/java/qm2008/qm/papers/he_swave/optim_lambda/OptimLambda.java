package papers.he_swave.optim_lambda;
import atom.angular.Spin;
import atom.data.AtomHe;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.smodel.HeSWaveAtom;
import atom.smodel.HeSWaveAtomNt50_LMBD4p0;
import atom.wf.lcr.WFQuadrLcr;
import math.func.FuncVec;
import math.func.arr.FuncArrDbgView;
import math.func.simple.FuncPowInt;
import math.mtrx.Mtrx;
import math.vec.Vec;
import math.vec.VecDbgView;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridModel;
import papers.he_swave.HeSWaveScatt;
import project.workflow.task.test.FlowTest;
import qm_station.QMSProject;
import scatt.jm_2008.jm.laguerre.JmLgrrLabelMaker;
import scatt.jm_2008.jm.laguerre.LgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.jm_2008.jm.target.ScttTrgtE3;
import stats.VecStats;

import javax.iox.FileX;
import javax.utilx.log.Log;
import javax.utilx.pair.Dble2;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,19/08/11,8:45 AM
 */
public class OptimLambda extends HeSWaveScatt {
  public static Log log = Log.getLog(OptimLambda.class);
  public static final int FROM_IDX = 1; //
  public static final int NUM_HE_LEVELS = 5; //
  public static final double MIN_ERR = 1.e-8; //
  public static final double MIN_LAMBDA_ERR = 0.5e-6; //
  private static String TAG = "";
  private static int count = 0;
  private static final int IDX_NT = count++;
  private static final int IDX_ERR_1 = count++;
  private static final int IDX_ERR_2 = count++;
  private static final int IDX_ERR_3 = count++;
  private static final int IDX_ERR_BEST = count++;
  private static final int IDX_LAMBDA_BEST = count++;
  private static final int IDX_COUNT = count++;


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
  public void calc(int newN, int newNt) {
  }
  public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
    project = QMSProject.makeInstance("HeSWaveBasisHeIon", "110606");
    TARGET_Z = AtomHe.Z;
    HOME_DIR = "C:\\dev\\physics\\papers\\output";
    MODEL_NAME = "HeSModelOptimLamdba";
    MODEL_DIR = "HeSModelOptimLamdba";
    runJob2();
//    runJob();
  }

  public void runJob2() {
    FlowTest.setLog(log);
    SPIN = Spin.ELECTRON;

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
//    LCR_FIRST = -6;  //-5
//    LCR_N = 1001;  //901
//    R_LAST = 250;

//    // upto N=25
//    LCR_FIRST = -5;
//    LCR_N = 701;
//    R_LAST = 200;

    LCR_FIRST = -5;
    LCR_N = 701;
    R_LAST = 200;

    Nc = 5;
    int[] arrNt = {5, 6, 7, 8, 9, 10 };
//    int[] arrNt = {5, 6, 7, 8, 9, 10, 15, 20, 25};
//    int[] arrNt = {5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 35};
//    int[] arrNt = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
//    int[] arrNt = {15, 16, 17, 18, 19, 20};
//    int[] arrNt = {21, 22};

//    Nc = 7;
//    int[] arrNt = {7, 10, 15, 20, 25, 30};

//    Nc = 10;
//    int[] arrNt = {10, 15, 20, 25, 30};

    double LAMBDA_1 = 2;
    double LAMBDA_2 = 3;
    double LAMBDA_3 = 4;
    Mtrx mRes = new Mtrx(arrNt.length, IDX_COUNT);
    for (int i = 0; i < arrNt.length; i++) {
      Nt = arrNt[i];             log.info("Nt=", Nt);
      Nc = Nt;                   log.info("Nc=", Nc);
      mRes.set(i, IDX_NT, Nt);

      LAMBDA = LAMBDA_1;
      double err = calcErr();        log.info("err_1=", err);
      mRes.set(i, IDX_ERR_1, err);

      LAMBDA = LAMBDA_2;
      err = calcErr();        log.info("err_2=", err);
      mRes.set(i, IDX_ERR_2, err);

      LAMBDA = LAMBDA_3;
      err = calcErr();        log.info("err_3=", err);
      mRes.set(i, IDX_ERR_3, err);

      double lamLL = 1;
      double lamRR = 10;
      int numLam = 100;
      Dble2 bestRes = findBestLambda(lamLL, lamRR, numLam);
      mRes.set(i, IDX_ERR_BEST, bestRes.a);
      mRes.set(i, IDX_LAMBDA_BEST, bestRes.b);
    }
    FileX.writeToFile(mRes.toGnuplot(), HOME_DIR, MODEL_DIR
      , MODEL_NAME + "_Nc" + Nc + "_maxNt" + arrNt[arrNt.length-1]
      + "_nLev" +  NUM_HE_LEVELS
      + TAG + ".txt");
    FileX.writeToFile(mRes.toGnuplot(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_Nc" + Nc
      + "_nLev" +  NUM_HE_LEVELS
      + TAG + ".txt");
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
//    LCR_FIRST = -6;  //-5
//    LCR_N = 1001;  //901
//    R_LAST = 250;

    // upto N=25
    LCR_FIRST = -5;
    LCR_N = 701;
    R_LAST = 200;

    IGNORE_BUG_PoetHeAtom = true;
    SPIN = Spin.ELECTRON;

    double lamLL = 1;
    double lamRR = 10;
    int numLam = 100;
    FlowTest.setLog(log);
    Nc = 10;
    Nt = 20;
    findBestLambda(lamLL, lamRR, numLam);
  }

  public Dble2 findBestLambda(double lamLL, double lamRR, int numLam) {
    IGNORE_BUG_PoetHeAtom = true;
    SPIN = Spin.ELECTRON;

    Dble2 res = new Dble2();
    LAMBDA = lamLL;
    double eLL = calcErr();        log.info("eLL=", eLL);
    res.a = eLL; res.b = LAMBDA;

    LAMBDA = lamRR;
    double eRR = calcErr();        log.info("eRR=", eRR);
    if (res.a > eRR) {res.a = eRR; res.b = LAMBDA;}

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
      if (res.a > eL) {res.a = eL; res.b = LAMBDA;}

      lamC = lamLL + 2 * step;
      LAMBDA = lamC;
      eC = calcErr();  log.info("eC=", eC);
      if (res.a > eC) {res.a = eC; res.b = LAMBDA;}

      lamR = lamLL + 3 * step;
      LAMBDA = lamR;
      eR = calcErr();  log.info("eR=", eR);
      if (res.a > eR) {res.a = eR; res.b = LAMBDA;}

      if (eLL >= eL  &&  eL >= eC  && eC >= eR  && eR >= eRR) {
        lamLL = lamR;
        eLL = eR;        log.info("new eLL=", eLL);
      } else if (eLL <= eL  &&  eL <= eC  && eC <= eR  && eR <= eRR) {
          lamRR = lamL;
          eRR = eL;        log.info("new eRR=", eRR);
      } else if (eLL >= eL  &&  eL >= eC  && eC <= eR  && eR <= eRR) {
        lamLL = lamL;
        eLL = eL;        log.info("new eLL=", eLL);
        lamRR = lamR;
        eRR = eR;        log.info("new eRR=", eRR);
      } else {
        if (eC <= eR  &&  eR <= eRR) {
          lamRR = lamR;
          eRR = eR;        log.info("new eRR=", eRR);
        }
        if (eLL >= eL  &&  eL >= eC) {
          lamLL = lamL;
          eLL = eL;        log.info("new eLL=", eLL);
        }
        if (eL > eC  &&  eC > eR) {
          lamLL = lamC;
          eLL = eC;        log.info("new eLL=", eLL);
        }
        if (eL < eC  &&  eC < eR) {
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
    return res;
  }

  public double calcErr() {
//    return calcErrHeIon();
    return calcErrJm();
  }
  public double calcErrHeIon() {
    TAG = "_HeIon";
    initProject();
    // from potScattTestOk
    StepGridModel sg = calcOpt.getGrid();           log.dbg("x step grid model =", sg);
    StepGrid x = new StepGrid(sg);                 log.dbg("x grid =", x);
    quadr = new WFQuadrLcr(x);                  log.dbg("x weights =", quadr);
    rVec = quadr.getR();                        log.dbg("r grid =", rVec);
    lgrrOptN = calcOpt.getLgrrModel();                 log.dbg("Laguerr model =", lgrrOptN);

    // from hydrScattTestOk
    lgrrOptN = new JmLgrrLabelMaker(lgrrOptN, Nt);    log.dbg("lgrrOptN =", lgrrOptN); // this is just for the file name label
    LgrrModel lgrrOptNt = new LgrrModel(lgrrOptN); // for the target N, i.e. N_t
    lgrrOptNt.setN(Nt);                             log.dbg("Laguerr model (N_t)=", lgrrOptNt);
    orthNt = new LgrrOrthLcr(quadr, lgrrOptNt); log.dbg("LgrrOrthLcr(N_t) = ", orthNt);
    potFunc = new FuncPowInt(-AtomHe.Z, -1);  // f(r)=-1./r
    pot = new FuncVec(rVec, potFunc);                       log.dbg("-1/r=", new VecDbgView(pot));

    // Making He+ eigen-states
    trgtPotH = new PotHMtrxLcr(L, orthNt, pot);       //log.dbg("trgtPotH=", trgtPotH);
    Vec basisEngs = trgtPotH.getEigEngs();                log.dbg("basisEngs=", new VecDbgView(basisEngs));
    trgtWfsNt = trgtPotH.getEigWfs();              log.dbg("targetNt=", new FuncArrDbgView(trgtWfsNt));
    SlaterLcr slater = new SlaterLcr(quadr);
    ScttTrgtE3 jmTrgt = makeTrgtBasisNt(slater, trgtWfsNt);

    Vec trgtEngs = jmTrgt.getEngs();                        log.info("trgtEngs=", trgtEngs);
    log.info("E_SORTED=", new Vec(HeSWaveAtom.E_SORTED));
    double err = VecStats.maxPosErr(trgtEngs.getArr(), HeSWaveAtom.E_SORTED, FROM_IDX, NUM_HE_LEVELS);   log.info("err=", err);
    return err;
  }
  public double calcErrJm() {
    TAG = "_JM";
    initProject();
    // from potScattTestOk
    StepGridModel sg = calcOpt.getGrid();           log.dbg("x step grid model =", sg);
    StepGrid x = new StepGrid(sg);                 log.dbg("x grid =", x);
    quadr = new WFQuadrLcr(x);                  log.dbg("x weights =", quadr);
    rVec = quadr.getR();                        log.dbg("r grid =", rVec);
    lgrrOptN = calcOpt.getLgrrModel();                 log.dbg("Laguerr model =", lgrrOptN);

    // Nt-part
    lgrrOptN = new JmLgrrLabelMaker(lgrrOptN, Nt);    log.dbg("lgrrOptN =", lgrrOptN); // this is just for the file name label
    LgrrModel lgrrOptNt = new LgrrModel(lgrrOptN); // for the target N, i.e. N_t
    lgrrOptNt.setN(Nt);                             log.dbg("Laguerr model (N_t)=", lgrrOptNt);
    orthNt = new LgrrOrthLcr(quadr, lgrrOptNt); log.dbg("LgrrOrthLcr(N_t) = ", orthNt);
    potFunc = new FuncPowInt(-AtomHe.Z, -1);  // f(r)=-1./r
    pot = new FuncVec(rVec, potFunc);                       log.dbg("-1/r=", new VecDbgView(pot));

    // making target  JM basis
    trgtWfsNt = orthNt;
    SlaterLcr slater = new SlaterLcr(quadr);
    ScttTrgtE3 jmTrgt = makeTrgtBasisNt(slater, trgtWfsNt);

    Vec trgtEngs = jmTrgt.getEngs();                        log.info("trgtEngs=", trgtEngs);
    log.info("E_SORTED=", new Vec(HeSWaveAtom.E_SORTED));
    double err = VecStats.maxPosErr(trgtEngs.getArr(), HeSWaveAtomNt50_LMBD4p0.E_SORTED, FROM_IDX, NUM_HE_LEVELS);   log.info("err=", err);
//    double err = VecStats.maxPosErr(trgtEngs.getArr(), HeSWaveAtom.E_SORTED, NUM_HE_LEVELS);   log.info("err=", err);
    return err;
  }


}