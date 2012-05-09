package papers.hy_swave;

import atom.AtomUtil;
import atom.data.AtomHy;
import atom.e_2.SysAtomE2;
import atom.energy.ConfHMtrx;
import atom.energy.part_wave.PotHMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.shell.ConfArr;
import atom.shell.ConfArrFactoryE2;
import atom.shell.Ls;
import atom.wf.lcr.LcrFactory;
import math.func.arr.FuncArr;
import math.func.arr.FuncArrDbgView;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import project.workflow.task.test.FlowTest;
import qm_station.QMSProject;
import scatt.jm_2008.e2.JmMthdBasisHyE2;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.target.ScttTrgtE2;
import scatt.jm_2008.jm.coulomb.ClmbHyContTest;
import scatt.jm_2008.jm.theory.JmD;
import scatt.partial.wf.JmClmbLcr;

import javax.iox.FileX;
import javax.utilx.log.Log;

/**
* Created by Dmitry.A.Konovalov@gmail.com, 15/02/2010, 1:03:23 PM
*/
public class HySWaveJmBasisHy extends HySWaveJm {
public static Log log = Log.getLog(HySWaveJmBasisHy.class);

public static void main(String[] args) {
  // NOTE!!! for Nt>40 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  HySWaveJmBasisHy runMe = new HySWaveJmBasisHy();
  runMe.setUp();
  runMe.testRun();
}

public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  project = QMSProject.makeInstance("HySWaveJmBasisHy", "110606");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_NAME = "HySWaveBasisHy";    MODEL_DIR = MODEL_NAME;
  CALC_TRUE_CONTINUUM = false; // if TRUE, increase LCR_N by about times 2.5
  LAMBDA = 2; // exact LAMBDA[H(1s)] = 2, LAMBDA[H(2s)] = 1;
//    LAMBDA = 1.7;

  // Note: run one at a time as only one set of result files is produced
//    setupEngExcite();
//    setupResonances_n2_n3_S1();
//    setupResonances_n2_S1();
  setupResonances_n2_S1_TestClosed();
//    setupEngExcite();
//    setupEngTICS();
//    setupEngSDCS();
  USE_CLOSED_CHANNELS = true;
  KEEP_CLOSED_N = 1;
  CALC_DENSITY = false;
  CALC_SDCS = false;
  runJob();
}

public void setUp() {
  super.setUp();
  log.info("log.info(HySWaveJmBasisHy)");
  log.setDbg();
}

