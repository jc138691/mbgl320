package papers.he_swave;
import atom.data.AtomHe;
import atom.data.AtomHy;
import atom.energy.LsConfHMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.wf.lcr.LcrFactory;
import math.func.arr.FuncArr;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
//import scatt.jm_2008.e2.JmResonE2_bad;
import scatt.jm_2008.e3.JmDe3;
import scatt.jm_2008.e3.JmMthdBasisJmE3;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.jm_2008.jm.target.ScttTrgtE3;

import javax.iox.FileX;
import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,5/05/11,11:30 AM
 */
public class HeSWaveBasisJm extends HeSWaveScatt {
  public static Log log = Log.getLog(HeSWaveBasisJm.class);
  public static void main(String[] args) {
    // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
    HeSWaveBasisJm runMe = new HeSWaveBasisJm();
    runMe.setUp();
    runMe.testRun();
  }

  public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
    MODEL_NAME = "HeSWaveBasisJm";
    project = QMSProject.makeInstance(MODEL_NAME, "110606");
    TARGET_Z = AtomHe.Z;
    HOME_DIR = "C:\\dev\\physics\\papers\\output";
    MODEL_DIR = MODEL_NAME;

    CALC_DENSITY = false;
    CALC_DENSITY_MAX_NUM = 2;
    SAVE_TRGT_ENGS = true;
    H_OVERWRITE = true;

    // Note: run one at a time as only one set of result files is produced
    runJob();
  }

  public void setUp() {
    super.setUp();
    log.info("log.info(HeSWaveBasisJm)");
//    JmResonE2_bad.log.setDbg();
    log.setDbg();
  }


  public void calc(int newN, int newNt) {
    log.info("-->calc(newN"+newN+", newNt="+newNt);
    N = newN;
    Nt = newNt;
    initProject();
    potScattTestOk();     // out: lgrrN, orthNt, lgrrBi
    hydrScattTestOk(AtomHy.Z);      // out: pt (for Hy), orthNt
    hydrScattTestOk(AtomHe.Z);      // out: pt (for He), orthNt
    jmHeTestOk();      // out: re-loading pt (for He)
    initLiJm();
    SlaterLcr slater = new SlaterLcr(quadr);
    calcHe(slater);    //verified: SysHeOldOk and SysHe yield exactly the same results.
    calcLi(slater);

    // Making He+ eigen-states from Nc (core N).   this is only to calc ionization threshold
    LgrrOpt lgrrOpt = new LgrrOpt(lgrrOptN); // for the target N, i.e. N_t
    lgrrOpt.setN(Nc);
    lgrrOpt.setLambda(LAMBDA_NC);                    log.dbg("Laguerr model (N_c)=", lgrrOpt);
    orthNc = new LgrrOrthLcr(quadr, lgrrOpt);     log.dbg("LgrrOrthLcr(N_c) = ", orthNc);
    trgtPotH = new PotHMtrxLcr(L, orthNc, pot);        log.dbg("trgtPotH=", trgtPotH);
    Vec basisEngs = trgtPotH.getEigEngs();                 log.dbg("eigVal=", new VecDbgView(basisEngs));
    FileX.writeToFile(basisEngs.toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_NcEngs_" + makeLabelNc());

    trgtWfsNt = orthNt;
    wfN = orthN;
    ScttTrgtE3 jmTrgt = makeTrgtBasisNt(slater, trgtWfsNt);
    jmTrgt.setInitTrgtIdx(FROM_CH);
    jmTrgt.setIonGrndEng(basisEngs.getFirst());
    jmTrgt.removeClosed(calcOpt.getGridEng().getLast(), FROM_CH, KEEP_CLOSED_N);
    jmTrgt.setNt(trgtWfsNt.size());
//    jmTrgt.replaceTrgtEngs_DONOT_USE(HeSWaveAtomNt50_LMBD4p0.E_SORTED);   log.info("REPLACING trgt engs with=", HeSWaveAtomNt50_LMBD4p0.E_SORTED);
    jmTrgt.loadSdcsW();
    saveTrgtInfo(jmTrgt);

    LsConfHMtrx sysH = makeSysBasisN(slater);

    JmMthdBasisJmE3 method = new JmMthdBasisJmE3(calcOpt);
    method.setTrgtE3(jmTrgt);
    Vec sEngs = sysH.getEigVal(H_OVERWRITE);                               log.dbg("sysConfH=", sEngs);
    method.setSysEngs(sEngs);
    method.setSysConfH(sysH);
    Vec D = new JmDe3(lgrrBiN, orthN, method.getCalcOpt().getTestOpt());   log.dbg("D_{i<Nt}=must be ZERO=", D); // MUST BE ALL ZERO!!!!!
    method.setOverD(D);

    if (CALC_DENSITY) {          log.info("if (CALC_DENSITY) {");
      FuncArr sysDens = sysH.getDensity(CALC_DENSITY_MAX_NUM);
      FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadr);  // NOTE!! convering density to R (not wf)
      FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_sysDensityR_" + makeLabelNc(method));
    }

    ScttRes res;
    if (scttEngs != null) {
//      res = method.calc(scttEngs);                  log.dbg("res=", res);
      res = null; // DBG
    }
    else {
//      res = method.calcScttEngModel();                  log.dbg("res=", res);
      res = method.calcAutoScttEngs(AUTO_ENG_POINTS);                  log.dbg("res=", res);
    }
    setupScattRes(res, method);

//    JmResonE2_bad.saveResRadDist(RES_MAX_LEVEL, res, sysConfH);
    res.writeToFiles();
  }

}