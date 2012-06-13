package papers.he_swave;
import atom.angular.Spin;
import atom.angular.test.Wign3jTest;
import atom.angular.test.Wign6jTest;
import atom.data.*;
import atom.e_2.SysE2;
import atom.e_2.SysE2OldOk;
import atom.e_2.SysHe;
import atom.e_2.SysHeOldOk;
import atom.e_2.test.HeClementiTest;
import atom.e_2.test.HeClementiZetaTest;
import atom.e_3.AtomShModelE3;
import atom.e_3.SysE3;
import atom.e_3.SysLi;
import atom.e_3.test.LiSlaterTest;
import atom.energy.LsConfHMtrx;
import atom.energy.slater.SlaterLcr;
import atom.shell.LsConfs;
import atom.smodel.HeSWaveAtom;
import atom.shell.ConfArrFactoryE2;
import atom.shell.ConfArrFactoryE3;
import atom.shell.Ls;
import atom.wf.coulomb.WfFactory;
import atom.wf.lcr.LcrFactory;
import atom.wf.slater.SlaterWFFactory;
import math.func.FuncVec;
import math.func.arr.FuncArr;
import math.func.simple.FuncPowInt;
import math.mtrx.Mtrx;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import papers.hy_swave.HyLikeSWave;
import papers.hy_swave.Jm2010Common;
import project.workflow.task.test.FlowTest;
import qm_station.jm.PotEigVecLcrTest;
import scatt.eng.EngGridFactory;
import scatt.eng.EngModel;
import scatt.eng.EngModelArr;
import scatt.jm_2008.e2.JmMthdBaseE2;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.laguerre.LgrrOpt;
import scatt.jm_2008.jm.laguerre.lcr.LgrrOrthLcr;
import scatt.jm_2008.jm.target.ScttTrgtE3;

import javax.iox.FileX;
import javax.utilx.log.Log;
/**
* dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,8/06/11,12:49 PM
*/
abstract public class HeSWaveScatt  extends HyLikeSWave {
public static Log log = Log.getLog(HeSWaveScatt.class);
protected static LgrrOrthLcr orthonNc;  // for N_c
protected static int Nc = 1;


public void setupEngResonance_2S() {
  ENG_FIRST = (float) AtomUnits.fromEV(18.5);
  ENG_LAST = (float)AtomUnits.fromEV(19.5);
  ENG_N = 11;
}


public void setUp() {
  super.setUp();
  log.info("log.info(HeSWaveScatt)");
//    JmResonE2.log.setDbg();
//    log.setDbg();
}

protected void saveTrgtInfo(ScttTrgtE3 jmTrgt) {
  double ionGrnd = jmTrgt.getIonGrndEng();
  int S1 = 0;
  Vec ionEngs = jmTrgt.getArrH().get(S1).getEigEngs().copy();
  ionEngs.add(-ionGrnd);
  ionEngs.calc(AtomUnits.funcToEV);
  FileX.writeToFile(ionEngs.toCSV(), HOME_DIR, MODEL_DIR
    , MODEL_NAME+"_thisTrgEngs_S1_ion_eV_" + makeLabelNc());  // THIS target energies, not the true S-wave energies

  int S3 = 1;
  ionEngs = jmTrgt.getArrH().get(S3).getEigEngs().copy();
  ionEngs.add(-ionGrnd);
  ionEngs.calc(AtomUnits.funcToEV);
  FileX.writeToFile(ionEngs.toCSV(), HOME_DIR, MODEL_DIR
    , MODEL_NAME+"_thisTrgEngs_S3_ion_eV_" + makeLabelNc());  // THIS target energies, not the true S-wave energies
}
public void setupScattRes(ScttRes res, JmMthdBaseE2 method) {
  super.setupScattRes(res, method);
  res.setCalcLabel(makeLabelNc(method));
}

protected static String makeLabelNc() {
  return "Nc" + Nc + "_" + Jm2010Common.makeLabelBasisOptN();
}
protected static String makeLabelNc(JmMthdBaseE2 method) {
  return "Nc" + Nc + "_" + Jm2010Common.makeLabelBasisOptOpen(method);
}

protected ScttTrgtE3 makeTrgtBasisNt(SlaterLcr slater, FuncArr basisNt) {
  log.info("-->makeTrgtBasisNt(SlaterLcr slater, FuncArr basisNt)");
  SysE2 tgrtE2 = new SysHe(slater);// NOTE -2 for Helium       // USES equations from the 2011 e-He paper

  Ls tLs = new Ls(0, Spin.SINGLET);  // t - for target
  LsConfs tConfArr = ConfArrFactoryE2.makeSModelE2(tLs, basisNt, Nc);    log.dbg("tConfArr=", tConfArr);
  LsConfHMtrx tH = new LsConfHMtrx(tConfArr, tgrtE2);                          log.dbg("tH=\n", new MtrxDbgView(tH));
  Mtrx tVecs = tH.getEigVec();                                             log.dbg("tH.getEigVec=", new MtrxDbgView(tVecs));
  if (SAVE_TRGT_ENGS)  {
    FileX.writeToFile(tH.getEigEngs().toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME+"_trgEngs_S1_" + makeLabelNc());
  }
  if (CALC_DENSITY) {
    FuncArr sysDens = tH.getDensity(CALC_DENSITY_MAX_NUM);
    FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadr);  // NOTE!! convering density to R (not wf)
    FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgtDensityR_S1_" + makeLabelNc());
  }

