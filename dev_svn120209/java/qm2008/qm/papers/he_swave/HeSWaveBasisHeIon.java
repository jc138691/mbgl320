package papers.he_swave;
import atom.AtomUtil;
import atom.angular.Spin;
import atom.data.AtomHe;
import atom.data.AtomHy;
import atom.energy.ConfHMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.wf.log_cr.LcrFactory;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
import scatt.jm_2008.e3.JmMethodAnyBasisE3;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.target.ScttTrgtE3;
import scatt.jm_2008.jm.theory.JmD;

import javax.iox.FileX;
import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,12/05/11,2:35 PM
 */
public class HeSWaveBasisHeIon extends HeSWaveScatt {
  public static Log log = Log.getLog(HeSWaveBasisHeIon.class);
  public static void main(String[] args) {
    // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
    HeSWaveBasisHeIon runMe = new HeSWaveBasisHeIon();
    runMe.setUp();
    runMe.testRun();
  }
  public void setUp() {
    super.setUp();
    log.info("log.info(HeSWaveBasisHeIon)");
    HeSWaveBasisHeIon.log.setDbg();
    log.setDbg();
  }
  public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
    project = QMSProject.makeInstance("HeSWaveBasisHeIon", "110606");
    TARGET_Z = AtomHe.Z;
    HOME_DIR = "C:\\dev\\physics\\papers\\output";
    MODEL_NAME = "HeSModelBasisHeIon";
    MODEL_DIR = "HeSModelBasisHeIon";
    LAMBDA = 2; // exact LAMBDA[He^+(1s)] = 4, LAMBDA[He^+(2s)] = 2;

    // Note: run one at a time as only one set of result files is produced
//    setupEngAu_3();
//    setupEngAu_4();
//    setupEng01_1000eV_SLOW();
//    setupEng1_100eV_SLOW();
//    setupEng01_1000eV_FAST();
//    setupEng10_30eV();
    setupEngResonance_2S();
//    setupEngTICS();
//    setupEngSDCS();
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

    Nc = 2;
    int currNt = 20;
    int currN = 21;
//    int currN = currNt + 1;
    IGNORE_BUG_PoetHeAtom = true;

    SPIN = Spin.ELECTRON;
    calc(currN, currNt);
//    calc(12, 11);
//    calc(13, 12);
//    calc(14, 13);
  }

  public void calc(int newN, int newNt) {
    N = newN;
    Nt = newNt;
    initProject();
    potScattTestOk();     // out: basisN, orthonNt, biorthN
    hydrScattTestOk(AtomHy.Z);      // out: pot (for Hy), orthonNt
    hydrScattTestOk(AtomHe.Z);      // out: pot (for Hy), orthonNt
    jmHeTestOk();      // out: re-loading pot (for He)
    initLiJm();
    SlaterLcr slater = new SlaterLcr(quadrLcr);
    calcHe(slater);    //verified: SysHe_OLD and SysHe yield exactly the same results.
    calcLi(slater);

    // Making He+ eigen-states
    trgtPotH = new PotHMtrxLcr(L, orthonNt, pot);       log.dbg("trgtPotH=", trgtPotH);
    Vec basisEngs = trgtPotH.getEigVal();                log.dbg("eigVal=", new VecDbgView(basisEngs));
    Mtrx basisVecs = trgtPotH.getEigVec();               log.dbg("eigVec=", new MtrxDbgView(basisVecs));
    FileX.writeToFile(basisEngs.toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_basisEngs_" + makeLabelNc());
    trgtStatesNt = trgtPotH.getEigFuncArr();              log.dbg("targetNt=", new FuncArrDbgView(trgtStatesNt));

    FuncArr basisR = LcrFactory.wfLcrToR(trgtStatesNt, quadrLcr);
    AtomUtil.trimTailSLOW(basisR);
    FileX.writeToFile(basisR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgtBasisNtR_" + makeLabelNc());

// TODO: check how Vec.size() is used
//    AtomUtil.trimTailSLOW(trgtStatesNt);

    trgtBasisN = orthonN;    // only the last wfs were used from  orthonNt, so now we can reuse it
    orthonN = null; // making sure nobody uses old ref
    trgtBasisN.copyFrom(trgtStatesNt, 0, trgtStatesNt.size());

    ScttTrgtE3 jmTrgt = makeTrgtBasisNt(slater, trgtStatesNt);
    jmTrgt.setInitTrgtIdx(FROM_CH);
    jmTrgt.setIonGrndEng(basisEngs.getFirst());
    jmTrgt.removeClosed(calcOpt.getGridEng().getLast(), FROM_CH, KEEP_CLOSED_N);
    jmTrgt.setNt(trgtStatesNt.size());
    jmTrgt.loadSdcsW();
    saveTrgtInfo(jmTrgt);

    ConfHMtrx sysH = makeSysBasisN(slater);

    JmMethodAnyBasisE3 method = new JmMethodAnyBasisE3(calcOpt);
    method.setTrgtE3(jmTrgt);
    Vec sEngs = sysH.getEigVal(H_OVERWRITE);                               log.dbg("sysConfH=", sEngs);
    method.setSysEngs(sEngs);
    method.setSysConfH(sysH);
    Vec D = new JmD(biorthN, trgtBasisN);             log.dbg("D_{n,N-1}=", D);
    method.setOverD(D);

    if (CALC_DENSITY) {          log.info("if (CALC_DENSITY) {");
      FuncArr sysDens = sysH.getDensity(CALC_DENSITY_MAX_NUM);
      FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadrLcr);  // NOTE!! convering density to R (not wf)
      FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_sysDensityR_" + makeLabelNc(method));
    }

    ScttRes res;
    if (scttEngs != null) {
      res = method.calc(scttEngs);                  log.dbg("res=", res);
    }
    else {
      res = method.calcForScatEngModel();                  log.dbg("res=", res);
    }
    setupScattRes(res, method);

//    JmResonE2.saveResRadDist(RES_MAX_LEVEL, res, sysConfH);
    res.writeToFiles();
  }

}