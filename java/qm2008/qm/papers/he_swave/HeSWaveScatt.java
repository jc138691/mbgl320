package papers.he_swave;
import atom.angular.Spin;
import atom.angular.test.Wign3jTest;
import atom.angular.test.Wign6jTest;
import atom.data.*;
import atom.e_2.SysAtomE2;
import atom.e_2.SysE2_OLD;
import atom.e_2.SysHe;
import atom.e_2.SysHe_OLD;
import atom.e_2.test.HeClementiTest;
import atom.e_2.test.HeClementiZetaTest;
import atom.e_3.AtomShModelE3;
import atom.e_3.SysAtomE3;
import atom.e_3.SysLi;
import atom.e_3.test.LiSlaterTest;
import atom.energy.ConfHMtrx;
import atom.energy.slater.SlaterLcr;
import atom.poet.HeSWaveAtom;
import atom.shell.ConfArr;
import atom.shell.ConfArrFactoryE2;
import atom.shell.ConfArrFactoryE3;
import atom.shell.Ls;
import atom.wf.coulomb.CoulombWFFactory;
import atom.wf.log_cr.LcrFactory;
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
import qm_station.jm.JmPotEigVecLcrTest;
import scatt.eng.EngGridFactory;
import scatt.eng.EngModel;
import scatt.eng.EngModelArr;
import scatt.jm_2008.e2.JmMethodBaseE2;
import scatt.jm_2008.e2.JmResonancesE2;
import scatt.jm_2008.jm.JmRes;
import scatt.jm_2008.jm.laguerre.JmLgrrModel;
import scatt.jm_2008.jm.laguerre.lcr.JmLgrrOrthLcr;
import scatt.jm_2008.jm.target.JmTrgtE3;

import javax.iox.FileX;
import javax.utilx.log.Log;
/**
 * dmitry.a.konovalov@gmail.com,dmitry.konovalov@jcu.edu.com,8/06/11,12:49 PM
 */
abstract public class HeSWaveScatt  extends HyLikeSWave {
  public static Log log = Log.getLog(HeSWaveScatt.class);
  protected boolean IGNORE_BUG_PoetHeAtom = false;
  protected static JmLgrrOrthLcr orthonNc;  // for N_c
  protected static int Nc = 1;


  public void setupEngResonance_2S() {
    ENG_FIRST = (float) AtomUnits.fromEV(18.5);
    ENG_LAST = (float)AtomUnits.fromEV(19.5);
    ENG_N = 11;
  }


  public void setUp() {
    super.setUp();
    log.info("log.info(HeSWaveScatt)");
//    JmResonancesE2.log.setDbg();
//    log.setDbg();
  }

  protected void saveTrgtInfo(JmTrgtE3 jmTrgt) {
    double ionGrnd = jmTrgt.getIonGrndEng();
    int S1 = 0;
    Vec ionEngs = jmTrgt.getArrH().get(S1).getEigVal().copy();
    ionEngs.add(-ionGrnd);
    ionEngs.calc(AtomUnits.funcToEV);
    FileX.writeToFile(ionEngs.toCSV(), HOME_DIR, MODEL_DIR
      , MODEL_NAME+"_trgEngs_S1_ion_eV_" + makeLabelNc());

    int S3 = 1;
    ionEngs = jmTrgt.getArrH().get(S3).getEigVal().copy();
    ionEngs.add(-ionGrnd);
    ionEngs.calc(AtomUnits.funcToEV);
    FileX.writeToFile(ionEngs.toCSV(), HOME_DIR, MODEL_DIR
      , MODEL_NAME+"_trgEngs_S3_ion_eV_" + makeLabelNc());
  }
  public void setupJmRes(JmRes res, JmMethodBaseE2 method) {
    super.setupJmRes(res, method);
    res.setCalcLabel(makeLabelNc(method));
  }

  protected static String makeLabelNc() {
    return "Nc" + Nc + "_" + Jm2010Common.makeLabelBasisOptN();
  }
  protected static String makeLabelNc(JmMethodBaseE2 method) {
    return "Nc" + Nc + "_" + Jm2010Common.makeLabelBasisOptOpen(method);
  }

