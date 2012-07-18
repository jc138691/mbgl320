package papers.he_swave;
import atom.AtomUtil;
import atom.angular.Spin;
import atom.data.AtomHe;
import atom.data.AtomHy;
import atom.energy.LsConfHMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.wf.lcr.LcrFactory;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.mtrx.api.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import papers.he_swave.pra_2012_setup.HeSWaveRes2012;
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
    MODEL_NAME = "HeSModelBasisHeIon";
    project = QMSProject.makeInstance(MODEL_NAME, "110606");
    TARGET_Z = AtomHe.Z;
    HOME_DIR = "C:\\dev\\physics\\papers\\output";
    MODEL_DIR = MODEL_NAME;

    CALC_DENSITY = false;
    CALC_DENSITY_MAX_NUM = 2;
    SAVE_TRGT_ENGS = true;
    H_OVERWRITE = true;

    LAMBDA = 2; // exact LAMBDA[He^+(1s)] = 4, LAMBDA[He^+(2s)] = 2;

    // Note: run one at a time as only one set of result files is produced
//    setupEngAu_3();
//    setupEngAu_4();
    setupEng01_1000eV_SLOW();
//    HeSWaveRes2012.setupResEngs_SLOW();
//    setupEng1_100eV_SLOW();
//    setupEng01_1000eV_FAST();
//    setupEng10_30eV();
//    setupEngResonance_2S();
//    setupEngTICS();
//    setupEngSDCS();
    runJob();
  }

  public void runJob() {
    int currN = 10;
    LCR_FIRST = -5. - 2. * Math.log(TARGET_Z);   log.dbg("LCR_FIRST=", LCR_FIRST);
//    LCR_N = 2001;//    N= 80
//    R_LAST = 450;//    N= 80
//    LCR_N = 2001;//    N= 70
//    R_LAST = 400;//    N= 70
//    LCR_N = 2001;//    N= 60
//    R_LAST = 400;//    N= 60
//    currN = 50;
//    LCR_N = 1001;//    N= 50
//    R_LAST = 250;//    N= 50
    currN = 40;
    LCR_N = 801;//    N= 40
    R_LAST = 200;//    N= 40

    Nc = 1;
    int currNt = 20;
//    int currN = currNt + 1;

    SPIN = Spin.ELECTRON;
    calc(currN, currNt);
//    calc(14, 13);
  }

  public void calc(int newN, int newNt) {
    N = newN;
    Nt = newNt;
    initProject();
    potScattTestOk();     // out: lgrrN, orthNt, lgrrBi
    hydrScattTestOk(AtomHy.Z);      // out: pt (for Hy), orthNt
    hydrScattTestOk(AtomHe.Z);      // out: pt (for Hy), orthNt
    jmHeTestOk();      // out: re-loading pt (for He)
    initLiJm();
    SlaterLcr slater = new SlaterLcr(quadr);
    calcHe(slater);    //verified: SysHeOldOk and SysHe yield exactly the same results.
    calcLi(slater);

    // Making He+ eigen-states
    trgtPotH = new PotHMtrxLcr(L, orthNt, pot);       log.dbg("trgtPotH=", trgtPotH);
    Vec basisEngs = trgtPotH.getEigEngs();                log.dbg("eigVal=", new VecDbgView(basisEngs));
    Mtrx basisVecs = trgtPotH.getEigVec();               log.dbg("eigVec=", new MtrxDbgView(basisVecs));
    FileX.writeToFile(basisEngs.toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_basisEngs_" + makeLabelNc());
    trgtWfsNt = trgtPotH.getEigWfs();              log.dbg("targetNt=", new FuncArrDbgView(trgtWfsNt));

    FuncArr basisR = LcrFactory.wfLcrToR(trgtWfsNt, quadr);
    AtomUtil.trimTailSLOW(basisR);
    FileX.writeToFile(basisR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgtBasisNtR_" + makeLabelNc());

// TODO: check how Vec.size() is used
//    AtomUtil.trimTailSLOW(trgtWfsNt);

    wfN = orthN;    // only the last wfs were used from  orthNt, so now we can reuse it
    orthN = null; // making sure nobody uses old ref
    wfN.copyFrom(trgtWfsNt, 0, trgtWfsNt.size());

    ScttTrgtE3 jmTrgt = makeTrgtBasisNt(slater, trgtWfsNt);
    jmTrgt.setInitTrgtIdx(FROM_CH);
    jmTrgt.setIonGrndEng(basisEngs.getFirst());
    jmTrgt.removeClosed(calcOpt.getGridEng().getLast(), FROM_CH, KEEP_CLOSED_N);
    jmTrgt.setNt(trgtWfsNt.size());
    jmTrgt.loadSdcsW();
    saveTrgtInfo(jmTrgt);

    LsConfHMtrx sysH = makeSysBasisN(slater);

    JmMethodAnyBasisE3 method = new JmMethodAnyBasisE3(calcOpt);
    method.setTrgtE3(jmTrgt);
    Vec sEngs = sysH.getEigVal(H_OVERWRITE);                               log.dbg("sysConfH=", sEngs);
    method.setSysEngs(sEngs);
    method.setSysConfH(sysH);
    Vec D = new JmD(lgrrBiN, wfN);             log.dbg("D_{n,N-1}=", D);
    method.setOverD(D);

    if (CALC_DENSITY) {          log.info("if (CALC_DENSITY) {");
      FuncArr sysDens = sysH.getDensity(CALC_DENSITY_MAX_NUM);
      FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadr);  // NOTE!! convering density to R (not wf)
      FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_sysDensityR_" + makeLabelNc(method));
    }

    ScttRes res;
    if (scttEngs != null) {
      res = method.calc(scttEngs);                  log.dbg("res=", res);
    }
    else {
      res = method.calcForScttEngModel();                  log.dbg("res=", res);
    }
    setupScattRes(res, method);

//    JmResonE2_bad.saveResRadDist(RES_MAX_LEVEL, res, sysConfH);
    res.writeToFiles();
  }

}