public void calc(int newN, int newNt) {
  SYS_LS = new Ls(0, SPIN);
  N = newN;
  Nt = newNt;
  initProject();
  potScattTestOk();  // basisN, biorthN, orthonNt, quadrLcr
  hydrScattTestOk(TARGET_Z);  // pot, orthonNt
  FlowTest.lockMaxErr(testOpt.getMaxIntgrlErr());      // LOCK MAX ERR

  trgtPotH = new PotHMtrxLcr(L, orthonNt, pot);    log.dbg("trgtPotH=", trgtPotH);
  Vec trgtEngs = trgtPotH.getEigVal();            log.dbg("eigVal=", new VecDbgView(trgtEngs));
  trgtStatesNt = trgtPotH.getEigFuncArr();      log.dbg("trgtStatesNt=", new FuncArrDbgView(trgtStatesNt));
//    FileX.writeToFile(trgtStatesNt.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgtBasisNtLcr_" + makeLabelTrgtS2());

  FuncArr basisR = LcrFactory.wfLcrToR(trgtStatesNt, quadrLcr);
  AtomUtil.trimTailSLOW(basisR);
//    FileX.writeToFile(basisR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgtBasisNtR_" + makeLabelTrgtS2());

  AtomUtil.trimTailSLOW(trgtStatesNt);

  ScttTrgtE2 trgt = new ScttTrgtE2();
  trgt.setNt(orthonNt.size());
  trgt.setStatesE1(trgtStatesNt);
  trgt.setEngs(trgtEngs);
  trgt.loadSdcsW();                log.dbg("trgt.getSdcsW()=", new VecDbgView(trgt.getSdcsW()));
  if (CALC_TRUE_CONTINUUM) {      // TARGET CONTINUUM
    double maxSysEng = calcOpt.getGridEng().getLast() + trgtEngs.get(0); // ASSUMED FROM H(1s)
    JmClmbLcr clmbNt = new JmClmbLcr(L, AtomHy.Z, trgtEngs, maxSysEng, quadrLcr);      log.dbg("JmClmbLcr =\n", clmbNt);
    clmbNt.normToE();
//      AtomUtil.setTailFrom(clmbNt, trgtStatesNt);
//      FileX.writeToFile(clmbNt.toTab(), HOME_DIR, "wf", "clmbNt.dat");
    if (!new ClmbHyContTest(clmbNt, trgtStatesNt).ok())
      return;
    trgt.setContE1(clmbNt);
    trgt.loadSdcsContW();   log.dbg("trgt.getSdcsW()=", new VecDbgView(trgt.getSdcsW()));
//      FileX.writeToFile(trgt.toTab(), HOME_DIR, "wf", "target_" + basisOptN.makeLabel() + ".dat");
  }

  PotHMtrx targetHTest = new PotHMtrxLcr(L, trgtStatesNt, pot, orthonNt.getQuadr());  log.dbg("targetHTest=", targetHTest); // only for debugging
  Vec D = new JmD(biorthN, trgtStatesNt);      log.dbg("D_{n,m>=Nt}=must be ZERO=", D); // MUST BE ALL ZERO!!!!!

  SYS_LS = new Ls(0, SPIN);
  ConfArr sysArr = ConfArrFactoryE2.makeSModelE2(SYS_LS, trgtStatesNt, orthonN);    log.dbg("sysArr=", sysArr);

  // one electron basis
  trgtBasisN = orthonN;    // only the last wfs were used from  orthonNt, so now we can reuse it
  orthonN = null; // making sure nobody uses old ref
  trgtBasisN.copyFrom(trgtStatesNt, 0, trgtStatesNt.size());
  D = new JmD(biorthN, trgtBasisN);   log.dbg("D_{n,N-1}=", D);

  SlaterLcr slater = new SlaterLcr(quadrLcr);
//    SysE2_OLD sys = new SysE2_OLD(-1., slater);// NOTE -1 for Hydrogen
  SysAtomE2 sys = new SysAtomE2(-TARGET_Z, slater);// NOTE -1 for Hydrogen
  ConfHMtrx sysH = new ConfHMtrx(sysArr, sys);    log.dbg("sysConfH=\n", new MtrxDbgView(sysH));
  Vec sEngs = sysH.getEigVal(H_OVERWRITE);                               log.dbg("sEngs=", sEngs);
//    double e0 = sysEngs.get(0);

  JmMthdBasisHyE2 mthd = new JmMthdBasisHyE2(calcOpt);
  mthd.setTrgtE2(trgt);
  mthd.setOverD(D);
  mthd.setSysEngs(sEngs);
  mthd.setSysConfH(sysH);
  mthd.setQuadrLcr(quadrLcr);

  if (CALC_DENSITY) {
    FuncArr sysDens = sysH.getDensity(CALC_DENSITY_MAX_NUM);
    FuncArr sysDensR = LcrFactory.densLcrToR(sysDens, quadrLcr);  // NOTE!! convering density to R (not wf)
    FileX.writeToFile(sysDensR.toTab(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_sysDensityR_" + makeLabelTrgtS2(mthd));
  }

  ScttRes res;
//    res = method.calcMidSysEngs();                  log.dbg("res=", res);
  if (scttEngs == null) {
    scttEngs = mthd.calcScattEngs();
//      Vec sysScatEngs = method.calcSysScatEngs();
//      scttEngs = scttEngs.append(sysScatEngs);
//      scttEngs.sort();

//      Vec vE = method.calcSysScatEngs();
//      Vec vE2 = method.calcSysScatEngs();
//      vE2.add(+1.0E-6);
//      scttEngs = vE.append(vE2);
//      scttEngs.sort();
  }
  res = mthd.calc(scttEngs);                  log.dbg("res=", res);
  setupScattRes(res, mthd);                        log.dbg("res=", res);
  res.writeToFiles();
}

}