  protected JmTrgtE3 makeTrgtBasisNt(SlaterLcr slater, FuncArr basisNt) {
    SysAtomE2 tgrtE2 = new SysHe(slater);// NOTE -2 for Helium       // USES equations from the 2011 e-He paper

    Ls tLs = new Ls(0, Spin.SINGLET);  // t - for target
    ConfArr tConfArr = ConfArrFactoryE2.makePoetConfE2(tLs, basisNt, Nc);    log.dbg("tConfArr=", tConfArr);
    ConfHMtrx tH = new ConfHMtrx(tConfArr, tgrtE2);                          log.dbg("tH=\n", new MtrxDbgView(tH));
    Mtrx tVecs = tH.getEigVec();                                             log.dbg("tH.getEigVec=", new MtrxDbgView(tVecs));
    FileX.writeToFile(tH.getEigVal().toCSV(), HOME_DIR, MODEL_DIR
      , MODEL_NAME+"_trgEngs_S1_" + makeLabelNc());

    if (CALC_DENSITY) {
      FuncArr sysDens = tH.getDensity();
      FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadrLcr);  // NOTE!! convering density to R (not wf)
      FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgtDensityR_S1_" + makeLabelNc());
    }

    tLs = new Ls(0, Spin.TRIPLET);  // t - for target
    tConfArr = ConfArrFactoryE2.makePoetConfE2(tLs, basisNt, Nc);            log.dbg("tConfArr=", tConfArr);
    ConfHMtrx tH2 = new ConfHMtrx(tConfArr, tgrtE2);                         log.dbg("tH=\n", new MtrxDbgView(tH2));
    FileX.writeToFile(tH2.getEigVal().toCSV(), HOME_DIR, MODEL_DIR
          , MODEL_NAME+"_trgEngs_S3_" + makeLabelNc());