  tLs = new Ls(0, Spin.TRIPLET);  // t - for target
  tConfArr = ConfArrFactoryE2.makeSModelE2(tLs, basisNt, Nc);            log.dbg("tConfArr=", tConfArr);
  LsConfHMtrx tH2 = new LsConfHMtrx(tConfArr, tgrtE2);                         log.dbg("tH=\n", new MtrxDbgView(tH2));
  if (SAVE_TRGT_ENGS)  {
    FileX.writeToFile(tH2.getEigEngs().toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME+"_trgEngs_S3_" + makeLabelNc());
  }
  if (CALC_DENSITY) {
    FuncArr sysDens = tH2.getDensity(CALC_DENSITY_MAX_NUM);
    FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadr);  // NOTE!! convering density to R (not wf)
    FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgtDensityR_S3_" + makeLabelNc());
  }

  ScttTrgtE3 res = new ScttTrgtE3();
  res.add(tH);
  res.add(tH2);
  res.makeReady();
  log.info("<--makeTrgtBasisNt");
  return res;
}


protected LsConfHMtrx makeSysBasisN(SlaterLcr slater) {
  log.info("-->makeSysBasisN(SlaterLcr slater)");
  SYS_LS = new Ls(0, Spin.ELECTRON);     // s - for system
  SysE3 sysE3 = new SysE3(AtomHe.Z, slater);    // NOTE!!! Helium (AtomHe.atomZ), not Li (AtomLi.atomZ)
  AtomShModelE3 modelE3 = new AtomShModelE3(Nc, Nt, N, SYS_LS);
  LsConfs sConfArr = ConfArrFactoryE3.makeSModel(modelE3, wfN);    log.dbg("sConfArr=", sConfArr);
  LsConfHMtrx res = new LsConfHMtrx(sConfArr, sysE3);                     log.dbg("sH=\n", new MtrxDbgView(res));
  log.info("<--makeSysBasisN");
  return res;
}


public void setupEngAu_2() {
  ENG_FIRST = 0.01f;
  ENG_LAST = 1.00f;
  ENG_N = 100;
}
public void setupEngAu_3() {
  ENG_FIRST = 0.001f;
  ENG_LAST = 1.00f;
  ENG_N = 1000;
}
public void setupEngAu_4() {
  ENG_FIRST = 0.0001f;
  ENG_LAST = 1.00f;
  ENG_N = 10000;
}

