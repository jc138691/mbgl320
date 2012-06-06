package papers.hy_swave.ees_bad;
import atom.angular.Spin;
import atom.data.AtomHy;
import atom.e_1.SysE1;
import atom.e_2.SysE2;
import atom.energy.ConfHMtrx;
import atom.energy.slater.SlaterLcr;
import atom.shell.ConfArr;
import atom.shell.ConfArrFactoryE2;
import atom.shell.Ls;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.grid.StepGrid;
import math.vec.grid.StepGridOpt;
import papers.hy_swave.HyLikeSWave;
import qm_station.QMSProject;
import scatt.jm_2008.jm.ScttRes;
import scatt.jm_2008.jm.target.ScttTrgtE3;

import javax.iox.FileX;
import javax.utilx.log.Log;
/**
* Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 8/03/12, 9:20 AM
*/
public class HySWaveEes  extends HyLikeSWave {
public static Log log = Log.getLog(HySWaveEes.class);

public static void main(String[] args) {
  // NOTE!!! for Nt>40 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  HySWaveEes runMe = new HySWaveEes();
  runMe.setUp();
  runMe.testRun();
}
public void setUp() {
  super.setUp();
  log.info("log.info(HySWaveEes_Test)");
  log.setDbg();
}
public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  project = QMSProject.makeInstance("HySWaveEes", "120308");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_NAME = "HySWaveEes";    MODEL_DIR = MODEL_NAME;
  CALC_TRUE_CONTINUUM = false; // if TRUE, increase LCR_N by about timesSelf 2.5
  LAMBDA = 1.5; // exact LAMBDA[H(1s)] = 2, LAMBDA[H(2s)] = 1;

  USE_CLOSED_CHANNELS = true;
  CALC_DENSITY = false;
  runJob();
}

public void runJob() {
  int currN = 20;
  LCR_FIRST = -5;
  LCR_N = 1501;
  R_LAST = 150;

  ENG_FIRST = 0.3;
  ENG_LAST = 0.5;

  double LAMBDA_MIN = 1;
  double LAMBDA_MAX = 1.5;
  int LAMBDA_N = 51;
  StepGrid lamGrid = new StepGrid(new StepGridOpt(LAMBDA_MIN, LAMBDA_MAX, LAMBDA_N));
  for (int i = 0; i < lamGrid.size(); i++) {
    LAMBDA = lamGrid.get(i);
    SPIN = Spin.SINGLET;
    calc(currN);
  }
//    SPIN = Spin.TRIPLET;
//    calc(currN);
}

public void calc(int newN) {
  SYS_LS = new Ls(0, SPIN);
  N = newN;
  Nt = newN;  // this is just to keep functions like hydrScattTestOk() working
  initProject();
  potScattTestOk();     // out: lgrrN, orthNt, lgrrBi
  hydrScattTestOk(TARGET_Z);      // out: pt (for TARGET_Z), orthNt
  SlaterLcr slater = new SlaterLcr(quadr);

  wfN = orthN;
  trgtWfsNt = null;
  orthNt = null;

//  AtomUtil.trimTailSLOW(lgrrN);     // todo: check if needed
  ScttTrgtE3 trgt = makeTrgtE3(slater);
  trgt.setScreenZ(TARGET_Z - 1);       // Hydrogen-like target has ONE electron
  trgt.setInitTrgtIdx(FROM_CH);
  trgt.setIonGrndEng(0);
  trgt.setNt(Nt);
  trgt.loadSdcsW();
  trgt.removeClosed(calcOpt.getGridEng().getLast(), FROM_CH, KEEP_CLOSED_N);

  ConfHMtrx sysH = makeSysH(SYS_LS, slater);

  EesMethodBasisAnyE2 method = new EesMethodBasisAnyE2(calcOpt);
  method.setTrgtE2(trgt);
  Vec sEngs = sysH.getEigVal(H_OVERWRITE);                               log.dbg("sysConfH=", sEngs);
  method.setSysEngs(sEngs);
  method.setSysConfH(sysH);
  method.setOrthNt(orthN);
  method.setWfsE1(wfN);   // is just orthNt, but different in _BasisHy

  ScttRes res = method.calcSysEngs();                  log.dbg("res=", res);
  setupScattRes(res, method);
  res.writeToFiles();
}


protected ScttTrgtE3 makeTrgtE3(SlaterLcr slater) {
  SysE1 tgrtE2 = new SysE1(-TARGET_Z, slater);
  Ls tLs = new Ls(0, Spin.ELECTRON);  // t - for target

  ConfArr tConfArr = ConfArrFactoryE2.makePoetConfE1(trgtWfsNt);     log.dbg("tConfArr=", tConfArr);

  ConfHMtrx tH = new ConfHMtrx(tConfArr, tgrtE2);                  log.dbg("tH=\n", new MtrxDbgView(tH));
  FileX.writeToFile(tH.getEigEngs().toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgEngs_" + makeLabelBasisOptN());
  FileX.writeToFile(tH.getEngEv(0).toCSV(), HOME_DIR, MODEL_DIR, MODEL_NAME + "_trgEngs_eV_" + makeLabelBasisOptN());

  ScttTrgtE3 res = new ScttTrgtE3();
  res.add(tH);
  res.makeReady();
  return res;
}

protected ConfHMtrx makeSysH(Ls sLs, SlaterLcr slater) {
  SysE2 sys = new SysE2(TARGET_Z, slater);// NOTE 1 for Hydrogen
  ConfArr sConfArr = ConfArrFactoryE2.makeSModelE2(sLs, trgtWfsNt, trgtWfsNt);   //log.dbg("sysArr=", sConfArr);
  ConfHMtrx res = new ConfHMtrx(sConfArr, sys);                  //log.dbg("sysConfH=\n", new MtrxDbgView(res));
  return res;
}
@Override
public void calc(int newN, int newNt) {
throw new IllegalArgumentException(log.error("use calc(int newN)"));
}
}