    if (CALC_DENSITY) {
      FuncArr sysDens = tH2.getDensity();
      FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadrLcr);  // NOTE!! convering density to R (not wf)
      FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgtDensityR_S3_" + makeLabelNc());
    }

    JmTrgtE3 res = new JmTrgtE3();
    res.add(tH);
    res.add(tH2);
    res.makeReady();
    return res;
  }
  private JmTrgtE3 makeTrgtE3_OLD(SlaterLcr slater) {
    // NOTE!!!  As of 2011, SysAtomE2 replaces SysE2_OLD.
    SysAtomE2 tgrtE2 = new SysHe(slater);// NOTE -2 for Helium       // USES equations from the 2011 e-He paper

    Ls tLs = new Ls(0, Spin.SINGLET);  // t - for target
    ConfArr tConfArr = ConfArrFactoryE2.makeSModelE2(tLs, orthonNt, orthonNt);     log.dbg("tConfArr=", tConfArr);
    ConfHMtrx tH = new ConfHMtrx(tConfArr, tgrtE2);                                   log.dbg("tH=\n", new MtrxDbgView(tH));
//    Vec tEngs = tH.getEigVal();                                                 log.dbg("tEngs=", new VecDbgView(tEngs));

    FileX.writeToFile(tH.getEigVal().toCSV(), HOME_DIR, "He"
      , "He_SModel_trgEngs_SING_" + basisOptN.makeLabel() + ".dat");


    tLs = new Ls(0, Spin.TRIPLET);  // t - for target
    tConfArr = ConfArrFactoryE2.makeSModelE2(tLs, orthonNt, orthonNt);               log.dbg("tConfArr=", tConfArr);
    ConfHMtrx tH2 = new ConfHMtrx(tConfArr, tgrtE2);                                   log.dbg("tH=\n", new MtrxDbgView(tH));

    FileX.writeToFile(tH2.getEigVal().toCSV(), HOME_DIR, "He"
          , "He_SModel_trgEngs_TRIP_" + basisOptN.makeLabel() + ".dat");

    JmTrgtE3 res = new JmTrgtE3();
    res.add(tH);
    res.add(tH2);
    res.makeReady();
    return res;
  }


  protected ConfHMtrx makeSysBasisN(SlaterLcr slater) {
    SYS_LS = new Ls(0, Spin.ELECTRON);     // s - for system
    SysAtomE3 sysE3 = new SysAtomE3(-AtomHe.Z, slater);    // NOTE!!! Helium (AtomHe.Z), not Li (AtomLi.Z)
    AtomShModelE3 modelE3 = new AtomShModelE3(Nc, Nt, N, SYS_LS);
    ConfArr sConfArr = ConfArrFactoryE3.makeSModel(modelE3, trgtBasisN);    log.dbg("sConfArr=", sConfArr);
    ConfHMtrx res = new ConfHMtrx(sConfArr, sysE3);                     log.dbg("sH=\n", new MtrxDbgView(res));
    return res;
  }
  private ConfHMtrx makeSysH_OLD(SlaterLcr slater) {
    Ls sLs = new Ls(0, Spin.ELECTRON);     // s - for system
    SysAtomE3 sysE3 = new SysAtomE3(-AtomHe.Z, slater);    // NOTE!!! Helium (AtomHe.Z), not Li (AtomLi.Z)
    AtomShModelE3 modelE3 = new AtomShModelE3(Nt, Nt, N, sLs);
    ConfArr sConfArr = ConfArrFactoryE3.makeSModel(modelE3, orthonN);    log.dbg("sConfArr=", sConfArr);
    ConfHMtrx res = new ConfHMtrx(sConfArr, sysE3);                     log.dbg("sH=\n", new MtrxDbgView(res));
//    Vec sEngs = sH.getEigVal();                                        log.dbg("sEngs=", new VecDbgView(sEngs));
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
      if (!new CoulombWFFactory().ok()) return;
      if (!new SlaterWFFactory(quadrLcr).ok()) return;

      if (!new JmPotEigVecLcrTest(AtomHe.Z, orthonNt).ok()) return;
      if (!new Wign3jTest().ok()) return;
      if (!new Wign6jTest().ok()) return;
      if (!new HeClementiTest().ok()) return;
      if (!new HeClementiZetaTest(quadrLcr).ok()) return;
    }
    FlowTest.unlockMaxErr();                             // FREE MAX ERR

    potFunc = new FuncPowInt(-AtomHe.Z, -1);  // f(r)=-Z/r
    pot = new FuncVec(rVec, potFunc);
    log.dbg("-Z/r=", new VecDbgView(pot));

    if (!new ConfArrFactoryE3().ok()) return;
  }

  protected void initLiJm() {
    FlowTest.setLog(log);
    FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());      // LOCK MAX ERR
    {
      if (!new AtomLiSlaterJoy(quadrLcr).ok()) return;
      if (!new AtomLiSlaterJoy3(quadrLcr).ok()) return;
      if (!new LiSlaterTest(quadrLcr).ok()) return;
    }
    FlowTest.unlockMaxErr();                             // FREE MAX ERR
  }

  protected void calcLi(SlaterLcr slater) { // NOTE!!! Local set up just to test three-electron equations
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
    JmLgrrModel optLiNt = new JmLgrrModel(basisOptN); // for the target N, i.e. N_t
    optLiNt.setN(LI_Nt);
    optLiNt.setLambda(lambdaLi);          log.dbg("Laguerr model (N_t, lambda)=", optLiNt);
    JmLgrrOrthLcr orthonLiNt = new JmLgrrOrthLcr(quadrLcr, optLiNt);        log.dbg("JmLgrrOrthLcr(N_t) = ", orthonLiNt);

    FlowTest.lockMaxErr(ABS_LI_ENG_ERR);      // LOCK MAX ERR
    {
      if (!new JmPotEigVecLcrTest(AtomLi.Z, orthonLiNt, false).ok()) return;
      if (!new JmPotEigVecLcrTest(AtomHe.Z, orthonLiNt, false).ok()) return;
      if (!new JmPotEigVecLcrTest(AtomHy.Z, orthonLiNt, false).ok()) return;
    }
    FlowTest.unlockMaxErr();                             // FREE MAX ERR
    FlowTest.lockMaxErr(REL_LI_ENG_ERR);      // LOCK MAX ERR
    {
      if (!new JmPotEigVecLcrTest(AtomLi.Z, orthonLiNt).ok()) return;
      if (!new JmPotEigVecLcrTest(AtomHe.Z, orthonLiNt).ok()) return;
      if (!new JmPotEigVecLcrTest(AtomHy.Z, orthonLiNt).ok()) return;
    }
    FlowTest.unlockMaxErr();                             // FREE MAX ERR


    // LITHIUM TEST
    Ls LS = new Ls(0, Spin.ELECTRON);
    SysAtomE3 tgrtE3 = new SysLi(slater);// NOTE -3 for lithium
    AtomShModelE3 modelE3 = new AtomShModelE3(LI_Nt, LI_Nt, LI_Nt, LS);

    ConfArr sysArr;
    ConfHMtrx sysH;
    Vec sysE;
    // Test with all different shells
    sysArr = ConfArrFactoryE3.makePoetDiffShells(modelE3, orthonLiNt);
    log.dbg("sysArr=", sysArr);
    sysH = new ConfHMtrx(sysArr, tgrtE3);
    log.dbg("sysH=\n", new MtrxDbgView(sysH));
    sysE = sysH.getEigVal();
    log.dbg("sysE=", new VecDbgView(sysE));

    // Test with all closed shells
    sysArr = ConfArrFactoryE3.makePoetClosedShell(modelE3, orthonLiNt);
    log.dbg("sysArr=", sysArr);
    sysH = new ConfHMtrx(sysArr, tgrtE3);
    log.dbg("sysH=\n", new MtrxDbgView(sysH));
    sysE = sysH.getEigVal();
    log.dbg("sysE=", new VecDbgView(sysE));

    // Test with all possible shells
    sysArr = ConfArrFactoryE3.makeSModel(modelE3, orthonLiNt);    log.dbg("sysArr=", sysArr);
    sysH = new ConfHMtrx(sysArr, tgrtE3);    log.dbg("sysH=\n", new MtrxDbgView(sysH));
    sysE = sysH.getEigVal();    log.dbg("sysE=", new VecDbgView(sysE));
    assertFloorRel("E_1s2_2s_2S_CI", AtomLi.E_1s2_2s_2S_CI, sysE.get(0), 0.005);

    assertCeilRel("Nt=", -7.446433, sysE.get(0), 0.005);     // this is for  // 15Dec2010; E(Nt=9,lambda=2.3) = -7.4464335
    assertCeilRel("Nt=", -7.320755, sysE.get(1), 0.005);     // this is for  // 15Dec2010; E(Nt=9,lambda=2.3)_1s^2,3s = -7.320755
//    assertFloorRel("E_1s2_2s_2S_CI", AtomLi.E_1s2_2s_2S_CI, sysE.get(0), 0.001);
  }

  protected void calcHe(SlaterLcr slater) {    // HELIUM TEST
    SysE2_OLD tgrtF = new SysHe_OLD(slater);// NOTE -2 for Helium // F-for Fano
    SysAtomE2 tgrt = new SysHe(slater);// NOTE -2 for Helium

    Ls S1 = new Ls(0, Spin.SINGLET);
    ConfArr arrS1 = ConfArrFactoryE2.makeSModelE2(S1, orthonNt, orthonNt);
    log.dbg("trgtArr=", arrS1);
    ConfHMtrx htS1 = new ConfHMtrx(arrS1, tgrt);
    ConfHMtrx htFS1 = new ConfHMtrx(arrS1, tgrtF);
    log.dbg("htFS1=\n", new MtrxDbgView(htFS1));
    log.dbg("htS1=\n", new MtrxDbgView(htS1));

    Vec etFS1 = htFS1.getEigVal();
    log.dbg("etFS1=", new VecDbgView(etFS1));
//    assertFloorRel("E_1s1s_1S", HeSWaveAtom.E_1s1s_1S, etFS1.get(0), 2e-4);
//    assertFloorRel("E_1s2s_1S", HeSWaveAtom.E_1s2s_1S, etFS1.get(1), 3e-5);
//    assertFloorRel("E_1s3s_1S", HeSWaveAtom.E_1s3s_1S, etFS1.get(2), 4e-4);

    Vec etS1 = htS1.getEigVal();
    log.dbg("etS1=", new VecDbgView(etS1));
//    assertFloorRel("E_1s1s_1S", HeSWaveAtom.E_1s1s_1S, etS1.get(0), 2e-4);
//    assertFloorRel("E_1s2s_1S", HeSWaveAtom.E_1s2s_1S, etS1.get(1), 3e-5);
//    assertFloorRel("E_1s3s_1S", HeSWaveAtom.E_1s3s_1S, etS1.get(2), 4e-4);

    Ls S3 = new Ls(0, Spin.TRIPLET);
    ConfArr arrS3 = ConfArrFactoryE2.makeSModelE2(S3, orthonNt, orthonNt);
    log.dbg("trgtArr=", arrS3);
    ConfHMtrx htS3 = new ConfHMtrx(arrS3, tgrtF);
    log.dbg("sysH=\n", new MtrxDbgView(htS3));
    Vec etS3 = htS3.getEigVal();
    log.dbg("eigVal=", new VecDbgView(etS3));
    if (!IGNORE_BUG_PoetHeAtom) {
      assertFloorRel("E_1s1s_3S", HeSWaveAtom.E_1s2s_3S, etS3.get(0), 7e-6);
      assertFloorRel("E_1s2s_3S", HeSWaveAtom.E_1s3s_3S, etS3.get(1), 2e-4);
    }
  }
}