public void setupEng01_1000eV_SLOW() {
  EngModelArr arr = new EngModelArr();

  int n = 189;
  float first = (float)AtomUnits.fromEV(0.1);
  float last = (float)AtomUnits.fromEV(18.9);
  arr.add(new EngModel(first, last, n));

  int n2 = 2000;
  float first2 = (float)AtomUnits.fromEV(18.9001);
  float last2 = (float)AtomUnits.fromEV(19.1);
  arr.add(new EngModel(first2, last2, n2));

  int n3 = 1000;
  float first3 = (float)AtomUnits.fromEV(19.11);
  float last3 = (float)AtomUnits.fromEV(29.1);
  arr.add(new EngModel(first3, last3, n3));

  int n4 = 971;
  float first4 = (float)AtomUnits.fromEV(30.0);
  float last4 = (float)AtomUnits.fromEV(1000.0);
  arr.add(new EngModel(first4, last4, n4));

  scttEngs = EngGridFactory.makeEngs(arr);

  ENG_N = n + n2 + n3 + n4;
  ENG_FIRST = first;
  ENG_LAST = last4;
}
public void setupEng1_100eV_SLOW() {
  EngModelArr arr = new EngModelArr();

  int n = 190;
  float first = (float)AtomUnits.fromEV(1);
  float last = (float)AtomUnits.fromEV(18.9);
  arr.add(new EngModel(first, last, n));

  int n2 = 1000;
  float first2 = (float)AtomUnits.fromEV(18.991);
  float last2 = (float)AtomUnits.fromEV(19.29);
  arr.add(new EngModel(first2, last2, n2));

  int n3 = 1001;
  float first3 = (float)AtomUnits.fromEV(19.30);
  float last3 = (float)AtomUnits.fromEV(29.3);
  arr.add(new EngModel(first3, last3, n3));

  int n4 = 71;
  float first4 = (float)AtomUnits.fromEV(30.0);
  float last4 = (float)AtomUnits.fromEV(100.0);
  arr.add(new EngModel(first4, last4, n4));

  scttEngs = EngGridFactory.makeEngs(arr);

  ENG_N = n + n2 + n3 + n4;
  ENG_FIRST = first;
  ENG_LAST = last4;
}
public void setupEng01_1000eV_FAST() {
  EngModelArr arr = new EngModelArr();

  int n = 1000;
  float first = (float)AtomUnits.fromEV(0.1);
  float last = (float)AtomUnits.fromEV(100.0);
  arr.add(new EngModel(first, last, n));

  int n2 = 900;
  float first2 = (float)AtomUnits.fromEV(101);
  float last2 = (float)AtomUnits.fromEV(1000);
  arr.add(new EngModel(first2, last2, n2));

  scttEngs = EngGridFactory.makeEngs(arr);

  ENG_N = n + n2;
  ENG_FIRST = first;
  ENG_LAST = last2;
}
public void setupEng10_30eV() {
  EngModelArr arr = new EngModelArr();

  int n = 1000;
  float first = (float)AtomUnits.fromEV(10.0);
  float last = (float)AtomUnits.fromEV(30.0);
  arr.add(new EngModel(first, last, n));

  scttEngs = EngGridFactory.makeEngs(arr);

  ENG_N = n;
  ENG_FIRST = first;
  ENG_LAST = last;
}
protected void jmHeTestOk() {
  FlowTest.setLog(log);
  FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());      // LOCK MAX ERR
  {
    if (!new WfFactory().ok()) return;
    if (!new SlaterWFFactory(quadr).ok()) return;

    if (!new PotEigVecLcrTest(AtomHe.Z, orthNt).ok()) return;
    if (!new Wign3jTest().ok()) return;
    if (!new Wign6jTest().ok()) return;
    if (!new HeClementiTest().ok()) return;
    if (!new HeClementiZetaTest(quadr).ok()) return;
  }
  FlowTest.unlockMaxErr();                             // FREE MAX ERR

  potFunc = new FuncPowInt(-AtomHe.Z, -1);  // f(r)=-atomZ/r
  pot = new FuncVec(vR, potFunc);
  log.dbg("-atomZ/r=", new VecDbgView(pot));

  if (!new ConfArrFactoryE3().ok()) return;
}

