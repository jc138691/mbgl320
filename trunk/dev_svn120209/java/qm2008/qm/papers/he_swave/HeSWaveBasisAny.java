package papers.he_swave;
import atom.data.AtomHe;
import atom.data.AtomHy;
import atom.energy.LsConfHMtrx;
import atom.energy.pw.lcr.PotHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.wf.lcr.LcrFactory;
import math.func.arr.FuncArr;
import math.integral.OrthFactory;
import math.mtrx.MtrxDbgView;
import math.mtrx.api.Mtrx;
import math.vec.Vec;
import math.vec.VecDbgView;
import qm_station.QMSProject;
import scatt.jm_2008.e3.JmMthdBasisAnyE3;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.jm_2008.jm.target.ScttTrgtE3;
import scatt.jm_2008.jm.theory.JmD;

import javax.iox.FileX;
import javax.iox.MtrxReader;
import javax.utilx.log.Log;
import java.io.File;
/**
* dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,12/05/11,2:35 PM
*/
public class HeSWaveBasisAny extends HeSWaveScatt {
public static Log log = Log.getLog(HeSWaveBasisAny.class);
public static void main(String[] args) {
  // NOTE!!! for Nt>20 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  HeSWaveBasisAny runMe = new HeSWaveBasisAny();
  runMe.setUp();
  runMe.testRun();
}
public void setUp() {
  super.setUp();
  log.info("log.info(HeSWaveBasisAny)");
  HeSWaveBasisAny.log.setDbg();
  log.setDbg();
}
public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  MODEL_NAME = "HeSWaveBasisHeIon";
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

public void testCalcAvr() {
  MODEL_NAME = "HeSWaveBasisHeIon";
  project = QMSProject.makeInstance(MODEL_NAME, "110606");
  TARGET_Z = AtomHe.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_DIR = MODEL_NAME;

  int MIN_NT = 25;
  int MAX_NT = 50;
  calcAvr("HeSWaveBasisHeIon_TICS_Nc1_L0_LMBD1.0_N101_Nt", MIN_NT, MAX_NT);
  calcAvr("HeSWaveBasisHeIon_TCS_Nc1_L0_LMBD1.0_N101_Nt", MIN_NT, MAX_NT);

//  int MIN_NT = 25;
//  int MAX_NT = 35;
//  calcAvr("HeSWaveBasisHeIon_TICS_Nc3_L0_LMBD1.0_N101_Nt", MIN_NT, MAX_NT);
//  calcAvr("HeSWaveBasisHeIon_TCS_Nc3_L0_LMBD1.0_N101_Nt", MIN_NT, MAX_NT);
}

public void calcAvr(String name, int minNt, int maxNt) {
  double[][] res = null;
  Mtrx mRes = null;
  int count = 0;
  int NUM_COLS = 10;
  for (int currNt = minNt; currNt <= maxNt; currNt++) {
    count++;       log.dbg("currNt=", currNt);
    String fileName = HOME_DIR + File.separator + MODEL_DIR + File.separator + name + currNt + ".dat";
    double[][] arr = new MtrxReader("\t").readMtrx(-1, NUM_COLS, -1, fileName);
    Mtrx mArr = new Mtrx(arr);         //log.dbg("mArr=\n", new MtrxDbgView(mArr));
    if (res == null) {
      res = arr;
      mRes = mArr;
      continue;
    }
    mRes.addEquals(mArr);      //log.dbg("mRes=\n", new MtrxDbgView(mRes));
  }
  mRes.multEquals(1./count);   log.dbg("mRes=\n", new MtrxDbgView(mRes));
  FileX.writeToFile(mRes.toTab(), HOME_DIR, MODEL_DIR, name + minNt + "_avr_" + maxNt + ".dat");
}


public void calc(int newN, int newNt) {
  System.gc();
  N = newN;
  Nt = newNt;
  initProject();
  potScattTestOk();     // out: lgrrN, orthNt, lgrrBi
  hydrScattTestOk(AtomHy.Z);      // out: pt (for Hy), orthNt
  hydrScattTestOk(AtomHe.Z);      // out: pt (for Hy), orthNt
  jmHeTestOk();      // out: re-loading pot (for He)
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

  // start tmp DEV ==============================
  ////    log.dbg("LgrrOrthLcr(N_c) = ", orthNc);
  OrthFactory.keepN(orthNc, quadr, orthNt); // span Nc-basis on Nt-basis
  //    log.dbg("LgrrOrthLcr(N_c) = ", orthNc);
  OrthFactory.norm(orthNc, quadr);
  orthNt.copyDeepFromY(0, orthNc, 0, orthNc.size()); // replace
  OrthFactory.makeOrthGram(orthNt, quadr);
  trgtWfsNt = orthNt;
  // end tmp DEV==============================

//    // Making He+ eigen-states
//    trgtPotH = new PotHMtrxLcr(L, orthNt, pot);       log.dbg("trgtPotH=", trgtPotH);
//    Vec basisEngs = trgtPotH.getEigEngs();                log.dbg("eigVal=", new VecDbgView(basisEngs));
//    Mtrx basisVecs = trgtPotH.getEigVec();               log.dbg("eigVec=", new MtrxDbgView(basisVecs));
//    FileX.writeToFile(basisEngs.toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_basisEngs_" + makeLabelNc());
//    trgtWfsNt = trgtPotH.getEigWfs();              log.dbg("targetNt=", new FuncArrDbgView(trgtWfsNt));
//    FuncArr basisR = LcrFactory.wfLcrToR(trgtWfsNt, quadr);
//    AtomUtil.trimTailSLOW(basisR);
//    FileX.writeToFile(basisR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgtBasisNtR_" + makeLabelNc());

// TODO: check how Vec.size() is used
//    AtomUtil.trimTailSLOW(trgtWfsNt);
  wfN = orthN;    // only the last wfs were used from  orthNt, so now we can reuse it
  orthN = null; // making sure nobody uses old ref
  wfN.copyFrom(0, trgtWfsNt, 0, trgtWfsNt.size());

  ScttTrgtE3 jmTrgt = makeTrgtBasisNt(slater, trgtWfsNt);
  jmTrgt.setInitTrgtIdx(FROM_CH);
  jmTrgt.setIonGrndEng(basisEngs.getFirst());
  jmTrgt.removeClosed(calcOpt.getGridEng().getLast(), FROM_CH, KEEP_CLOSED_N);
  jmTrgt.setNt(trgtWfsNt.size());
//    jmTrgt.replaceTrgtEngs_DONOT_USE(HeSWaveAtomNt50_LMBD4p0.E_SORTED, REPLACE_TRGT_ENGS_N);   log.info("REPLACING trgt engs with=", HeSWaveAtomNt50_LMBD4p0.E_SORTED);
  jmTrgt.loadSdcsW();
  saveTrgtInfo(jmTrgt);

  LsConfHMtrx sysH = makeSysBasisN(slater);

  JmMthdBasisAnyE3 mthd = new JmMthdBasisAnyE3(calcOpt);
  mthd.setTrgtE3(jmTrgt);
  Vec sEngs = sysH.getEigVal(H_OVERWRITE);                               log.dbg("sysConfH=", sEngs);
  mthd.setSysEngs(sEngs);
  mthd.setSysConfH(sysH);
  Vec D = new JmD(lgrrBiN, wfN);             log.dbg("D_{n,N-1}=", D);
  mthd.setOverD(D);

  if (CALC_DENSITY) {          log.info("if (CALC_DENSITY) {");
    FuncArr sysDens = sysH.getDensity(CALC_DENSITY_MAX_NUM);
    FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadr);  // NOTE!! converting density to R (not wf)
    FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_sysDensityR_" + makeLabelNc(mthd));
  }

  ScttRes res;
  if (scttEngs != null) {
    res = mthd.calc(scttEngs);                  log.dbg("res=", res);
  }
  else {
//      res = method.calcScttEngModel();                  log.dbg("res=", res);
    res = mthd.calcAutoScttEngs(AUTO_ENG_POINTS);                  log.dbg("res=", res);
  }
  setupScattRes(res, mthd);

//    JmResonE2_bad.saveResRadDist(RES_MAX_LEVEL, res, sysConfH);
  res.writeToFiles();

}

}