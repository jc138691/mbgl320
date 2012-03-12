package papers.hy_swave.method_ees;
import atom.angular.Spin;
import atom.data.AtomHy;
import atom.e_1.SysE1;
import atom.e_2.SysAtomE2;
import atom.energy.ConfHMtrx;
import atom.energy.part_wave.PotHMtrxLcr;
import atom.energy.slater.SlaterLcr;
import atom.shell.ConfArr;
import atom.shell.ConfArrFactoryE2;
import atom.shell.Ls;
import math.func.arr.FuncArrDbgView;
import math.mtrx.MtrxDbgView;
import math.vec.Vec;
import math.vec.VecDbgView;
import papers.hy_swave.HyLikeSWave;
import qm_station.QMSProject;
import scatt.jm_2008.jm.ScattRes;
import scatt.jm_2008.jm.target.ScattTrgtE3;

import javax.iox.FileX;
import javax.utilx.log.Log;
/**
 * Dmitry.Konovalov@jcu.edu.au Dmitry.A.Konovalov@gmail.com 12/03/12, 10:05 AM
 */
public class HySWaveEesBasisHy extends HySWaveEes {
public static Log log = Log.getLog(HySWaveEesBasisHy.class);

public static void main(String[] args) {
  // NOTE!!! for Nt>40 you may need to increase the JVM memory: I used -Xmx900M for a laptop with 2GB RAM
  HySWaveEesBasisHy runMe = new HySWaveEesBasisHy();
  runMe.setUp();
  runMe.testRun();
}
public void setUp() {
  super.setUp();
  log.info("log.info(HySWaveEesBasisHy)");
  log.setDbg();
}
public void testRun() { // starts with 'test' so it could be run via JUnit without the main()
  project = QMSProject.makeInstance("HySWaveEesBasisHy", "120308");
  TARGET_Z = AtomHy.Z;
  HOME_DIR = "C:\\dev\\physics\\papers\\output";
  MODEL_NAME = "HySWaveEesBasisHy";    MODEL_DIR = MODEL_NAME;
  CALC_TRUE_CONTINUUM = false;
  LAMBDA = 1.5; // exact LAMBDA[H(1s)] = 2, LAMBDA[H(2s)] = 1;

  USE_CLOSED_CHANNELS = true;
  CALC_DENSITY = false;
  runJob();
}

public void calc(int newN) {
  SYS_LS = new Ls(0, SPIN);
  N = newN;
  Nt = newN;
  initProject();
  potScattTestOk();     // out: basisN, orthonN, biorthN
  hydrScattTestOk(TARGET_Z);      // out: pot (for TARGET_Z), orthonNt
  SlaterLcr slater = new SlaterLcr(quadrLcr);

  trgtPotH = new PotHMtrxLcr(L, orthonNt, pot);    log.dbg("trgtPotH=", trgtPotH);
  Vec targetEngs = trgtPotH.getEigVal();            log.dbg("eigVal=", new VecDbgView(targetEngs));
  trgtBasisNt = trgtPotH.getEigFuncArr();      log.dbg("trgtBasisNt=", new FuncArrDbgView(trgtBasisNt));
  trgtBasisN = null;
  orthonNt = null;
//  orthonN = null;

//  AtomUtil.trimTailSLOW(trgtBasisN);     // todo: check if needed
  ScattTrgtE3 trgt = makeTrgtE3(slater);
  trgt.setScreenZ(TARGET_Z - 1);       // Hydrogen-like target has ONE electron
  trgt.setInitTrgtIdx(FROM_CH);
  trgt.setIonGrndEng(0);
  trgt.setNt(Nt);
  trgt.loadSdcsW();
  trgt.removeClosed(calcOpt.getGridEng().getLast(), FROM_CH, KEEP_CLOSED_N);

  ConfHMtrx sysH = makeSysH(SYS_LS, slater);

  EesMethodE2 method = new EesMethodE2(calcOpt);
  method.setTrgtE3(trgt);
  Vec sEngs = sysH.getEigVal(H_OVERWRITE);                               log.dbg("sysConfH=", sEngs);
  method.setSysEngs(sEngs);
  method.setSysConfH(sysH);
  method.setOrthonN(orthonN);
  method.setTrgtBasisNt(trgtBasisNt);

  ScattRes res = method.calcSysEngs();                  log.dbg("res=", res);
  setupScattRes(res, method);
  res.writeToFiles();
}


}