protected void initLiJm() {
  FlowTest.setLog(log);
  FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());      // LOCK MAX ERR
  {
    if (!new AtomLiSlaterJoy(quadr).ok()) return;
    if (!new AtomLiSlaterJoy3(quadr).ok()) return;
    if (!new LiSlaterTest(quadr).ok()) return;
  }
  FlowTest.unlockMaxErr();                             // FREE MAX ERR
}

protected void calcLi(SlaterLcr slater) { // NOTE!!! Local set up just to test three-electron equations
  log.info("-->calcLi(SlaterLcr slater)");
  float ABS_LI_ENG_ERR = 0.001f;
  float REL_LI_ENG_ERR = 0.001f;
  double lambdaLi = 2.3;      //
//    int LI_Nt = Nt;
  int LI_Nt = 10;
  // 15Dec
  // E(Nt=10, lambda=2.3) = -7.447498;
  // E(Nt=9,lambda=2.3) = -7.4464335
  // E(Nt=9,lambda=2.4) = -7.447015
  // E(Nt=10,lambda=2.4) = -7.4477787
  // E(Nt=11,lambda=2.4) = -7.448129
  // E(Nt=11,lambda=2.5) = -7.4482327
  // E(Nt=11,lambda=2.6) = -7.4483075
  // E(Nt=11,lambda=2.7) = -7.448364
  // E(Nt=11,lambda=2.8) = -7.4484067
  LgrrOpt optLiNt = new LgrrOpt(lgrrOptN); // for the target N, i.e. N_t
  optLiNt.setN(LI_Nt);
  optLiNt.setLambda(lambdaLi);          log.dbg("Laguerr model (N_t, lambda)=", optLiNt);
  LgrrOrthLcr orthonLiNt = new LgrrOrthLcr(quadr, optLiNt);        log.dbg("LgrrOrthLcr(N_t) = ", orthonLiNt);

  FlowTest.lockMaxErr(ABS_LI_ENG_ERR);      // LOCK MAX ERR
  {
    if (!new PotEigVecLcrTest(AtomLi.Z, orthonLiNt, false).ok()) return;
    if (!new PotEigVecLcrTest(AtomHe.Z, orthonLiNt, false).ok()) return;
    if (!new PotEigVecLcrTest(AtomHy.Z, orthonLiNt, false).ok()) return;
  }
  FlowTest.unlockMaxErr();                             // FREE MAX ERR
  FlowTest.lockMaxErr(REL_LI_ENG_ERR);      // LOCK MAX ERR
  {
    if (!new PotEigVecLcrTest(AtomLi.Z, orthonLiNt).ok()) return;
    if (!new PotEigVecLcrTest(AtomHe.Z, orthonLiNt).ok()) return;
    if (!new PotEigVecLcrTest(AtomHy.Z, orthonLiNt).ok()) return;
  }
  FlowTest.unlockMaxErr();                             // FREE MAX ERR


  // LITHIUM TEST
  Ls LS = new Ls(0, Spin.ELECTRON);
  SysE3 tgrtE3 = new SysLi(slater);// NOTE -3 for lithium
  AtomShModelE3 modelE3 = new AtomShModelE3(LI_Nt, LI_Nt, LI_Nt, LS);

  LsConfs sysArr;
  LsConfHMtrx sysH;
  Vec sysE;
  // Test with all different shells
  sysArr = ConfArrFactoryE3.makePoetDiffShells(modelE3, orthonLiNt);
  log.dbg("sysArr=", sysArr);
  sysH = new LsConfHMtrx(sysArr, tgrtE3);
  log.dbg("sysConfH=\n", new MtrxDbgView(sysH));
  sysE = sysH.getEigEngs();
  log.dbg("sysE=", new VecDbgView(sysE));

  // Test with all closed shells
  sysArr = ConfArrFactoryE3.makePoetClosedShell(modelE3, orthonLiNt);
  log.dbg("sysArr=", sysArr);
  sysH = new LsConfHMtrx(sysArr, tgrtE3);
  log.dbg("sysConfH=\n", new MtrxDbgView(sysH));
  sysE = sysH.getEigEngs();
  log.dbg("sysE=", new VecDbgView(sysE));

  // Test with all possible shells
  sysArr = ConfArrFactoryE3.makeSModel(modelE3, orthonLiNt);    log.dbg("sysArr=", sysArr);
  sysH = new LsConfHMtrx(sysArr, tgrtE3);    log.dbg("sysConfH=\n", new MtrxDbgView(sysH));
  sysE = sysH.getEigEngs();    log.dbg("sysE=", new VecDbgView(sysE));
  assertFloorRel("E_1s2_2s_2S_CI", AtomLi.E_1s2_2s_2S_CI, sysE.get(0), 0.005);

  assertCeilRel("Nt=", -7.446433, sysE.get(0), 0.005);     // this is for  // 15Dec2010; E(Nt=9,lambda=2.3) = -7.4464335
  assertCeilRel("Nt=", -7.320755, sysE.get(1), 0.005);     // this is for  // 15Dec2010; E(Nt=9,lambda=2.3)_1s^2,3s = -7.320755
//    assertFloorRel("E_1s2_2s_2S_CI", AtomLi.E_1s2_2s_2S_CI, sysE.get(0), 0.001);
  log.info("<--calcLi");
}

protected void calcHe(SlaterLcr slater) {    // HELIUM TEST
  SysE2OldOk oldE2 = new SysHeOldOk(slater);// NOTE -2 for Helium // F-for Fano
  SysE2 sysE2 = new SysHe(slater);// NOTE -2 for Helium

  Ls S1 = new Ls(0, Spin.SINGLET);
  LsConfs confs = ConfArrFactoryE2.makeSModelE2(S1, orthNt, orthNt);
  log.dbg("trgtArr=", confs);
  LsConfHMtrx sysH = new LsConfHMtrx(confs, sysE2);
  LsConfHMtrx oldH = new LsConfHMtrx(confs, oldE2);
  log.dbg("htFS1=\n", new MtrxDbgView(oldH));
  log.dbg("htS1=\n", new MtrxDbgView(sysH));

  Vec oldEngs = oldH.getEigEngs();
  log.dbg("oldEngs=", new VecDbgView(oldEngs));
  assertCeilRel("E_1s1s_1S", HeSWaveAtom.E_1s1s_1S, oldEngs.get(0), 2e-4);
  assertCeilRel("E_1s2s_1S", HeSWaveAtom.E_1s2s_1S, oldEngs.get(1), 3e-5);
//    assertFloorRel("E_1s3s_1S", HeSWaveAtom.E_1s3s_1S, etFS1.get(2), 4e-4);

  Vec sysEngs = sysH.getEigEngs();
  log.dbg("sysEngs=", new VecDbgView(sysEngs));
//    assertFloorRel("E_1s1s_1S", HeSWaveAtom.E_1s1s_1S, etS1.get(0), 2e-4);
//    assertFloorRel("E_1s2s_1S", HeSWaveAtom.E_1s2s_1S, etS1.get(1), 3e-5);
//    assertFloorRel("E_1s3s_1S", HeSWaveAtom.E_1s3s_1S, etS1.get(2), 4e-4);

  Ls S3 = new Ls(0, Spin.TRIPLET);
  LsConfs arrS3 = ConfArrFactoryE2.makeSModelE2(S3, orthNt, orthNt);
  log.dbg("trgtArr=", arrS3);
  LsConfHMtrx htS3 = new LsConfHMtrx(arrS3, oldE2);
  log.dbg("sysConfH=\n", new MtrxDbgView(htS3));
  Vec etS3 = htS3.getEigEngs();
  log.dbg("eigVal=", new VecDbgView(etS3));
  assertCeilRel("E_1s1s_3S", HeSWaveAtom.E_1s2s_3S, etS3.get(0), 7e-6);
  assertCeilRel("E_1s2s_3S", HeSWaveAtom.E_1s3s_3S, etS3.get(1), 2e-4);
}
}